package com.chinamworld.bocmbci.biz.forex.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexCustomerFixRateInfoAdapter;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexCustomerRateInfoAdapter;
import com.chinamworld.bocmbci.biz.forex.rate.ForexAccSettingActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickCurrentSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 我的外汇首页 活期账户：显示用户的账户以及账户余额 用户点击卖出，进入外汇买卖交易流程
 * 
 * @author Administrator
 * 
 */
public class ForexCustomerRateInfoActivity extends ForexBaseActivity {
	private static final String TAG = "ForexCustomerRateInfoActivity";
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	/** 外汇交易账户按钮 */
	private TextView topAccText = null;
	/** 顶部账户设置View */
	private View topAccView = null;
	private ListView accSellGridView = null;
	/**
	 * 活期----存储符合条件的卖出币种的全部信息-未进行处理
	 */
	private List<Map<String, Object>> accList = null;
	/** 账户别名 */
	private String nickName = null;
	/** 账户类型名称 */
	private String type = null;
	/** 活期适配器 */
	private ForexCustomerRateInfoAdapter adapter = null;

	// 默认选中第一个
	private int customerSelectedPosition = -1;
	/** 1-活期，2-定期 */
	private int currencyOrFix = 1;

	/** 账户重设View */
	private View topAccRestView = null;
	private ListView listView = null;
	/** 定期-- 符合条件的卖出账户信息 */
	private List<Map<String, Object>> accInfoList = null;
	/** 账户 */
	private String account = null;
	/** 是否刷新数据，isShow:false：不是 isShow:true是 */
	private boolean isShow = false;
	private ForexCustomerFixRateInfoAdapter fixAdapter = null;
	/** 主界面 */
	private Button btnRight = null;
	/** 1-请求接口，获取数据，2-设定账户，返回数据 */
	private int tag = 1;
	/** 活期-有账户余额 */
	private View currHasMoneyView = null;
	/** 活期-无账户余额 */
	private View currNoMoneyView = null;
	/** 定期-有账户余额 */
	private View fixHasMoneyView = null;
	/** 定期-无账户余额 */
	private View fixNoMoneyView = null;
	/** 1-有账户，2-没有账户 */
	private int hasMoney = 1;
	private TextView currentTitleText = null;
	private TextView fixTitleText = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.forex_customer));
		stopPollingFlag();
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回按钮
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexCustomerRateInfoActivity.this, SecondMainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				finish();
			}
		});
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		accInfoList = new ArrayList<Map<String, Object>>();
		accList = new ArrayList<Map<String, Object>>();
		taskTag = 2;
		menuOrTrade = 1;
		BaseHttpEngine.showProgressDialogCanGoBack();
		if (StringUtil.isNull(commConversationId)) {
			requestCommConversationId();
		} else {
			iscomformFootFastOrOther();
		}
	}

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	/** 用于区分是否是快捷键启动 */
	private void iscomformFootFastOrOther() {
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			// 快捷键启动
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		} else {
			if (isOpen && isSettingAcc) {
				// 判断加载定期布局还是活期布局
				customerPsnForexActIsset();
			} else {
				// 判断用户是否开通投资理财服务
				requestPsnInvestmentManageIsOpen();
			}
		}
	}

	/** 初始化活期布局页面 */
	public void init() {
		View currRateInfoView = LayoutInflater.from(ForexCustomerRateInfoActivity.this).inflate(
				R.layout.forex_customer_main, null);
		tabcontent.removeAllViews();
		tabcontent.addView(currRateInfoView);
		setLeftButtonPopupGone();
		topAccView = (View) findViewById(R.id.top_acc_layout);
		topAccText = (TextView) findViewById(R.id.customer_accNumber);
		currHasMoneyView = findViewById(R.id.has_acc);
		currNoMoneyView = findViewById(R.id.no_acc);
		currHasMoneyView.setVisibility(View.INVISIBLE);
		currentTitleText = (TextView) currRateInfoView.findViewById(R.id.current_title);
		accSellGridView = (ListView) findViewById(R.id.forex_accGridView);

	}

	/** 显示活期账户余额信息布局 */
	private void currentShowDateView() {
		if (hasMoney == 1) {
			currHasMoneyView.setVisibility(View.VISIBLE);
			currNoMoneyView.setVisibility(View.GONE);
		} else {
			currHasMoneyView.setVisibility(View.GONE);
			currNoMoneyView.setVisibility(View.VISIBLE);
			currentTitleText.setVisibility(View.VISIBLE);
		}
	}

	private void getDate() {
		if (tag == 1) {
			account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_RATE_ACCOUNT_RES);
			String accType = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
			type = LocalData.AccountType.get(accType);
			nickName = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_RATE_ACCOUNTNICKNAME_RES);
		} else {
			// 数据来自设定账户--选卡页面
			account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_RATE_ACCOUNT_RES);
			String accType = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.Forex_ACCOUNYTYPE_RES);
			type = LocalData.AccountType.get(accType);
			nickName = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_NICKNAME_RES);
		}
	}

	public void initData() {
		getDate();
		if (StringUtil.isNullOrEmpty(account)) {
			return;
		} else {
			String acc = StringUtil.getForSixForString(account);
			topAccText.setText(acc);
		}

	}

	public void initOnClick() {
		// 顶部账户设置区域
		topAccView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重设外汇交易账户
				BaseDroidApp.getInstanse().showCustomerResetAccDialog(account, nickName, type, listener);
			}
		});

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			iscomformFootFastOrOther();
		}
	}

	/**
	 * 判断是否开通投资理财服务---回调 快速交易
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.requestPsnInvestmentManageIsOpenCallback(resultObj);
		// 设定账户的请求
		requestPsnForexActIsset();
	}

	/**
	 * 交易条件
	 * 
	 * @任务提示框----判断是否设置账户
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnForexActIssetCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isSettingAcc = false;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				isSettingAcc = false;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

				if (StringUtil.isNullOrEmpty(investBindingInfo)) {
					isSettingAcc = false;
				} else {
					isSettingAcc = true;
				}
			}
		}
		if (!isOpen || !isSettingAcc) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}
		if (isOpen && isSettingAcc) {
			customerPsnForexActIsset();
		}

	}

	/***
	 * 页面初始化时需判断是定期还是活期，再加载页面 查询用户绑定的账户类型----回调
	 */
	public void customerPsnForexActIssetCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexCustomerRateInfoActivity.this.getResources().getString(R.string.forex_no_acc));
			return;
		}
		canTwoSided = (String) result.get(Forex.FOREX_RATE_CANTWOSIDED_RES);
		BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_CANTWOSIDED_RES, canTwoSided);
		Map<String, String> investBindingInfo = (Map<String, String>) result
				.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
		// 账户类型
		String accType = null;
		if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
			volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
			// 存储volumeNumberList
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
		}
		if (StringUtil.isNullOrEmpty(investBindingInfo)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexCustomerRateInfoActivity.this.getResources().getString(R.string.forex_no_acc));
			return;
		} else {
			String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
			// 存储投资账号
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
			String accountId = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTID_RES);
			// 存储accountId
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTID_RES, accountId);
			// 账户类型
			accType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTTYPE_RES, accType);
			// type = LocalData.AccountType.get(accountType);
			String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
			// 账户别名
			String nickName = investBindingInfo.get(Forex.Forex_RATE_ACCOUNTNICKNAME_RES);
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNTNICKNAME_RES, nickName);
			if (StringUtil.isNull(accType)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ForexCustomerRateInfoActivity.this.getResources().getString(R.string.forex_no_acc));
				return;
			}
		}
		if (accType.equals(ConstantGloble.FOREX_ACCTYPE_DQYBT)) {
			// 定期
			getFixView();
			currencyOrFix = 2;
		} else {
			// 活期
			getCurrencyView();
			currencyOrFix = 1;
		}
		BaseHttpEngine.canGoBack = false;
		// 查询账户余额信息
		requestPsnForexQueryBlanceCucyList();
	}

	/**
	 * 活期、定期 查询子账户状态、卖出币种、可用余额信息
	 */
	private void requestPsnForexQueryBlanceCucyList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		if (currencyOrFix == 1) {
			// 活期---回调
			HttpManager.requestBii(biiRequestBody, this, "requestPsnForexQueryBlanceCucyListCallback");
		} else {
			// 定期---回调
			HttpManager.requestBii(biiRequestBody, this, "requestFixQueryBlanceCucyListCallback");
		}

	}

	/** 得到活期布局以及页面数据 */
	private void getCurrencyView() {
		init();
		initData();
		initOnClick();
	}

	/** 得到定期的布局以及页面数据 */
	private void getFixView() {
		initFixView();
		initFixData();
		initFixOnClick();
	}

	/**
	 * 活期-----查询子账户状态、卖出币种、可用余额信息---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnForexQueryBlanceCucyListCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) {
			hasMoney = 2;
			currentShowDateView();
			return;
		}
		List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
		if (accList != null && !accList.isEmpty()) {
			// 将list数据清空
			accList.clear();
		}
		if (StringUtil.isNullOrEmpty(sellList)) {
			hasMoney = 2;
			currentShowDateView();
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
			if (StringUtil.isNullOrEmpty(accList)) {
				hasMoney = 2;
				currentShowDateView();
				return;
			} else {
				hasMoney = 1;
				currentShowDateView();
				isShow = true;
				adapter = new ForexCustomerRateInfoAdapter(ForexCustomerRateInfoActivity.this, accList);
				adapter.setCustomerOnItemClickListener(customerOnItemClickListener);
				accSellGridView.setAdapter(adapter);
			}
		}

	}

	/**
	 * 定期----查询定期账户余额、以及币种信息--回调 定期账户----回调 定期账户 定期账户
	 * 
	 * @param resultObj
	 */
	public void requestFixQueryBlanceCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			hasMoney = 2;
			fixShowDateView();
			return;
		}
		List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result
				.get(Forex.FORX_TERMSUBACCOUNT_RES);
		if (accInfoList != null && !accInfoList.isEmpty()) {
			// 将list数据清空
			accInfoList.clear();
		}
		if (StringUtil.isNullOrEmpty(termSubAccountList)) {
			hasMoney = 2;
			fixShowDateView();
			return;
		} else {
			int len = termSubAccountList.size();
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FORX_TERMSUBACCOUNT_RES, termSubAccountList);
			for (int i = 0; i < len; i++) {
				Map<String, Object> termSubAccountMap = termSubAccountList.get(i);
				Map<String, Object> balance = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
				String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
				// 存款种类
				String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
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
				hasMoney = 2;
				fixShowDateView();
				return;
			} else {
				hasMoney = 1;
				fixShowDateView();
				isShow = true;
				fixAdapter = new ForexCustomerFixRateInfoAdapter(ForexCustomerRateInfoActivity.this, accInfoList);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACCINFOLIST_KEY, accInfoList);
				listView.setAdapter(fixAdapter);
			}
		}
	}

	/** 活期重设账户 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 查询所有的外汇交易账户
			BaseHttpEngine.showProgressDialog();
			isCustomer = true;
			customerPsnForexActAvai();
		}
	};

	/**
	 * 置外汇账户 查询所有的账户
	 */
	private void customerPsnForexActAvai() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "customerPsnForexActAvaiCallback");
	}

	/** 重设外汇交易账户---- 查询所有的外汇交易账户---回调 */
	public void customerPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
			return;
		}
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList == null || resultList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
		// 跳转到账户设置页面
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), ForexAccSettingActivity.class);
		startActivityForResult(intent, ConstantGloble.FOREX_CUSTOMER_RESETACC_ACTIVITY);// 401

	};

	private OnItemClickListener customerOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			customerSelectedPosition = position;
			LogGloble.d(TAG, "curr  sell------");
			// 处理卖出币种集合
			dealSellCodeList();
			// 判断买入币种是否存在
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

	/** 查询买入币种是否存在----回调 */
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
		if (tradeBuyCodeResultList == null || tradeBuyCodeResultList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexCustomerRateInfoActivity.this.getString(R.string.forex_rateinfo_buy_codes));
			return;
		} else {
			getBuyCode(tradeBuyCodeResultList);
			Intent intent = new Intent(ForexCustomerRateInfoActivity.this, ForexQuickCurrentSubmitActivity.class);
			intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_CUSTOMER_CURRENT_TAG);
			intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, customerSelectedPosition);
			startActivity(intent);
		}
	};

	/** 处理买入币种信息 */
	private void getBuyCode(List<Map<String, String>> list) {
		List<Map<String, String>> buyCodeResultList = list;
		int len = buyCodeResultList.size();
		List<String> buyCodeList = new ArrayList<String>();
		List<String> buyCodeDealList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			Map<String, String> buyCodeResulMap = buyCodeResultList.get(i);
			String buyCodeResul = buyCodeResulMap.get(Forex.FOREX_BUY_CODE_RES).trim();
			String code = null;
			if (LocalData.Currency.containsKey(buyCodeResul)) {
				code = LocalData.Currency.get(buyCodeResul);
				buyCodeList.add(buyCodeResul);
				buyCodeDealList.add(code);
			} else {
				continue;
			}
		}
		if (buyCodeDealList == null || buyCodeDealList.size() <= 0 || buyCodeList == null || buyCodeList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY, buyCodeDealList);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODELIST_KEY, buyCodeList);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.FOREX_CUSTOMER_RESETACC_ACTIVITY:// 401--重设外汇交易账户
				// 查询外汇账户类型
				BaseHttpEngine.dissMissProgressDialog();
				BaseHttpEngine.showProgressDialogCanGoBack();
				tabcontent.removeAllViews();
				tag = 1;
				customerPsnForexActIsset();
				break;
			}

			break;
		case RESULT_CANCELED:
			// 开通失败的响应
			switch (requestCode) {
			case ConstantGloble.FOREX_CUSTOMER_RESETACC_ACTIVITY:// 401:
				if (isCustomer) {
					// 重设外汇交易账户
					BaseDroidApp.getInstanse().showCustomerResetAccDialog(account, nickName, type, listener);
				}
				break;
			default:
				break;
			}

			break;
		}
	}

	/** 加载定期View */
	private void initFixView() {
		View fixRateInfoView = LayoutInflater.from(ForexCustomerRateInfoActivity.this).inflate(
				R.layout.forex_customer_fix, null);
		tabcontent.removeAllViews();
		tabcontent.addView(fixRateInfoView);
		topAccText = (TextView) findViewById(R.id.customer_accNumber);
		topAccRestView = findViewById(R.id.top_acc_layout);
		listView = (ListView) findViewById(R.id.fix_listView);
		fixHasMoneyView = findViewById(R.id.has_acc);
		fixNoMoneyView = findViewById(R.id.no_acc);
		fixHasMoneyView.setVisibility(View.INVISIBLE);
		fixTitleText = (TextView) fixRateInfoView.findViewById(R.id.fix_title);
	}

	/** 显示定期账户数据信息 */
	private void fixShowDateView() {
		if (hasMoney == 1) {
			fixHasMoneyView.setVisibility(View.VISIBLE);
			fixNoMoneyView.setVisibility(View.GONE);
		} else {
			fixHasMoneyView.setVisibility(View.GONE);
			fixNoMoneyView.setVisibility(View.VISIBLE);
			fixTitleText.setVisibility(View.VISIBLE);
		}
	}

	/** 定期账户数据 */
	private void initFixData() {
		getDate();
		if (StringUtil.isNullOrEmpty(account)) {
			return;
		} else {

			String acc = StringUtil.getForSixForString(account);
			topAccText.setText(acc);
		}

	}

	public void initFixOnClick() {
		// 顶部账户重设
		topAccRestView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查询所有的外汇交易账户列表
				BaseDroidApp.getInstanse().showCustomerResetAccDialog(account, nickName, type, listener);
			}
		});
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.d(TAG, "我的外汇----------");
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
									HttpManager.stopPolling();
								} // 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// 非会话超时错误拦截
								// 区分定期活期，显示布局
								showContentView();
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissMessageDialog();

											}
										});
								return true;
							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常
		return super.httpRequestCallBackPre(resultObj);
	};

	/** 显示图片 */
	private void showContentView() {
		if (currencyOrFix == 1) {
			// 活期
			currHasMoneyView.setVisibility(View.GONE);
			currNoMoneyView.setVisibility(View.VISIBLE);
			currentTitleText.setVisibility(View.INVISIBLE);
		} else if (currencyOrFix == 2) {
			// 定期
			fixHasMoneyView.setVisibility(View.GONE);
			fixNoMoneyView.setVisibility(View.VISIBLE);
			fixTitleText.setVisibility(View.INVISIBLE);
		}
	}

//	private void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(ForexCustomerRateInfoActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (KeyEvent.KEYCODE_BACK == keyCode) {
////			ActivityTaskManager.getInstance().removeAllActivity();
////			Intent intent = new Intent();
////			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
////			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
//			goToMainActivity();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
