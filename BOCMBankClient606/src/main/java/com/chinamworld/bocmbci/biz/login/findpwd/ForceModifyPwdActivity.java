package com.chinamworld.bocmbci.biz.login.findpwd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.biz.login.LoginDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: ForceModifyPwdActivity
 * @Description: 强制修改密码
 * @author JiangWei
 * @date 2013-7-9 下午4:27:07
 */
public class ForceModifyPwdActivity extends LoginBaseAcitivity {

	private static final String TAG = "ForceModifyPwdActivity";
	/** 加密控件 */
	private SipBox sipBox1;
	/** 加密控件 */
	private SipBox sipBox2;
	/** 加密控件 */
	private SipBox sipBox3;
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	/** 原密码 */
	private String oldPassword;
	/** 随机数 */
	private String password_RC1;
	private String password_RC2;
	private String oldPassword_RC;
	/** 确认 */
	private Button confirmBtn;
	/** 随机数 */
	private String randomNumber;
	/** 会话id */
	private String conversationId;
	
	private EditText accountNumEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.retrieve_pwd_title);

		View view = LayoutInflater.from(this).inflate(
				R.layout.findpwd_force_resetpwd_activity, null);
		tabcontent.addView(view);

		// ibBack.setVisibility(View.GONE);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(FORCE_MODIFY_REQUEST_BACK);
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

		aquireData();
		requestCommConversationId();
	}

	/**
	 * @Title: aquireData
	 * @Description: 初始化数据
	 * @param
	 * @return void
	 */
	private void aquireData() {
		randomNumber = LoginDataCenter.getInstance().getRandomNumber();
		conversationId = LoginDataCenter.getInstance().getConversationId();
		// oldPassword = this.getIntent().getStringExtra(Login.OLD_PASSWORD);
		// oldPassword_RC =
		// this.getIntent().getStringExtra(Login.OLD_PASSWORD_RC);
	}

	/**
	 * @Title: init
	 * @Description:初始化控件
	 * @param
	 * @return void
	 */
	private void init() {
		// 加密控件1
		// LinearLayout linearLayout1 = (LinearLayout)
		// findViewById(R.id.ll_sip1);
		sipBox1 = (SipBox) findViewById(R.id.ll_sip1);
		sipBox1.setCipherType(SystemConfig.CIPHERTYPE);
		initPasswordSipBox(sipBox1);
		sipBox1.setPasswordRegularExpression(CheckRegExp.RESETPWD);
		// linearLayout1.addView(sipBox1);

		// 加密控件2
		// LinearLayout linearLayout2 = (LinearLayout)
		// findViewById(R.id.ll_sip2);
		sipBox2 = (SipBox) findViewById(R.id.ll_sip2);
		sipBox2.setCipherType(SystemConfig.CIPHERTYPE);
		initPasswordSipBox(sipBox2);
		sipBox2.setPasswordRegularExpression(CheckRegExp.RESETPWD);
		// linearLayout2.addView(sipBox2);

		// 加密控件2
		// LinearLayout linearLayout3 = (LinearLayout)
		// findViewById(R.id.ll_sip3);
		sipBox3 = (SipBox) findViewById(R.id.ll_sip3);
		sipBox3.setCipherType(SystemConfig.CIPHERTYPE);
		initPasswordSipBox(sipBox3);
		sipBox3.setPasswordRegularExpression(CheckRegExp.OLD_LOGIN_PWD);
		// linearLayout3.addView(sipBox3);

		accountNumEditText =(EditText)findViewById(R.id.accountNumET);
		
		confirmBtn = (Button) findViewById(R.id.findpwd_btn_conf);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtil.isNullOrEmpty(sipBox3.getText().toString()
						.trim())) {
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog("请输入原密码");
					return;
				}
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				// RegexpBean passReg22 = new
				// RegexpBean(ForceModifyPwdActivity.this.getString(R.string.password_no_label),
				// sipBox3.getText().toString().trim(),
				// "newForceModifyPassword");
				// lists.add(passReg22);
				// if (!RegexpUtils.regexpDate(lists)){
				// return;
				// }
				try {
					oldPassword = sipBox3.getValue().getEncryptPassword();
					oldPassword_RC = sipBox3.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog("原密码格式不正确");
					return;
				}
				if (StringUtil.isNullOrEmpty(sipBox1.getText().toString()
						.trim())) {
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog("请输入新密码");
					return;
				}

				RegexpBean passReg = new RegexpBean("新密码", sipBox1
						.getText().toString().trim(), "newForceModifyPassword");
				lists.add(passReg);
				if (!RegexpUtils.regexpDate(lists)) {
					return;
				}
				lists.clear();
				if (StringUtil.isNullOrEmpty(sipBox2.getText().toString()
						.trim())) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"请输入确认新密码");
					return;
				}
				RegexpBean passEnsureReg = new RegexpBean(
						"确认新密码",
						sipBox2.getText().toString().trim(),
						"newForceModifyPassword");
				lists.add(passEnsureReg);
				if (!RegexpUtils.regexpDate(lists)) {
					return;
				}
				try {
					password1 = sipBox1.getValue().getEncryptPassword();
					password_RC1 = sipBox1.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"新密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
					return;
				}

				try {
					password2 = sipBox2.getValue().getEncryptPassword();
					password_RC2 = sipBox2.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"确认新密码格式不正确，请参照页面温馨提示中的密码格式要求重新输入");
					return;
				}
				if(accountNumEditText.getText().toString().trim().length() < 6){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入六位数字");
					return;
				}
