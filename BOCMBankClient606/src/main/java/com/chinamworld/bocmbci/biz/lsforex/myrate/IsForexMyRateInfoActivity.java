package com.chinamworld.bocmbci.biz.lsforex.myrate;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexBindAccountDetailAdapter;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexMyRateInfoAdapter;
import com.chinamworld.bocmbci.biz.lsforex.bail.IsForexBailInfoDetailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 我的双向宝 首页 */
public class IsForexMyRateInfoActivity extends IsForexBaseActivity implements OnClickListener {
	public static final String TAG = "IsForexMyRateInfoActivity";
	/**
	 * IsForexMyRateInfoActivity的主布局
	 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 交易账户 */
	private TextView accNumberText = null;
	private ListView accResultListView = null;
	/** 双向宝账户 */
	private String accountNumber = null;
	/** 双向宝持仓信息 */
	private List<Map<String, Object>> resultList = null;
	private IsForexMyRateInfoAdapter adapter = null;
	/** 顶部--双向宝账户区域 */
	private View topAccView = null;
	/** 网银账户标识 */
	private String accountId = null;
	private Button btnRight = null;
	private// 得到双向宝账户信息
	Map<String, String> accReaultMap = null;
	// /////////////////////////////////////////////////////
	/** 登陆按钮 */
	private Button mainUI = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.isForex_myRate_title));

		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
		
		taskTag = 1;
		BaseDroidApp.getInstanse().setCurrentAct(this);
		setLeftSelectedPosition("isForexStorageCash_3");
		init();
		// 请求登录后的commConversationId
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}




	private void init() {
		rateInfoView = LayoutInflater.from(IsForexMyRateInfoActivity.this).inflate(R.layout.isforex_myrate_main, null);
		tabcontent.addView(rateInfoView);
		accNumberText = (TextView) findViewById(R.id.customer_accNumber);
		accResultListView = (ListView) findViewById(R.id.fix_listView);
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(backBtnClick);
		topAccView = findViewById(R.id.top_acc_layout);
		topAccView.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(getResources().getString(R.string.isForex_myRate_top_right));
		btnRight.setOnClickListener(this);
	}

	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent1 = new Intent();
//			intent1.setClass(IsForexMyRateInfoActivity.this, SecondMainActivity.class);
//			startActivityForResult(intent1, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
			finish();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 判断是否开通投资理财服务
		requestPsnInvestmentManageIsOpen();

	}

	@Override
	public void requestMenuIsOpenCallback(Object resultObj) {
		super.requestMenuIsOpenCallback(resultObj);
		if (isOpen) {
			// 查询是否签约保证金产品
			searchBaillAfterOpen = 1;
			requestPsnVFGBailListQueryCondition();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			searchBaillAfterOpen = 2;
			getPop();
		}
	}

	/** 查询是否签约保证金产品--------回调 */
	@Override
	public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
		super.requestPsnVFGBailListQueryConditionCallback(resultObj);
		List<Map<String, String>> result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_SIGN);
		if (result == null || result.size() <= 0) {
			isSign = false;
			isSettingAcc = false;
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		} else {
			isSign = true;
			isCondition = true;
			// 是否设置双向宝账户
			requestPsnVFGGetBindAccount();
		}
	}

	/** 判断是否设置外汇双向宝账户 --------条件判断 */
	@Override
	public void requestConditionAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
			} else {
				isSettingAcc = true;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
			}
		}
		if (isOpen && isSign && isSettingAcc) {
			BaseDroidApp.getInstanse().dismissMessageDialogFore();
			// 查询双向宝持仓信息
			requestPsnVFGPositionInfo();
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}

	}

	/** 双向宝持仓信息----回调 */
	public void requestPsnVFGPositionInfoCallbank(Object resultObj) {
		// 是否需要查询双向宝交易账户信息
		isRequestBindAccinfo();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (resultList == null || resultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		resultList = dealResultDate(resultList);
		BaseHttpEngine.dissMissProgressDialog();
		if (resultList == null || resultList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		dealDate();
	}

	/** 是否需要查询双向宝交易账户信息 */
	private void isRequestBindAccinfo() {
		// 得到双向宝账户信息
		accReaultMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_ACCREAULTMAP);
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isCondition = false;
			// 查询双向宝交易账户信息
			requestPsnVFGGetBindAccount();
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 得到双向宝账户信息
			getBindAccInfo(accReaultMap);
		}
	}

	/** 双向宝账户信息----回调 */
	@Override
	public void requestMenuAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
			// 得到双向宝账户信息
			getBindAccInfo(accReaultMap);
		}
	}

	/** 得到双向宝交易账户信息 */
	private void getBindAccInfo(Map<String, String> accReaultMap) {
		accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
		accountId = accReaultMap.get(IsForex.ISFOREX_ACCOUNTID_RES);
		if (StringUtil.isNull(accountNumber)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			String number = StringUtil.getForSixForString(accountNumber);
			accNumberText.setText(number);
			BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_ACCOUNTNUMBER_REQ, accountNumber);

		}
	}

	/** 处理页面显示数据 */
	private void dealDate() {
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULTLIST_KEY, resultList);
		adapter = new IsForexMyRateInfoAdapter(IsForexMyRateInfoActivity.this, resultList);
		accResultListView.setAdapter(adapter);
	}

	/** 判断得到的数据是否正常 */
	private List<Map<String, Object>> dealResultDate(List<Map<String, Object>> lists) {
		int len = lists.size();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = lists.get(i);
			if (StringUtil.isNullOrEmpty(map)) {
				continue;
			}
			// 结算币种
			Map<String, String> settleCurrency = (Map<String, String>) map.get(IsForex.ISFOREX_SETTLECURRENCY_RES);
			if (StringUtil.isNullOrEmpty(settleCurrency)) {
				continue;
			}
			String settle = settleCurrency.get(IsForex.ISFOREX_CODE_RES);
			if (StringUtil.isNull(settle) || !LocalData.Currency.containsKey(settle)) {
				continue;
			}
			List<Map<String, Object>> details = (List<Map<String, Object>>) map.get(IsForex.ISFOREX_DETAILS_RES);
			if (details.size() <= 0 || details == null) {
				continue;
			}
			int lens = details.size();
			for (int j = 0; j < lens; j++) {
				Map<String, Object> detailsMap = details.get(j);
				Map<String, String> currency1 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY1_RES);
				Map<String, String> currency2 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY2_RES);
				if (StringUtil.isNullOrEmpty(currency1) || StringUtil.isNullOrEmpty(currency2)) {
					continue;
				}
				String code1 = currency1.get(IsForex.ISFOREX_CODE_RES);
				String code2 = currency2.get(IsForex.ISFOREX_CODE_RES);
				if (StringUtil.isNull(code1) || !LocalData.Currency.containsKey(code1) || StringUtil.isNull(code2)
						|| !LocalData.Currency.containsKey(code2)) {
					continue;
				}
				//清理空数据（双向宝持仓为0的货币对仍然展示，影响客户的使用；对于持仓为0.00（四舍五入后）的不予以展示）
				String balance = (String) detailsMap.get(IsForex.ISFOREX_BALANCE_RES);
				String mon = null;
				if (!StringUtil.isNull(balance)) {
					mon = StringUtil.parseStringPattern(balance, 2);
					if("0.00".equals(mon)){
						continue;
					}
				}else {
					continue;
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(ConstantGloble.ISFOREX_SETTLE, settle);
				resultMap.put(ConstantGloble.ISFOREX_DETAILSMAP, detailsMap);
				list.add(resultMap);
			}
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:// 返回按钮
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			goToMainActivity();
			break;
		case R.id.ib_top_right_btn:// 盈亏状态
			BaseHttpEngine.showProgressDialog();
			// / 查询保证金账户信息
