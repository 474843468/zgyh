package com.chinamworld.bocmbci.biz.login.reg;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.biz.login.LoginDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.SipBox;

/**
 * 注册---设置密码
 * 
 * @author WJP
 * 
 */
public class RegisterSetNamePwdActivity extends LoginBaseAcitivity {

	/** 是否需要动态因子标识 */
	public static final String IS_NEED_ACTIVE = "isNeedActive";
	/** 加密控件 */
	private SipBox sipBox1;
	/** 加密控件 */
	private SipBox sipBox2;
	/** 加密控件 动态口令 */
	private SipBox sipBox3;
	/** 登录名 */
	private EditText loginNameEt;
	private String loginName;
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	/** 随机数 */
	private String password_RC1;
	private String password_RC2;
	/** 动态口令 */
	private String otp = "";
	private String otp_RC = "";
	/**手机确认码*/
	private String smc = "";
	private String smc_RC ="";
	/** 下一步 */
	private Button nextBtn;
	/** 返回 */
	private Button ibBack;
	/** 手机确认码 */
	private Button phoneCheck;
	/** 随机数 */
	private String randomNumber;
	/** 账户类型 */
	private String accountType;
	/** 账户卡号 */
	private String accountNumber;
	/** 是否需要动态因子 */
	private boolean isNeedActive;
	/** 防重标识 */
	private String tokenId;
	private String registerName;

//	private EditText editPhoneCheck;
	//手机确认码改为加密控件
	private SipBox editPhoneCheck;
	private EditText editWelcomeEt;
	/** 是否为电子卡 */
	private boolean isEcard;
	/** 手机号 */
	private String mobile;
	/** 是否为电子银行 */
//	private boolean isEbank;
	private LinearLayout ll_isebank;
	private TextView loginnametv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.self_reg_title);

		View view = LayoutInflater.from(this).inflate(
				R.layout.register_setnamepwd_activity, null);
		tabcontent.addView(view);

		randomNumber = LoginDataCenter.getInstance().getRandomNumber();

		accountNumber = this.getIntent().getStringExtra(Login.ACCOUNT_NUM);
		accountType = this.getIntent().getStringExtra(Login.ACCOUNT_TYPE);
		isNeedActive = this.getIntent().getBooleanExtra(IS_NEED_ACTIVE, false);
		isEcard = this.getIntent().getBooleanExtra(Login.ISECARD, false);
		mobile = this.getIntent().getStringExtra(Login.REGISTER_MOBILE);
		// 初始化控件
		init();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(R.string.title_step1),
						this.getResources().getString(R.string.register_step2),
						this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);

		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginName = loginNameEt.getText().toString().trim();
				if(loginName.indexOf("*")!=-1)
					loginName = mobile;
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				if(!isEcard){
					RegexpBean regBankNum = new RegexpBean(
					RegisterSetNamePwdActivity.this
							.getString(R.string.regexp_phoneNum_tran),
					loginName, "longMobile");
					lists.add(regBankNum);
				}
				if(StringUtil.isNullOrEmpty(sipBox1.getText())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入登陆密码");
					return;
				}else{
					if(sipBox1.getText().length()>20||sipBox1.getText().length()<8){
						BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
						return;
					}
				}

				if(false == CommPublicTools.checkCFCAInfo(sipBox1, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
					return ;
//				RegexpBean passReg = new RegexpBean(
//						RegisterSetNamePwdActivity.this
//								.getString(R.string.registe_password), sipBox1
//								.getText().toString().trim(),
//						"newForceModifyPasswordNew");
//				lists.add(passReg);
				if(StringUtil.isNullOrEmpty(sipBox2.getText())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入确认登陆密码");
					return;
				}else {
					if (sipBox2.getText().length() > 20 || sipBox2.getText().length() < 8) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
						return;
					}
				}
				if(false == CommPublicTools.checkCFCAInfo(sipBox2, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
					return ;
//				RegexpBean passEnsureReg = new RegexpBean(
//						RegisterSetNamePwdActivity.this
//								.getString(R.string.registe_repassword),
//						sipBox2.getText().toString().trim(),
//						"newForceModifyPasswordNew");
//				lists.add(passEnsureReg);
				if (!StringUtil.isNull(editWelcomeEt.getText().toString()
						.trim())) {
					String editText = editWelcomeEt.getText().toString();
					if(!StringUtil.simpleCheckPreHint(editText)){
						StringBuffer sb = new StringBuffer();
						sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
						sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
						String editInfo = sb.toString();
						BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								BaseDroidApp.getInstanse().dismissMessageDialog();
							}
						});
						return;
					}
//					editText = editText.toLowerCase();
//					//去掉空格
//					editText = editText.replaceAll(" ", "");
//					//去掉换行符
//					editText = editText.replaceAll("\r|\n", "");
//					if(editText.contains("eval(")){
//						int evalLength = editText.indexOf("eval(");
//						int last = editText.indexOf(")", evalLength);
//						if(last>=0){
//							StringBuffer sb = new StringBuffer();
//							sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//							sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//							String editInfo = sb.toString();
//							BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									BaseDroidApp.getInstanse().dismissMessageDialog();
//								}
//							});
//							return;
//						}
//					}
//					if(editText.contains("onload(")){
//						int onloadLength = editText.indexOf("onload(");
//						int last = editText.indexOf(")", onloadLength);
//						if(last>=0){
//							StringBuffer sb = new StringBuffer();
//							sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//							sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//							String editInfo = sb.toString();
//							BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									BaseDroidApp.getInstanse().dismissMessageDialog();
//								}
//							});
//							return;
//						}
//					}
//					if(editText.contains("javascript:")||editText.contains("vbscript:")||editText.contains("src='")){
//						StringBuffer sb = new StringBuffer();
//						sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//						sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//						String editInfo = sb.toString();
//						BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}
//						});
//						return;
//					}
					RegexpBean welcomMessageReg = new RegexpBean(
							RegisterSetNamePwdActivity.this
									.getString(R.string.set_obligatemessage_no),
							editWelcomeEt.getText().toString().trim(),
							"oblimessage");
					lists.add(welcomMessageReg);
				}
				/*RegexpBean phone_check = new RegexpBean(
						RegisterSetNamePwdActivity.this
								.getString(R.string.smskey_regexp),
						editPhoneCheck.getText().toString().trim(), "postcode");
				lists.add(phone_check);*/
				// edit_phone_check
				if (!RegexpUtils.regexpDate(lists)) {
					return;
				}
				try {
					password1 = sipBox1.getValue().getEncryptPassword();
					password_RC1 = sipBox1.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
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
				try {
						if (StringUtil.isNullOrEmpty(editPhoneCheck.getText())
								|| 0 >= editPhoneCheck.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入手机确认码");
							ViewUtils.scrollShow(editPhoneCheck);
							return;
						}
						smc = editPhoneCheck.getValue().getEncryptPassword();
						smc_RC = editPhoneCheck.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"手机确认码由6位数字组成");
					ViewUtils.scrollShow(editPhoneCheck);
					return;
				}
				try {
					if (((LinearLayout) findViewById(R.id.code_layout))
							.getVisibility() == View.VISIBLE) {
						if (StringUtil.isNullOrEmpty(sipBox3.getText())
								|| 0 >= sipBox3.getText().toString().length()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请输入动态口令");
							ViewUtils.scrollShow(sipBox3);
							return;
						}
						otp = sipBox3.getValue().getEncryptPassword();
						otp_RC = sipBox3.getValue().getEncryptRandomNum();
					}
				} catch (CodeException e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"动态口令由6位数字组成");
					ViewUtils.scrollShow(sipBox3);
					return;
				}
				requestRegisterUnique();// 检查用户名唯一性
			}
		});
	}

	private void init() {
		LinearLayout linearActive = (LinearLayout) findViewById(R.id.code_layout);
		LinearLayout linearPhoneCheck = (LinearLayout) findViewById(R.id.phone_check_layout);
		linearPhoneCheck.setVisibility(View.VISIBLE);
		if (isNeedActive) {
			linearActive.setVisibility(View.VISIBLE);
		} else {
			linearActive.setVisibility(View.GONE);
		}
		// 加密控件1
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.ll_sip1);
		sipBox1 = new SipBox(this);
		sipBox1.setCipherType(SystemConfig.CIPHERTYPE);
		initSipBox(sipBox1);
		sipBox1.setPasswordRegularExpression(CheckRegExp.RESETPWD);
		linearLayout1.addView(sipBox1);
		
		final TextView textAnotation = (TextView)findViewById(R.id.annotations);
		textAnotation.setVisibility(View.GONE);
		sipBox1.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					PopupWindowUtils.getInstance().showAllTextPopUpWindowOnTop(RegisterSetNamePwdActivity.this, sipBox1,"密码长度为8－20位，区分大小写，至少需要包含一个字母和一个数字，支持键盘可见字符（不包括空格，不包括汉字），不允许全角的字符。",true);
