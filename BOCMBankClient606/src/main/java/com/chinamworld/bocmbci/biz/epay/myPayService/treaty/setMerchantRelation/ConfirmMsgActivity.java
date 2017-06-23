package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

public class ConfirmMsgActivity extends EPayBaseActivity {

	private View confirmMsg;

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private TextView tv_cust_name;
	private TextView tv_merchant_name;
	private TextView tv_merchant_id;
	private TextView tv_account;
	private TextView tv_acc_nickname;
	private TextView tv_addup_quota;
	private TextView tv_day_quota;

	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;

	private Button bt_smsbtn;
	private Button bt_ensure;

	private PubHttpObserver httpObserver;
	private Context treatyContext;

	private String randomKey;

	private String accountId;
	private String accountNumber;
	private String accountType;
	private String accNickname;
	private String merchantId;
	private String merchantName;
	private String username;
	private String identityNumber;
	private String identityType;
	private String mobile;
	private String addUpQuota;
	private String custMaxQuota;
	private String perMaxQuota;
	private String bocNo;

	private List<Object> factorList;
	private boolean confirmOtp;
	private boolean confirmSmc;

	private String otp;
	private String otpRC;
	private String smc;
	private String smcRC;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;

	/** 加密控件 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_TREATY);
		treatyContext = TransContext.getTreatyTransContext();
		confirmMsg = LayoutInflater.from(this).inflate(
				R.layout.epay_treaty_add_confirm, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(confirmMsg);
		super.onCreate(savedInstanceState);

		getTransData();
	}

	// TODO
	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(sb_dynamic_code, sb_note_code,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub

						BaseHttpEngine.showProgressDialog();

						httpObserver.req_getToken("getTokenCallback");
					}
				});

	}

	private void getTransData() {
		factorList = treatyContext.getList(PubConstants.PUB_FIELD_FACTORLIST);
		Intent intent = getIntent();
		accountId = intent
				.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID);
		accountNumber = intent
				.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER);
		accountType = intent
				.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE);
		accNickname = intent
				.getStringExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME);
		merchantId = intent
				.getStringExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID);
		merchantName = intent
				.getStringExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME);
		username = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME);
		identityNumber = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_NUMBER);
		identityType = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_TYPE);
		mobile = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_MOBILE);
		addUpQuota = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA);
		custMaxQuota = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA);
		perMaxQuota = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA);
		bocNo = intent
				.getStringExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_BOC_NO);
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_randomKey("randomKeyCallback");
	}

	public void randomKeyCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		randomKey = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		initCurPage();
	}

	private void initCurPage() {

		// TODO
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) confirmMsg.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		if (isOtp) {

			ll_otp = (LinearLayout) confirmMsg.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
			sb_dynamic_code = (SipBox) confirmMsg.findViewById(R.id.sb_dynamic_code);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sb_dynamic_code, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//			sb_dynamic_code.setCipherType(SystemConfig.CIPHERTYPE);
//			sb_dynamic_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sb_dynamic_code.setId(10002);
//			sb_dynamic_code.setPasswordMinLength(6);
//			sb_dynamic_code.setPasswordMaxLength(6);
//			sb_dynamic_code.setPasswordRegularExpression(CheckRegExp.OTP);
//			sb_dynamic_code.setSipDelegator(this);
//			sb_dynamic_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sb_dynamic_code.setRandomKey_S(randomKey);
		}

		if (isSmc) {

			ll_smc = (LinearLayout) confirmMsg.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sb_note_code = (SipBox) confirmMsg.findViewById(R.id.sb_note_code);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sb_note_code, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//			sb_note_code.setCipherType(SystemConfig.CIPHERTYPE);
//			sb_note_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sb_note_code.setId(10002);
//			sb_note_code.setPasswordMinLength(6);
//			sb_note_code.setPasswordMaxLength(6);
//			sb_note_code.setPasswordRegularExpression(CheckRegExp.OTP);
//			sb_note_code.setSipDelegator(this);
//			sb_note_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sb_note_code.setRandomKey_S(randomKey);
		}

		tv_cust_name = (TextView) confirmMsg.findViewById(R.id.tv_cust_name);
		tv_cust_name.setText(username);
		tv_merchant_name = (TextView) confirmMsg
				.findViewById(R.id.tv_merchant_name);
		tv_merchant_name.setText(merchantName);
		tv_merchant_id = (TextView) confirmMsg
				.findViewById(R.id.tv_merchant_id);
		tv_merchant_id.setText(merchantId);
		tv_acc_nickname = (TextView) confirmMsg
				.findViewById(R.id.tv_acc_nickname);
		tv_acc_nickname.setText(accNickname);
		tv_account = (TextView) confirmMsg.findViewById(R.id.tv_account);
		tv_account.setText(StringUtil.getForSixForString(accountNumber));
		tv_addup_quota = (TextView) confirmMsg
				.findViewById(R.id.tv_addup_quota);
		tv_addup_quota.setText(StringUtil.parseStringPattern(addUpQuota, 2));
		tv_day_quota = (TextView) confirmMsg.findViewById(R.id.tv_day_quota);
		tv_day_quota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));

		/*
		 * for (Object obj : factorList) { String confirmType =
		 * EpayUtil.getString(obj, ""); if
		 * (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
		 * ll_otp.setVisibility(View.VISIBLE); confirmOtp = true; } else if
		 * (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
		 * ll_smc.setVisibility(View.VISIBLE); confirmSmc = true; } }
		 */

