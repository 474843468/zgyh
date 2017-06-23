package com.chinamworld.bocmbci.biz.lsforex.bail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/** 保证金交易首页 */
public class IsForexBailInfoActivity extends IsForexBaseActivity implements OnClickListener, OnItemSelectedListener {
	public static final String TAG = "IsForexBailInfoActivity";
	/**
	 * IsForexBailInfoActivity的主布局
	 */
	private View rateInfoView = null;
	/** 资金账户-账户类型 */
	private TextView zjAccTypeText = null;
	/** 资金账户-账户别名 */
	private TextView zjAccNickText = null;
	/** 资金账户-账户 */
	private TextView zjAccNumberText = null;
	/** 资金账户-币种 */
	private TextView zjAccCodeText = null;
	/** 资金账户-账户余额 */
	private TextView zjAccMoneyText = null;
	/** 保证金---账号 */
	private TextView bzjAccNumberText = null;
	/** 保证金---账户名称 */
	private TextView bzjAccNameText = null;
	/** 保证金-解算币种 */
	private TextView bzjAccCodeText = null;
	/** 保证金-账户余额 */
	private TextView bzjAccMoneyText = null;
	/** 转账设定title型 */
	private TextView titleText = null;
	/** 保证金账户信息 */
	 private Button bzjInfoButton = null;
	/** 资金账户转保证金账户 */
	private Button zjAccToBzjButton = null;
	/** 保证金账户转资金账户 */
	private Button bzjAccToZjButton = null;
	/** 返回 */
	private Button backButton = null;
	/** 币种 */
	private Spinner codeSpinner = null;
	/** 钞汇 */
	private Spinner cashSpinner = null;
	/** 转账金额 */
	private EditText inputMoneyEdit = null;
	/** 资金账户、保证金账户按钮区域 */
	private View twoButtonView = null;
	/** 输入转账金额区域 */
	private View inputMoneyView = null;

	/** 账户类型----代码 */
	private String accountType = null;
	/** 账号 */
	private String accountNumber = null;
	/** 账户别名 */
	private String nickName = null;
	/** 账户余额 */
	private String balance = null;
	/** 币种---代码 */
	private String currencyCode = null;
	/** 结算币种----代码 */
	private String jsCode = null;
	/** 账户余额 */
	private String jsMoney = null;
	/** 钞汇标志 */
	private String cashRemit = null;
	/** 保证金---账户 */
	private String bzjAccNumber = null;
	/** 保证金---账户名称 */
	private String bzjAccName = null;
	/** 用户输入的转账金额 */
	private String inputMoney = null;
	/** 保证金存入/转出标志，1：资金转保证金2：保证金转资金/ */
	private int flag = 1;
	/** 双向宝账户详情 */
	private List<Map<String, String>> accountDetaiList = null;
	/** 保证金账户信息 */
	private List<Map<String, Object>> result = null;
	/** 转账设定--币种代码 */
	private List<String> throwCodeList = null;
	/** 转账设定--币种代码名称 */
	private List<String> throwCodeNameList = null;
	/** 币种名称 */
	private String codeName = null;
	/** 币种代码 */
	private String codeCode = null;
	/** 操作方式代码 */
	private String typeCode = null;
	/** 钞汇币种区域 */
	private View cashView = null;
	/** 下一步 */
	private Button nextButton = null;
	/** 倒立三角形 */
	private TextView upTagText = null;
	/** 转账设定区域 */
	private View titleView = null;
	/** 下一步按钮区域 */
	private View nextButtonView = null;
	/** 0-未显示转账区域，1-资金转保证金，2-保证金转资金 */
	private int isShow = 0;
	private boolean isComoFromFast = false;
	private View choiseView = null;
	private View bailView = null;
	private View shwoBailInfoView = null;
//	private TextView leftText = null;
//	private TextView maxMoneyText = null;
	/** 结算币种是人民币或美元现钞时保证金的最大可用额 */
	private String bailMaxBalance = null;
	/** 结算币种美元现汇时保证金的最大可用额 */
	private String bailXHMaxBalance = null;
	private boolean isInit = true;
	private boolean isFirstReqMaxMoney = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_throw_title));
		BaseDroidApp.getInstanse().setCurrentAct(this);
		setLeftSelectedPosition("isForexStorageCash_2");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(backBtnClick);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		taskTag = 2;
		stopPollingFlag();
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}
	
	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent1 = new Intent();