//					textAnotation.setVisibility(View.VISIBLE);
				}else{
//					textAnotation.setVisibility(View.GONE);
					sipBox1.hideSecurityKeyBoard();
				}
				
			}
		});
		// 加密控件2
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.ll_sip2);
		sipBox2 = new SipBox(this);
		sipBox2.setCipherType(SystemConfig.CIPHERTYPE);
		initSipBox(sipBox2);
		sipBox2.setPasswordRegularExpression(CheckRegExp.RESETPWD);
		linearLayout2.addView(sipBox2);

		// 加密控件3
		LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.ll_sip3);
		sipBox3 = new SipBox(this);
		sipBox3.setCipherType(SystemConfig.CIPHERTYPE);
		initSipBox1(sipBox3);
		sipBox3.setPasswordRegularExpression(CheckRegExp.OTP);
		linearLayout3.addView(sipBox3);

//		editPhoneCheck = (EditText) findViewById(R.id.edit_phone_check);
		//修改为加密控件
		editPhoneCheck = (SipBox) findViewById(R.id.edit_phone_check);
		editPhoneCheck.setCipherType(SystemConfig.CIPHERTYPE);
		initSipBoxWithNoLayout(editPhoneCheck);
		editPhoneCheck.setPasswordRegularExpression(CheckRegExp.OTP);
		
		editWelcomeEt = (EditText) findViewById(R.id.register_welcome_et);
		EditTextUtils.setLengthMatcher(this, editWelcomeEt, 60);
		loginNameEt = (EditText) findViewById(R.id.register_et_login_name);
		ll_isebank=(LinearLayout)findViewById(R.id.ll_isebank);
		loginnametv= (TextView) findViewById(R.id.register_tv_login_name);
		TextView loginname = (TextView)findViewById(R.id.register_tv_login);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,loginname);
		TextView passInfo = (TextView)findViewById(R.id.passinfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, passInfo);
		TextView passConfir = (TextView)findViewById(R.id.passconfir);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, passConfir);
		if(isEcard){	
			if(!StringUtil.isNullOrEmpty(mobile)){
				loginNameEt.setText(StringUtil.getThreeFourThreeString(mobile));
			}else {
				loginNameEt.setText("-");
			}
			loginNameEt.setEnabled(false);
		}else{
			ll_isebank.setVisibility(View.VISIBLE);
			if(!StringUtil.isNullOrEmpty(mobile)){
				loginnametv.setText(StringUtil.getThreeFourThreeString(mobile));	
			}else {
				loginnametv.setText("-");
			}
		}

		phoneCheck = (Button) findViewById(R.id.btn_phone_check);
		phoneCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				editPhoneCheck.setText("");
				loginName = loginNameEt.getText().toString().trim();
				if(loginName.indexOf("*")!=-1)
					loginName = mobile;
				
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				RegexpBean regBankNum = new RegexpBean(
//						RegisterSetNamePwdActivity.this
//								.getString(R.string.regexp_phoneNum_tran),
//						loginName, "longMobile");
//				lists.add(regBankNum);
//				if (!RegexpUtils.regexpDate(lists)) {
//					return;
//				}
				TimeCount time = new TimeCount(60 * 1000, 1000, phoneCheck);// 构造CountDownTimer对象
				time.start();
				requestSendRandomCode();
			}
		});

		nextBtn = (Button) findViewById(R.id.findpwd_btn_next);
		ibBack = (Button) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(BACK_TO_THIS);
				finish();
			}
		});
	}

	/**
	 * 计时器
	 * 
	 * @author xby
	 * 
	 */
	private class TimeCount extends CountDownTimer {
		private int waitTime = 60;
		private Button button;

		public TimeCount(long millisInFuture, long countDownInterval,
				Button button) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
			this.button = button;
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			button.setText(R.string.epay_pub_bt_get_note_code);
			button.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			button.setClickable(false);
			waitTime--;
			button.setText("" + waitTime);
		}
	}

	/**
	 * @Title: initSipBox
	 * @Description: 初始化sipBox
	 * @param @param sipBox 加密控件
	 * @return void
	 */
	private void initSipBox(SipBox sipBox) {
		LinearLayout.LayoutParams param2 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param2);
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setPadding(getResources().getDimensionPixelSize(R.dimen.edittext_paddinglr),0,0,0);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.PWD_MIN_LENGTH,
				ConstantGloble.MAX_LENGTH, SipBox.COMPLETE_KEYBOARD, randomNumber, this);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
