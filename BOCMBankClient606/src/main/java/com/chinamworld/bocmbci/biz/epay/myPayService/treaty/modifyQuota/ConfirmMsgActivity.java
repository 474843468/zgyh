package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.modifyQuota;

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
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
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

public class ConfirmMsgActivity extends EPayBaseActivity {

	private View confirm;

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;

	private TextView tv_per_quota;
	private TextView tv_addup_quota;
	private TextView tv_day_quota;

	private SipBox sb_dynamic_code;
	private SipBox sb_note_code;

	private Button bt_ensure;
	private Button bt_smsbtn;

	private Context treatyContext;
	private PubHttpObserver httpObserver;

	private String merchantNo;
	private String agreementId;
	private String holderMerId;
	private String merchantName;
	private String dailyQuota;
	private String newDailyQuota;
	private String accountNumber;
	private String accountType;
	private String bankDailyQuota;
	private String bankSingleQuota;
	/** 币种Code */
	private String currency_code;

	private String randomKey;
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
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_TREATY);
		confirm = LayoutInflater.from(this).inflate(
				R.layout.epay_treaty_modify_quota_confirm, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(confirm);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		initTitleRightButton("关闭", rBtncloseListener);
		hideFoot();
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

						httpObserver.req_getToken("tokenCallback");
					}
				});

	}

	private void getTransData() {
		Intent intent = getIntent();

		bankDailyQuota = intent
				.getStringExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA);
		bankSingleQuota = intent
				.getStringExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA);
		newDailyQuota = intent.getStringExtra("newCustQuota");
		currency_code = intent.getStringExtra("currency_code");
		factorList = treatyContext.getList(PubConstants.PUB_FIELD_FACTORLIST);

		Map<Object, Object> merchant = treatyContext
				.getMap(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT);

		agreementId = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID),
						"");
		merchantNo = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO),
						"");
		holderMerId = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID),
						"");
		merchantName = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME),
						"");
		accountNumber = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO),
						"");
		accountType = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE),
						"");
		dailyQuota = EpayUtil
				.getString(
						merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA),
						"");
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_randomKey("randomKeyCallback");
	}

	public void randomKeyCallback(Object resultObj) {
		randomKey = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		initCurPage();
		BiiHttpEngine.dissMissProgressDialog();
	}

	private void initCurPage() {
		// TODO
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);

		usbKeytext = (UsbKeyText) confirm.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomKey, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();

		tv_per_quota = (TextView) confirm.findViewById(R.id.tv_per_quota);
		tv_per_quota.setText(StringUtil.parseStringPattern(bankSingleQuota, 2));
		tv_addup_quota = (TextView) confirm.findViewById(R.id.tv_addup_quota);
		tv_addup_quota
				.setText(StringUtil.parseStringPattern(bankDailyQuota, 2));
		tv_day_quota = (TextView) confirm.findViewById(R.id.tv_day_quota);
		tv_day_quota.setText(StringUtil.parseStringPattern(newDailyQuota, 2));
		// 币种显示
		TextView currencyView = (TextView) findViewById(R.id.tv_currency);
		currencyView.setText(LocalData.Currency.get(currency_code));

		if (isOtp) {

			ll_otp = (LinearLayout) confirm.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
			sb_dynamic_code = (SipBox) confirm.findViewById(R.id.sb_dynamic_code);
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

			ll_smc = (LinearLayout) confirm.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sb_note_code = (SipBox) confirm.findViewById(R.id.sb_note_code);
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

		bt_smsbtn = (Button) confirm.findViewById(R.id.bt_smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});

		bt_ensure = (Button) confirm.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					checkDate();
				}
			}
		});

		/*
		 * for (Object obj : factorList) { String confirmType =
		 * EpayUtil.getString(obj, ""); if
		 * (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
		 * ll_otp.setVisibility(View.VISIBLE); confirmOtp = true; } else if
		 * (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
		 * ll_smc.setVisibility(View.VISIBLE); confirmSmc = true; } }
		 */
		// TODO
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

	public void tokenCallback(Object resultObj) {
		String token = EpayUtil
				.getString(httpObserver.getResult(resultObj), "");
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_MERCHANT_NO,
				merchantNo);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_AGREEMENT_ID,
				agreementId);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_HOLDER_MER_ID,
				holderMerId);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_MERCHANT_NAME,
				merchantName);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_DAILY_QUOTA,
				dailyQuota);
		params.put(
				TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_NEW_DAILY_QUOTA,
				newDailyQuota);
		params.put(
				TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_ACCOUNT_NUMBER,
				accountNumber);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_ACCOUNT_TYPE,
				accountType);
		params.put(
				TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_BANK_DAILY_QUOTA,
				bankDailyQuota);
		params.put(
				TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_BANK_SINGLE_QUOTA,
				bankSingleQuota);
		params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_FIELD_CURRENCY,
				currency_code);
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);

		/*
		 * if (confirmOtp) { params.put(PubConstants.PUB_FIELD_OTP, otp);
		 * params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC); } if (confirmSmc) {
		 * params.put(PubConstants.PUB_FIELD_SMC, smc);
		 * params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC); }
		 */
		// TODO
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		httpObserver.req_modifyTreatyMaxQuota(params,
				"modifyTreatyMaxQuotaCallback");
	}

	public void modifyTreatyMaxQuotaCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(
				TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA,
				bankDailyQuota);
		intent.putExtra(
				TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA,
				bankSingleQuota);
		intent.putExtra("newCustQuota", newDailyQuota);
		intent.putExtra("currency_code", currency_code);
		startActivityForResult(intent, 0);
	}
}