		bt_smsbtn = (Button) confirmMsg.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});

		bt_ensure = (Button) confirmMsg.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}
			}
		});

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cust_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_merchant_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_acc_nickname);
	}

	private boolean checkSubmitData() {
		boolean isSuccess = true;

		RegexpBean rbOtp = new RegexpBean(getResources().getString(
				R.string.active_code_regex), EpayUtil.getString(
				sb_dynamic_code.getText(), ""), "otp");
		RegexpBean rbSms = new RegexpBean(getResources().getString(
				R.string.set_smc_no), EpayUtil.getString(
				sb_note_code.getText(), ""), "smc");
		ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();

		// if (confirmOtp) {
		// list.add(rbOtp);
		// }
		if (confirmSmc) {
			list.add(rbSms);
		}
		if (confirmOtp) {
			list.add(rbOtp);
		}

		if (RegexpUtils.regexpDate(list)) {
			if (confirmOtp) {
				try {
					otp = sb_dynamic_code.getValue().getEncryptPassword();
					otpRC = sb_dynamic_code.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							e.getMessage());
					isSuccess = false;
				}
			}
			if (confirmSmc) {
				try {
					smc = sb_note_code.getValue().getEncryptPassword();
					smcRC = sb_note_code.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							e.getMessage());
					isSuccess = false;
				}
			}
		} else {
			isSuccess = false;
		}

		return isSuccess;
	}

	/**
	 * 获取防重提交标识回调方法
	 * 
	 * @param resultObj
	 */
	public void getTokenCallback(Object resultObj) {
		String token = EpayUtil
				.getString(httpObserver.getResult(resultObj), "");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);
		params.put(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_HOLDER_MERID,
				merchantId);
		params.put(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_INPUT_QUOTA,
				custMaxQuota);
		params.put(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_SINGLE_QUOTA,
				perMaxQuota);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_CN_NAME,
				merchantName);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_BOC_NO,
				bocNo);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_ACCOUNT_ID,
				accountId);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_DAY_QUOTA,
				addUpQuota);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_USERNAME,
				username);
		params.put(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_IDENTITY_TYPE,
				identityType);
		params.put(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_IDENTITY_NUMBER,
				identityNumber);
		params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_FIELD_MOBILE,
				mobile);

		/*
		 * if (confirmOtp) { params.put(PubConstants.PUB_FIELD_OTP, otp);
		 * params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC); }
		 * 
		 * if (confirmSmc) { params.put(PubConstants.PUB_FIELD_SMC, smc);
		 * params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC); }
		 */
		// TODO
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		httpObserver.req_addTreatyMerchant(params, "addTreatyMerchantCallback");
	}

	/**
	 * 商户签约提交交易回调方法
	 * 
	 * @param resultObj
	 */
	public void addTreatyMerchantCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		// Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(httpObserver
				.getResult(resultObj));
		// agreementId
		String agreementId = EpayUtil.getString(resultMap.get("agreementId"),
				"");
		String signChannel = EpayUtil.getString(resultMap.get("signChannel"),
				"");
		String signDate = EpayUtil.getString(resultMap.get("signDate"), "");

		// String treatyNum = EpayUtil.getString(result, "");
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("treatyNum", agreementId);
		intent.putExtra("signChannel", signChannel);
		intent.putExtra("signDate", signDate);
		intent.putExtra(
				TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID,
				merchantId);
		intent.putExtra(
				TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME,
				merchantName);
		intent.putExtra(
				PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER,
				accountNumber);
		intent.putExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME,
				accNickname);
		intent.putExtra(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME,
				username);
		intent.putExtra(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA,
				addUpQuota);
		intent.putExtra(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA,
				custMaxQuota);
		intent.putExtra(
				TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA,
				perMaxQuota);

		HashMap<String, String> merchant = new HashMap<String, String>();
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_TYPE,
				identityType);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_NUMBER,
				identityNumber);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID,
				merchantId);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA,
				custMaxQuota);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS, "V");
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_VERIFY_TYPE,
				"1");
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID,
				agreementId);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO,
				bocNo);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO,
				accountNumber);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE,
				accountType);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_NAME,
				username);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_CHANNEL,
				signChannel);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_VERIFY_MOBILE,
				"0");
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MOBILE_NUMBER,
				mobile);
		merchant.put(
				TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME,
				merchantName);

		treatyContext.setData("newMerchant", merchant);

		startActivityForResult(intent, 0);
	}
}
