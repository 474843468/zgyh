package com.chinamworld.bocmbci.biz.setting.limit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.control.SettingControl;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

public class EditLimitConfirmActivity extends LimitSettingBaseActivity {
	private static final String TAG = "EditLimitConfirmActivity";
	/**
	 * 限额种类显示框
	 */
	private TextView serviceNameTextView;
	/**
	 * 币种显示框
	 */
	private TextView currencyTextView;
	/**
	 * 系统日限额显示框
	 */
	private TextView dayMaxTextView;
	/**
	 * 个人日限额显示框
	 */
	private TextView preDayMaxTextView;
	/**
	 * 设定限额输入框
	 */
	private TextView resetTv;
	private Button getBtn;
	/**
	 * 动态口令输入框
	 */
	private SipBox otpEdit;
	private SipBox smcEdit;
	/**
	 * 确定按钮
	 */
	private Button confrimBtn;
	/**
	 * 取消按钮
	 */
	private Button consernBtn;

	private String resetpreDayMax;
	private String preDayMax;
	private String dayMax;
	private String currency;
	private String otp_Rc;
	private String smc_Rc;
	private String otpStr;
	private String smcStr;
	private String serviceId;
	private StringBuffer limit; // 组合好的参数

	private boolean isSms = false;
	private boolean isOtp = false;
	private LinearLayout smsLayout;
	private LinearLayout otpLayout;
	/** 加密控件里的随即数 */
	private String randomNumber;

	DisplayMetrics dm = new DisplayMetrics();