//		// sipBox.setPasswordMinLength(ConstantGloble.PWD_MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MAX_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
//		sipBox.setPadding(getResources().getDimensionPixelSize(R.dimen.edittext_paddinglr),0,0,0);
		// sipBox.setHint(this.getResources().getString(R.string.hint_reg_pwd));
		InputFilter[] filters2 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters2);
	}

	/**
	 * @Title: initSipBox
	 * @Description: 初始化sipBox 数字键盘
	 * @param @param sipBox 加密控件
	 * @return void
	 */
	private void initSipBox1(SipBox sipBox) {
		LinearLayout.LayoutParams param2 = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param2);
		initSipBoxWithNoLayout(sipBox);
	}

	
	
	/**
	 * @Title: initSipBox
	 * @param @param sipBox 加密控件
	 * @return void
	 */
	private void initSipBoxWithNoLayout(SipBox sipBox) {
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		// sipBox.setPasswordMinLength(ConstantGloble.PWD_MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// sipBox.setHint(this.getResources().getString(R.string.hint_reg_pwd));
		InputFilter[] filters2 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters2);
	}

	
	/**
	 * @Title: requestSendRandomCode
	 * @Description: 获取并发送手机验证码
	 * @param
	 * @return void
	 */
	private void requestSendRandomCode() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_SEND_RANDOM_CODE_TO_MOBILE);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		Map<String, String> map = new HashMap<String, String>();
		map.put(Login.REGISTER_MOBILE, loginName);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestSendRandomCodeCallBack");
	}

	/**
	 * @Title: requestSendRandomCodeCallBack
	 * @Description: 获取并发送手机验证码回调
	 * @param
	 * @return void
	 */
	public void requestSendRandomCodeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			// TimerTaskUtil.getInstance().startGet(this, phoneCheck);
		}
	}

	/**
	 * @Title: requestTokenIdLoginPre
	 * @Description: 获得防重标识token（登录前）
	 * @param
	 * @return void
	 */
	private void requestTokenIdLoginPre() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_GET_TOKENID_LOGIN_PRE);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		Map<String, String> map = new HashMap<String, String>();
		map.put(Login.LOGIN_NAME, loginName);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestTokenIdLoginPreCallBack");
	}

	/**
	 * @Title: requestTokenIdLoginPreCallBack
	 * @Description: 获得防重标识token（登录前） 回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestTokenIdLoginPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		requstOpenMobileBank();
	}

	/**
	 * @Title: requstOpenMobileBank
	 * @Description:申请开通手机银行
	 * @param
	 * @return void
	 */
	private void requstOpenMobileBank() {
		BaseHttpEngine.showProgressDialog();

		Map<String, Object> registerVerifyData = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.REGISTER_VERIFY_CALLBACK_KEY);

		String userId = (String) registerVerifyData.get(Login.USER_ID);
		String mobile = (String) registerVerifyData.get(Login.REGISTER_MOBILE);
