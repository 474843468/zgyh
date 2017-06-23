package com.chinamworld.bocmbci.biz.setting.pass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 修改密码输入动态口令 等信息
 * 
 * @author xyl
 * 
 */
public class EditPassConfirmActivity extends PassBaseActivity {
	private static final String TAG = "EditPassConfirmActivity";
	/**
	 * token
	 */
	private String tokenId;
	/**
	 * 动态口令输入框
	 */
	private SipBox otpEdit;
	/**
	 * 手机验证码输入
	 */
	private SipBox smcEdit;
	private UsbKeyText usbKeyText;
	/**
	 * 确定按钮
	 */
	private Button confirm;
	private Button getBtn;
	/**
	 * 用户输入的动态口令码
	 */
	private String otpString;
	private String otp_Rc;
	/**
	 * 用户输入的手机验证码
	 */
	private String smcString;
	private String smc_RC;
	// private ArrayList<String> factorListName;
	private boolean isSms = false;
	private LinearLayout smsLayout;
	private boolean isOtp = false;
	private LinearLayout otpLayout;

	// private int httpTag;
	// private static final int RANDOM = 0;
	// private static final int EDITPASS = 1;
	private String random;
	private String conversationId;
	private View childView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		random = getIntent().getStringExtra(Setting.I_RANDOM);
		conversationId = getIntent().getStringExtra(Setting.I_CONVERSATIONID);
		childView = mainInflater.inflate(R.layout.setting_editpass_confirm,
				null);
		tabcontent.addView(childView);
		init();
		// httpTag = RANDOM;
		// requestForRandomNumber();
		// BaseHttpEngine.showProgressDialogCanGoBack();
	}

	// @Override
	// public void requestCommConversationIdCallBack(Object resultObj) {
	// super.requestCommConversationIdCallBack(resultObj);
	// switch (httpTag) {
	// case RANDOM:
	// break;
	// case EDITPASS:
	// requestPSNGetTokenId();
	// break;
	//
	// default:
	// break;
	// }
	//
	// }

	// @Override
	// public void queryRandomNumberCallBack(Object resultObj) {
	// super.queryRandomNumberCallBack(resultObj);
	// BaseHttpEngine.dissMissProgressDialog();
	// if (!StringUtil.isNullOrEmpty(settingControl.randomNumber)) {
	// random = settingControl.randomNumber;
	// init();
	// }
	// }

	// 请求token 的回调
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		settingLoginPass();

	}

	@Override
	public void sendMSCToMobileCallback(Object resultObj) {
		super.sendMSCToMobileCallback(resultObj);
	}

	protected void settingLoginPass() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_PWD_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		// if (isOtp) {
		// map.put(Setting.SET_PWD_RESULT_Otp, otpString);
		// map.put(Comm.Otp_Rc, otp_Rc);
		// }
		// if (isSms) {
		// map.put(Setting.SET_PWD_RESULT_SMC, smcString);
		// map.put(Comm.Smc_Rc, smc_RC);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Setting.TOKEN, tokenId);
		// 防欺诈信息
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager
				.requestBii(biiRequestBody, this, "settingLoginPassCallBack");
	}

	@Override
	public void settingLoginPassCallBack(Object resultObj) {
		super.settingLoginPassCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				getResources().getString(R.string.set_editpasssuccess_info));
		activityTaskManager.removeAllActivity();
		startActivity(new Intent(this, EditLoginPassActivity.class));
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		// View childView = mainInflater.inflate(
		// R.layout.setting_editpass_confirm, null);
		// tabcontent.addView(childView);
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		otpEdit = (SipBox) childView.findViewById(R.id.set_otp_edit);
		otpEdit.setCipherType(SystemConfig.CIPHERTYPE);
		smcEdit = (SipBox) childView.findViewById(R.id.set_smc_edit);
		smcEdit.setCipherType(SystemConfig.CIPHERTYPE);
		otpEdit.setRandomKey_S(random);
		smcEdit.setRandomKey_S(random);

		otpEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		smcEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);

		otpEdit.setPasswordRegularExpression(CheckRegExp.OTP);
		smcEdit.setPasswordRegularExpression(CheckRegExp.OTP);

		smcEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		otpEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);

		smcEdit.setSipDelegator(this);
		otpEdit.setSipDelegator(this);

		// 设定为数字键盘
		otpEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		smcEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

		confirm = (Button) childView.findViewById(R.id.set_editpass_confirm);
		getBtn = (Button) childView.findViewById(R.id.set_get);
		confirm.setOnClickListener(this);
		getBtn.setOnClickListener(this);

		smsLayout = (LinearLayout) childView.findViewById(R.id.set_smc_layout);
		otpLayout = (LinearLayout) childView.findViewById(R.id.set_otp_layout);
		setTitle(getResources().getString(R.string.set_title_editpass));

		// factorListName = new ArrayList<String>();
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
		if (isSms) {
			smsLayout.setVisibility(View.VISIBLE);
		}
		if (isOtp) {
			otpLayout.setVisibility(View.VISIBLE);
		}
		SmsCodeUtils.getInstance().addSmsCodeListner(getBtn, this);
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) childView.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, random,
				settingControl.responseResult, this);
		isOtp = usbKeyText.getIsOtp();
		isSms = usbKeyText.getIsSmc();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_back:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.set_editpass_confirm:
			// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			// if (isSms) {
			// RegexpBean SMCRegex = new RegexpBean(getResources().getString(
			// R.string.acc_smc_regex), smcEdit.getText().toString(),
			// ConstantGloble.SIPSMCPSW);
			// lists.add(SMCRegex);
			//
			// }
			// if (isOtp) {
			// RegexpBean otpRegex = new RegexpBean(getResources().getString(
			// R.string.active_code_regex), otpEdit.getText()
			// .toString(), ConstantGloble.SIPOTPPSW);
			// lists.add(otpRegex);
			// }
			//
			// if (RegexpUtils.regexpDate(lists)) {
			// if (isOtp) {
			// try {
			// otpString = otpEdit.getValue().getEncryptPassword();
			// otp_Rc = otpEdit.getValue().getEncryptRandomNum();
			// } catch (CodeException e) {
			// LogGloble.e(TAG, "smcStr 密码控件问题");
			// BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
			// return;
			// }
			// }
			// if (isSms) {
			// try {
			// smcString = smcEdit.getValue().getEncryptPassword();
			// otp_Rc = smcEdit.getValue().getEncryptRandomNum();
			// } catch (CodeException e) {
			// LogGloble.e(TAG, "smcStr 密码控件问题");
			// BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
			// return;
			// }
			//
			// }
			// // httpTag = EDITPASS;
			// // requestCommConversationId();
			// requestPSNGetTokenId(conversationId);
			// BaseHttpEngine.showProgressDialog();
			// }
			/** 安全工具数据校验 */
			usbKeyText.checkDataUsbKey(otpEdit, smcEdit,
					new IUsbKeyTextSuccess() {

						@Override
						public void SuccessCallBack(String result, int errorCode) {
							// TODO Auto-generated method stub
							requestPSNGetTokenId(conversationId);
							BaseHttpEngine.showProgressDialog();
						}
					});
			break;
		case R.id.set_get:
			sendMSCToMobile(conversationId);
			break;
		default:
			break;
		}
	}

}