	RelativeLayout relative;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		relative = (RelativeLayout) findViewById(R.id.rltotal);
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber(SettingControl.getInstance().editLimitConversationId);
	}

	private List<String> factorListName;

	@Override
	public void editLimitConfirmCallback(Object resultObj) {
		super.editLimitConfirmCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();

	}

	public void editLimitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		Intent intent = new Intent();
		intent.setClass(EditLimitConfirmActivity.this,
				EditLimitSuccessActivity.class);
		intent.putExtra(Setting.I_SERVICEID, serviceId);
		intent.putExtra(Setting.I_CURRENCY, currency);
		intent.putExtra(Setting.I_DAYMAX, dayMax);
		intent.putExtra(Setting.I_PREMAX, resetpreDayMax);
		startActivity(intent);
	}

	@Override
	public void sendMSCToMobileCallback(Object resultObj) {
		super.sendMSCToMobileCallback(resultObj);
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childView = mainInflater.inflate(
				R.layout.setting_editlimit_confirm, null);
		setTitle(getResources().getString(R.string.set_title_limitsetting));
		tabcontent.addView(childView);
		StepTitleUtils.getInstance().initTitldStep(this,
				settingControl.getStepsForEditLimit());
		StepTitleUtils.getInstance().setTitleStep(2);
		serviceNameTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_servicename);
		currencyTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_currency);
		dayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_daymax);
		TextView dayMaxPreTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_resetpredaymax_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				dayMaxPreTextView);
		preDayMaxTextView = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_predaymax);
		resetTv = (TextView) childView
				.findViewById(R.id.set_editlimitconfirm_resetpredaymax);

		// getBtn = (Button) childView.findViewById(R.id.set_get);

		/*
		 * String commConversationId = (String) BaseDroidApp.getInstanse()
		 * .getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		 */
		String commConversationId = settingControl.editLimitConversationId;
		usbKeytext = (UsbKeyText) childView.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber,
				EditLimitMainActivity.map, this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();

		if (isOtp) {
			otpLayout = (LinearLayout) childView
					.findViewById(R.id.set_otp_layout);
			otpLayout.setVisibility(View.VISIBLE);
			otpEdit = (SipBox) childView
					.findViewById(R.id.set_editlimitconfirm_otp);
			otpEdit.setCipherType(SystemConfig.CIPHERTYPE);
			otpEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			otpEdit.setRandomKey_S(randomNumber);
			otpEdit.setPasswordRegularExpression(CheckRegExp.OTP);
			otpEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			otpEdit.setSipDelegator(this);
			// 设定为数字键盘
			otpEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		}

		if (isSms) {
			smsLayout = (LinearLayout) childView
					.findViewById(R.id.set_smc_layout);
			smsLayout.setVisibility(View.VISIBLE);
			smcEdit = (SipBox) childView
					.findViewById(R.id.set_editlimitconfirm_smc);
			smcEdit.setCipherType(SystemConfig.CIPHERTYPE);
			smcEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			smcEdit.setRandomKey_S(randomNumber);
			smcEdit.setPasswordRegularExpression(CheckRegExp.OTP);
			smcEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			smcEdit.setSipDelegator(this);
			// 设定为数字键盘
			smcEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			getBtn = (Button) childView.findViewById(R.id.set_get);
			getBtn.setOnClickListener(this);
			SmsCodeUtils.getInstance().addSmsCodeListner(getBtn, this);
		}

		// otpEdit = (SipBox) childView
		// .findViewById(R.id.set_editlimitconfirm_otp);
		// smcEdit = (SipBox) childView
		// .findViewById(R.id.set_editlimitconfirm_smc);
		// smcEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// otpEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// otpEdit.setRandomKey_S(randomNumber);
		// smcEdit.setRandomKey_S(randomNumber);
		// smcEdit.setPasswordRegularExpression(CheckRegExp.OTP);
		// otpEdit.setPasswordRegularExpression(CheckRegExp.OTP);
		// otpEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		// smcEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		// smcEdit.setSipDelegator(this);
		// otpEdit.setSipDelegator(this);
		// //设定为数字键盘
		// otpEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// smcEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// smcEdit.setSaveEnabled(true);
		// smcEdit.setSaveFromParentEnabled(true);

		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		relative.measure(w, h);
		// otpLayout = (LinearLayout)
		// childView.findViewById(R.id.set_otp_layout);
		// smsLayout = (LinearLayout)
		// childView.findViewById(R.id.set_smc_layout);
		confrimBtn = (Button) childView.findViewById(R.id.set_confirm);
		consernBtn = (Button) childView.findViewById(R.id.set_consern);
		confrimBtn.setOnClickListener(this);
		consernBtn.setOnClickListener(this);
		// getBtn.setOnClickListener(this);
		initRightBtnForMain();
	}

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(otpEdit, smcEdit, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub

				BaseHttpEngine.showProgressDialog();
				String commConversationId = settingControl.editLimitConversationId;
				requestPSNGetTokenId(commConversationId);
				/*
				 * BaseHttpEngine.showProgressDialog();
				 * requestPSNGetTokenId(settingControl.editLimitConversationId);
				 */

			}
		});

	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		currency = extras.getString(Setting.I_CURRENCY);
		serviceId = extras.getString(Setting.I_SERVICEID);
		dayMax = extras.getString(Setting.I_DAYMAX);
		preDayMax = extras.getString(Setting.I_PREMAX);
		resetpreDayMax = extras.getString(Setting.I_RESETPREMAX);
		currencyTextView.setText(currency);
		serviceNameTextView.setText(LocalData.serviceCodeMap.get(serviceId));
		preDayMaxTextView.setText(StringUtil.parseStringPattern(preDayMax, 2));
		resetTv.setText(StringUtil.parseStringPattern(resetpreDayMax, 2));
		dayMaxTextView.setText(StringUtil.parseStringPattern(dayMax, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				serviceNameTextView);
		limit = new StringBuffer();
		limit = limit.append(serviceId);
		limit = limit.append("_");
		limit = limit.append(resetpreDayMax);

		factorListName = new ArrayList<String>();
		// if (!StringUtil.isNullOrEmpty(settingControl.factorList)) {
		// for (int i = 0; i < settingControl.factorList.size(); i++) {
		// Map<String, Object> itemMap = settingControl.factorList.get(i);
		// Map<String, String> securityMap = (Map<String, String>) itemMap
		// .get(Inves.FIELD);
		// String name = securityMap.get(Inves.NAME);
		// if (Inves.Smc.equals(name)) {
		// isSms = true;
		// smsLayout.setVisibility(View.VISIBLE);
		// } else if (Inves.Otp.equals(name)) {
		// isOtp = true;
		// otpLayout.setVisibility(View.VISIBLE);
		// }
		// factorListName.add(name);
		// }
		// }
		// SmsCodeUtils.getInstance().addSmsCodeListner(getBtn, this);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String token = (String) biiResponseBody.getResult();
		editLimit(token);
	}

	protected void editLimit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_EDITLIMIT);
		biiRequestBody
				.setConversationId(settingControl.editLimitConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.SET_EDITLIMIT_PSNLIMIT, limit.toString());
		/*
		 * if (isOtp) { map.put(Comm.Otp, otpStr); map.put(Comm.Otp_Rc, otp_Rc);
		 * 
		 * } if (isSms) { map.put(Comm.Smc, smcStr); map.put(Comm.Smc_Rc,
		 * smc_Rc); }
		 */

		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);
		map.put(Setting.TOKEN, token);
		// 防欺诈信息
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "editLimitCallback");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	@Override
	public void queryRandomNumberCallBack(Object resultObj) {
		super.queryRandomNumberCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		settingControl.randomNumber = (String) biiResponseBody.getResult();
		if (!StringUtil.isNullOrEmpty(settingControl.randomNumber)) {
			randomNumber = settingControl.randomNumber;
			init();
			initData();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_confirm:
			checkDate();
			/*
			 * ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>(); if
			 * (isSms) { RegexpBean otpRegex = new
			 * RegexpBean(getResources().getString( R.string.acc_smc_regex),
			 * smcEdit.getText().toString(), ConstantGloble.SIPSMCPSW);
			 * lists.add(otpRegex); } if (isOtp) { RegexpBean otpRegex = new
			 * RegexpBean(getResources().getString( R.string.active_code_regex),
			 * otpEdit.getText() .toString(), ConstantGloble.SIPOTPPSW);
			 * lists.add(otpRegex); }
			 * 
			 * if (RegexpUtils.regexpDate(lists)) { if (isOtp) { try { otpStr =
			 * otpEdit.getValue().getEncryptPassword(); otp_Rc =
			 * otpEdit.getValue().getEncryptRandomNum(); } catch (CodeException
			 * e) { LogGloble.e(TAG, "smcStr 密码控件问题");
			 * BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
			 * return; } } if (isSms) { try { smcStr =
			 * smcEdit.getValue().getEncryptPassword(); smc_Rc =
			 * smcEdit.getValue().getEncryptRandomNum(); } catch (CodeException
			 * e) { LogGloble.e(TAG, "smcStr 密码控件问题");
			 * BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
			 * return; } } BaseHttpEngine.showProgressDialog();
			 * requestPSNGetTokenId(settingControl.editLimitConversationId); }
			 */
			break;

		case R.id.set_consern:
			finish();
			break;
		case R.id.set_get:
			if (settingControl.editLimitConversationId != null) {
				sendMSCToMobile(settingControl.editLimitConversationId);
			}
			break;
		default:
			break;
		}
	}

}