//			intent1.setClass(IsForexBailInfoActivity.this, SecondMainActivity.class);
//			startActivityForResult(intent1, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
			finish();
		}
	};

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}

	/** 开通投资理财服务---回调 */
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
			showPop();
			return;
		} else {
			isSign = true;
			isCondition = true;
			// 是否设置双向宝账户
			requestPsnVFGGetBindAccount();
		}
	}

	/** 弹出任务提示框 */
	private void showPop() {
		BaseHttpEngine.dissMissProgressDialog();
		isOpen = true;
		isSign = false;
		isSettingAcc = false;
		// 弹出任提示框
		getPop();
	}

	/** 判断是否设置外汇双向宝账户 --isCondition = true------条件判断 */
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
			// 得到双向宝交易账户信息
			getBailDate(accReaultMap);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}

	}

	/** 双向宝交易账户信息----回调---点击左侧菜单----isCondition = false */
	@Override
	public void requestMenuAccountCallback(Object resultObj) {
		super.requestMenuAccountCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.acc_transferquery_null));
				return;
			} else {
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
				getBailDate(accReaultMap);
			}
		}

	}

	/** 得到双向宝交易账户信息 */
	private void getBailDate(Map<String, String> accReaultMap) {
		accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
		accountType = accReaultMap.get(IsForex.ISFOREX_ACCOUNTTYPE_RES);
		nickName = accReaultMap.get(IsForex.ISFOREX_NICKNAME_RES);
		String accountId = accReaultMap.get(IsForex.ISFOREX_ACCOUNTID_RES);
		if (StringUtil.isNull(accountId) || StringUtil.isNull(accountNumber) || StringUtil.isNull(accountType)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		// 查询账户详情
		requestPsnAccountQueryAccountDetail(accountId);
	}

	@Override
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
		super.requestPsnAccountQueryAccountDetailCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (!result.containsKey(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES)
				|| StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		accountDetaiList = (List<Map<String, String>>) result.get(IsForex.ISFOREX_ACCOUNTDETAILLIST_RES);
		if (accountDetaiList == null || accountDetaiList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		getRmbMoney();
		if (StringUtil.isNull(currencyCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		// 查询保证金账户信息
//		requestPsnVFGGetBailAccountInfo();
		requestPsnVFGGetBailAccountInfos();
	}

	/** 得到人民币余额 */
	private void getRmbMoney() {
		int len = accountDetaiList.size();
		boolean t = false;
		for (int i = 0; i < len; i++) {
			String code = accountDetaiList.get(i).get(IsForex.ISFOREX_CURRENCYCODE_RES);
			if (LocalData.rmbCodeList.contains(code)) {
				currencyCode = accountDetaiList.get(i).get(IsForex.ISFOREX_CURRENCYCODE_RES);
				balance = accountDetaiList.get(i).get(IsForex.ISFOREX_AVAILABLEBALANCE_RES);
				t = true;
				break;
			}
		}
		if (!t) {
			currencyCode = accountDetaiList.get(0).get(IsForex.ISFOREX_CURRENCYCODE_RES);
			balance = accountDetaiList.get(0).get(IsForex.ISFOREX_AVAILABLEBALANCE_RES);
		}
	}

	
	private Map<String, Object> CloneData(Map<String, Object> source,Map<String, Object> funditem)
	{
		Map<String,Object> map = new HashMap<String,Object>(source);
		List<Map<String,Object>> fundList = new ArrayList<Map<String,Object>>();
		fundList.add(funditem);
		map.put(IsForex.ISFOREX_FUNDLIST_RES,fundList);
		return map;
	}
	
	@Override
	public void requestPsnVFGGetBailAccountInfoCallback(Object resultObj) {
		super.requestPsnVFGGetBailAccountInfoCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		result = (List<Map<String, Object>>) map.get("list");
//		result = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (result == null || result.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
//		List<Map<String, Object>> fundListt = new ArrayList<Map<String, Object>>();
//		for(int i = 0; i < result.size();i++){
//			List<Map<String, Object>> fundList = (List<Map<String, Object>>)result.get(i).get(IsForex.ISFOREX_FUNDLIST_RES);
//			for(int j=0; j < fundList.size(); j++){
//				fundListt.add(CloneData(result.get(i),fundList.get(j)));
//			}
//		}
//		result = fundListt;
		
		
		
		
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		// 保证金账户默认选择第一个
		getBailInfo(0);
		isFirstReqMaxMoney = true;
		searchMaxMoney();
	}

	/** 查询最大金额 */
	private void searchMaxMoney() {
		if (LocalData.rmbCodeList.contains(jsCode)) {
			requestPsnVFGQueryMaxAmount(jsCode, ConstantGloble.ISFOREX_CASHREMIT_ZREO);
		} else {
			requestPsnVFGQueryMaxAmount(jsCode, ConstantGloble.ISFOREX_CASHREMIT_ONE);
		}
	}

	/** 得到保证金账户的信息 */
	private void getBailInfo(int position) {
		Map<String, Object> map = result.get(position);
		if (StringUtil.isNullOrEmpty(map)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Map<String, String> settleCurrency = (Map<String, String>) map.get(IsForex.ISFOREX_SETTLECURRENCY1_RES);
		if (StringUtil.isNullOrEmpty(settleCurrency)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		jsCode = settleCurrency.get(IsForex.ISFOREX_CODE1_RES);
//		List<Map<String, Object>> fundList = (List<Map<String,Object>>)map.get(IsForex.ISFOREX_FUNDLIST_RES);
//		String amount = (String)fundList.get(0).get(IsForex.ISFOREX_AMOUNT_RES);
//		jsMoney = amount;
		jsMoney = (String) map.get(IsForex.ISFOREX_MARGINFUND_RES);
		bzjAccNumber = (String) map.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES);
		bzjAccName = (String) map.get(IsForex.ISFOREX_MARGINACCOUNTNAME_RES);
		if (!StringUtil.isNull(jsCode)) {
			getSpinnerList(jsCode);
		}
	}

	/** 根据保证金账户币种，得到Spinnner的数据 */
	private void getSpinnerList(String jsCode) {
		throwCodeList = new ArrayList<String>();
		throwCodeNameList = new ArrayList<String>();
		if (LocalData.rmbCodeList.contains(jsCode)) {
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);
		} else if (LocalData.myCodeList.contains(jsCode)) {
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);
		} else if(LocalData.ouCodeList.contains(jsCode)){
//			038欧元
//			013港币
//			027日元
//			029澳元
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);

		}else if(LocalData.gangCodeList.contains(jsCode)){
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);
		}else if(LocalData.riCodeList.contains(jsCode)){
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);
		}else if(LocalData.aOCodeList.contains(jsCode)){
			String codeName = LocalData.Currency.get(jsCode);
			throwCodeList.add(jsCode);
			throwCodeNameList.add(codeName);
		}
		
	}

	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		// initAllDate();
		isFirstReqMaxMoney = true;
		searchMaxMoney();
	}

	/**
	 * 保证金账户可转出最大金额查
	 * 
	 * @param currencyCode
	 *            :签约货币--014-美元001-人民币
	 * @param cashRemit
	 *            :钞汇标识 0 ：人民币 1：钞 2：汇
	 */
	private void requestPsnVFGQueryMaxAmount(String currencyCode, String cashRemit) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGQUERYMAXAMOUNT_API);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODE_REQU, currencyCode);
		map.put(IsForex.ISFOREX_CASHREMIT_REQU, cashRemit);
		biiRequestBody.setParams(map);
		if (isInit) {
			HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGQueryMaxAmountCallback");
		} else {
			HttpManager.requestBii(biiRequestBody, this, "requestNOinitPsnVFGQueryMaxAmountCallback");
		}
	}

	/** 保证金账户可转出最大金额查----回调 */
	public void requestPsnVFGQueryMaxAmountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> bailMaxMoneyResultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(bailMaxMoneyResultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (LocalData.rebList.contains(jsCode)) {
			bailMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
			initAllDate();
		} else {
			if (!isFirstReqMaxMoney) {
				// 美元现汇
				bailXHMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
				initAllDate();
			} else {
				// 美元现钞最大值
				bailMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
				// 请求美元现汇最大值
				isFirstReqMaxMoney = false;
				requestPsnVFGQueryMaxAmount(jsCode, ConstantGloble.ISFOREX_CASHREMIT_TWO);
			}
		}

	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bail_main, null);
		tabcontent.addView(rateInfoView);
		zjAccTypeText = (TextView) findViewById(R.id.isforex_zjAccType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zjAccTypeText);
		zjAccNickText = (TextView) findViewById(R.id.isforex_zjAccNick);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zjAccNickText);
		zjAccNumberText = (TextView) findViewById(R.id.isforex_zjAccNum);
		zjAccCodeText = (TextView) findViewById(R.id.isforex_zjCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zjAccCodeText);
		zjAccMoneyText = (TextView) findViewById(R.id.isforex_zjAccMoney);
		bzjAccCodeText = (TextView) findViewById(R.id.isforex_bzjAccCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bzjAccCodeText);
		bzjAccMoneyText = (TextView) findViewById(R.id.isforex_bzjAccMoney);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bzjAccMoneyText);
		titleText = (TextView) findViewById(R.id.three_title);
		bzjAccNameText = (TextView) findViewById(R.id.isForex_numberName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bzjAccNameText);
		bzjAccNumberText = (TextView) findViewById(R.id.isForex_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bzjAccNumberText);
		bzjAccToZjButton = (Button) findViewById(R.id.bjzToZjAcc);
		bzjAccToZjButton.setOnClickListener(this);
		zjAccToBzjButton = (Button) findViewById(R.id.zjAccToBzj);
		bailView = findViewById(R.id.dept_in_info_layout);
		choiseView = findViewById(R.id.choise_layout);
		shwoBailInfoView = findViewById(R.id.showBaillInfoView);
		// choiseView.setOnClickListener(this);
		zjAccToBzjButton.setOnClickListener(this);
		twoButtonView = findViewById(R.id.layout_button);
		inputMoneyView = findViewById(R.id.inputMoney);
		inputMoneyEdit = (EditText) findViewById(R.id.forex_trade_sellmoney_Edit);
		codeSpinner = (Spinner) findViewById(R.id.forex_custoner_fix_code);
		codeSpinner.setOnItemSelectedListener(this);
		cashSpinner = (Spinner) findViewById(R.id.forex_customer_fix_cash);
		cashSpinner.setOnItemSelectedListener(this);
		cashView = findViewById(R.id.cash_layout);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		nextButton.setOnClickListener(this);
		upTagText = (TextView) findViewById(R.id.tv_neiku);
		titleView = findViewById(R.id.rl_save_title);
		nextButtonView = findViewById(R.id.nextButton_layout);
