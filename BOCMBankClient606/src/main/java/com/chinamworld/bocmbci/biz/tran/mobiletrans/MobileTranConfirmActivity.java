package com.chinamworld.bocmbci.biz.tran.mobiletrans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 
 * @author WJP
 * 
 */
@SuppressWarnings("ResourceType")
public class MobileTranConfirmActivity extends TranBaseActivity implements
		OnClickListener {

	private static final String TAG = MobileTranConfirmActivity.class
			.getSimpleName();
	/** 上一步按钮 */
	private Button mLastBtn = null;
	/** 下一步按钮 */
	private Button mNextBtn = null;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 执行方式 */
	// private TextView exeTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;
	/** 执行方式 */
	// private String relExecuteType = null;

	/** 手机交易码 */
	private LinearLayout smcLinearLayout;
	private SipBox sipBoxSmc = null;
	/** 动态口令卡 加密控件 */
	private LinearLayout optLinearLayout;
	private SipBox sipBoxOpt = null;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	private Button smsbtn;
	// private String combinId;
	// 手机交易码
	private String smc = "";
	// 动态口令
	private String otp = "";
	private String smc_rc = "";
	private String otp_rc = "";
	private Map<String, Object> mobileTranMap = null;

	/** 转出账户昵称 */
	private TextView accoutNicknameTv;
	/** 收款人姓名 */
	private TextView accinNickNameTv;

	/** 应收费用 */
	private TextView shouldChargeTv;
	/** 拟收费用 */
	private TextView fakeChargeTv;

	private String accOutNo;
	private String outNickName;

	// /**转账类型*/
	// private TextView tranTypeTv;
	// 转出账户
	// private String dueDate;
	private String fromAccountId;
	// private String executeType;
	/** 收款人手机号 */
	private TextView payeeMobileTv;
	private String mobileNumber;
	/** 收款人姓名 */
	private String payeeName;
	/** 转入账户地区 */
	private String toAccountArea;
	private String toAccountType;
	private String currencySer;
	private String isHaveAcct;
	private String signData;
	private String tokenId;
	// 转账日期 //立即执行：该字段为空，BII自动取为当前系统日期
	// 预约日期执行：为预约日期
	// 预约周期执行：为起始日期
	/** 手机交易码有效时间 */
	// private String smcTrigerInterval;
	// private int smcTime;

	private String amount;
	private String remark;

	private String randomNumber;

	// private List<Map<String, Object>> factorList;

	/** 应收手续费 */
	private String shouldCharge;
	/** 实收手续费 */
	private String fakeCharge;

	private TextView titleTv;
	/** 动态口令 */
	private Boolean isOtp;
	/** 手机短信 */
	private Boolean isSmc;
	/**基准费用*/
	private LinearLayout ll_need; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.mobile_trans));
		View view = mInflater.inflate(R.layout.tran_mobile_confirm_activity,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		/** 预交易请求返回数据 */
		mobileTranMap = TranDataCenter.getInstance().getMobileTranCallBackMap();
		// 请求随机数
		requestForRandomNumber();
		// setupView();
		// initData();
		// displayTextView();
		// // 请求随机数
		// requestForRandomNumber();
	}

	/**
	 * 显示数据
	 */
	private void initData() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		accOutNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		fromAccountId = (String) accOutInfoMap.get(Tran.ACC_ACCOUNTID_REQ);
		// 转出账户昵称
		outNickName = (String) accOutInfoMap.get(Tran.NICKNAME_RES);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		// 账户名称
		payeeName = userInputMap.get(Tran.INPUT_PAYEE_NAME);
		// 收款人手机号
		mobileNumber = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
			String remitSetMealFlag = (String) chargeMissionMap
					.get(Tran.REMITSETMEAL_FLAG);
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&&!chargeMissionMap.containsKey(Tran.NEED_COMMISSION_CHARGE)){
				
			}else{
				ll_need.setVisibility(View.VISIBLE);	
			}
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag));
				}
			}
			// 应收费用
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
				
			}
			// 拟收费用
			fakeCharge = (String) chargeMissionMap
					.get(Tran.PRECOMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(fakeCharge)) {
				fakeCharge = StringUtil.parseStringPattern(fakeCharge, 2);
				
			}
			// shouldChargeTv.setText(StringUtil.isNull(shouldCharge) ? "-"
			// : shouldCharge);
			// fakeChargeTv.setText(StringUtil.isNull(fakeCharge) ? "-"
			// : fakeCharge);
		}

		// mobileTranMap = TranDataCenter.getInstance()
		// .getMobileTranCallBackMap();
		// 转入账户地区
		toAccountArea = (String) mobileTranMap.get(Tran.TRANS_PAYEE_BANK_NUM);
		if (StringUtil.isNullOrEmpty(toAccountArea)) {
			toAccountArea = "-";
		}

		toAccountType = (String) mobileTranMap.get(Tran.TRANS_TO_ACCOUNT_TYPE);
		isHaveAcct = (String) mobileTranMap.get(Tran.IS_HAVEACCT);
		signData = (String) mobileTranMap.get(Tran.CREDIT_CARD__PLAINDATA_RES);

		currencySer = ConstantGloble.PRMS_CODE_RMB;
		// combinId = userInputMap.get(Tran._COMBINID_REQ);
		//
		// smcTrigerInterval = (String) mobileTranMap
		// .get(Tran.CREDIT_CARD_SMCTRIGERINTERVAL_RES);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> factorList = (List<Map<String, Object>>) mobileTranMap
				.get(Login.FACTORLIST);
		// TODO 时间带确认
		// if (!StringUtil.isNullOrEmpty(smcTrigerInterval)) {
		// smcTime = Integer.parseInt(smcTrigerInterval);
		// } else {
		// smcTime = 60;
		// }
		if (StringUtil.isNullOrEmpty(factorList)) {
			return;
		}
		// if (factorList.size() == 1) {
		// @SuppressWarnings("unchecked")
		// Map<String, String> field = (Map<String, String>) factorList.get(0)
		// .get(Login.FIELD);
		// String name = field.get(Login.NAME);
		// if (name.equals(Login.OTP)) {// 动态口令
		// optLinearLayout.setVisibility(View.VISIBLE);
		// } else if (name.equals(Login.SMC)) {// 手机验证码
		// smcLinearLayout.setVisibility(View.VISIBLE);
		// }
		// } else {
		// optLinearLayout.setVisibility(View.VISIBLE);
		// smcLinearLayout.setVisibility(View.VISIBLE);
		// }

	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		titleTv = (TextView) findViewById(R.id.tv_content);
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);
		mLastBtn = (Button) findViewById(R.id.btn_last_trans_rel_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);

		accOutTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		payeeMobileTv = (TextView) findViewById(R.id.tv_acc_in_mobile);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_rel_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		// exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);

		shouldChargeTv = (TextView) findViewById(R.id.tran_commission_charge_tv);
		fakeChargeTv = (TextView) findViewById(R.id.tran_commission_fake_tv);

		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		if (isSmc) {
			smcLinearLayout = (LinearLayout) findViewById(R.id.tran_smc_layout);
			smcLinearLayout.setVisibility(View.VISIBLE);
			sipBoxSmc = (SipBox) findViewById(R.id.et_smc_confirm_info);
			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
			sipBoxSmc.setPasswordRegularExpression(CheckRegExp.OTP);
			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sipBoxSmc.setPasswordMaxLength(6);
			sipBoxSmc.setPasswordMinLength(6);
			sipBoxSmc.setId(10003);
			sipBoxSmc.setSipDelegator(this);
			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			sipBoxSmc.setRandomKey_S(randomNumber);

			smsbtn = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发送获取短信验证码的请求
							sendSMSCToMobile();// PsnSendSMSCodeToMobile获取并发送手机交易码
						}
					});
		}

		if (isOtp) {
			optLinearLayout = (LinearLayout) findViewById(R.id.ll_opt);
			optLinearLayout.setVisibility(View.VISIBLE);
			sipBoxOpt = (SipBox) findViewById(R.id.et_opt_confirm_info);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxOpt, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxOpt.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxOpt.setPasswordRegularExpression(CheckRegExp.OTP);// \\S*
//			sipBoxOpt.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxOpt.setPasswordMaxLength(6);
//			sipBoxOpt.setPasswordMinLength(6);
//			sipBoxOpt.setId(10002);
//			sipBoxOpt.setSipDelegator(this);
//			sipBoxOpt.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxOpt.setRandomKey_S(randomNumber);
		}

		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);

		// SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 发送获取短信验证码的请求
		// sendSMSCToMobile();// PsnSendSMSCodeToMobile获取并发送手机交易码
		// }
		// });
		// 发起新转账
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().closeAllActivity();
				Intent intent = new Intent();
				intent.setClass(MobileTranConfirmActivity.this,
						MobileTranActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, mobileTranMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
	}

	private void displayTextView() {
		accOutTv.setText(StringUtil.getForSixForString(accOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accoutNicknameTv.setText(outNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		payeeMobileTv.setText(mobileNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				payeeMobileTv);
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		currencyTv.setText(LocalData.Currency.get(currencySer));
		remarkTv.setText(StringUtil.isNull(remark) ? "-" : remark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
		// 手续费待确定
		if (isHaveAcct.equals(ConstantGloble.IS_HAVE_ACC_1)) {
			shouldChargeTv.setText(shouldCharge);
			// shouldChargeTv.setText(StringUtil.isNullOrEmpty(shouldCharge)?"-":shouldCharge);
			shouldChargeTv.setTextColor(getResources().getColor(R.color.red));
			// fakeChargeTv.setText(StringUtil.isNullOrEmpty(fakeCharge)?"-":fakeCharge);
			fakeChargeTv.setText(fakeCharge);
			fakeChargeTv.setTextColor(getResources().getColor(R.color.red));
		} else {
			String strTitle = this.getString(R.string.mobile_no_acc_title);
			titleTv.setText(strTitle);
			String shouldDefault = this.getString(R.string.mobile_tran_charge);
			shouldChargeTv.setText(shouldDefault);
			String fakeDefault = this.getString(R.string.mobile_tran_charge);
			fakeChargeTv.setText(fakeDefault);
		}
		// exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_last_trans_rel_confirm:// 用户点击上一步
			finish();
			break;
		case R.id.btn_next_trans_rel_confirm:// 用户点击下一步

			// if (smcLinearLayout.isShown() && !optLinearLayout.isShown()) {
			// try {
			// String str = sipBoxSmc.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.reg_smc_str));
			// return;
			// }
			// smc_rc = sipBoxSmc.getValue().getEncryptRandomNum();
			// smc = sipBoxSmc.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_smc));
			// return;
			// }
			// }
			// if (optLinearLayout.isShown() && !smcLinearLayout.isShown()) {
			// //
			// try {
			// String str = sipBoxOpt.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.forex_inves_notify));
			// return;
			// }
			// //
			// otp_rc = sipBoxOpt.getValue().getEncryptRandomNum();
			// otp = sipBoxOpt.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_opt));
			// return;
			// }
			// }
			//
			// if (optLinearLayout.isShown() && smcLinearLayout.isShown()) {
			// try {
			// String str = sipBoxSmc.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.reg_smc_str));
			// return;
			// }
			// smc_rc = sipBoxSmc.getValue().getEncryptRandomNum();
			// smc = sipBoxSmc.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_smc));
			// return;
			// }
			// try {
			// String str = sipBoxOpt.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.forex_inves_notify));
			// return;
			// }
			// //
			// otp_rc = sipBoxOpt.getValue().getEncryptRandomNum();
			// otp = sipBoxOpt.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_opt));
			// return;
			// }
			// }
			/** 安全工具数据校验 */
			usbKeyText.checkDataUsbKey(sipBoxOpt, sipBoxSmc,
					new IUsbKeyTextSuccess() {

						@Override
						public void SuccessCallBack(String result, int errorCode) {
							// TODO Auto-generated method stub
							// 显示通讯框
							BaseHttpEngine.showProgressDialog();
							requestPSNGetTokenId((String) BaseDroidApp
									.getInstanse().getBizDataMap()
									.get(ConstantGloble.CONVERSATION_ID));
						}
					});
			// // 显示通讯框
			// BaseHttpEngine.showProgressDialog();
			// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
			// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			break;
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnMobileTransferSubmit();
	}

	/**
	 * PsnSendSMSCodeToMobile获取并发送手机交易码 req
	 */
	public void requestForSendSMSCodeToMobile() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.SENDSMSCODETOMOBILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestForSendSMSCodeToMobileCallBack");
	}

	/**
	 * PsnSendSMSCodeToMobile获取并发送手机交易码 res
	 */
	public void requestForSendSMSCodeToMobileCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// String smc = (String) biiResponseBody.getResult();

	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		LogGloble.i(TAG, "randomNumber=" + randomNumber);
		// 加密控件设置随机数
		// sipBoxOpt.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		setupView();
		initData();
		displayTextView();
	}

	/**
	 * 手机转账交易提交
	 */
	private void requestPsnMobileTransferSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSN_MOBILE_TRANSFER_SUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_TO_ACCOUNT_TYPE, toAccountType);
		map.put(Tran.PAYEEMOBILE_REQ, mobileNumber);
		map.put(Tran.TRANS_PAYEE_BANK_NUM, toAccountArea);
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_REMARK_REQ, remark);
		map.put(Tran.PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.IS_HAVEACCT, isHaveAcct);
		// map.put(Tran.TRANS_SMC_REQ, smc);
		// map.put(Tran.TRANS_SMC_RC_REQ, smc_rc);
		// map.put(Tran.TRANS_OTP_REQ, otp);
		// map.put(Tran.TRANS_OPT_RC_REQ, otp_rc);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Tran.TRANS_TOKEN_REQ, tokenId);
		// map.put(Tran.TRANS_SIGNEDDATA_REQ, signData);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileTransferSubmitCallBack");
	}

	public void requestPsnMobileTransferSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setMobileTranDealCallBackMap(resultMap);
		Intent intent = new Intent();
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
		intent.setClass(this, MobileTranSuccessActivity.class);
		startActivity(intent);
	}

}