//			requestPsnVFGGetBailAccountInfo();
			requestPsnVFGGetBailAccountInfos();
			break;
		case R.id.top_acc_layout:// 顶部双向宝账户
			if (StringUtil.isNull(accountId)) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			// 查询账户详情
			requestPsnAccountQueryAccountDetail(accountId);
			break;
		default:
			break;
		}
	}

	@Override
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
		super.requestPsnAccountQueryAccountDetailCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (!result.containsKey(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES)
				|| StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) result
				.get(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES);
		if (accountDetaiList == null || accountDetaiList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNull(accountNumber)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		// 显示双向宝账户详情
		showAccDetailInfo(accountNumber, accountDetaiList);
	}

	/**
	 * 点击投资基金，显示账户详细信息 加载新布局
	 */
	public void showAccDetailInfo(String accountNumber, List<Map<String, String>> list) {
		View accDetailInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bindaccount_detail, null);
		// 账户
		TextView accIdText = (TextView) accDetailInfoView.findViewById(R.id.finc_accountId);
		// 账户详细信息
		ListView accListView = (ListView) accDetailInfoView.findViewById(R.id.finc_accListView);
		// 保证金账户信息
		Button resetButton = (Button) accDetailInfoView.findViewById(R.id.isForex_bzjAccInfoButton);
		ImageView closeBtn = (ImageView) accDetailInfoView.findViewById(R.id.img_exit_accdetail);
		closeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
			}
		});
		accIdText.setText(StringUtil.getForSixForString(accountNumber));

		// 加载适配器
		accListView.setAdapter(new IsForexBindAccountDetailAdapter(IsForexMyRateInfoActivity.this, list));

		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// / 查询保证金账户信息
				requestPsnVFGGetBailAccountInfos();
			}
		});
		BaseDroidApp.getInstanse().showForexMessageDialog(accDetailInfoView);
	}

	@Override
	public void requestPsnVFGGetBailAccountInfoCallback(Object resultObj) {
		super.requestPsnVFGGetBailAccountInfoCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("list");
		if (result == null || result.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		// 跳转到保证金账户详情页面
		Intent intent2 = new Intent(this, IsForexBailInfoDetailActivity.class);
		startActivity(intent2);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			goToMainActivity();
		}
		return super.onKeyDown(keyCode, event);
	}
}