//		leftText = (TextView) findViewById(R.id.forex_trade_maxmoney_left);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
//		maxMoneyText = (TextView) findViewById(R.id.forex_trade_maxmoney);
		bailView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
						IsForexBailChoiseBailActivity.class);
				startActivityForResult(intent1, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);// 10
				overridePendingTransition(R.anim.push_up_in, R.anim.no_animation); // 选择保证金账户
				BaseHttpEngine.dissMissProgressDialog();
				LogGloble.d(TAG + "======isShow", isShow + "");
//				maxMoneyText.setText("");
			}
		});
		// 显示所有的保证金账户
		if (result.size() == 1) {
			shwoBailInfoView.setVisibility(View.VISIBLE);
			choiseView.setVisibility(View.GONE);
			bailView.setClickable(false);
		} else if (result.size() > 1) {
			choiseView.setVisibility(View.VISIBLE);
			shwoBailInfoView.setVisibility(View.GONE);
			bailView.setClickable(true);
		}
		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				IsForexBailInfoActivity.this,
				titleView,
				new String[] { getResources().getString(R.string.isForex_zjToBzj),
						getResources().getString(R.string.isForex_bzjToZj) }, new OnClickListener() {

					@Override
					public void onClick(View v) {

						switch (Integer.valueOf(String.valueOf(v.getTag()))) {
						case 0:// 资金转保证金
							selectedZjToBaj();
//							maxMoneyText.setText("");
							isShow = 1;
//							String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//							maxMoneyText.setText(money);
							break;
						case 1:// 保证金转资金
							selectedBzjToZj();
//							maxMoneyText.setText("");
							isShow = 2;
							String money1 = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//							maxMoneyText.setText(money1);
							break;

						default:
							break;
						}
					}

				});
		titleView.setClickable(false);
	}

	/** 初始化所有的数据 */
	private void initAllDate() {
		// 初始化界面
		init();
		initDate();
		BaseHttpEngine.dissMissProgressDialog();
		BaseHttpEngine.canGoBack = false;
	}

	/** 初始化页面数据 */
	private void initDate() {
		String type = LocalData.AccountType.get(accountType);
		zjAccTypeText.setText(type);
		zjAccNickText.setText(nickName);
		String number = StringUtil.getForSixForString(accountNumber);
		zjAccNumberText.setText(number);
		String code1 = LocalData.Currency.get(currencyCode);
		zjAccCodeText.setText(code1);
		String money = StringUtil.parseStringCodePattern(currencyCode, balance, fourNumber);
		if (StringUtil.isNullOrEmpty(balance)) {
			zjAccMoneyText.setText("-");
		} else {
			zjAccMoneyText.setText(money);
		}
		setBailValue();
	}

	/** 设置保证金账户控件信息 */
	private void setBailValue() {
		String code = LocalData.Currency.get(jsCode);
		bzjAccCodeText.setText(code);
		String m = StringUtil.parseStringCodePattern(jsCode, jsMoney, fourNumber);
		if (StringUtil.isNullOrEmpty(jsMoney)) {
			bzjAccMoneyText.setText("-");
		} else {
			bzjAccMoneyText.setText(m);
		}
		if (StringUtil.isNull(bzjAccName)) {
			bzjAccNameText.setText("-");
		} else {
			bzjAccNameText.setText(bzjAccName);
		}
		String cc = StringUtil.getForSixForString(bzjAccNumber);
		bzjAccNumberText.setText(cc);
	}

	/** 清空保证金账户控件信息 */
	private void clearBailValue() {
		bzjAccCodeText.setText("");
		bzjAccMoneyText.setText("");
		bzjAccNameText.setText("");
		bzjAccNumberText.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:// 返回
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			goToMainActivity();
			break;
		case R.id.bjzToZjAcc:// 保证金转资金
//			maxMoneyText.setText("");
			isShow = 2;
//			String money1 = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//			maxMoneyText.setText(money1);
			if (choiseView.isShown()) {
				CustomDialog.toastInCenter(this, getString(R.string.isForex_bail_choise_message));
			} else {
				titleView.setClickable(true);
				selectedBzjToZj();
			}

			break;
		case R.id.zjAccToBzj:// 资金转保证金
//			maxMoneyText.setText("");
			isShow = 1;
			String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//			maxMoneyText.setText(money);
			if (choiseView.isShown()) {
				CustomDialog.toastInCenter(this, getString(R.string.isForex_bail_choise_message));
			} else {
				titleView.setClickable(true);
				selectedZjToBaj();
			}

			break;
		case R.id.trade_nextButton:// 下一步
			cheanInputDate();
			break;
		default:
			break;
		}
	}

	/** 选择保证金转资金 */
	private void selectedBzjToZj() {
		titleText.setText(R.string.isForex_bzjToZj);
		twoButtonView.setVisibility(View.GONE);
		inputMoneyView.setVisibility(View.VISIBLE);
		nextButtonView.setVisibility(View.VISIBLE);
		flag = 2;
		typeCode = LocalData.isForexfundTransferDirList.get(1);
		setValue();
	}

	/** 选择资金转保证金 */
	private void selectedZjToBaj() {
		titleText.setText(R.string.isForex_zjToBzj);
		twoButtonView.setVisibility(View.GONE);
		inputMoneyView.setVisibility(View.VISIBLE);
		nextButtonView.setVisibility(View.VISIBLE);
		flag = 1;
		typeCode = LocalData.isForexfundTransferDirList.get(0);
		setValue();
	}

	/** 设置转账区域前提 */
	private void setValue() {
		upTagText.setVisibility(View.VISIBLE);
		isDateEmpty();
	}

	/** 判断结算币种是否为空 */
	private void isDateEmpty() {
		if (throwCodeNameList == null || throwCodeNameList.size() <= 0 || throwCodeList == null
				|| throwCodeList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		setInputMoneyViewDate();
	}

	/** 设置转账区域的值 */
	private void setInputMoneyViewDate() {
		// 币种
		ArrayAdapter<String> codeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, throwCodeNameList);
		codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		codeSpinner.setAdapter(codeAdapter);
		codeSpinner.setSelection(0);
		codeName = throwCodeNameList.get(0);
		codeCode = throwCodeList.get(0);
		codeIsRMB(codeCode);
	}

	/** 币种是否为人民币 ---钞汇处理方式 */
	private void codeIsRMB(String code) {
		if (LocalData.rebList.contains(code)) {
			// 币种为人民币，没有钞汇标志
			setCashSpinnerDate(LocalData.nullcashremitList);
			cashSpinner.setEnabled(false);
			cashSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_default));
			cashRemit = LocalData.isForexcashRemitList.get(2);

		} else {
			setCashSpinnerDate(LocalData.cashremitList);
			cashSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner));
			cashSpinner.setEnabled(true);
			cashSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
					if (LocalData.rmbCodeList.contains(codeCode)) {					
						cashRemit = LocalData.isForexcashRemitList.get(2);
//						if (isShow == 1) {
//							// 资金转保证金
//							maxMoneyText.setText("");
//							String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//							maxMoneyText.setText(money);
//						} else if (isShow == 2) {
//							maxMoneyText.setText("");
//							// 保证金转资金
//							String money = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//							maxMoneyText.setText(money);
//						}
					} else {
						switch (position) {
						case 0:// 现钞							
							cashRemit = LocalData.isForexcashRemitList.get(0);
							break;
//							if (isShow == 1) {
//								maxMoneyText.setText("");
//								// 资金转保证金
//								String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//								maxMoneyText.setText(money);
//							} else if (isShow == 2) {
//								maxMoneyText.setText("");
//								// 保证金转资金
//								String money = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//								maxMoneyText.setText(money);
//							}
//							break;
						case 1:// 现汇
							cashRemit = LocalData.isForexcashRemitList.get(1);
							break;
//							if (isShow == 1) {
//								maxMoneyText.setText("");
//								// 资金转保证金
//								String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//								maxMoneyText.setText(money);
//							} else if (isShow == 2) {
//								maxMoneyText.setText("");
//								// 保证金转资金
//								String money = StringUtil.parseStringCodePattern(jsCode, bailXHMaxBalance, 2);
//								maxMoneyText.setText(money);
//							}
//							break;

						default:
							break;
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

		}
	}

	/** 设置钞汇Spinner */
	private void setCashSpinnerDate(List<String> list) {
		ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, list);
		cashRemitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashSpinner.setAdapter(cashRemitAdapter);
		cashSpinner.setSelection(0);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.forex_custoner_fix_code:// 币种
			codeName = throwCodeNameList.get(position);
			codeCode = throwCodeList.get(position);
			// 根据币种，判断钞汇是否可选
			codeIsRMB(codeCode);

			break;
		case R.id.forex_customer_fix_cash:// 钞汇