//				lists.clear();
//				RegexpBean accNum = new RegexpBean(
//						"关联账户后六位",
//								accountNumEditText.getText().toString().trim(),
//						"postcode");
//				lists.add(accNum);
//				if (!RegexpUtils.regexpDate(lists)) {
//					return;
//				}
				
				BaseHttpEngine.showProgressDialog();
				requestForceModifyPwd();
			}
		});
	}

	/**
	 * @Title: requestForceModifyPwd
	 * @Description: 强制修改密码接口的请求
	 * @param
	 * @return void
	 */
	private void requestForceModifyPwd() {
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.FORCE_MODIFY_PASSWORD);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.LOGIN_NAME, "");
		map.put(Login.OLD_PASSWORD, oldPassword);
		map.put(Login.OLD_PASSWORD_RC, oldPassword_RC);
		map.put(Login.FINDPWD_NEW_PASS, password1);
		map.put(Login.FINDPWD_NEW_PASS_RC, password_RC1);
		map.put(Login.FINDPWD_NEW_PASS2, password2);
		map.put(Login.FINDPWD_NEW_PASS2_RC, password_RC2);
		map.put(Login.COMBIN_ID, "8");
		map.put("cardNoSixLast", this.accountNumEditText.getText().toString());
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForceModifyPwdCallBack");
	}

	/**
	 * @Title: requestForceModifyPwdCallBack
	 * @Description: 强制修改密码接口请求的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestForceModifyPwdCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getResources().getString(
							R.string.reset_password_success_detail),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							// setResult(RESULT_OK);
							// finish();
							requestForLogout();
							BaseDroidApp.getInstanse().clientLogOut();
							// TODO P402
							ActivityTaskManager.getInstance()
									.removeAllActivity();

//							Intent intent = new Intent();
//							intent.setClass(ForceModifyPwdActivity.this,
//									LoginActivity.class);
//							ForceModifyPwdActivity.this.startActivity(intent);
							BaseActivity.getLoginUtils(ForceModifyPwdActivity.this).exe(new LoginTask.LoginCallback() {

								@Override
								public void loginStatua(boolean isLogin) {

								}
							});
						}
					});
		} else {
			BiiError error = biiResponseBody.getError();
			LogGloble.e(TAG, "error =" + error.getMessage());
		}
	}

	/**
	 * @Title: initPasswordSipBox
	 * @Description: 初始化密码控件
	 * @param @param sipBox
	 * @return void
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		// LinearLayout.LayoutParams param1 = new
		// LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// sipBox.setLayoutParams(param1);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
		// sipBox.setPasswordMinLength(ConstantGloble.PWD_MIN_LENGTH);
		// sipBox.setId(10002);
		sipBox.setPasswordMaxLength(ConstantGloble.MAX_LENGTH);
		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		// sipBox.setBackgroundResource(R.drawable.input_text_bg);
		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(
//				R.string.sipboxtextsize)));
		sipBox.setSipDelegator(this);
		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters1);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
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
		LoginDataCenter.getInstance().setRandomNumber(randomNumber);
		// 加密控件设置随机数
		init();
		LogGloble.i(TAG, resultObj.toString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		// 返回按钮的时候 退出登录
		case KeyEvent.KEYCODE_BACK:
			setResult(FORCE_MODIFY_REQUEST_BACK);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出登录Logout
	 */
	public void requestForLogout() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGOUT_API);
		HttpManager
				.requestBii(biiRequestBody, this, "requestForLogoutCallBack");
	}

	/**
	 * 退出登录Logout
	 */
	public void requestForLogoutCallBack(Object resultObj) {

	}
}
