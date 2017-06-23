package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.modifyQuota;

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
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
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

/**
 * 无卡支付—限额修改-确认页面
 * 
 */
public class ModifyQuotaConfirmActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaConfirmActivity";

	private View ePayModifyQuotaConfirm;

	private TextView tv_acc_number;
	// private TextView tv_acc_type;
	// private TextView tv_acc_nickname;
	private TextView tv_currency;
	private TextView tv_cust_max_quota_txt;
	private TextView tv_cust_max_quota;

	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private Button bt_ensure;
	private Button bt_smsbtn;
	// private Button bt_back;

	private String accNumber;
	private String accType;
	private String accNickname;
	private String custMaxQuota;
	private String currency;

	private String tranDate;
	private String accId;
	private String custName;
	private String operateType = "修改";
	private String clientName;
	private String identityType;
	private String identityNumber;
	private String telphone;
	private String custCif;
	private String status;

	private boolean confirmOtp = false;
	private boolean confirmSmc = false;

	private PubHttpObserver httpObserver;

	private Context withoutCardTransContext;

	private List<Object> factorList;

	private String randomKey;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_WITHOUT_CARD);

		ePayModifyQuotaConfirm = LayoutInflater.from(this).inflate(
				R.layout.epay_wc_modify_quota_confirm, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
//		super.setTitleName(PubConstants.TITLE_WITHOUT_CARD);
		super.setContentView(ePayModifyQuotaConfirm);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
//		initTitleRightButton("关闭", rBtncloseListener);
		hideFoot();
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息",
		// "修改成功" });

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

						httpObserver.req_getToken("getToken");
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
		accId = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
				"");
		accType = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
				"");
		accNickname = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
		accNumber = EpayUtil
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
		custMaxQuota = withoutCardTransContext.getString(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA,
				"");
		if(serviceType==1){
			status = EpayUtil.getString(selectedAccount
					.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS),
					"");
		}else if(serviceType==2){
			status=EpayUtil.getString(selectedAccount.
	        		get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTSTATUS), "");
		}
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_randomKey("getRandomKeyCallback");
	}

	/**
	 * 请求加密控件随机数回调方法
	 * 
	 * @param resultObj
	 */
	public void getRandomKeyCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		randomKey = EpayUtil.getString(result, "");
		initCurPage();
	}

	private void initCurPage() {
		// TODO
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) ePayModifyQuotaConfirm
				.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		// tv_acc_type = (TextView)
		// ePayModifyQuotaConfirm.findViewById(R.id.tv_acc_type);
		// tv_acc_nickname = (TextView)
		// ePayModifyQuotaConfirm.findViewById(R.id.tv_acc_nickname);
		tv_acc_number = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_acc_number);
		tv_currency = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_currency);
		tv_cust_max_quota_txt= (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_cust_max_quota_txt);

		if (serviceType == 1) {
			tv_cust_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_cust_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		tv_cust_max_quota = (TextView) ePayModifyQuotaConfirm
				.findViewById(R.id.tv_cust_max_quota);

		if (isOtp) {

			ll_otp = (LinearLayout) ePayModifyQuotaConfirm
					.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
			sb_dynamic_code = (SipBox) ePayModifyQuotaConfirm.findViewById(R.id.sb_dynamic_code);
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

			ll_smc = (LinearLayout) ePayModifyQuotaConfirm
					.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sb_note_code = (SipBox) ePayModifyQuotaConfirm.findViewById(R.id.sb_note_code);
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

		bt_smsbtn = (Button) ePayModifyQuotaConfirm
				.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});

		// bt_back = (Button) ePayModifyQuotaConfirm.findViewById(R.id.bt_back);
		// bt_back.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// ModifyQuotaConfirmActivity.this.finish();
		// }
		// });

		bt_ensure = (Button) ePayModifyQuotaConfirm
				.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}
			}
		});
		initDisplay();
	}

	/**
	 * 初始化显示内容
	 */
	private void initDisplay() {
		/*
		 * for (Object obj : factorList) { String confirmType =
		 * EpayUtil.getString(obj, ""); if
		 * (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
		 * ll_otp.setVisibility(View.VISIBLE); confirmOtp = true; } else if
		 * (PubConstants.PUB_FIELD_SMC.equals(confirmType)) { confirmSmc = true;
		 * ll_smc.setVisibility(View.VISIBLE); } }
		 */

		// tv_acc_type.setText(LocalData.AccountType.get(accType));
		// tv_acc_nickname.setText(accNickname);
		tv_acc_number.setText(StringUtil.getForSixForString(accNumber));
		tv_currency.setText(LocalData.Currency.get(currency));
		tv_cust_max_quota.setText(StringUtil
				.parseStringPattern(custMaxQuota, 2));
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
	public void getToken(Object resultObj) throws Exception {
		String token = EpayUtil
				.getString(httpObserver.getResult(resultObj), "");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(BomConstants.METHOD_SET_MAX_QUOTA_FIELD_QUOTA, custMaxQuota);
		/*
		 * if (confirmOtp) { params.put(PubConstants.PUB_FIELD_OTP, otp);
		 * params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC); } if (confirmSmc) {
		 * params.put(PubConstants.PUB_FIELD_SMC, smc);
		 * params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC); }
		 */
		clientName = custName;
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_ACCOUNT_ID,
				accId);
		params.put(
				WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_ACCOUNT_NUMBER,
				accNumber);
//		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_CLIENT_NAME,
//				clientName); // cyf
								// 核心用户名
								// 未知
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_CUSTOMER_NAME,
				custName);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_CURRENT_QUOTA,
				custMaxQuota);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_CUST_CIF,
				custCif);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_IDENTITY_TYPE,
				identityType);
		params.put(
				WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_IDENTITY_NUMBER,
				identityNumber);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_OPERATE_TYPE,
				operateType);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_TELPHONE,
				telphone);
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_TRANDATE,
				EpayUtil.converSystemTime(tranDate));
		params.put(WithoutCardContants.METHOD_MODIFY_QUOTA_FIELD_STATUS, status);


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

		SipBoxUtils.setSipBoxParams(params);
		httpObserver.req_setWcPaymentQuota(params, "setWcPaymentQuota");
	}

	/**
	 * 修改无卡支付账户交易限额回调方法
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void setWcPaymentQuota(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) httpObserver
				.getResult(resultObj);
		String cifMobile = null;
		if (result.containsKey(WithoutCardContants.GIF_MOBILE)) {
			cifMobile = (String) result.get(WithoutCardContants.GIF_MOBILE);
		}
		Intent intent = new Intent(this, ModifyQuotaResultActivity.class)
				.putExtra(WithoutCardContants.GIF_MOBILE, cifMobile);
		startActivityForResult(intent, 0);
	}

}
