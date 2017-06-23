package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

public class SetPaymentAccConfirmActivity extends EPayBaseActivity {

	private View ePaySetPaymentAccConfirm;

	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;

	// private TextView tv_acc_type;
	private TextView tv_acc_number;
	// private TextView tv_acc_nickname;
	private TextView tv_acc_currency;
	private TextView tv_day_max_quota_txt;
	private TextView tv_day_max_quota;

	// private Button bt_back;
	private Button bt_submit;
	private Button bt_smsbtn;

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private Context withoutCardTransContext;
	private PubHttpObserver httpObserver;

	private List<Object> factorList;

	private String tokenId;

	private String acc_type;
	private String acc_nickname;
	private String acc_number;
	private String currency;
	private String tranDate;
	private String custName;
	private String operatorType = "开通";
	private String currentQuota;
	private String custCif;
	private String identityType;
	private String identityNumber;
	private String telphone;
	private String status;

	private boolean confirmOtp;
	private boolean confirmSmc;

	private String randomKey;

	private String otp;

	private String otpRC;

	private String smc;

	private String smcRC;

	private String accountId;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_WITHOUT_CARD);
		ePaySetPaymentAccConfirm = LayoutInflater.from(this).inflate(
				R.layout.epay_wc_spa_confirm, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
		super.setContentView(ePaySetPaymentAccConfirm);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 3, new String[]{"选择账户","填写信息","确认信息"});
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

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		tranDate = getIntent().getStringExtra("tranDate");
		Map<Object, Object> selectedAccount = withoutCardTransContext
				.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		factorList = withoutCardTransContext
				.getList(PubConstants.PUB_FIELD_FACTORLIST);
		accountId = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
				"");
		acc_type = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
				"");
		acc_nickname = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
		acc_number = EpayUtil
				.getString(
						selectedAccount
								.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
						"");
		custName = EpayUtil.getString(selectedAccount
				.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM),
				"");
		currency = EpayUtil
				.getString(
						selectedAccount
								.get(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE),
						"");
//		custCif = EpayUtil.getString(selectedAccount
//				.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF), "");
//		identityNumber = EpayUtil
//				.getString(
//						selectedAccount
//								.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM),
//						"");
//		identityType = EpayUtil
//				.getString(
//						selectedAccount
//								.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE),
//						"");
//		telphone = EpayUtil
//				.getString(
//						selectedAccount
//								.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE),
//						"");
		currentQuota = withoutCardTransContext.getString(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA,
				"");
		status = EpayUtil.getString(selectedAccount
				.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS),
				"");
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_randomKey("getRandomKeyCallback");
	}

	public void getRandomKeyCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		randomKey = EpayUtil.getString(result, "");
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		// TODO
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) ePaySetPaymentAccConfirm
				.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		sb_dynamic_code = (SipBox) ePaySetPaymentAccConfirm.findViewById(R.id.sb_dynamic_code);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sb_dynamic_code, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//		sb_dynamic_code.setCipherType(SystemConfig.CIPHERTYPE);
//		sb_dynamic_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sb_dynamic_code.setId(10002);
//		sb_dynamic_code.setPasswordMinLength(6);
//		sb_dynamic_code.setPasswordMaxLength(6);
//		sb_dynamic_code.setPasswordRegularExpression(CheckRegExp.OTP);
//		sb_dynamic_code.setSipDelegator(this);
//		sb_dynamic_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sb_dynamic_code.setRandomKey_S(randomKey);

		sb_note_code = (SipBox) ePaySetPaymentAccConfirm.findViewById(R.id.sb_note_code);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sb_note_code, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomKey, this);
