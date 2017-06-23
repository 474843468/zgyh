package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.ArrayList;
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

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * Atm取款确认信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRemitConfirmActivity extends TranBaseActivity {
	/** atm取款确认信息页面 */
	private View view;
	/** 汇出账户 */
	private TextView confirmAcount;
	/** 手机号码 */
	private TextView confirmPhone;
	/** 姓名 */
	private TextView confirmName;
	/** 汇款金额 */
	private TextView confirmAmount;
	/** 附言 */
	private TextView confirmFuyan;
	/** 下方按钮 */
	private Button btnConfirm;
	private String acountId;
	private String acountNumber;
	private String phoneNumber;
	private String remitName;
	private String remitCurrencyCode;
	private String remitAmount;
	private String remark;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 加密控件——预留密码 */
	private SipBox sipBoxOne = null;
	/** 加密控件——重输预留密码 */
	private SipBox sipBoxAgain = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	private Map<String, Object> atmPreMap;
	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 预留密码 */
	private String oneStr;
	/** 重输预留密码 */
	private String againStr;
	private String one_password_RC = "";
	private String again_password_RC = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_atm_title));
		view = addView(R.layout.tran_atm_confirm);
		toprightBtnAtm();
		getIntentData();
		requestForRandomNumber();
		// init();
	}

	/**
	 * @Title: getIntentData
	 * @Description: 获取intent带来的数据
	 * @param
	 * @return void
	 */
	private void getIntentData() {
		Intent intent = this.getIntent();
		acountId = intent.getStringExtra(Comm.ACCOUNT_ID);
		acountNumber = intent.getStringExtra(Comm.ACCOUNTNUMBER);
		phoneNumber = intent.getStringExtra(DrawMoney.PAYEE_MOBILE);
		remitName = intent.getStringExtra(DrawMoney.PAYEE_NAME);
		remitCurrencyCode = intent.getStringExtra(DrawMoney.REMIT_CURRENCY_CODE);
		remitAmount = intent.getStringExtra(DrawMoney.REMIT_AMOUNT);
		remark = intent.getStringExtra(DrawMoney.REMARK);
	}

	private void init() {
		atmPreMap = TranDataCenter.getInstance().getAtmpremap();
		factorList = (List<Map<String, Object>>) atmPreMap.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);
		confirmAcount = (TextView) view.findViewById(R.id.remitout_confirm_account);
		confirmPhone = (TextView) view.findViewById(R.id.remit_confirm_phone);
		confirmName = (TextView) view.findViewById(R.id.remit_confirm_name);
		confirmAmount = (TextView) view.findViewById(R.id.remit_confirm_amount);
		confirmFuyan = (TextView) view.findViewById(R.id.remit_confirm_fuyan);

		btnConfirm = (Button) view.findViewById(R.id.remit_confirm_next_btn);
		confirmAcount.setText(StringUtil.getForSixForString(String.valueOf(acountNumber)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, confirmAcount);
		confirmPhone.setText(phoneNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, confirmPhone);
		confirmName.setText(remitName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, confirmName);
		confirmAmount.setText(StringUtil.parseStringPattern(remitAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, confirmAmount);
		confirmFuyan.setText(StringUtil.isNullChange(remark));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, confirmFuyan);
		// // 手机交易码
		// ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		// // 动态口令
		// ll_active_code = (LinearLayout)
		// view.findViewById(R.id.ll_active_code);
		// // 动态口令
		// sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		// sipBoxActiveCode
		// .setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		// sipBoxActiveCode.setId(10002);
		// sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
		// sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		// sipBoxActiveCode
		// .setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		// sipBoxActiveCode.setSingleLine(true);
		// sipBoxActiveCode.setSipDelegator(this);
		// sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// // 手机交易码
		// sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		// sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		// sipBoxSmc.setId(10002);
		// sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
		// sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		// sipBoxSmc
		// .setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		// sipBoxSmc.setSingleLine(true);
		// sipBoxSmc.setSipDelegator(this);
		// sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

		// 预留密码
		sipBoxOne = (SipBox) view.findViewById(R.id.sipbox_one);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxOne, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxOne.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxOne.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxOne.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxOne.setId(10002);
//		sipBoxOne.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxOne.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxOne.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxOne.setSingleLine(true);
//		sipBoxOne.setSipDelegator(this);
//		sipBoxOne.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxOne.setRandomKey_S(randomNumber);
		// 重输预留密码
		sipBoxAgain = (SipBox) view.findViewById(R.id.sipbox_again);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxAgain, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxAgain.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxAgain.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxAgain.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxAgain.setId(10002);
//		sipBoxAgain.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxAgain.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxAgain.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxAgain.setSingleLine(true);
//		sipBoxAgain.setSipDelegator(this);
//		sipBoxAgain.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxAgain.setRandomKey_S(randomNumber);
		// Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		// SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 发送验证码到手机
		// sendMSCToMobile();
		// }
		// });
		// requestForRandomNumber();
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		if (isOtp) {
			// 动态口令
			ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
			ll_active_code.setVisibility(View.VISIBLE);
			// 动态口令
			sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setId(10002);
//			sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxActiveCode.setSingleLine(true);
//			sipBoxActiveCode.setSipDelegator(this);
//			sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		if (isSms) {
			// 手机交易码
			ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			// 手机交易码
			sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setId(10002);
//			sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxSmc.setSingleLine(true);
//			sipBoxSmc.setSipDelegator(this);
//			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxSmc.setRandomKey_S(randomNumber);
			Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 发送验证码到手机
					sendMSCToMobile();
				}
			});
		}
		// 判断
		// initSmcAndOtp();
		btnConfirm.setOnClickListener(confirmClickListener);
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, atmPreMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSms = usbKeyText.getIsSmc();
	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RegexpBean sipOne = new RegexpBean(
					AtmRemitConfirmActivity.this.getString(R.string.trans_atm_obligatePassword_null), sipBoxOne.getText()
							.toString(), ConstantGloble.SIPOTPPSW);
			RegexpBean sipAgain = new RegexpBean(
					AtmRemitConfirmActivity.this.getString(R.string.trans_atm_obligatePassword_again_null), sipBoxAgain
							.getText().toString(), ConstantGloble.SIPSMCPSW);
			// 动态口令
			// RegexpBean sipRegexpBean = new RegexpBean(
			// AtmRemitConfirmActivity.this
			// .getString(R.string.active_code_regex),
			// sipBoxActiveCode.getText().toString(),
			// ConstantGloble.SIPOTPPSW);
			// RegexpBean sipSmcpBean = new RegexpBean(
			// AtmRemitConfirmActivity.this
			// .getString(R.string.acc_smc_regex),
			// sipBoxSmc.getText().toString(), ConstantGloble.SIPSMCPSW);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(sipOne);
			lists.add(sipAgain);
			// if (isSms) {
			// lists.add(sipSmcpBean);
			// }
			// if (isOtp) {
			// lists.add(sipRegexpBean);
			// }
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				try {
					oneStr = sipBoxOne.getValue().getEncryptPassword();
					one_password_RC = sipBoxOne.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
				}
				try {
					againStr = sipBoxAgain.getValue().getEncryptPassword();
					again_password_RC = sipBoxAgain.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
				}
				// if (isSms) {
				// try {
				// smcStr = sipBoxSmc.getValue().getEncryptPassword();
				// smc_password_RC = sipBoxSmc.getValue()
				// .getEncryptRandomNum();
				// } catch (CodeException e) {
				// LogGloble.exceptionPrint(e);
				// }
				//
				// }
				// if (isOtp) {
				// try {
				// otpStr = sipBoxActiveCode.getValue()
				// .getEncryptPassword();
				// otp_password_RC = sipBoxActiveCode.getValue()
				// .getEncryptRandomNum();
				// } catch (CodeException e) {
				// LogGloble.exceptionPrint(e);
				// }
				//
				// }
				/** 安全工具数据校验 */
				usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
						BiiHttpEngine.showProgressDialog();
					}
				});
				// TODO
				// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				// BiiHttpEngine.showProgressDialog();
			}
		}
	};

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		requestPsnPasswordRemitPayment();
	}

	/**
	 * 请求汇款交易
	 */
	public void requestPsnPasswordRemitPayment() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITPAYMENT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		dateTime = QueryDateUtils.getcurrentDate(dateTime);
		// 执行日期
		map.put(Tran.TRAN_ATM_SUB_DUEDATE_REQ, dateTime);
		// 汇款类型
		map.put(Tran.TRAN_ATM_SUB_TYPE_REQ, ConstantGloble.ATM_TYPE);
		// 账户Id
		map.put(Tran.TRAN_ATM_SUB_FROMACCOUNTID_REQ, acountId);
		// 收款人姓名
		map.put(Tran.TRAN_ATM_SUB_PAYEENAME_REQ, remitName);
		// 转账金额
		map.put(Tran.TRAN_ATM_SUB_AMOUNT_REQ, StringUtil.parseStringPattern(remitAmount, 2));
		// 币种
		map.put(Tran.TRAN_ATM_SUB_CURRENCYCODE_REQ, ConstantGloble.BOCINVT_CURRENCY_RMB);
		// 收款人手机号
		map.put(Tran.TRAN_ATM_SUB_PAYEEMOBILE_REQ, phoneNumber);
		// 是否允许在ATM取现
		map.put(Tran.TRAN_ATM_SUB_ATMWITHDRAW_REQ, ConstantGloble.ATM_TYPE);
		// 附言
		map.put(Tran.TRAN_ATM_SUB_FURINF_REQ, remark);
		// 预留密码
		map.put(Tran.TRAN_ATM_SUB_OBLIGATEPASSWORD_REQ, oneStr);
		map.put(Tran.TRAN_ATM_SUB_OBLIGATEPASSWORDRC_REQ, one_password_RC);
		map.put(Tran.TRAN_ATM_SUB_REOBLIGATEPASSWORD_REQ, againStr);
		map.put(Tran.TRAN_ATM_SUB_REOBLIGATEPASSWORDRC_REQ, again_password_RC);
		map.put(Tran.TRAN_ATM_SUB_TOKEN_REQ, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		// map.put(Tran.TRAN_ATM_SUB_SIGNEDDATA_REQ,
		// atmPreMap.get(Tran.TRAN_ATM_PLAINDATA_RES));
		// if (isOtp) {
		// map.put(Comm.Otp, otpStr);
		// map.put(Comm.Otp_Rc, otp_password_RC);
		//
		// }
		// if (isSms) {
		// map.put(Comm.Smc, smcStr);
		// map.put(Comm.Smc_Rc, smc_password_RC);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitPaymentCallBack");
	}

	/**
	 * 请求汇款交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnPasswordRemitPaymentCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> atmmap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(atmmap)) {
			return;
		}
		String dueDate = (String) atmmap.get(DrawMoney.DUE_DATE);
		String status = (String) atmmap.get(DrawMoney.STATUS);
		Intent intent = new Intent(this, AtmRemitSuccessActivity.class);
		intent.putExtra(Comm.ACCOUNT_ID, acountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, acountNumber);
		intent.putExtra(DrawMoney.PAYEE_MOBILE, phoneNumber);
		intent.putExtra(DrawMoney.PAYEE_NAME, remitName);
		intent.putExtra(DrawMoney.REMIT_CURRENCY_CODE, ConstantGloble.BOCINVT_CURRENCY_RMB);
		intent.putExtra(DrawMoney.REMIT_AMOUNT, remitAmount);
		intent.putExtra(DrawMoney.REMARK, remark);
		intent.putExtra(DrawMoney.DUE_DATE, dueDate);
		intent.putExtra(DrawMoney.STATUS, status);
		intent.putExtra(Tran.TRAN_ATM_DETAIL_STATUS_RES, dueDate);
		intent.putExtra(Tran.TRAN_ATM_SUB_REMITNO_RES, (String) atmmap.get(Tran.TRAN_ATM_SUB_REMITNO_RES));
		startActivity(intent);
	}

	/** 判断是动态口令还是手机交易码 */
	// public void initSmcAndOtp() {
	// if (!StringUtil.isNullOrEmpty(factorList)) {
	// for (int i = 0; i < factorList.size(); i++) {
	// Map<String, Object> itemMap = factorList.get(i);
	// Map<String, String> securityMap = (Map<String, String>) itemMap
	// .get(Inves.FIELD);
	// String name = securityMap.get(Inves.NAME);
	// if (Inves.Smc.equals(name)) {
	// isSms = true;
	// ll_smc.setVisibility(View.VISIBLE);
	// } else if (Inves.Otp.equals(name)) {
	// isOtp = true;
	// ll_active_code.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// requestForRandomNumber();
	// }

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 加密控件设置随机数
		// sipBoxActiveCode.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		// sipBoxAgain.setRandomKey_S(randomNumber);
		// sipBoxOne.setRandomKey_S(randomNumber);
		BiiHttpEngine.dissMissProgressDialog();
		init();
	}

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
	}

}