//			maxMoneyText.setText("");
			if (LocalData.rmbCodeList.contains(codeCode)) {
				cashRemit = LocalData.isForexcashRemitList.get(2);
//				if (isShow == 1) {
//					// 资金转保证金
//					String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//					maxMoneyText.setText(money);
//				} else if (isShow == 2) {
//					// 保证金转资金
//					String money = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//					maxMoneyText.setText(money);
//				}
			} else {
				switch (position) {
				case 0:// 现钞
					cashRemit = LocalData.isForexcashRemitList.get(0);
					break;
//					if (isShow == 1) {
//						// 资金转保证金
//						String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//						maxMoneyText.setText(money);
//					} else if (isShow == 2) {
//						// 保证金转资金
//						String money = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//						maxMoneyText.setText(money);
//					}
//					break;
				case 1:// 现汇
					cashRemit = LocalData.isForexcashRemitList.get(1);
					break;
//					if (isShow == 1) {
//						// 资金转保证金
//						String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//						maxMoneyText.setText(money);
//					} else if (isShow == 2) {
//						// 保证金转资金
//						String money = StringUtil.parseStringCodePattern(jsCode, bailXHMaxBalance, 2);
//						maxMoneyText.setText(money);
//					}
//					break;
//
				default:
					break;
				}
				break;

			}
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/** 验证用户输入的数据 */
	private void cheanInputDate() {
		inputMoney = inputMoneyEdit.getText().toString().trim();
		// 使用正则验证
		checkDateValue();
	}

	/** 使用正则表达式验证用户输入的数据 */
	private void checkDateValue() {
		if (tradeCheckCodeNoNumber.contains(codeCode)) {
			RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.isForex_throw_money), inputMoney,
					"spetialAmount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				gotoConfirmActivity();
			} else {
				return;
			}
		} else {
			RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.isForex_throw_money), inputMoney,
					"amount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				gotoConfirmActivity();
			} else {
				return;
			}
		}

	}

	/** 跳转到确认页面 */
	private void gotoConfirmActivity() {
		Intent intent = new Intent(this, IsForexBailInfoConfirmActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ, accountNumber);// 资金账户
		intent.putExtra(ConstantGloble.ISFOREX_FUNDTRANSFERDIR, typeCode);// 操作方式代码
		intent.putExtra(IsForex.ISFOREX_CASEREMIT_RES, cashRemit);// 钞汇代码
		intent.putExtra(IsForex.ISFOREX_CODE1_RES, codeCode);// 币种代码
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODE_RES, codeName);// 币种名称
		intent.putExtra(ConstantGloble.ISFOREX_INPUTMONEY, inputMoney);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		BaseHttpEngine.dissMissProgressDialog();
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE:// 保证金交易
				tabcontent.removeAllViews();
				BaseHttpEngine.showProgressDialogCanGoBack();
				isCondition = false;
				isInit = true;
				requestPsnVFGGetBindAccount();
				break;
			case ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE:// 重新选择保证金
				int position = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_CASEREMIT_RES);