//		String identifyNumber = (String) registerVerifyData
//				.get(Login.IDENTIFY_NUM);
//		String identifyType = (String) registerVerifyData
//				.get(Login.IDENTIFY_TYPE);
		String name = (String) registerVerifyData.get(Login.REGISTER_NAME);
		registerName = name;
		String isEbank = (String) registerVerifyData.get(Login.IS_EBANK);
		String identifyTypes = (String) registerVerifyData.get("identifyType");
		String identityNumber = (String) registerVerifyData.get("identifyNumber");
		
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_OPEN_MOBILEBANK);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Login.IS_EBANK, isEbank);
//		map.put(Login.QUESTION_ONE, "");
//		map.put(Login.ANSWER_ONE, "");
		String loginNameEtText = loginNameEt.getText().toString().trim();
		if(loginNameEtText.indexOf("*")!=-1){
			loginNameEtText = mobile;
		}
		map.put(Login.REGISTER_MOBILE, loginNameEtText);
//		map.put(Login.CONFIRM_NUMBER, editPhoneCheck.getText().toString()
//				.trim());
		map.put(Login.CONFIRM_NUMBER, smc);
		map.put(Login.CONFIRM_NUMBER_RC, smc_RC);
		map.put(Login.LOGIN_NAME, loginNameEtText);//name
		map.put(Login.FINDPWD_NEW_PASS, password1);
		map.put(Login.FINDPWD_NEW_PASS_RC, password_RC1);
		map.put(Login.FINDPWD_NEW_PASS2, password2);
		map.put(Login.FINDPWD_NEW_PASS2_RC, password_RC2);
		map.put(Login.TOKEN, tokenId);
