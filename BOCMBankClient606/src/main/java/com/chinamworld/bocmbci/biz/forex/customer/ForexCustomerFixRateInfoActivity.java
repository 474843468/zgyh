package com.chinamworld.bocmbci.biz.forex.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexCustomerFixRateInfoAdapter;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexCustomerRateInfoAdapter;
import com.chinamworld.bocmbci.biz.forex.rate.ForexAccSettingActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexNoAccountActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickCurrentSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的外汇首页---定期账户信息 活期、定期两页面合并，不在使用此页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexCustomerFixRateInfoActivity extends ForexBaseActivity {

	private static final String TAG = "ForexCustomerFixRateInfoActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexCustomerFixRateInfoActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	/** 外汇交易账户按钮 */
	private TextView topAccText = null;
	/** 账户重设View */
	private View topAccRestView = null;

	/** 用户所有的折册号列表 */
	private List<String> volumeNumberList = null;
	private ListView listView = null;
	/** 符合条件的卖出账户信息 */
	private List<Map<String, Object>> accInfoList = null;
	/** 账户类型 */
	private String accountType = null;
	/** 账户别名 */
	private String nickName = null;
	/** 账户 */
	private String account = null;
	// 活期账户页面
	/**
	 * 存储符合条件的卖出币种的全部信息-未进行处理
	 */
	private List<Map<String, Object>> accList = null;
	private View topCurrAccView = null;
	private ListView accSellGridView = null;
	/** 默认是定期 1-定期，2-活期 */
	private int currencyOrFixReq = 1;
	// 默认选中第一个
	private int customerSelectedPosition = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		LogGloble.d(TAG, "onCreate");
		initPulldownBtn();
		initLeftSideList(this, LocalData.forexStorageCashLeftList);
		initFootMenu();
		volumeNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		} else {
			currencyOrFixReq = 1;
			init();
			initData();
			initOnClick();
		}

	}

	public void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		rateInfoView = LayoutInflater.from(ForexCustomerFixRateInfoActivity.this).inflate(R.layout.forex_customer_fix,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_customer));
		backButton = (Button) findViewById(R.id.ib_back);
		topAccText = (TextView) findViewById(R.id.customer_accNumber);
		topAccRestView = findViewById(R.id.top_acc_layout);
		listView = (ListView) findViewById(R.id.fix_listView);
		accInfoList = new ArrayList<Map<String, Object>>();
		account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_RATE_ACCOUNT_RES);
	}

	public void initData() {
		if (StringUtil.isNullOrEmpty(account)) {
			return;
		} else {
			String acc = StringUtil.getForSixForString(account);
			topAccText.setText(acc);
			currencyOrFixReq = 1;
			// 查询定期账户币种、可用余额信息
			requestPsnForexQueryBlanceCucyList();
		}

	}

	public void initOnClick() {
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到主activity
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexCustomerFixRateInfoActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});
		// 顶部账户重设
		topAccRestView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查询所有的外汇交易账户列表
				customerPsnForexActAvai();
			}
		});
	}

	/**
	 * 查询定期账户余额、以及币种信息
	 */
	private void requestPsnForexQueryBlanceCucyList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		if (currencyOrFixReq == 1) {
			HttpManager.requestBii(biiRequestBody, this, "requestPsnForexQueryBlanceCucyListCallback");
		} else {
			HttpManager.requestBii(biiRequestBody, this, "requestCurrencyBlanceCucyListCallback");
		}

	}

	/**
	 * 定期账户----回调 查询定期账户余额、以及币种信息--回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnForexQueryBlanceCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		} else {
			List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result
					.get(Forex.FORX_TERMSUBACCOUNT_RES);
			if (termSubAccountList == null||termSubAccountList.size() == 0) {
				return;
			} else {
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FORX_TERMSUBACCOUNT_RES, termSubAccountList);
				int len = termSubAccountList.size();
				for (int i = 0; i < len; i++) {
					Map<String, Object> termSubAccountMap = termSubAccountList.get(i);
					// 存款种类
					String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
					String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
					Map<String, Object> balance = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
					// TODO---账户类型正常 存单类型为整存整取 定活两遍
					if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)
							&& (type.equals(ConstantGloble.FOREX_ACCTYPE_ZCZQ) || type
									.equals(ConstantGloble.FOREX_ACCTYPE_DHLB))) {
						// 得到币种
						String code = null;

						Map<String, String> currency = (Map<String, String>) termSubAccountMap
								.get(Forex.FOREX_CURRENCY_RES);
						if (StringUtil.isNullOrEmpty(currency)) {
							continue;
						} else {
							// 得到币种
							code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
							if (StringUtil.isNull(code)) {
								continue;
							}
						}
						if (StringUtil.isNullOrEmpty(balance)) {
							continue;
						} else {
							String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
							double b = Double.valueOf(availableBalance);
							if (b <= 0) {
								continue;
							}
						}
						accInfoList.add(termSubAccountMap);
					} else {
						// 账户状态不正常
						continue;
					}
				}// for
				if (StringUtil.isNullOrEmpty(accInfoList)) {
					return;
				} else {
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACCINFOLIST_KEY, accInfoList);
					listView.setAdapter(new ForexCustomerFixRateInfoAdapter(ForexCustomerFixRateInfoActivity.this,
							accInfoList));
				}
			}
		}
	}

	/**
	 * 顶部外汇账户按钮 查询所有的外汇交易账户列表
	 */
	private void customerPsnForexActAvai() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "customerPsnForexActAvaiCallback");
	}

	/**
	 * 顶部外汇账户按钮 查询所有的外汇交易账户列表----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void customerPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		} else {
			String type = null;
			int len = resultList.size();
			boolean flag = false;
			for (int i = 0; i < len; i++) {
				Map<String, String> map = resultList.get(i);
				String accountNumber = map.get(Forex.FOREX_ACCOUNTNUMBER_RES);
				if (account.equals(accountNumber)) {
					accountType = map.get(Forex.FOREX_ACCOUNTTYPE_RES);
					type = LocalData.AccountType.get(accountType);
					nickName = map.get(Forex.FOREX_NICKNAME_RES);
					BaseDroidApp.getInstanse().showCustomerResetAccDialog(account, nickName, type, listener);
					flag = true;
					break;
				}
			}
			if (!flag) {
				BaseDroidApp.getInstanse().createDialog(null,
						ForexCustomerFixRateInfoActivity.this.getResources().getString(R.string.forex_customer_acc));
				return;
			}

		}
	}

	/** 定期重设账户 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 我的外汇---账户重设定
			// 查询所有的账户
			BaseHttpEngine.showProgressDialog();
			requestPsnForexActAvai();
		}
	};

	/**
	 * 查询所有的账户----回调 不重写父类的方法
	 */
	public void requestPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList == null||resultList.size() == 0) {
			Intent intent = new Intent(ForexCustomerFixRateInfoActivity.this, ForexNoAccountActivity.class);
			startActivity(intent);
		} else {
			// 跳转到账户设置页面
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
			Intent intent = new Intent(ForexCustomerFixRateInfoActivity.this, ForexAccSettingActivity.class);
			startActivityForResult(intent, ConstantGloble.FOREX_CUSTOMER_RESETACC_FIX_ACTIVITY);// 501

		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.FOREX_CUSTOMER_RESETACC_FIX_ACTIVITY:// 重设账户 501
				// 查询用户绑定的账户类型
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseHttpEngine.showProgressDialog();
				ratePsnForexActIsset();
				break;
			default:
				break;
			}

			break;
		case RESULT_CANCELED:
			// 开通失败的响应，不关闭弹出框

			break;
		}
	};

	/** 查询用户绑定的账户类型----回调 */
	@Override
	public void ratePsnForexActIssetCallback(Object resultObj) {
		super.ratePsnForexActIssetCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		Map<String, String> investBindingInfo = (Map<String, String>) result
				.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
		String accountType = null;
		if (StringUtil.isNullOrEmpty(investBindingInfo)) {
			return;
		} else {
			String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
			// 存储投资账号
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
			String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
			// 存储账号
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
			accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
			if (StringUtil.isNull(accountType)) {
				return;
			}
		}
		if (accountType.equals(ConstantGloble.FOREX_ACCTYPE_DQYBT)) {
			// 定期
			currencyOrFixReq = 1;
			init();
			initData();
			initOnClick();
		} else {
			// 活期
			currencyOrFixReq = 2;
			initCurrency();
			initCurrOnClick();
		}
	}

	/** 初始化活期页面 */
	public void initCurrency() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View currInfoView = LayoutInflater.from(ForexCustomerFixRateInfoActivity.this).inflate(
				R.layout.forex_customer_main, null);
		tabcontent.removeView(rateInfoView);
		tabcontent.removeAllViews();
		tabcontent.addView(currInfoView);
		setTitle(getResources().getString(R.string.forex_customer));
		accList = new ArrayList<Map<String, Object>>();
		backButton = (Button) findViewById(R.id.ib_back);
		topCurrAccView = (View) findViewById(R.id.top_acc_layout);
		topAccText = (TextView) findViewById(R.id.customer_accNumber);
		accSellGridView = (ListView) findViewById(R.id.forex_accGridView);
		String account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_RATE_ACCOUNT_RES);
		if (StringUtil.isNullOrEmpty(account)) {
			return;
		} else {
			String acc = StringUtil.getForSixForString(account);
			topAccText.setText(acc);
			currencyOrFixReq = 2;
			// 查询该账户余额以及币种信息
			requestPsnForexQueryBlanceCucyList();
		}
	}

	/** 活期账户----顶部账户按钮事件 */
	private void initCurrOnClick() {
		topCurrAccView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerPsnForexActAvai();
			}
		});
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexCustomerFixRateInfoActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();

			}
		});
	}

	/** 活期账户信息-----回调 */
	public void requestCurrencyBlanceCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
		if (sellList == null||sellList.size() == 0) {
			return;
		} else {
			// 得到所有的子账户状态
			int len = sellList.size();
			for (int i = 0; i < len; i++) {
				String status = (String) sellList.get(i).get(Forex.FOREX_STATUS_RES);
				// 子账户状态正常
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)) {
					accList.add(sellList.get(i));
				} else {
					continue;
				}
			}// for

			if (accList == null||accList.size() <= 0) {
				return;
			} else {
				ForexCustomerRateInfoAdapter adapter = new ForexCustomerRateInfoAdapter(
						ForexCustomerFixRateInfoActivity.this, accList);
				adapter.setCustomerOnItemClickListener(customerOnItemClickListener);
				accSellGridView.setAdapter(adapter);
			}
		}

	}

	// 活期账户卖出按钮事件
	public OnItemClickListener customerOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			customerSelectedPosition = position;
			// 处理卖出币种集合
			dealSellCodeList();
			// 查询买入币种是否存在
			BaseHttpEngine.showProgressDialog();
			tradeConditionPsnForexQueryBuyCucyList();
		}

	};

	private void dealSellCodeList() {
		tradeSellCurrResultList = new ArrayList<Map<String, Object>>();
		int len = accList.size();
		// accList里的数据账户状态已正常
		for (int i = 0; i < len; i++) {
			Map<String, Object> balance = (Map<String, Object>) accList.get(i).get(Forex.FOREX_BALANCE_RES);
			if (!StringUtil.isNullOrEmpty(balance)) {
				String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
				double moneyBolance = Double.valueOf(availableBalance);
				Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
				// 币种存在
				if (!StringUtil.isNullOrEmpty(currency)) {
					String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
					// 币种不允许为人民币
					if (!StringUtil.isNull(code)
							&& (!code.equals(ConstantGloble.FOREX_RMB_TAG1) || !code
									.equalsIgnoreCase(ConstantGloble.FOREX_RMB_CNA_TAG2))) {
						// 账户余额大于0
						if (moneyBolance > 0) {
							tradeSellCurrResultList.add(accList.get(i));
							tradeConditionSellTag = true;
						}
					}

				}
			}
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_CURR_TRADESELLCODERESULTLIST, tradeSellCurrResultList);
	}

	/** 买入币种是否存在---回调 */
	public void tradeConditionPsnForexQueryBuyCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.forex_rateinfo_buy_codes));
			return;
		}
		tradeBuyCodeResultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tradeBuyCodeResultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexCustomerFixRateInfoActivity.this.getString(R.string.forex_rateinfo_buy_codes));
			return;
		} else {
			Intent intent = new Intent(ForexCustomerFixRateInfoActivity.this, ForexQuickCurrentSubmitActivity.class);
			intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_CUSTOMER_CURRENT_TAG);
			intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, customerSelectedPosition);
			startActivity(intent);
		}
	};
}
