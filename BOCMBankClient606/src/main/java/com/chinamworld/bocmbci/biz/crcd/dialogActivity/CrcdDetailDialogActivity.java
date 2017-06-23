package com.chinamworld.bocmbci.biz.crcd.dialogActivity;

import com.chinamworld.bocmbci.base.activity.BaseActivity;

/**
 * 我的信用卡------信用卡详情
 * 
 * @author huangyuchao
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class CrcdDetailDialogActivity extends BaseActivity {/*
	private static final String TAG = "CrcdDetailDialogActivity";
	// 详情视图
	private RelativeLayout view;
	RelativeLayout rl_bank;
	*//** 用户选择的信用卡数据 *//*
	Map<String, Object> crcdAccount;
	*//** 用户选择的信用卡的币种对应的详情 *//*
	Map<String, Object> mapResult;
	*//** 当前币种代码 *//*
	String currency;
	String currency1, currency2;
	*//** 信用卡类型 *//*
	String cardType;
	private String accountId = null;
	private String accountNumber = null;
	*//** 1-消费服务设置，2-对账单服务,3-附属卡服务定制,4-信用卡购汇还款 *//*
	private int tag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crcd_for_dialog);
		initData();
	}

	public void initData() {
		crcdAccount = MyCreditCardActivity.crcdAccount;
		accountId = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTID_RES);
		mapResult = MyCreditCardActivity.resultDetail;
		accountNumber = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		cardType = MyCreditCardActivity.cardType;
		if (!StringUtil.isNull(MyCreditCardActivity.currency1)) {
			currency1 = MyCreditCardActivity.currency1;
		}
		if (!StringUtil.isNull(MyCreditCardActivity.currency2)) {
			currency2 = MyCreditCardActivity.currency2;
		}
		if (!StringUtil.isNull(currency1)) {
			currency = currency1;
			init();
			return;
		} else {
			if (!StringUtil.isNull(currency2)) {
				currency = currency2;
				init();
				return;
			} else {
				return;
			}
		}
	}

	public void init() {
		BaseDroidApp.getInstanse().setDialogAct(true);

		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);

		InflaterViewDialog dialog = new InflaterViewDialog(this);
		// 弹出信用卡详情页面
		view = (RelativeLayout) dialog
				.initCrcdMessageDialogView(crcdAccount, mapResult,
						crcdSetupClick, exitAccDetailClick,
						updatenicknameClick, renmibiClick, dollerClick,
						currency, currency1, currency2, cardType, gotoTransfer,
						greatwallListener);

		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	*//** 退出账户详情监听事件 *//*
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	*//** 不在使用 *//*
	protected OnClickListener updatenicknameClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}

	};

	OnClickListener crcdSetupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showCrcdSetupTypeDialog(onclickListener);
		}

	};

	OnClickListener onclickListener = new OnClickListener() {

		public void onClick(View v) {

		};
	};

	*//** 人民币监听事件 *//*
	protected OnClickListener renmibiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!StringUtil.isNull(currency1)) {
				currency = currency1;
				requestPsnCrcdQueryAccountDetail(crcdAccount, currency);
			}

		}
	};

	*//** 外币监听事件 *//*
	protected OnClickListener dollerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 币种2不能为空
			if (!StringUtil.isNull(currency2)) {
				currency = currency2;
				requestPsnCrcdQueryAccountDetail(crcdAccount, currency);
			}

		}
	};

	*//**
	 * 请求查询信用卡详情
	 *//*
	public void requestPsnCrcdQueryAccountDetail(Map<String, Object> map,
			String value) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		String accountId = (String) map.get(Crcd.CRCD_ACCOUNTID_RES);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, value);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryAccountDetailCallBack");
	}

	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		mapResult = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(mapResult)) {
			return;
		}
		TranDataCenter.getInstance().setCurrInDetail(mapResult);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		view = (RelativeLayout) inflaterdialog
				.initCrcdMessageDialogView(crcdAccount, mapResult,
						crcdSetupClick, exitAccDetailClick,
						updatenicknameClick, renmibiClick, dollerClick,
						currency, currency1, currency2, cardType, gotoTransfer,
						greatwallListener);
		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	*//** 底部按钮监听事件 *//*
	OnClickListener gotoTransfer = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 转账需要的map
			// Intent it = new Intent(CrcdDetailDialogActivity.this,
			// TransferManagerActivity1.class);
			// it.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
			// Crcd.TRANS_CREDIT);
			switch (v.getId()) {
			case R.id.btn_shoukuan:// 转入
				Intent it = new Intent(CrcdDetailDialogActivity.this,
						TransferManagerActivity1.class);
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
						Crcd.TRANS_CREDIT);
				Crcd.tranState = Crcd.TRANS_IN;
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_OPER_TYPE,
						Crcd.tranState);
				it.putExtra(ConstantGloble.TRANS_ACCOUNT_TRANSFER_TYPE,
						Crcd.TRANS_GUALIAN);
				it.putExtra(Crcd.TRANSTATE, Crcd.tranState);
				startActivity(it);
				break;
			case R.id.btn_fukuan:// 转出
				Intent it1 = new Intent(CrcdDetailDialogActivity.this,
						TransferManagerActivity1.class);
				it1.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
						Crcd.TRANS_CREDIT);
				Crcd.tranState = Crcd.TRANS_OUT;
				it1.putExtra(ConstantGloble.TRANS_ACCOUNT_OPER_TYPE,
						Crcd.tranState);
				it1.putExtra(Crcd.TRANSTATE, Crcd.tranState);
				startActivity(it1);
				break;
			case R.id.btn_xinyonghuan:// 信用卡还款
				Intent it2 = new Intent(CrcdDetailDialogActivity.this,
						TransferManagerActivity1.class);
				it2.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
						Crcd.TRANS_CREDIT);
				Crcd.tranState = Crcd.TRANS_IN;
				TranDataCenter.getInstance().setModuleType(
						ConstantGloble.CRCE_TYPE);
				it2.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
						ConstantGloble.REL_CRCD_REPAY);
				startActivity(it2);
				finish();
				break;
			case R.id.btn_gohuihuan:// 购汇
				List<Map<String, String>> crcdAccountDetailList = (List<Map<String, String>>) mapResult
						.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
				Map<String, String> map = crcdAccountDetailList.get(0);
				String currentbalance = String.valueOf(map
						.get(Crcd.CRCD_CURRENTBALANCE));
				// 账面余额标志位
				String currentBalanceflag = String.valueOf(map
						.get(Crcd.CRCD_CURRENTBALANCEFLAG));
				if (StringUtil.isNull(currentBalanceflag)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.mycrcd_waibi_nojieqian));
					return;
				}
				if (ConstantGloble.CRCD_SEARCH_ZERO.equals(currentBalanceflag)) {
					// 判断账面余额是0
					// 信用卡购汇还款
					tag = 4;
					requestCommConversationId();
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.mycrcd_waibi_nojieqian));
					return;
				}
				break;
			}

		}
	};

	*//** 长城、单外币信用卡---点击“更多”监听事件 *//*
	OnClickListener greatwallListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			switch (tag) {
			case 0:// 消费服务设置
				xiaoFeiSet();
				break;
			case 1:// 对账单服务
				accSreviceSet();
				break;
			case 2:// 还款方式设定
				paymentSet();
				break;
			case 3:// 附属卡服务设定
				fuShuKaSet();
				break;
			case 4:// 信用卡挂失/补卡
				String nick = (String) crcdAccount.get(Crcd.CRCD_NICKNAME_RES);
				String type = (String) crcdAccount
						.get(Crcd.CRCD_ACCOUNTTYPE_RES);
				String strAccountType = LocalData.AccountType.get(type);
				Intent intent = new Intent(CrcdDetailDialogActivity.this,
						CrcdGuashiInfoActivity.class);
				intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				intent.putExtra(Crcd.CRCD_NICKNAME_RES, nick);
				intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
				finish();
				break;
			}
			Button tv = (Button) v;
			String text = tv.getText().toString();
			if (text.equals(ConstantGloble.ACC_CRCD_TRAN)) {
				Intent it2 = new Intent(CrcdDetailDialogActivity.this,
						TransferManagerActivity1.class);
				TranDataCenter.getInstance().setAccOutInfoMap(crcdAccount);
				it2.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
						Crcd.TRANS_CREDIT);
				Crcd.tranState = Crcd.TRANS_IN;
				TranDataCenter.getInstance().setModuleType(
						ConstantGloble.CRCE_TYPE);
				TranDataCenter.getInstance().setCurrOutDetail(mapResult);
				it2.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
						ConstantGloble.ACC_TO_TRAN_CRCD);
				startActivity(it2);
				finish();
			}else if(text.equals(ConstantGloble.ACC_TRAN_REMIT)){
				Intent it3 = new Intent(CrcdDetailDialogActivity.this,
						TransferManagerActivity1.class);
				TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
				it3.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
						Crcd.TRANS_CREDIT);
				Crcd.tranState = Crcd.TRANS_IN;
				TranDataCenter.getInstance().setModuleType(
						ConstantGloble.CRCE_TYPE);
				TranDataCenter.getInstance().setCurrInDetail(mapResult);
				it3.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
						ConstantGloble.ACC_TO_TRAN_REMIT);
				startActivity(it3);
				finish();
			}
		}

	};

	*//** 消费服务设置 *//*
	private void xiaoFeiSet() {
		tag = 1;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (tag == 1) {
			psnCrcdServiceSetInput(accountId);
		} else if (tag == 2) {
			psnCrcdQueryCheck(accountId);
		} else if (tag == 3) {
			psnCrcdAppertainTranSetQuery(accountId);
		} else if (tag == 4) {
			LogGloble.d(TAG, "accountId=======" + accountId);
			// 查询信用卡购汇还款信息
			requestForCrcdForeignPayOff();
		}

	}

	*//**
	 * 信用卡查询购汇还款信息
	 *//*
	public void requestForCrcdForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.CREDITCARD_CRCDID_REQ, accountId);// 转入账户信息 crcdId
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdForeignPayOffCallBack");
	}

	*//**
	 * 信用卡查询购汇还款信息返回
	 * 
	 * @param resultObj
	 *//*
	public void requestForCrcdForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);

		Intent intent = new Intent(this, TransferManagerActivity1.class);
		Crcd.tranState = Crcd.TRANS_GOHUAN;
		TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
				ConstantGloble.REL_CRCD_BUY);
		if (StringUtil.isNull(currency2)) {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency1);
		} else {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency2);
		}
		intent.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
		finish();
		startActivity(intent);
	}

	*//** 消费服务设置-----信用卡服务设置定制信息输入 *//*
	public void psnCrcdServiceSetInput(String accountId) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSERVICESETINPUT);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdServiceSetInputCallBack");
	}

	*//** 消费服务设置-----信用卡服务设置定制信息输入-----回调 *//*
	public void psnCrcdServiceSetInputCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, String> returnMap = (Map<String, String>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String shortMsgLimitAmount = returnMap
				.get(Crcd.CRCD_SHORTMSGLIMITAMOUNT);
		String posLimitAmount = returnMap.get(Crcd.CRCD_POSLIMITAMOUNT);
		String posFlag = returnMap.get(Crcd.CRCD_POSFLAG);
		Intent it = new Intent(CrcdDetailDialogActivity.this,
				CrcdServiceSetupDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, currency);
		it.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 1);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 1);
		it.putExtra(Crcd.CRCD_SHORTMSGLIMITAMOUNT, shortMsgLimitAmount);
		it.putExtra(Crcd.CRCD_POSLIMITAMOUNT, posLimitAmount);
		it.putExtra(Crcd.CRCD_POSFLAG, posFlag);
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
		// startActivity(it);
	}

	*//** 对账单服务 *//*
	private void accSreviceSet() {
		tag = 2;
		requestCommConversationId();
	}

	*//** 信用卡账单查询 *//*
	private void psnCrcdQueryCheck(String accountId) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCHECK);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdQueryCheckCallBack");
	}

	*//** 信用卡账单查询----回调 *//*
	public void psnCrcdQueryCheckCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, String> returnMap = (Map<String, String>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Intent it = new Intent(this, CrcdPsnQueryCheckDetail.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, currency);
		it.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 2);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 2);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_RESULT_KEY, returnMap);
		BaseHttpEngine.dissMissProgressDialog();
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);
	}

	*//** 还款方式设定 *//*
	private void paymentSet() {
		psnCrcdQueryCrcdPaymentWay(accountId);
	}

	*//** 信用卡还款方式查询 *//*
	private void psnCrcdQueryCrcdPaymentWay(String accountId) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCRCDPAYMENTWAY_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "getqueryPaymentCallBack");
	}

	*//** 信用卡还款方式查询 ------回调 *//*
	public void getqueryPaymentCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Intent it = new Intent(this, MyCrcdSetupDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 3);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 3);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, returnMap);
		BaseHttpEngine.dissMissProgressDialog();
		// startActivity(it);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
	}

	*//** 附属卡服务定制 *//*
	private void fuShuKaSet() {
		tag = 3;
		requestCommConversationId();
	}

	*//** 附属卡查询 *//*
	private void psnCrcdAppertainTranSetQuery(String accountId) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdAppertainTranSetQueryCallBack");
	}

	*//** 附属卡查询结果 *//*
	public void psnCrcdAppertainTranSetQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body
				.getResult();
		Intent it = new Intent(this, MySupplymentDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 4);
		it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, 4);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, returnList);
		// startActivity(it);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BaseDroidApp.getInstanse().setDialogAct(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:// 成功
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE:// 消费服务设置
				finish();
				break;
			case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 对账单服务
				finish();
				break;
			case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 还款方式设定
				finish();
				break;
			case ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE:// 附属卡服务定制
				finish();
				break;
			case ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE:// 信用卡挂失及补卡
				finish();
				break;
			default:
				break;
			}
			break;

		case RESULT_CANCELED:// 失败
			break;
		}
	}
*/
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