//		sb_note_code.setCipherType(SystemConfig.CIPHERTYPE);
//		sb_note_code.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sb_note_code.setId(10002);
//		sb_note_code.setPasswordMinLength(6);
//		sb_note_code.setPasswordMaxLength(6);
//		sb_note_code.setPasswordRegularExpression(CheckRegExp.OTP);
//		sb_note_code.setSipDelegator(this);
//		sb_note_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sb_note_code.setRandomKey_S(randomKey);

		bt_smsbtn = (Button) ePaySetPaymentAccConfirm
				.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});

		// et_noteCode = (EditText)
		// ePaySetPaymentAccConfirm.findViewById(R.id.et_note_code);
		ll_otp = (LinearLayout) ePaySetPaymentAccConfirm
				.findViewById(R.id.ll_otp);
		ll_smc = (LinearLayout) ePaySetPaymentAccConfirm
				.findViewById(R.id.ll_smc);
		// tv_acc_type =
		// (TextView)ePaySetPaymentAccConfirm.findViewById(R.id.tv_acc_type);
		tv_acc_number = (TextView) ePaySetPaymentAccConfirm
				.findViewById(R.id.tv_acc_number);
		// tv_acc_nickname =
		// (TextView)ePaySetPaymentAccConfirm.findViewById(R.id.tv_acc_nickname);
		tv_acc_currency = (TextView) ePaySetPaymentAccConfirm
				.findViewById(R.id.tv_currency);
		tv_day_max_quota_txt= (TextView) ePaySetPaymentAccConfirm
				.findViewById(R.id.tv_day_max_quota_txt);
		if (serviceType == 1) {
			tv_day_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_day_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		tv_day_max_quota = (TextView) ePaySetPaymentAccConfirm
				.findViewById(R.id.tv_day_max_quota);

		// bt_back =
		// (Button)ePaySetPaymentAccConfirm.findViewById(R.id.bt_back);
		// bt_back.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// SetPaymentAccConfirmActivity.this.finish();
		// }
		// });

		bt_submit = (Button) ePaySetPaymentAccConfirm
				.findViewById(R.id.bt_submit);
		bt_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}
			}
		});

		initDisplay();
	}

	private void initDisplay() {
		for (Object obj : factorList) {
			String confirmType = EpayUtil.getString(obj, "");
			if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
				confirmOtp = true;
				ll_otp.setVisibility(View.VISIBLE);
			}
			if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
				confirmSmc = true;
				ll_smc.setVisibility(View.VISIBLE);
			}
		}

		// tv_acc_type.setText(LocalData.AccountType.get(acc_type));
		tv_acc_number.setText(StringUtil.getForSixForString(acc_number));
		// tv_acc_nickname.setText(acc_nickname);
		tv_acc_currency.setText(LocalData.Currency.get(currency));
		tv_day_max_quota
				.setText(StringUtil.parseStringPattern(currentQuota, 2));
		BiiHttpEngine.dissMissProgressDialog();
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
	 * 获取TokenId
	 * 
	 * @param resultObj
	 */
	public void getTokenCallback(Object resultObj) throws Exception {
		tokenId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		HashMap<String, Object> params = new HashMap<String, Object>();
		/*
		 * if (confirmOtp) { params.put(PubConstants.PUB_FIELD_OTP, otp);
		 * params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC); } if (confirmSmc) {
		 * params.put(PubConstants.PUB_FIELD_SMC, smc);
		 * params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC); }
		 */

		params.put(PubConstants.PUB_FIELD_TOKEN_ID, tokenId);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_ACCOUNT_ID,
				accountId);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_ACCOUNT_NUMBER,
				acc_number);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_CLIENT_NAME,
				custName); // cyf
							// 核心用户名
							// 未知
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_CUSTOMER_NAME,
				custName);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_CURRENT_QUOTA,
				currentQuota);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_CUST_CIF,
				custCif);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_IDENTITY_TYPE,
				identityType);
		params.put(
				WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_IDENTITY_NUMBER,
				identityNumber);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_OPERATE_TYPE,
				operatorType);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_TELPHONE,
				telphone);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_TRANDATE,
				tranDate);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_FIELD_STATUS, status);
		if (serviceType == 1) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"1");
		} else if (serviceType == 2) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"2");
		}

		// TODO
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(params);
		// 请求开通无卡支付账户
		SipBoxUtils.setSipBoxParams(params);
		httpObserver.req_setWcPaymentServiceAcc(params,
				"setPaymentServiceAccCallback");
	}

	/**
	 * 开通无卡支付账户
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void setPaymentServiceAccCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) httpObserver
				.getResult(resultObj);
		String cifMobile = null;
		if (result.containsKey(WithoutCardContants.GIF_MOBILE)) {
			cifMobile = (String) result.get(WithoutCardContants.GIF_MOBILE);
		}
		Intent intent = new Intent(this, SetPaymentAccResultActivity.class)
				.putExtra(WithoutCardContants.GIF_MOBILE, cifMobile);
		startActivityForResult(intent, 0);
	}

}
