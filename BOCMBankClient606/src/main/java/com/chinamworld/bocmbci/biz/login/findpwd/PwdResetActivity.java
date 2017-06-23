package com.chinamworld.bocmbci.biz.login.findpwd;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cfca.mobile.device.SecResult;
import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.biz.login.LoginDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.SipBox;

/**
 * 找回密码 --设置密码
 * 
 * @author WJP
 * 
 */
public class PwdResetActivity extends LoginBaseAcitivity {

	private static final String TAG = "ResetPwdActivity";
	/** 加密控件 */
	private SipBox sipBox1;
	/** 加密控件 */
	private SipBox sipBox2;
	/** 动态因子layout */
	private LinearLayout linearLayout;
	/** 旧密码 */
	private LinearLayout ll_old_password;
	/** 动态口令编辑框 */
	// private EditText etActivecode;
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	/** 随机数 */
	private String password_RC1;
	private String password_RC2;
	/** 上一步 */
	// private Button lastBtn;
	/** 确认 */
	private Button confirmBtn;
	// private Button aquireActive;
	/** 随机数 */
	private String randomNumber;
	/** 账户卡号 */
	private String accountNumber;
	/** 证件类型 */
	private String identityType;
	/** 证件号码 */
	private String identityNumber;
	/** 手机验证码layout */
	private LinearLayout mobileLayout;
	/** 手机验证码 */
	private SipBox mobileSip;
	/** 动态口令Layout */
	private LinearLayout activeLayout;
	/** 动态口令 */
	private SipBox activeSip;
	/** 绑定标示 */
	private String combineFlag;
	/** string动态口令 */
	private String otp;
	private String otp_RC;
	/** String手机验证码 */
	private String smc;
	private String smc_RC;
	private String conversationId;
	/** 标识是否有硬件绑定 */
	private boolean isDeviceInfo = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.retrieve_pwd_title);

		View view = LayoutInflater.from(this).inflate(
				R.layout.findpwd_resetpwd_activity, null);
		tabcontent.addView(view);

		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(BACK_TO_THIS);
				finish();
			}
		});

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(R.string.title_step1),
						this.getResources().getString(R.string.findpwd_step2),
						this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		// 初始化数据
		aquireData();
		// 初始化控件
		init();

		if (combineFlag.equals(ConstantGloble.COMBINE_FLAG_N)) {
			linearLayout.setVisibility(View.INVISIBLE);
			confirmBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//					RegexpBean passReg = new RegexpBean(PwdResetActivity.this
//							.getString(R.string.password_no_label), sipBox1
//							.getText().toString().trim(),
//							"newForceModifyPasswordNew");
//					lists.add(passReg);
					if(StringUtil.isNullOrEmpty(sipBox1.getText())){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入密码");
						return;
					}else{
						if(sipBox1.getText().length()>20||sipBox1.getText().length()<8){
							BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
							return;
						}
					}
					if(false == CommPublicTools.checkCFCAInfo(sipBox1, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
						return ;
					if(StringUtil.isNullOrEmpty(sipBox2.getText())){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入确认密码");
						return;
					}else {
						if (sipBox2.getText().length() > 20 || sipBox2.getText().length() < 8) {
							BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
							return;
						}
					}

					if(false == CommPublicTools.checkCFCAInfo(sipBox2, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
						return ;
//					RegexpBean passEnsureReg = new RegexpBean(
//							PwdResetActivity.this
//									.getString(R.string.password_no_label),
//							sipBox2.getText().toString().trim(),
//							"newForceModifyPasswordNew");
//					lists.add(passEnsureReg);
					if (!RegexpUtils.regexpDate(lists)) {
						return;
					}

					try {
						password1 = sipBox1.getValue().getEncryptPassword();
						password_RC1 = sipBox1.getValue().getEncryptRandomNum();

					} catch (CodeException e) {
						BaseDroidApp.getInstanse().createDialog(null,
								"密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
					}
					try {
						password2 = sipBox2.getValue().getEncryptPassword();
						password_RC2 = sipBox2.getValue().getEncryptRandomNum();
					} catch (CodeException e) {
						BaseDroidApp.getInstanse().createDialog(null,
								"确认密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
					}

					BaseHttpEngine.showProgressDialog();
					requestForNoCombinResult();

				}
			});
		} else {// 绑定动态因子 Logon_retrievePassResult
			linearLayout.setVisibility(View.VISIBLE);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> factorList = (List<Map<String, Object>>) LoginDataCenter
					.getInstance().getRetrievePassActive()
					.get(Login.FACTORLIST);
			if (StringUtil.isNullOrEmpty(factorList)) {
				return;
			}
			int length = factorList.size();
			for (int i = 0; i < length; i++) {
				Map<String, String> field = (Map<String, String>) factorList
						.get(i).get(Login.FIELD);
				String name = field.get(Login.NAME);
				if (name.equals(Login.OTP)) {// 动态口令
					activeLayout.setVisibility(View.VISIBLE);
				} else if (name.equals(Login.SMC)) {// 手机验证码
					mobileLayout.setVisibility(View.VISIBLE);
				} else if (name.equals(Comm.DEVICEINFO)) {// 硬件绑定+手机验证码
					isDeviceInfo = true ;
					mobileLayout.setVisibility(View.VISIBLE);
				}
			}
			confirmBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//					RegexpBean passReg = new RegexpBean(PwdResetActivity.this
//							.getString(R.string.password_no_label), sipBox1
//							.getText().toString().trim(),
//							"newForceModifyPassword");
//					lists.add(passReg);
//					RegexpBean passEnsureReg = new RegexpBean(
//							PwdResetActivity.this
//									.getString(R.string.password_confirm_no_label),
//							sipBox2.getText().toString().trim(),
//							"newForceModifyPassword");
//					lists.add(passEnsureReg);
					if(StringUtil.isNullOrEmpty(sipBox1.getText())){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入密码");
						return;
					}else{
						if(sipBox1.getText().length()>20||sipBox1.getText().length()<8){
							BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
							return;
						}
					}
					if(false == CommPublicTools.checkCFCAInfo(sipBox1, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
						return ;
					if(StringUtil.isNullOrEmpty(sipBox2.getText())){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入确认密码");
						return;
					}else {
						if (sipBox2.getText().length() > 20 || sipBox2.getText().length() < 8) {
							BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
							return;
						}
					}

					if(false == CommPublicTools.checkCFCAInfo(sipBox2, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
						return ;
					if (!RegexpUtils.regexpDate(lists)) {
						return;
					}

					try {
						password1 = sipBox1.getValue().getEncryptPassword();
						password_RC1 = sipBox1.getValue().getEncryptRandomNum();

					} catch (CodeException e) {
						LogGloble.v(TAG, e.getMessage());
						BaseDroidApp.getInstanse().createDialog(null,
								"密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
						return;
					}
					try {
						password2 = sipBox2.getValue().getEncryptPassword();
						password_RC2 = sipBox2.getValue().getEncryptRandomNum();
					} catch (CodeException e) {
						BaseDroidApp.getInstanse().createDialog(null,
								"确认密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
						return;
					}

					if (activeLayout.isShown() && !mobileLayout.isShown()) {
						if (StringUtil.isNullOrEmpty(activeSip.getText())
								|| 0 >= activeSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入动态口令");
							return;
						}

						if (6 != activeSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"动态口令由6位数字组成");
							return;
						}

						try {
							otp = activeSip.getValue().getEncryptPassword();
							otp_RC = activeSip.getValue().getEncryptRandomNum();
						} catch (CodeException e) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"动态口令由6位数字组成");
						}
					} else if (!activeLayout.isShown()
							&& mobileLayout.isShown()) {
						if (StringUtil.isNullOrEmpty(mobileSip.getText())
								|| 0 >= mobileSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入手机交易码");
							return;
						}

						if (6 != mobileSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"手机交易码由6位数字组成");
							return;
						}
						try {
							smc = mobileSip.getValue().getEncryptPassword();
							smc_RC = mobileSip.getValue().getEncryptRandomNum();
						} catch (CodeException e) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"手机交易码由6位数字组成");
							return;
						}
					} else {
						if (StringUtil.isNullOrEmpty(activeSip.getText())
								|| 0 >= activeSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入动态口令");
							return;
						}

						if (6 != activeSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"动态口令由6位数字组成");
							return;
						}

						if (StringUtil.isNullOrEmpty(mobileSip.getText())
								|| 0 >= mobileSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入手机交易码");
							return;
						}

						if (6 != mobileSip.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"手机交易码由6位数字组成");
							return;
						}

						try {
							otp = activeSip.getValue().getEncryptPassword();
							otp_RC = activeSip.getValue().getEncryptRandomNum();
						} catch (CodeException e) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"动态口令由6位数字组成");
							return;
						}
						try {
							smc = mobileSip.getValue().getEncryptPassword();
							smc_RC = mobileSip.getValue().getEncryptRandomNum();
						} catch (CodeException e) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"手机交易码由6位数字组成");
							return;
						}
					}
					// if (combineFlag.equals(ConstantGloble.COMBINE_FLAG_N)) {
					// // 没有绑定动态因子
					// requestForNoCombinResult();
					// } else {
					// // 有绑定动态因子
					// requestLoginRetrievePassResult();
					// }

					BaseHttpEngine.showProgressDialog();
					requestLoginRetrievePassResult();

				}
			});
		}
		// 上一步按钮监听
		// lastBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// 加密控件1 -- start
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.ll_sip1);
		sipBox1 = new SipBox(this);
		// sipBox1.setHint(this.getResources().getString(R.string.hint_reg_pwd));
		initPasswordSipBox(sipBox1);
		linearLayout1.addView(sipBox1);
		// 加密控件1 -- end

		// 加密控件2 -- start
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.ll_sip2);
		sipBox2 = new SipBox(this);
		// sipBox2.setHint(this.getResources().getString(R.string.hint_findpwd_pwd_cof));
		initPasswordSipBox(sipBox2);
		linearLayout2.addView(sipBox2);
		// 加密控件2 -- end

		// 手机验证码
		mobileSip = (SipBox) this.findViewById(R.id.mobile_code_sip);
		mobileSip.setCipherType(SystemConfig.CIPHERTYPE);
		initCodeSipBox(mobileSip);
		// mobileSip.setHint(this.getResources().getString(R.string.hint_mobile_code));
		mobileLayout = (LinearLayout) this
				.findViewById(R.id.mobile_code_layout);

		SmsCodeUtils.getInstance().addSmsCodeListner(
				(Button) this.findViewById(R.id.smsbtn), smcOnclickListener);

		// 动态口令
		activeSip = (SipBox) this.findViewById(R.id.active_code_sip);
		activeSip.setCipherType(SystemConfig.CIPHERTYPE);
		initCodeSipBox(activeSip);
		// activeSip.setHint(this.getResources().getString(
		// R.string.hint_active_code));
		activeLayout = (LinearLayout) this
				.findViewById(R.id.active_code_layout);

		linearLayout = (LinearLayout) findViewById(R.id.code_layout);

		// lastBtn = (Button) findViewById(R.id.findpwd_btn_last);
		confirmBtn = (Button) findViewById(R.id.findpwd_btn_conf);
		// aquireActive = (Button) findViewById(R.id.findpwd_aquire_code_btn);

		ll_old_password = (LinearLayout) findViewById(R.id.ll_old_password);
		// 隐藏原密码
		ll_old_password.setVisibility(View.GONE);
	}

	/** 获取手机确认码监听 */
	private OnClickListener smcOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};

	/** 请求登录前手机确认码 */
	public void sendSMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnSendSMSCodeToMobilePre");
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		biiRequestBody.setParams(null);
		HttpManager
				.requestBii(biiRequestBody, this, "sendSMSCToMobileCallback");
	};

	private void aquireData() {
		randomNumber = LoginDataCenter.getInstance().getRandomNumber();
		conversationId = LoginDataCenter.getInstance().getConversationId();
		combineFlag = this.getIntent().getStringExtra(Login.COMBINE_FLAG);
		accountNumber = this.getIntent().getStringExtra(Login.ACCOUNT_NUM);
		identityType = this.getIntent().getStringExtra(Login.IDENTIFY_TYPE);
		identityNumber = this.getIntent().getStringExtra(Login.IDENTIFY_NUM);
	}

	/*
	 * private void requestForActive(){ BiiRequestBody biiRequestBody = new
	 * BiiRequestBody(); biiRequestBody.setId(BiiApi.Login.PASS_ACTIVE_API.id);
	 * biiRequestBody.setMethod(BiiApi.Login.PASS_ACTIVE_API.method);
	 * biiRequestBody.setConversationId(commConversationId); Map<String, String>
	 * map = new HashMap<String, String>();
	 * map.put(ConstantGloble.FINDPWD_ACCOUNT_NUM, "");
	 * map.put(ConstantGloble.FINDPWD_IDENTIFY_TYPE, "");
	 * map.put(ConstantGloble.FINDPWD_IDENTIFY_NUM, "");
	 * map.put(ConstantGloble.FINDPWD_COMBINID, "");
	 * biiRequestBody.setParams(map); HttpManager.requestBii(biiRequestBody,
	 * this, "requestForActiveCallback", true); }
	 * 
	 * /* public void requestForActiveCallback(Object resultObj){ BiiResponse
	 * biiResponse = (BiiResponse) resultObj; List<BiiResponseBody>
	 * biiResponseBodys = biiResponse.getResponse(); BiiResponseBody
	 * biiResponseBody = biiResponseBodys.get(0); Map<String,Object> result =
	 * (Map<String, Object>) biiResponseBody.getResult(); //手机验证码有效时间 String
	 * smcTrigerInterval = (String) result.get("smcTrigerInterval"); //安全因子数组
	 * List<Map<String,Map>> factorList = (List<Map<String, Map>>)
	 * result.get("factorList"); //
	 * "factorList":[{"field":{"name":"Otp","type":"password"}}]
	 * 
	 * 
	 * }
	 */
	/**
	 * 找回密码请求-没有绑定动态因子
	 */
	private void requestForNoCombinResult() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PASS_NO_COMBINE_RESULT_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.FINDPWD_NEW_PASS, password1);
		map.put(Login.FINDPWD_NEW_PASS_RC, password_RC1);
		map.put(Login.FINDPWD_NEW_PASS2, password2);
		map.put(Login.FINDPWD_NEW_PASS2_RC, password_RC2);
		map.put(Login.ACCOUNT_NUM, accountNumber);
		map.put(Login.IDENTIFY_TYPE, identityType);
		map.put(Login.IDENTIFY_NUM, identityNumber);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForNoCombinResultCallBack");
	}

	/**
	 * 找回密码请求回调-没有绑定动态因子 返回
	 * 
	 * @param resultObj
	 */
	public void requestForNoCombinResultCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {// 请求成功
			String result = (String) biiResponseBody.getResult();
			LogGloble.i(TAG, "result =" + result);
			Intent intent = new Intent();
			intent.setClass(PwdResetActivity.this,
					PwdResetSuccessActivity.class);
			startActivityForResult(intent, 0);
		} else {
			BiiError error = biiResponseBody.getError();
			LogGloble.e(TAG, "error =" + error.getMessage());
		}
	}

	/**
	 * 找回密码请求-有绑定动态因子
	 */
	private void requestLoginRetrievePassResult() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PASS_RETRIEVE_RESULT_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.FINDPWD_NEW_PASS, password1);
		map.put(Login.FINDPWD_NEW_PASS_RC, password_RC1);
		map.put(Login.FINDPWD_NEW_PASS2, password2);
		map.put(Login.FINDPWD_NEW_PASS2_RC, password_RC2);
		map.put(Login.ACCOUNT_NUM, accountNumber);
		map.put(Login.IDENTIFY_TYPE, identityType);
		map.put(Login.IDENTIFY_NUM, identityNumber);
		map.put(Login.SMC, smc);
		map.put(Login.SMC_RC, smc_RC);
		map.put(Login.OTP, otp);
		map.put(Login.OTP_RC, otp_RC);
		if(isDeviceInfo){
			//硬件信息    当用户选择96硬件绑定+手机交易码 
//			DeviceSecCryptor instance = DeviceSecCryptor.createInstatnce(PwdResetActivity.this);
//			String deviceInfo = "";
//			String deviceInfo_RC = "";
//			try {
//				SecResult secResult = instance.getEncryptedInfo(SystemConfig.CIPHERTYPE, false, ConstantGloble.OUT_PUT_VALUE_TYPE, 
//						randomNumber, BaseDroidApp.getInstanse().getOperatorId());
//				deviceInfo_RC = secResult.getEncryptedRC();
//				deviceInfo = secResult.getEncryptedInfo();
//			} catch (CodeException e) {
//				e.printStackTrace();
//			}
//			map.put(Comm.DEVICEINFO, deviceInfo);
//			map.put(Comm.DEVICEINFO_RC, deviceInfo_RC);
			
			SecResult secResult=DeviceInfoTools.getSecDeviceInfo(PwdResetActivity.this, randomNumber, BaseDroidApp.getInstanse().getOperatorId());
			if(secResult!=null){
				map.put("deviceInfo", secResult.getEncryptedInfo());
				map.put("deviceInfo_RC", secResult.getEncryptedRC());	
			}
		}
		map.put(Login.FINDPWD_SIGNEDDATA, "");
		SipBoxUtils.setSipBoxParams(map);
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestLoginRetrievePassResultCallBack");
	}

	/**
	 * 找回密码请求-有绑定动态因子 返回
	 * 
	 * @param resultObj
	 */
	public void requestLoginRetrievePassResultCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// if(StringUtil.isNullOrEmpty(result)){
		// BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.result_null));
		// }
		// TODO 需要改responsebody结构 返回数据没有放在result里面
		// DENY（拒绝）
		// CCC（外呼）
		// QUESTION（QA挑战），若返回QUESTION则接口不会返回交易数据
		// SMS（短信挑战）
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			Intent intent = new Intent();
			intent.setClass(PwdResetActivity.this,
					PwdResetSuccessActivity.class);
			startActivityForResult(intent, 0);
		} else {
			BiiError error = biiResponseBody.getError();
			LogGloble.e(TAG, "error =" + error.getMessage());
		}
	}

	/**
	 * 初始化密码控件
	 * 
	 * @param sipBox
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param1);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.PWD_MIN_LENGTH,
				ConstantGloble.MAX_LENGTH, SipBox.COMPLETE_KEYBOARD, randomNumber, this);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
//		sipBox.setPasswordMinLength(ConstantGloble.PWD_MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MAX_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.RESETPWD);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters1);
	}

	private void initCodeSipBox(SipBox sipBox) {
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		// sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(BACK_TO_THIS);
			finish();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (UN_FORGET_PWD_SUCCESS == resultCode) {
			setResult(UN_FORGET_PWD_SUCCESS);
			finish();
		}
	}

}