//				maxMoneyText.setText("");
				isInit = false;
				isFirstReqMaxMoney = true;
				clearBailValue();
				// 得到保证金账户信息
				getBailInfo(position);
				setBailValue();
				shwoBailInfoView.setVisibility(View.VISIBLE);
				choiseView.setVisibility(View.GONE);
				if (isShow == 1) {
					// 资金转保证金
					titleView.setClickable(true);
					String money = StringUtil.parseStringCodePattern(jsCode, balance, 2);
//					maxMoneyText.setText(money);
					selectedZjToBaj();
				} else if (isShow == 2) {
					// 保证金转资金
					BaseHttpEngine.showProgressDialog();
					searchMaxMoney();
				} else if (isShow == 0) {
					// 保证金转资金
					BaseHttpEngine.showProgressDialog();
					searchMaxMoney();
				}
				break;
			default:
				break;
			}

			break;

		default:// 失败
			break;
		}
	}

	/** 保证金账户可转出最大金额查----回调 */
	public void requestNOinitPsnVFGQueryMaxAmountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> bailMaxMoneyResultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(bailMaxMoneyResultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (LocalData.rebList.contains(jsCode)) {
			bailMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
			showViewByIsShow();
			BaseHttpEngine.dissMissProgressDialog();
		} else {
			if (!isFirstReqMaxMoney) {
				// 美元现汇
				bailXHMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
				showViewByIsShow();
				BaseHttpEngine.dissMissProgressDialog();
			} else {
				// 美元现钞最大值
				bailMaxBalance = bailMaxMoneyResultMap.get(IsForex.ISFOREX_MAXBALABCE_RES);
				// 请求美元现汇最大值
				isFirstReqMaxMoney = false;
				requestPsnVFGQueryMaxAmount(jsCode, ConstantGloble.ISFOREX_CASHREMIT_TWO);

			}
		}

	}

	private void showViewByIsShow() {
		if (isShow == 2) {
//			String money = StringUtil.parseStringCodePattern(jsCode, bailMaxBalance, 2);
//			maxMoneyText.setText(money);
			titleView.setClickable(true);
			setInputMoneyViewDate();
		} else if (isShow == 0) {
			setBailValue();
		}
	}
}