//		map.put(Login.IDENTIFY_TYPE, identifyType);
//		map.put(Login.IDENTIFY_NUM, identifyNumber);
		map.put(Login.ACCOUNT_TYPE, accountType);
		map.put(Login.ACCOUNT_NUM, accountNumber);
		map.put(Login.REGISTER_WELCOMEINFO, editWelcomeEt.getText().toString()
				.trim());
		if(isNeedActive) {
			map.put(Login.OTP, otp);
			map.put(Login.OTP_RC, otp_RC);	
		}

		map.put(Login.USER_ID, userId);
		map.put("identifyType", identifyTypes);
		map.put("identifyNumber", identityNumber);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requstOpenMobileBankCallback");
	}

	/**
	 * @Title: requstOpenMobileBankCallback
	 * @Description: 开通手机银行的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requstOpenMobileBankCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();

		String mobileNum = resultMap.get(Login.REGISTER_MOBILE);
		String loginNames = resultMap.get(Login.LOGIN_NAME);

		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {// 注册成功

			Intent intent = new Intent(this, RegisterSuccessActivity.class);
			intent.putExtra(Login.LOGIN_NAME, registerName);// 注册用户姓名
			intent.putExtra(Login.REGISTER_MOBILE, mobileNum);// 注册手机号
			intent.putExtra(BocnetDataCenter.ModleName, getIntent()
					.getBooleanExtra(BocnetDataCenter.ModleName, false));
			startActivityForResult(intent, 0);
		}
	}

	/**
	 * 检查用户唯一性 请求
	 */
	private void requestRegisterUnique() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSN_IS_MOBILE_REGISTED);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		Map<String, String> map = new HashMap<String, String>();
		map.put(Login.REGISTER_MOBILE, loginName);
		// map.put(Login.CHANNEL, "1");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestRegisterUniqueCallBack");
	}

	/**
	 * 检查用户唯一性 请求回调
	 * 
	 * @param resultObj
	 */
	public void requestRegisterUniqueCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			Map<String, Object> result = (Map<String, Object>) biiResponseBody
					.getResult();// true 存在 false 不存在
			String isRegisted = (String) result.get(Login.IS_REGISTED);
			if (isRegisted.equals("N")) {
				requestTokenIdLoginPre();
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().createDialog(
						null,
						RegisterSetNamePwdActivity.this.getResources()
								.getString(R.string.phone_number_not_unique));
			}
		} else {
			BiiError error = biiResponseBody.getError();
			BaseDroidApp.getInstanse().createDialog(null, error.getMessage());
		}
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
		if (REGIST_SUCCESS == resultCode) {
			setResult(REGIST_SUCCESS);
			finish();
		}
	}
}
