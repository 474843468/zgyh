
package com.chinamworld.bocmbci.biz.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cfca.mobile.sip.SipBox;

import com.alibaba.fastjson.MyJSON;
import com.cfca.mobile.device.SecResult;
import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.AudioKey;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Ecard;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Login.LoginStatusType;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.creditcard.CreditCardAcountActivity;
import com.chinamworld.bocmbci.biz.bocnet.debitcard.DebitCardAcountActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderMainActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdCreditCardAcountActivity;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDetialActivityAfterLogin;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainActivity;
import com.chinamworld.bocmbci.biz.infoserve.bean.NeedReadMessage;
import com.chinamworld.bocmbci.biz.login.binding.DeviceBinding;
import com.chinamworld.bocmbci.biz.login.findpwd.FindPwdActivity;
import com.chinamworld.bocmbci.biz.login.findpwd.ForceModifyPwdActivity;
import com.chinamworld.bocmbci.biz.login.findpwd.VolumeProtocolActivity;
import com.chinamworld.bocmbci.biz.login.reg.RegisteVerifyActivity;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.BaseRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.server.BocMBCManager;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.IActionCallBack;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;
//import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity;
import com.chinamworld.bocmbci.boc.ModelBoc;

/**
 * 登录页面
 * 
 * @author WJP
 * 
 */
public class LoginActivity extends LoginTopBaseActivity {

	private static final String TAG = "LoginActivity";
	public static final int FORCE_MODIFY_REQUEST = 1001;
	private static final int MERGIN_PROTOCOL = 1002;
	private static final int REQUEST_FINDPWD_CODE = 1003;
	private static final int REQUEST_REGISTE_CODE = 1004;

	/** 加密之后加密随机数 */
	private String password_RC = "";
	/** 输入框密码 */
	private String password = "";
	/** 登录用户名 */
	private String loginName = "";
	/** 本地保存的的手机号 */
	private String preferencePhone = "";
	/** 加密后的手机号 */
	private String encryptPhone = "";
	/** 登录按钮 */
	private Button btnLogin;
	/** 验证码图片 */
	private Button imageCodeButton;
	/** 验证码 */
	private String imageCode;
	/** 用户名输入框 */
	private EditText etLoginname;
	/** 验证码输入框 */
	private EditText etImageCode;
	/** 加密控件 */
	private SipBox sipBox = null;
	/** 验证码布局 [如果验证码布局显示则校验验证码，如果不显示则不校验验证码] */
	private ViewGroup mImageCodeLayout;
	/** 找回密码 */
	private RelativeLayout findPwd;
	/** 自助注册 */
	private RelativeLayout selfReg;
	/** 远程开户 */
	private RelativeLayout remoteOpen;
	/** 帮助信息 */
	// private LinearLayout helpInfo;
	/** 加密控件里的随机数 */
	private String randomNumber;
	/** 记住手机号 */
	// private Switch numSwitch;
	private CheckBox savePhone;
	private TextView tvSavePhone;
	/** 手机号输入有误 错误信息 */
	private String codeNull;
	/** 密码输入为空 */
	private String passwordNull;
	/** 账号注册状态 */
	private String loginStatus;
	private String regtype;
	private String userNameStr;

	private boolean isShowedTimeOut = false;
	/** 登陆标记 */
	private boolean mAutoLoginFlag = false;
	private boolean conversiIdRequested = false;
	/** 是否正在请求验证码 */
	private boolean isRequestingCode = false;
	private boolean isCodeSucceed = false;
	/** 返回到九宫格页面 */
	private boolean back_to_main = false;

	/** 跳转到消息服务页面 */
	private boolean go_to_info = false;
	/** mGofast: */
	private boolean mGofast = false;

	private int optFlag;
	/** 绑定设备信息操作 */
	private final static int SET_MOBILE_INFO_OPT = 101;

	private String conversationIdForSetMobileInfo;
	
	/****** 开户送电子银行 ******/
	private boolean isBocnet = false;
	/** 银行卡号 */
	private EditText editAccNum;
	// /** 年 */
	// private EditText editYear;
	// /** 月 */
	// private EditText editMonth;
	// /** cvv2 */
	// private SipBox mSipBxoCVV2;
	/** 借记卡 查询密码 */
	private SipBox mSipBxoPas;
	/** 信用卡 查询密码 */
	private SipBox bocnet_cvv2;
	/** 验证码 */
	private EditText bocImageCode;
	private Button bocImageCodeBtn;
	/** 卡类型 */
	private String cardType;
	private String bocnetConversation = null;
	private String accountSeq = null;
	/** 手机银行登录/注册标识 */
	private String eBankingFlag;
	// 获得登陆的身份证 identityNumber
	public static String identityNumber;
	// 证件类型
	public static String identityType;
	// 登陆姓名
	public static String loginNames;
	// 用户的电话号码
	public static String mobile;
	// 用户的性别
	public static String gender;

	public static String qryCustType;
	String crcdPoint;// 信用卡积分

	//wuhan 民生解析用到
	public static String cifNumber;
	public static String moblies ;
	/**登录后返回用户市场细分   10查询版*/
	private String segment;
	
	/** 系统消息集合 */
	public List<NeedReadMessage> mNeedReadMessageList;
	/**必读消息请求码*/
	private int INFOSERVE_REQUESTCODE=1005;
	
	private Map<String, Object> resultMap1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityTaskManager.getInstance().clearLoginAct();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		mNeedReadMessageList=InfoServeDataCenter.getInstance().getmNeedReadMessageList();
		back_to_main = getIntent().getBooleanExtra(ConstantGloble.BACK_TO_MAIN,
				false);
		go_to_info = getIntent().getBooleanExtra(
				ConstantGloble.GO_TO_INFOSERVER, false);
		GetPhoneInfo.initActFirst(this);
		isShowedTimeOut = true;
		// 初始化弹出框
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();

		// 清除Cookie
		BaseDroidApp.getInstanse().clientLogOut();
		try {
			BaseHttpEngine.initProxy(this);
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		
//		// 登陆前关闭会话 
//
//		if(mtype!=null&&!mtype.equals("")){
//			requestCloseLoginPreOutlayConversationId(mtype);
//			mtype=null;
//			LogGloble.e("mtype==清空=", mtype );
//		}
		 
		// 加密控件
		sipBox = (SipBox) findViewById(R.id.login_pwd_sip);
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		sipBox.setPasswordMaxLength(ConstantGloble.MAX_LENGTH);
		sipBox.setBackgroundDrawable(null);
		sipBox.setPasswordRegularExpression(CheckRegExp.OLD_LOGIN_PWD);
		sipBox.setTextSize(Integer.valueOf(getResources().getString(
				R.string.sipboxtextsize)));
		sipBox.setSipDelegator(this);

		codeNull = this.getResources().getString(R.string.code_null);
		passwordNull = this.getResources().getString(
				R.string.login_password_no_null);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(loginClicklistener);

		etImageCode = (EditText) findViewById(R.id.ed_image_code);
		etImageCode.setFocusable(true);

		etLoginname = (EditText) findViewById(R.id.ed_login_name);
		mImageCodeLayout = (ViewGroup) findViewById(R.id.image_code_layout);
		// etLoginname.setText("25563731");
		findPwd = (RelativeLayout) findViewById(R.id.login_findpwd);
		findPwd.setOnClickListener(findPwdClicklistener);
		selfReg = (RelativeLayout) findViewById(R.id.login_selfreg);
		selfReg.setOnClickListener(selfRegClicklistener);
		remoteOpen = (RelativeLayout) findViewById(R.id.Remote_Open);
		remoteOpen.setOnClickListener(remoteOpenlistener);
//		remoteOpen.setVisibility(View.GONE);
		findViewById(R.id.login_order).setOnClickListener(branchOrderlistener);
		// helpInfo = (LinearLayout) findViewById(R.id.login_helpinfo);
		// helpInfo.setOnClickListener(helpInfoClicklistener);

		imageCodeButton = (Button) findViewById(R.id.ib_image_code);
		imageCodeButton.setOnClickListener(imageCodeClickListener);

		// rememberNum = (CheckBox) findViewById(R.id.remember_num);
		savePhone = (CheckBox) findViewById(R.id.cbSavePhone);
		tvSavePhone = (TextView) findViewById(R.id.tvSavePhone);
		tvSavePhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savePhone.performClick();
			}
		});
		// 反显手机号
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				ConstantGloble.CONFIG_FILE, MODE_PRIVATE);
		// 默认不记住手机号
		savePhone.setChecked(sharedPreferences.getBoolean(
				ConstantGloble.LOGIN_REMEBER, false));

		if (sharedPreferences.getString(ConstantGloble.LOGIN_NAME, null) != null) {
			preferencePhone = sharedPreferences.getString(Login.LOGIN_NAME,
					null);
			encryptPhone = StringUtil.getThreeFourThreeString(preferencePhone);
			etLoginname.setText(encryptPhone);
			etLoginname.setSelection(etLoginname.getText().toString().length());
		}
		etLoginname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (etLoginname.getText().toString().contains("*")) {
					etLoginname.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		etLoginname.setLongClickable(false);
		conversiIdRequested = false;
		isRequestingCode = false;
		// 请求conversationId
		//此接口在每次点击登陆按钮时调用
//		requestLoginPreConversationId();

		((Button) findViewById(R.id.ib_back))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (BaseRequestThread.isConnecting) {
							// BaseHttpEngine.sClient.getConnectionManager().shutdown();
							BaseRequestThread.currentThread.interrupt();
							BaseRequestThread.isConnecting = false;
						}
						BaseDroidApp.getInstanse().setMainItemAutoClick(false);
						if (back_to_main) {
							// startActivity(new Intent(LoginActivity.this,
							// MainActivity.class));
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
						}
						finish();

					}
				});
		initBocnetView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (1 == SharedPreUtils.getInstance()
		// .getInt(ConstantGloble.TIME_OUT, 0)
		// || 2 == SharedPreUtils.getInstance().getInt(
		// ConstantGloble.TIME_OUT, 0))

		if (getIntent().getBooleanExtra(ConstantGloble.TIME_OUT_CONFIRM, false)) {// 代表超时强制退出
			if (isShowedTimeOut) {// 还没有显示过
				BaseDroidApp.getInstanse().showTimeOutDialog(null);
				isShowedTimeOut = false;
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 取消自动跳转
		go_to_info = false;
		// back_to_main = true;
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// ScrollView sc = (ScrollView) findViewById(R.id.idSL);

		sipBox.setRandomKey_S(randomNumber);

	}

	/**
	 * 请求conversationId 回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	@Override
	public void requestLoginPreConversationIdCallBack(Object resultObj) {
		super.requestLoginPreConversationIdCallBack(resultObj);
		conversiIdRequested = true;
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOGIN_PRECONVERSATIONID));
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
//		if (mImageCodeLayout.getVisibility() != View.VISIBLE && mAutoLoginFlag) {
//			// 登陆：验证码不显示并且随机数无效,点击登陆
//			btnLogin.performClick();
//		} else {
//			// 请求验证码图片
//			requestForImagecode();
//		}
		if(httpCallBack != null)
			httpCallBack.requestHttpSuccess(randomNumber);
	}

	private IHttpCallBack httpCallBack;
	
	/**
	 * @Title: requestRandomNumberForImage
	 * @Description: 在点击获取验证码图片时 如果未获取到随机数时调用
	 * @param
	 * @return void
	 */
	public void requestRandomNumberForImage() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOGIN_PRECONVERSATIONID));
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomForImageCallBack");
	}

	/**
	 * @Title: queryRandomForImageCallBack
	 * @Description: 获取验证码图片时 请求随机数的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void queryRandomForImageCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		LoginDataCenter.getInstance().setRandomNumber(randomNumber);
		// 加密控件设置随机数
		sipBox.setRandomKey_S(randomNumber);
		// 请求验证码图片
		requestForImagecode();
		LogGloble.i(TAG, resultObj.toString());
	}

	/**
	 * 请求验证码图片
	 */
	public void requestForImagecode() {
		isRequestingCode = false;
		imageCodeButton.setText(R.string.imagecodeloading);
		imageCodeButton
				.setBackgroundResource(R.drawable.selector_for_image_code);
		if (mImageCodeLayout.getVisibility() == View.VISIBLE) {
			// 如果验证码布局显示则请求验证码
			HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE,
					ConstantGloble.GO_METHOD_GET, null, this,
					"imagecodeCallBackMethod");
		}
	}

	/**
	 * 验证码请求回调
	 * 
	 * @param resultObj
	 *            请求成功 返回参数
	 */
	public void imagecodeCallBackMethod(Object resultObj) {
		// 取消通讯框
		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		imageCodeButton.setBackgroundDrawable(imageCodeDrawable);
		imageCodeButton.setText("");
		isRequestingCode = false;
		isCodeSucceed = true;
	}

	/**
	 * 查询客户上次登录状态Callback
	 * 
	 * @param resultObj
	 */
	public void requestLastLoginStatusCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		// lastLoginStatus
		String lastLoginStatus = (String) resultMap
				.get(Login.LAST_LOGIN_STATUS);
		boolean lastLoginSucceed = LoginStatusType
				.isLastLoginSucceed(lastLoginStatus);
		if (lastLoginSucceed) {
			// 上次登录成功
			btnLogin.performClick();
		} else {
			// 上次登录失败
			BaseHttpEngine.dissMissProgressDialog();
			mImageCodeLayout.setVisibility(View.VISIBLE);
			requestForImagecode();
		}
	}

	/**
	 * 请求登录
	 * 
	 * @param isNeedImageCode
	 *            true需要上传验证码，false不需要上传验证码
	 */
	public void requestForLoginPre(boolean isNeedImageCode) {
		try 
		{
			password_RC = sipBox.getValue().getEncryptRandomNum();
			password = sipBox.getValue().getEncryptPassword();
		}
		catch (Exception e) {
			return;
		}
		
		
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGINPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOGIN_PRECONVERSATIONID));
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put(Login.LOGIN_NAME, "ibmtest003");测试用
		map.put(Login.LOGIN_NAME, loginName);
		map.put(Login.PASSWORD, password);
		map.put(Login.PASSWORD_RC, password_RC);
		if (isNeedImageCode) {
			map.put(Login.VALIDATIONCHAR, imageCode);
		}
		map.put(Login.SEGMENT, "1");
		map.put(Login.WP7_LOGIN_TYPE, "2");
		SipBoxUtils.setSipBoxParams(map);// 添加密码控件需要送上字段
		// GetPhoneInfo.addPhoneInfo(map);// 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForLoginPreCallback");
	}

	/**
	 * 预交易登录返回
	 */
	public void requestForLoginPreCallback(Object resultObj) {
		// requestForloginTm();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.i(TAG, TAG + biiResponseBody.getResult().toString());
		// TODO 登录返回数据
		// 当userStatus不为VERIFIED并且challengeQuestion不为空且长度大于0时，需弹出安全保护问题设置页面。
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
			segment = resultMap.get("segmentId")+"";
		BaseDroidApp.getInstanse().setSegmentInfo(segment);
		String needValidationChars = (String) resultMap
				.get(Login.NEED_VALIDATIONCHARS);
		if (!StringUtil.isNull(needValidationChars)
				&& mImageCodeLayout.getVisibility() != View.VISIBLE) {
			// 存在上次登陆状态字段,1代表上次登陆失败
			if ("1".equals(needValidationChars)) {
				BaseHttpEngine.dissMissProgressDialog();
				mImageCodeLayout.setVisibility(View.VISIBLE);
				// sipBox.clearText();
				etImageCode.setText("");
				requestForImagecode();
				return;
			}
		}

		loginStatus = (String) resultMap.get(Login.LOGIN_STATUS);
		String combinStatus = (String) resultMap.get(Login.COMBINE_STATUS);
		regtype = (String) resultMap.get(Login.REG_TYPE);
		userNameStr = (String) resultMap.get(Login.NAME);
		if (!StringUtil.isNullOrEmpty(regtype)) {
			Intent intent = new Intent(this, VolumeProtocolActivity.class);
			intent.putExtra(Login.NAME, userNameStr);
			intent.putExtra(Login.LOGIN_STATUS, loginStatus);
			intent.putExtra(Login.OLD_PASSWORD, password);
			intent.putExtra(Login.OLD_PASSWORD_RC, password_RC);
			intent.putExtra(Login.REG_TYPE, regtype);

			if (!StringUtil.isNullOrEmpty(combinStatus)) {
				if (combinStatus.equals(ConstantGloble.COMBINSTATUS_11)) {// 主客户首次登陆
					intent.putExtra(Login.COMBINE_STATUS,
							ConstantGloble.COMBINSTATUS_11);
					BaseHttpEngine.dissMissProgressDialog();
					startActivityForResult(intent, FORCE_MODIFY_REQUEST);
					return;
				} else if (combinStatus.equals(ConstantGloble.COMBINSTATUS_10)) {// 从客户首次登陆
					requsetRegisterLoginName();
					return;
				}
			}
		}

		judgeModifyPassword();
	}

	/**
	 * @Title: judgeModifyPassword
	 * @Description: 判断是否需要强制修改密码 如果需要 执行 不需要的话 直接执行登录请求
	 * @param
	 * @return void
	 */
	private void judgeModifyPassword() {
		if (StringUtil.isNullOrEmpty(loginStatus)) {
			// 取消通讯框
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(null,
					"login status is null");
			return;
		}
		// 强制修改密码
		if (loginStatus.equals("1") || loginStatus.equals("2")) {
			BaseHttpEngine.dissMissProgressDialog();
			// TODO 弹出密码修改框
			// BaseDroidApp.getInstanse().showModifyPwdDialog(onclicklistener);
			Intent intent = new Intent(this, ForceModifyPwdActivity.class);
			intent.putExtra(Login.OLD_PASSWORD, password);
			intent.putExtra(Login.OLD_PASSWORD_RC, password_RC);
			startActivityForResult(intent, FORCE_MODIFY_REQUEST);
			return;
		}
		// requestForLoginInfo();
		requestPsnWMSQueryWealthRank();
	}

	private void requestPsnWMSQueryWealthRank() {
		custLevel = null;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PsnWMSQueryWealthRank);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnWMSQueryWealthRankCallback");
	}

	private String custLevel = "";

	public void requestPsnWMSQueryWealthRankCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (ConstantGloble.STATUS_SUCCESS.equals(biiResponseBody.getStatus())) {
			custLevel = (String) biiResponseBody.getResult();
			requestForLoginInfo();
		}
	}

	/**
	 * 请求登录提交交易
	 */
	// private void requestForloginTm() {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Login.LOGINFORTM_API);
	// biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
	// .getBizDataMap().get(ConstantGloble.LOGIN_PRECONVERSATIONID));
	// Map<String, Object> map = new HashMap<String, Object>();
	// GetPhoneInfo.addPhoneInfo(map);// 设备指纹
	// biiRequestBody.setParams(map);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestForloginTmCallBack");
	// }

	/**
	 * 请求登录提交交易回调
	 * 
	 * @param resultObj
	 */
	// public void requestForloginTmCallBack(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// LogGloble.i(TAG, biiResponseBody.getResult().toString());
	// // TODO 登录返回数据
	// // 当userStatus不为VERIFIED并且challengeQuestion不为空且长度大于0时，需弹出安全保护问题设置页面。
	// Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
	// .getResult();
	// String combinStatus = (String) resultMap.get(Login.COMBINE_STATUS);
	// if (!StringUtil.isNullOrEmpty(combinStatus)) {
	// if (combinStatus.equals(ConstantGloble.COMBINSTATUS_10)) {// 主客户首次登陆
	// // 取消通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// // TODO 确认提示信息 弹窗提示用户用18位 登录
	// BaseDroidApp.getInstanse().showErrorDialog(null,
	// "please use 18 numbers login");
	// return;
	// } else if (combinStatus.equals(ConstantGloble.COMBINSTATUS_11)) {//
	// 从客户首次登陆
	// // 取消通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// // 弹窗提示
	// BaseDroidApp.getInstanse().showErrorDialog("please agree",
	// R.string.not_agree, R.string.agree, onclicklistener);
	// return;
	// }
	// }
	//
	// String loginStatus = (String) resultMap.get(Login.LOGIN_STATUS);
	// if(StringUtil.isNullOrEmpty(loginStatus)){
	// // 取消通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// BaseDroidApp.getInstanse().showErrorDialog(null, "login status is null");
	// return;
	// }
	// if (loginStatus.equals("1")) {// TODO 柜台首次登陆
	// // 取消通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// // TODO 弹出密码修改框
	// BaseDroidApp.getInstanse().showModifyPwdDialog(onclicklistener);
	// return;
	// } else if (loginStatus.equals("2")) {// TODO 柜台重置密码
	// // 取消通讯框
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// }
	//
	// requestForLoginInfo();
	// }

	/**
	 * 请求登录信息
	 */
	private void requestForLoginInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGIN_INFO_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForLoginInfoCallBack");
	}

	/**
	 * P402 为消息推送绑定设备 
	 */
	private void bindingDeviceForPushService(){
		optFlag = SET_MOBILE_INFO_OPT;
		PushManager.getInstance(this).setPushDevice(null);
		requestCommConversationId();
		InfoServeDataCenter.getInstance().setIsRefreshMessages(true);
		
	}
	
	/**
	 * 请求登录信息的回调
	 */
	public void requestForLoginInfoCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.i(TAG, biiResponseBody.getResult().toString());
		// TODO 登录返回数据
		// 当userStatus不为VERIFIED并且challengeQuestion不为空且长度大于0时，需弹出安全保护问题设置页面。
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		resultMap1=resultMap;
		// 保存登录信息，传递给移动支付使用
		String loginInfo = MyJSON.toJSONString(biiResponseBody);
		BaseDroidApp.getInstanse().setLoginInfo(loginInfo);
		// 将登陆之后的信息保存到全局map中
		resultMap.put(Login.custLevel, custLevel);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BIZ_LOGIN_DATA, resultMap);
		// 获得登陆的身份证 identityNumber
		identityNumber = (String) resultMap.get(Login.IDENTITYNUMBER);
		// 身份证类型IDENTITYTYPE
		identityType = (String) resultMap.get(Login.IDENTITYTYPE);
		// 获得登陆姓名
		loginNames = (String) resultMap.get(Login.CUSTOMER_NAME);
		// 获得用户的电话号码
		mobile = (String) resultMap.get(Login.REGISTER_MOBILE);
		moblies = mobile;
		// 获得用户的性别
		gender = (String) resultMap.get(Login.GENDER);

		
		qryCustType= (String) resultMap.get(Login.REGISTER_QRYCUSTTYPE);
        //wuhan cifNumber 民生解析中会用到
        cifNumber = (String) resultMap.get("cifNumber");
		// 更改登录状态
	//	setUserLoginStatus(true);
		// int i = BaseDroidApp.getInstanse().getSharedPrefrences()
		// .getInt(ConstantGloble.LOGIN_TIMES, 0) + 1;
		SharedPreferences.Editor editor = BaseDroidApp.getInstanse()
				.getSharedPrefrences().edit();
		// editor.putInt(ConstantGloble.LOGIN_TIMES, i);
		// 记录日志保存用户名
		editor.putString("loginNameForLog", loginName);

		if (savePhone.isChecked()) {
			editor.putString(ConstantGloble.LOGIN_NAME, loginName);
			editor.putBoolean(ConstantGloble.LOGIN_REMEBER, true);
		} else {
			editor.putString(ConstantGloble.LOGIN_NAME, null);
			editor.putBoolean(ConstantGloble.LOGIN_REMEBER, false);
		}
		editor.commit();
		if(BaseDroidApp.getInstanse().getSflag()){
			// 上送设备信息
			new BocMBCManager().sendFunctionUsedAction(this,""+resultMap.get(Login.CIFNUMBER));
		}
		// BaseDroidApp.getInstanse().showModifyPwdDialog(onclicklistener);
		// 402登录成功绑定设备信息
//		bindingDeviceForPushService(); // 此处不能直接调用 ，手机硬件绑定后才可以调用
//		// 请求token
//		optFlag = SET_MOBILE_INFO_OPT;
//		PushManager.getInstance(this).setPushDevice(null);
//		requestCommConversationId();
//		InfoServeDataCenter.getInstance().setIsRefreshMessages(true);

		// 501登录成功检测是否开通音频key(中银E盾)
		String caMerchId = (String) resultMap.get(ConstantGloble.CA_MERCH_ID);
		String certUid = (String) resultMap.get(ConstantGloble.CERT_UID);
		String certExpire = (String) resultMap.get(ConstantGloble.CERT_EXPIRE);
		if (StringUtil.isNullOrEmpty(caMerchId)
				&& StringUtil.isNullOrEmpty(certUid)
				&& StringUtil.isNullOrEmpty(certExpire)) {
			AudioKeyManager.getInstance().setHasAudioKey(false);
		} else {
			BaseDroidApp.getInstanse().initAudioReceiver();
			AudioKeyManager.getInstance().setHasAudioKey(true);
			// AudioKeyManager.getInstance().initKeyDriver(this, null, null);

			// 发送用户登录并且开通音频key的消息
			Message msg = uiHandler.obtainMessage();
			msg.what = AudioKey.LOGIN_AND_HAS_KEY_WHAT;
			uiHandler.sendMessage(msg);
		}

		
		//  通知登录观察者登录成功
//		BaseDroidApp.getInstanse().getLoginObserver().notifyWatches(true);
		
		
		
		if("10".equals(segment)){
			BaseDroidApp.setLogin(true);
			if("2".equals(qryCustType)){
				requestEcardoutAccountList();	
			}else{
				requestPsnSvrGlobalMsgList();
			}
			
			
			
//			bindingDeviceForPushService();//登录完成，需要绑定设备
//			// 更改登录状态
//			setUserLoginStatus(true);
//		Intent intent = new Intent();
//		BaseDroidApp.getInstanse().setActFirst(
//				BaseDroidApp.getInstanse().getMainAct());
//		if (go_to_info) {
//			intent.setClass(this, InfoServeMainActivity.class);
//			startActivity(intent);
//			finish();
//			return;
//		}
//		if (back_to_main
//				|| getIntent().getBooleanExtra(ConstantGloble.TIME_OUT_CONFIRM,
//						false)) {
//			if (getIntent().getBooleanExtra(ConstantGloble.FAST_LOGIN, false)
//					|| mGofast) {
//				intent.putExtra(ConstantGloble.FAST_LOGIN, true);
//				intent.putExtra(ConstantGloble.FAST_INDEX, getIntent()
//						.getIntExtra(ConstantGloble.FAST_INDEX, 0));
//			}
//			intent.setClass(this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			finish();
//			return;
//		}
//			setResult(RESULT_OK, intent);
//			LoginActivity.this.finish();
			return;
		}
        //登录后强绑定设备start/////////////////////////////////////////////////////
        // 获取用户的网银用户号
        BaseDroidApp.getInstanse().setOperatorId(resultMap.get("operatorId")+"");
        // 获取用户是否绑定设备
     	String hasBindingDevice = resultMap.get("hasBindingDevice")+"";
     	BaseDroidApp.getInstanse().setHasBindingDevice(hasBindingDevice);
     	// 服务器返回未设定
     	String localBindInfo = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, "");// IMEI
     	String cfcaString = DeviceInfoTools.getLocalCAOperatorId(this, BaseDroidApp.getInstanse().getOperatorId(),1);
     	
     	String localBindInfo_mac = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
    	String cfcamacString = DeviceInfoTools.getLocalCAOperatorId(this, BaseDroidApp.getInstanse().getOperatorId(),2);// mac
     	
 		if ("1".equals(hasBindingDevice)) {
 			if (!"".equals(localBindInfo)
 				&& cfcaString.equals(localBindInfo)) {
 				// 已经绑定本机 请求服务器 currentDeviceFlag
 				requestCommConversationIdLogined();
 				return;
 			} else {
 				if(cfcamacString.equals(localBindInfo_mac)){
 					requestCommConversationIdLogined();
 	 				return;
 				}else{
 					//当前账号已被他人绑定
 	 				BaseDroidApp.getInstanse().showMessageDialog("您已绑定其它手机设备，暂不能使用手机银行服务。您可以在原手机设备、网银、柜台进行解绑，才可在此设备正常使用手机银行服务。",
 	 						new OnClickListener() {
 	 							@Override
 	 							public void onClick(View v) {
 	 								BaseDroidApp.getInstanse()
 	 										.dismissMessageDialog();
 	 								requestForLogout();
 	 								BaseDroidApp.getInstanse().clientLogOut();
 	 								finish();
 	 							}
 	 						});
 	 				
 				}
 			}
 				
 		}else if ("0".equals(hasBindingDevice)) {
 			// P503 投产后修改  若账号未绑定过，则直接进入绑定流程
 			DeviceBinding.goToDeviceBingdingFlow(this, new IActionCallBack(){

				@Override
				public void callBack(Object params) {
					BaseDroidApp.setLogin(true);
					requestPsnSvrGlobalMsgList();
					// 更改登录状态
//					bindingDeviceForPushService();
				}
					
				});
		// wuhan
		// if(isGold){
		// intent.setClass(this, PrmsPricesActivity.class);
		// setResult(111,intent);
		// finish();
 		}
 		// 取消通讯框
 		BaseHttpEngine.dissMissProgressDialog();
	        //登录后强绑定设备end/////////////////////////////////////////////////////
	}
	
	
	/**
	 * 请求最新消息(必读消息)的列表请求
	 */
	public void requestPsnSvrGlobalMsgList() {
	//  通知登录观察者登录成功  
		// 登录成功 都要调必须消息接口 所以在这设置登录状态
//		BaseDroidApp.getInstanse().getLoginObserver().notifyWatches(true);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.PSNSVRGLOBALMSGLIST);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestMessagesListCallback");
	}
	/**
	 * 请求回调方法
	 * @param resultObj
	 */
	public void requestMessagesListCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		try {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			String requestMethod = biiResponseBody.getMethod();
				if (Login.PSNSVRGLOBALMSGLIST.equals(requestMethod)) {
				// system
				doNeedReadMessageCallback(biiResponseBody,this);
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}
	/**
	 * 根据请求回调的数据组装消息详情数据列表，组装bundle数据
	 * @param biiResponseBody
	 * @param needReadMessageList
	 * @param activity
	 */
	public void doNeedReadMessageCallback(BiiResponseBody biiResponseBody,Activity activity) {
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList != null&&resultList.size()!=0) {
			mNeedReadMessageList.clear();
			for (Map<String, String> map : resultList) {
				String popupFlag = map.get(Login.POPUPFLAG);
				if (popupFlag==null||popupFlag.equals("0")) {
					continue;
				}
				String subject = map.get(Login.SUBJECT);
				String content = map.get(Login.CONTENT);
				String globalMsgId = map.get(Login.GLOBALMSGID);
				NeedReadMessage message = new NeedReadMessage();
				message.setGlobalMsgId(globalMsgId);
				message.setContent(content);
				message.setSubject(subject);
				mNeedReadMessageList.add(message);
			}
		}
		if (mNeedReadMessageList.size()>0) {
			/*Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("needReadMessage", (ArrayList<? extends Parcelable>) mNeedReadMessageList);*/
			toInfoServeDetialActivityAfterLogin(/*bundle,*/activity);
			
		}else {
			toNextStep();
		}
//			refreshTable();
//			refreshListView();
	}
	/**
	 * 登陆后，有必读消息时跳转到消息详情页
	 * @param bundle
	 * @param activity
	 */
	public void toInfoServeDetialActivityAfterLogin(/*Bundle bundle,*/Activity activity){
		LogGloble.i("必读消息", MainActivity.class.getSimpleName()+"类中,从"+activity.getLocalClassName()+"-跳转到必读消息");
		Intent intent = new Intent(LoginActivity.this,InfoServeDetialActivityAfterLogin.class);
//		intent.putExtras(bundle);
		activity.startActivityForResult(intent, INFOSERVE_REQUESTCODE);
	}
	
	/**进入后续流程*/
	private void toNextStep(){
		BaseDroidApp.getInstanse().getLoginObserver().notifyWatches(true);//结汇购汇使用
		bindingDeviceForPushService();  // 硬件绑定服务器检查成功
		// 更改登录状态
		setUserLoginStatus(true);
		Intent intent = new Intent();
		BaseDroidApp.getInstanse().setActFirst(
				BaseDroidApp.getInstanse().getMainAct());
		if (go_to_info) {
			intent.setClass(this, InfoServeMainActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		if (back_to_main
				|| getIntent().getBooleanExtra(ConstantGloble.TIME_OUT_CONFIRM,
						false)) {
			if (getIntent().getBooleanExtra(ConstantGloble.FAST_LOGIN, false)
					|| mGofast) {
				intent.putExtra(ConstantGloble.FAST_LOGIN, true);
				intent.putExtra(ConstantGloble.FAST_INDEX, getIntent()
						.getIntExtra(ConstantGloble.FAST_INDEX, 0));
			}
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			return;
		}
		setResult(RESULT_OK, intent);
		LoginActivity.this.finish();
	}
	
	
	/**
	 * 请求conversationId
	 */
	public void requestCommConversationIdLogined() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestCommConversationIdLoginedCallBack");
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdLoginedCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		requestForRandomNumberLogined(commConversationId);
	}
	
	/**
	 * 请求密码控件随机数
	 */
	
	String commConversation;
	public void requestForRandomNumberLogined(String commConversationId ) {
		commConversation=commConversationId;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForRandomNumberLoginedCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void requestForRandomNumberLoginedCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		sendCurrentDeviceFlag();
	}
	
	/***
	 * 检查是否本机绑定接口请求
	 */
	private void sendCurrentDeviceFlag(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.CURRENTDEVICECHECK_API);
		biiRequestBody.setConversationId(commConversation);
		Map<String, Object> params = new HashMap<String, Object>();
		// TODO 调用CFCA接口进行加密
//		DeviceSecCryptor instance = DeviceSecCryptor.createInstatnce(LoginActivity.this);
//		String deviceInfo = "";
//		String deviceInfo_RC = "";
//		try {
//			SecResult secResult = instance.getEncryptedInfo(SystemConfig.CIPHERTYPE, false, ConstantGloble.OUT_PUT_VALUE_TYPE, 
//					randomNumber, BaseDroidApp.getInstanse().getOperatorId());
//			deviceInfo_RC = secResult.getEncryptedRC();
//			deviceInfo = secResult.getEncryptedInfo();
//		} catch (CodeException e) {
//			e.printStackTrace();
//		}
		SecResult secResult=DeviceInfoTools.getSecDeviceInfo(LoginActivity.this, randomNumber, BaseDroidApp.getInstanse().getOperatorId());
		if(secResult!=null){
			params.put("deviceInfo", secResult.getEncryptedInfo());
			params.put("deviceInfo_RC", secResult.getEncryptedRC());	
		}
		
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"sendCurrentDeviceFlagCallback");
	}
	
	/**
	 * 检查是否本机绑定接口请求回调
	 * 
	 * @param resultObj
	 */
	public void sendCurrentDeviceFlagCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		Map<String , Object> result = (Map<String, Object>) biiResponseBodys.get(0).getResult();
		String currentDeviceFlag = result.get("currentDeviceFlag")+"";
		/**"1"为本机 "0"为非本机*/
		if("1".equals(currentDeviceFlag)){
			BaseDroidApp.setLogin(true);
			requestPsnSvrGlobalMsgList();
			
//			bindingDeviceForPushService();  // 硬件绑定服务器检查成功
//			// 更改登录状态
//			setUserLoginStatus(true);
//			Intent intent = new Intent();
//			BaseDroidApp.getInstanse().setActFirst(
//					BaseDroidApp.getInstanse().getMainAct());
//			if (go_to_info) {
//				intent.setClass(this, InfoServeMainActivity.class);
//				startActivity(intent);
//				finish();
//				return;
//			}
//			if (back_to_main
//					|| getIntent().getBooleanExtra(ConstantGloble.TIME_OUT_CONFIRM,
//							false)) {
//				if (getIntent().getBooleanExtra(ConstantGloble.FAST_LOGIN, false)
//						|| mGofast) {
//					intent.putExtra(ConstantGloble.FAST_LOGIN, true);
//					intent.putExtra(ConstantGloble.FAST_INDEX, getIntent()
//							.getIntExtra(ConstantGloble.FAST_INDEX, 0));
//				}
//				intent.setClass(this, MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);
//				finish();
//				return;
//			}
//			setResult(RESULT_OK, intent);
//			LoginActivity.this.finish();
		}else{
			/**绑定非当前手机*/
			BaseDroidApp.getInstanse().showMessageDialog("您已绑定其它手机设备，暂不能使用手机银行服务。您可以在原手机设备、网银、柜台进行解绑，才可在此设备正常使用手机银行服务。",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
								requestForLogout();
								BaseDroidApp.getInstanse().clientLogOut();
								finish();
							}
						});
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (optFlag == SET_MOBILE_INFO_OPT) {
			conversationIdForSetMobileInfo = (String) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID);
			LogGloble.i("conversationIdForSetMobileInfo",
					conversationIdForSetMobileInfo);
			requestPSNGetTokenId(conversationIdForSetMobileInfo);
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		if (optFlag == SET_MOBILE_INFO_OPT) {
			String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.TOKEN_ID);
			// 绑定设备信息
			requestPsnSetMobileInfo(token);
		}
	}

	/**
	 * @Title: requestPsnSetMobileInfo
	 * @Description: 设备绑定信息添加
	 * @param token
	 * @return void
	 */
	public void requestPsnSetMobileInfo(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_SET_MOBILE_INFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Push.DEVICE_INFO, PushDevice.load().getDeviceId());
		paramsmap.put(Push.DEVICE_STYLE, "03");
		paramsmap.put(Push.DEVICE_SUB_STYLE, "01");
		paramsmap.put(Push.PUSHADDRESS, null);
		paramsmap.put(Push.BINDFLAG, "A");
		paramsmap.put(Comm.TOKEN_REQ, token);
		paramsmap.put("conversationId", conversationIdForSetMobileInfo);
		biiRequestBody.setParams(paramsmap);
		LogGloble.i("SetMobileInfo Request", paramsmap.toString());
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnSetMobileInfoCallBack");
	}

	/**
	 * @Title: requestPsnSetMobileInfoCallBack
	 * @Description: 设备绑定信息添加返回
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnSetMobileInfoCallBack(Object resultObj) {
		LogGloble.i("LoginActivity", "SetMobileInfo Success");
		PushDevice pd = PushDevice.load();
		PushManager.getInstance(this).setPushDevice(pd);
		PushManager.getInstance(this).restartPushService();
	}

	/**
	 * 登录 点击登陆： 1 根据预登陆返回字段判断是否显示验证码
	 */
	OnClickListener loginClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isBocnet) {
				bocnetLogin();
				return;
			}
			mAutoLoginFlag = false;
			// 是否需要上传和校验验证码
			boolean isNeedImageCode = mImageCodeLayout.getVisibility() == View.VISIBLE;
			try {
				imageCode = etImageCode.getText().toString().trim();
				loginName = etLoginname.getText().toString().trim();
				if (!TextUtils.isEmpty(loginName)
						&& loginName.equals(encryptPhone)) {
					loginName = preferencePhone;
				}
				if (StringUtil.isNullOrEmpty(loginName)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入手机号");
					return;
				}

				if (isNeedImageCode) {
					if (!conversiIdRequested) {// conversionId为空
						// requestLoginPreConversationId();
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								"验证码获取失败");
						return;
					} else if (TextUtils.isEmpty(randomNumber)) {// 随机数为空
						// requestRandomNumberForImage();
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								"验证码获取失败");
						return;
					} else if (!isCodeSucceed) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								"验证码获取失败");
						return;
					}
				}

				if (StringUtil
						.isNullOrEmpty(sipBox.getText().toString().trim())) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入登录密码");
					return;
				}

				RegexpBean regTel = new RegexpBean(
						LoginActivity.this.getString(R.string.tel_num_str),
						loginName, "longMobile");
				// RegexpBean regTel = new RegexpBean(
				// LoginActivity.this.getString(R.string.tel_num_str),
				// loginName, "shoujiH_01_15");
				RegexpBean regPw = new RegexpBean(
						getString(R.string.password_no_label), sipBox.getText()
								.toString(), "oldForceModifyPassword");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(regTel);
				lists.add(regPw);

				if (RegexpUtils.regexpDate(lists)) {// 校验通过
//					if (!isNeedImageCode) {
//						// 不需要验证码没有对随机数和conversionId校验
//						if (!conversiIdRequested) {// conversionId为空
//							// 请求conversationId
//							BaseHttpEngine.showProgressDialog();
//							mAutoLoginFlag = true;
//							requestLoginPreConversationId();
//							return;
//						} else if (TextUtils.isEmpty(randomNumber)) {// 随机数为空
//							BaseHttpEngine.showProgressDialog();
//							mAutoLoginFlag = true;
//							requestRandomNumberForImage();
//							return;
//						}
//					}
//
//					password_RC = sipBox.getValue().getEncryptRandomNum();
//					password = sipBox.getValue().getEncryptPassword();
				} else {
					return;
				}
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getResources().getString(R.string.zcode_invalible));
				return;
			}

			if (isNeedImageCode) {
				if (StringUtil.isNullOrEmpty(imageCode)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(codeNull);
					return;
				} else if (imageCode.length() != 4) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.zcode_invalible));
					return;
				}
			}
			
//			requestForLoginPre(isNeedImageCode);
			requestLoginWP7(isNeedImageCode);
		}
	};

	
	private void requestLoginWP7(final boolean isNeedImageCode) {
		
		BiiHttpEngine.showProgressDialog();
		httpCallBack = new IHttpCallBack() {  // 请求完随机数后，将会调用此方法
			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
			
				requestForLoginPre(isNeedImageCode);
			}
		};
		
		requestLoginPreConversationId();
	}
	
	/**
	 * 找回密码
	 */
	OnClickListener findPwdClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 取消自动跳转
			go_to_info = false;
			BaseDroidApp.getInstanse().setMainItemAutoClick(false);
			// 申请conversationId
			requsetFindpwdConversationId();
		}
	};

	/**
	 * 自助注册
	 */
	OnClickListener selfRegClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 取消自动跳转
			go_to_info = false;
			BaseDroidApp.getInstanse().setMainItemAutoClick(false);
			// 申请conversationId
			requsetRegisterConversationId();
		}
	};

	/**
	 * 网点预约排队
	 */
	OnClickListener branchOrderlistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			LoginActivity.this.startActivity(new Intent(LoginActivity.this,
					BranchOrderMainActivity.class));
		}
	};
	/**
	 * 远程开户
	 */

	OnClickListener remoteOpenlistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			 ModelBoc.BocStartActivity(LoginActivity.this, "com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity");
//			Intent intent = new Intent();
//			intent.setClass(LoginActivity.this, RemoteOpenAccActivity.class);
//			startActivity(intent);
		
		}
	};
	/**
	 * 验证码
	 */
	private OnClickListener imageCodeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isRequestingCode) {
				isRequestingCode = true;
				isCodeSucceed = false;
				if (!conversiIdRequested) {// conversionId为空
					// 请求conversationId
					requestLoginPreConversationId();
				} else if (TextUtils.isEmpty(randomNumber)) {// 随机数为空
					requestRandomNumberForImage();
				} else {
					requestForImagecode();
				}

			}
		}
	};

	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CONFIRM:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				LoginActivity.this.finish();
				ActivityTaskManager.getInstance().removeActivity(TAG);
				break;
			case CustomDialog.TAG_SURE:// 同意
				BaseDroidApp.getInstanse().dismissErrorDialog();

				break;
			case CustomDialog.TAG_CANCLE:// 不同意
				BaseDroidApp.getInstanse().dismissErrorDialog();

				break;
			case CustomDialog.TAG_MODIFY_PWD:// 修改密码
				// TODO 发送通讯修改密码
				HashMap<String, String> map = BaseDroidApp.getInstanse()
						.getMessageDialog().getModifyPwd();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 请求conversationId ---找回密码
	 */
	private void requsetFindpwdConversationId() {
		// 展示通信框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requsetFindpwdConversationIdCallBack");

	}

	/**
	 * 请求conversationId返回 --- 找回密码
	 * 
	 * @param resultObj
	 */
	public void requsetFindpwdConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String loginPreConversationId = (String) biiResponseBody.getResult();
		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);

		requestFindPwdRandomNumber();
	}

	/**
	 * 请求随机数---找回密码
	 */
	private void requestFindPwdRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"requestFindPwdRandomNumberCallBack");
	}

	/**
	 * 请求随机数返回---找回密码
	 * 
	 * @param resultObj
	 */
	public void requestFindPwdRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		LoginDataCenter.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, FindPwdActivity.class);
		startActivityForResult(intent, REQUEST_FINDPWD_CODE);
	}

	/**
	 * 请求conversationId ---找回密码
	 */
	private void requsetRegisterConversationId() {
		// 展示通信框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requsetRegisterConversationIdCallBack");

	}

	/**
	 * 请求conversationId返回 --- 找回密码
	 * 
	 * @param resultObj
	 */
	public void requsetRegisterConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String loginPreConversationId = (String) biiResponseBody.getResult();
		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);
		requestRegisterRandomNumber();
	}

	/**
	 * 请求随机数---自助注册
	 */
	private void requestRegisterRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(LoginDataCenter.getInstance()
				.getConversationId());
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"requestRegisterRandomNumberCallBack");
	}

	/**
	 * 请求随机数返回---自助注册
	 * 
	 * @param resultObj
	 */
	public void requestRegisterRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		LoginDataCenter.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisteVerifyActivity.class);
		startActivityForResult(intent, REQUEST_REGISTE_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogGloble.d("onActivityResult", "resultCode" + resultCode);
		if (resultCode == RESULT_OK && requestCode == FORCE_MODIFY_REQUEST) {
			// 强制修改密码成功
			requestForLoginInfo();
		} else if (resultCode == 100 && requestCode == FORCE_MODIFY_REQUEST) {
			// 同意协议
			judgeModifyPassword();
		} else if (resultCode == FORCE_MODIFY_REQUEST_BACK
				&& requestCode == FORCE_MODIFY_REQUEST) {
			sipBox.clearText();
			etImageCode.setText("");
			BaseDroidApp.getInstanse().clientLogOut();
			try {
				BaseHttpEngine.initProxy(this);
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}
			// 请求conversationId
			requestLoginPreConversationId();
		} else if (requestCode == REQUEST_FINDPWD_CODE
				|| requestCode == REQUEST_REGISTE_CODE) {
			// 重置密码或自助注册
			requestForImagecode();
			sipBox.clearText();
			etImageCode.setText("");
		}else if(requestCode == 101){
			Toast.makeText(LoginActivity.this,"绑定成功。。。。。" , Toast.LENGTH_LONG).show();
		}
		//必读消息关闭，进入后续流程
				else if (resultCode == RESULT_OK && requestCode == INFOSERVE_REQUESTCODE) {
					toNextStep();
				}
		// 开户送电子银行
		bocnetOnActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 超时后退出到登录页面或者消息服务未登录跳转到登录页面的
			if (getIntent().getBooleanExtra(ConstantGloble.TIME_OUT_CONFIRM,
					false)
					|| back_to_main) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
			}
			if (BaseRequestThread.isConnecting) {
				// BaseHttpEngine.sClient.getConnectionManager().shutdown();
				BaseRequestThread.currentThread.interrupt();
				BaseRequestThread.isConnecting = false;
			}
			finish();
		}
		return true;
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		// 如果获取conversitionId或者随机数 或者验证码 请求异常 只需要把验证码状态改成获取失败就行，不需要弹出对话框
		if (Login.AQUIRE_CONVERSATION_ID_API.equals(requestMethod)
				|| Login.AQUIRE_RANDOM_NUMBER_API.equals(requestMethod)
				|| ConstantGloble.BII_IMAGECODE_METHOD.equals(requestMethod)) {
			isRequestingCode = false;
			imageCodeButton.setText(R.string.imagecodeloadfail);
			imageCodeButton
					.setBackgroundResource(R.drawable.selector_for_image_code);
		}
		super.commonHttpErrorCallBack(requestMethod);

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		// HttpManager.stopConnect();
		// if (BaseRequestThread.isConnecting) {
		// // BaseHttpEngine.sClient.getConnectionManager().shutdown();
		// BaseRequestThread.currentThread.interrupt();
		// BaseRequestThread.isConnecting = false;
		// }
		// if (BaseHttpEngine.canGoBack) {
		// BaseDroidApp.getInstanse().getCurrentAct().finish();
		// BaseHttpEngine.canGoBack = false;
		// }
	}

	/**
	 * Bii请求前拦截-可处理统一的错误弹出框 有返回数据（response） 对于包含exception 的数据进行统一弹框
	 * 
	 * @param BiiResponse
	 *            resultObj
	 * @return 是否终止业务流程
	 */
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (Login.LOGINPRE_API.equals(body.getMethod())) {

					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						// 消除通信框
						BaseHttpEngine.dissMissProgressDialog();

						BiiError biiError = body.getError();
						// 判断是否存在error
						if (biiError != null) {
							// 登陆失败显示验证码
							if (mImageCodeLayout.getVisibility() != View.VISIBLE) {
								mImageCodeLayout.setVisibility(View.VISIBLE);
								etImageCode.setText("");
								requestForImagecode();
							}

							if (biiError.getCode() != null) {
								// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(
										biiError.getCode(),
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												requestForImagecode();
												etImageCode.setText("");
											}
										});

								return true;
							}
							// 弹出公共的错误框
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseDroidApp.getInstanse().createDialog("",
									biiError.getMessage(),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestForImagecode();
											etImageCode.setText("");
										}
									});
						} else {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							// 避免没有错误信息返回时给个默认的提示
							BaseDroidApp.getInstanse().createDialog(
									"",
									getResources().getString(
											R.string.request_error),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestForImagecode();
											etImageCode.setText("");
										}
									});
						}

						return true;
					}
				} else if (Push.PSN_SET_MOBILE_INFO.equals(body.getMethod())) {
					// 绑定设备接口通信错误时不显示
					return false;
				} else if (Login.PsnWMSQueryWealthRank.equals(body.getMethod())) {
					// 财富等级
					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						requestForLoginInfo();
					}
				} else if (Bocnet.METHODLOGININFO.equals(body.getMethod())
						|| Bocnet.METHODCRCDDETAIL.equals(body.getMethod())
						|| Bocnet.METHODDEBITDETAIL.equals(body.getMethod())) {
					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						BaseHttpEngine.dissMissProgressDialog();
						BiiError biiError = body.getError();
						if (biiError != null
								&& !LocalData.timeOutCode.contains(biiError
										.getCode())) {
							BaseDroidApp.getInstanse().createDialog(
									biiError.getCode(), biiError.getMessage(),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											BaseDroidApp.getInstanse()
													.deleteCookie();
											requestBocnetLogout();
										}
									});
						} else {
							super.doBiihttpRequestCallBackPre(response);
						}
					}
				} else if (Bocnet.METHODACCBOCNETLOGIN.equals(body.getMethod())) {
					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						BaseHttpEngine.dissMissProgressDialog();
						BiiError biiError = body.getError();
						if (biiError != null
								&& !LocalData.timeOutCode.contains(biiError
										.getCode())) {
							BaseDroidApp.getInstanse().createDialog(
									biiError.getCode(), biiError.getMessage(),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse()
													.dismissErrorDialog();
											requestForImagecodeBoc();
										}
									});
						} else {
							requestForImagecodeBoc();
							super.doBiihttpRequestCallBackPre(response);
						}
					}
				} else if (Crcd.CRCD_PSNACCBOCNETQRYCRCDPOINT.equals(body
						.getMethod())) {
					// 持卡登录 查询积分 不报错
					return false;
				} else {
					return super.doBiihttpRequestCallBackPre(response);
				}
			}
		}

		return false;
	}

	public boolean isGo_to_info() {
		return go_to_info;
	}

	public void setGo_to_info(boolean go_to_info) {
		this.go_to_info = go_to_info;
	}

	/**
	 * 获取从客户登陆号码
	 */
	private void requsetRegisterLoginName() {
		// 展示通信框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.CUSTOMER_COMBININFO);
		HttpManager.requestBii(biiRequestBody, this,
				"requsetCustomerCombinInfoCallBack");
	}

	public void requsetCustomerCombinInfoCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		String loginNames = (String) resultMap.get("terminalsIdNew");
		//合并后客户证件类型（主）
		String identityTypeNew = (String)resultMap.get("identityTypeNew");
		//合并后客户证件号码（主）
		String identityNumNew = (String)resultMap.get("identityNumNew");
		Intent intent = new Intent(this, VolumeProtocolActivity.class);
		intent.putExtra(Login.NAME, userNameStr);
		intent.putExtra(Login.LOGIN_STATUS, loginStatus);
		intent.putExtra(Login.OLD_PASSWORD, password);
		intent.putExtra(Login.OLD_PASSWORD_RC, password_RC);
		intent.putExtra(Login.REG_TYPE, regtype);
		intent.putExtra(Login.COMBINE_STATUS, ConstantGloble.COMBINSTATUS_10);
		intent.putExtra("loginNames", loginNames);
		intent.putExtra("identityTypeNew", identityTypeNew);
		intent.putExtra("identityNumNew", identityNumNew);
		startActivityForResult(intent, FORCE_MODIFY_REQUEST);
	}

	/**
	 * 当通信请求成功返回（响应码为200）但是数据包为空（无response）时统一回调该方法
	 * 
	 * @param requestMethod
	 *            http 请求方法名称
	 */
	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		// 设备绑定时错误不处理
		if (!Push.PSN_SET_MOBILE_INFO.equals(requestMethod)) {
			super.commonHttpResponseNullCallBack(requestMethod);
		}
	}

	/**
	 * 快捷方式是否需要跳转到登录页面
	 * 
	 * @param imageAndText
	 * @return
	 */
	public boolean needLoginforFast(ImageAndText imageAndText, int fastIndex) {
		
		if (!imageAndText.isMustLogin()) {
			return false;
		}
		
		if (!BaseDroidApp.getInstanse().isLogin()) {
			BaseDroidApp.getInstanse().setFastItemCHoosed(imageAndText);
			back_to_main = true;
			mGofast = true;
		}
		return true;
	}

	/********************** T43开户送电子银行服务功能 ****************************/

	/**
	 * 初始化开户送电子银行控件
	 */
	private void initBocnetView() {
		editAccNum = (EditText) findViewById(R.id.bocnet_accnum);
		// editMonth = (EditText) findViewById(R.id.bocnet_month);
		// editYear = (EditText) findViewById(R.id.bocnet_year);
		bocImageCode = (EditText) findViewById(R.id.bocImageCode);
		bocImageCodeBtn = (Button) findViewById(R.id.bocImageCodeBtn);
		bocImageCodeBtn.setOnClickListener(bocImageCodeClick);
		// editAccNum.addTextChangedListener(mWatcher);
		// editAccNum.setOnEditorActionListener(onEditorActionListener);
		editAccNum.setOnFocusChangeListener(onFocusChangeListener);
		// mSipBxoCVV2 = (SipBox) findViewById(R.id.bocnet_cvv2);
		// mSipBxoCVV2.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// mSipBxoCVV2.setPasswordMinLength(ConstantGloble.BOCNET_CVV2_LENGTH);
		// mSipBxoCVV2.setId(10002);
		// mSipBxoCVV2.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// mSipBxoCVV2.setPasswordMaxLength(ConstantGloble.BOCNET_CVV2_LENGTH);
		// mSipBxoCVV2.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		// mSipBxoCVV2.setSipDelegator(this);

		mSipBxoPas = (SipBox) findViewById(R.id.bocnet_pas);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(mSipBxoPas, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		mSipBxoPas.setCipherType(SystemConfig.CIPHERTYPE);
//		// bocnet_cvv2 = (SipBox) findViewById(R.id.bocnet_cvv2);
//
//		mSipBxoPas.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		mSipBxoPas.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		mSipBxoPas.setId(10002);
//		mSipBxoPas.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		mSipBxoPas.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		mSipBxoPas.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		mSipBxoPas.setSipDelegator(this);

		bocnet_cvv2 = (SipBox) findViewById(R.id.bocnet_cvv2);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(bocnet_cvv2, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		bocnet_cvv2.setCipherType(SystemConfig.CIPHERTYPE);
//		bocnet_cvv2.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		bocnet_cvv2.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		bocnet_cvv2.setId(10002);
//		bocnet_cvv2.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		bocnet_cvv2.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		bocnet_cvv2.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		bocnet_cvv2.setSipDelegator(this);
	}

	OnClickListener bocImageCodeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isRequestingCode) {
				isRequestingCode = true;
				isCodeSucceed = false;
				requestForImagecodeBoc();
			}
		}
	};

	// String lastAccount;
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus
					&& (findViewById(R.id.layout_acclogin).getVisibility() == View.VISIBLE)
					) {
				String accountNum = ((EditText) v).getText().toString();
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean regAcc = new RegexpBean(
						"", accountNum,
						"bontCard");
				lists.add(regAcc);
				if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
					return;
				}
				// if(!BocnetUtils.isStrEquals(lastAccount, accountNum)){
				// lastAccount = accountNum;
				requestCardType(accountNum);
				// }
			}
		}
	};

	/**
	 * 切换手机银行按钮
	 * 
	 * @param v
	 */
	int isImageCodeVisible = View.GONE;

	public void btnMobileBankUserOnclick(View v) {
		isBocnet = false;
		findViewById(R.id.login_middle_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_mobilebank).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_acclogin).setVisibility(View.GONE);
		findViewById(R.id.image_code_layout).setVisibility(isImageCodeVisible);
	}

	/**
	 * 切换持卡用户按钮
	 * 
	 * @param v
	 */
	public void btnCardBankUserOnclick(View v) {
		isBocnet = true;
		isImageCodeVisible = findViewById(R.id.image_code_layout)
				.getVisibility();
		findViewById(R.id.image_code_layout).setVisibility(View.GONE);
		findViewById(R.id.login_middle_layout).setVisibility(View.GONE);
		findViewById(R.id.layout_mobilebank).setVisibility(View.GONE);
		findViewById(R.id.layout_acclogin).setVisibility(View.VISIBLE);
		ImageView imageBtn = (ImageView)findViewById(R.id.imagebtn);
		imageBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.bocnet_info));
			}
		});
		if (StringUtil.isNull(bocnetConversation)) {
			BaseHttpEngine.showProgressDialog();
			requestConversationPre();
		}
	}

	/**
	 * 开户送电子银行登录
	 */
	private void bocnetLogin() {
		// 信用卡
		// if (!StringUtil.isNull(cardType) && cardType.equals("1")) {
		// try {
		// String month = editMonth.getText().toString();
		// String year = editYear.getText().toString();
		// if(StringUtil.isNull(month) || StringUtil.isNull(year)){
		// BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.bocnet_input_lasttime));
		// return;
		// }
		// if(Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("卡片有效期月份应为1-12之间的整数");
		// return;
		// }
		// if(year.length() != 2){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("卡片有效期年份由2位数字组成");
		// return;
		// }
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// RegexpBean rebSms = new RegexpBean(
		// BocnetUtils.getNoColonStr(getString(R.string.bocnet_accnum_cvv2)),
		// mSipBxoCVV2.getText()
		// .toString(), "cvv2");
		// lists.add(rebSms);
		// if (RegexpUtils.regexpDate(lists)) {
		// String identifyCodeStr = bocImageCode.getText().toString().trim();
		// if(StringUtil.isNull(identifyCodeStr)){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("请输入验证码");
		// return ;
		// }
		// if (identifyCodeStr.length() != 4) {
		// BaseDroidApp.getInstanse()
		// .showInfoMessageDialog(getResources().getString(R.string.zcode_invalible));
		// return ;
		// }
		// String password = mSipBxoCVV2.getValue().getEncryptPassword();
		// String randomNum = mSipBxoCVV2.getValue().getEncryptRandomNum();
		// requestBocnetLogin(password,randomNum);
		// return ;
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// return;
		// }
		// return;
		// }
		// 借记卡
		try {						
			String editAcc = editAccNum.getText().toString().trim();
			if (StringUtil.isNull(editAcc)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入银行卡号");
				return;
			}	
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regAcc = new RegexpBean(
					"", editAcc,
					"bontCard");
			lists.add(regAcc);
			
			if (!StringUtil.isNull(cardType) && cardType.equals("2")) {
				
				RegexpBean rebSms = new RegexpBean(
						getString(R.string.third_pas_tip), mSipBxoPas.getText()
								.toString(), ConstantGloble.SIPSMCPSW);
				lists.add(rebSms);
			} else if(bocnet_cvv2.isShown() == true) {
				RegexpBean rebSms = new RegexpBean(
						getString(R.string.third_pas_tip), bocnet_cvv2
								.getText().toString(), ConstantGloble.SIPSMCPSW);
				lists.add(rebSms);
			}
			if (RegexpUtils.regexpDate(lists)) {
				String identifyCodeStr = bocImageCode.getText().toString()
						.trim();
				if (StringUtil.isNull(identifyCodeStr)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入验证码");
					return;
				}
				if (identifyCodeStr.length() != 4) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.zcode_invalible));
					return;
				}
				String password = null;
				String randomNum = null;
				if (!StringUtil.isNull(cardType) && cardType.equals("1")) {
					password = bocnet_cvv2.getValue().getEncryptPassword();
					randomNum = bocnet_cvv2.getValue().getEncryptRandomNum();
				} else  if(mSipBxoPas.isShown()==true){
					
					password = mSipBxoPas.getValue().getEncryptPassword();
					randomNum = mSipBxoPas.getValue().getEncryptRandomNum();
				}
				requestBocnetLogin(password, randomNum);

			}
		} catch (CodeException e) {
//			BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
			LogGloble.exceptionPrint(e);
			return;
		}
	}

	/**
	 * 登录前创建会话id
	 */
	public void requestConversationPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.CONVERSATIONPRE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "conversationPreCallBack");
	}

	public void conversationPreCallBack(Object resultObj) {
		bocnetConversation = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(bocnetConversation)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.communication_fail));
			return;
		}
		requestRandomPre();
	}

	/**
	 * 登录前随机数
	 */
	public void requestRandomPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODGETRANDOMPRE);
		biiRequestBody.setConversationId(bocnetConversation);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "randomPreCallBack");
	}

	public void randomPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String random = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(random)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.communication_fail));
			return;
		}
		bocnet_cvv2.setRandomKey_S(random);
		mSipBxoPas.setRandomKey_S(random);
		requestForImagecodeBoc();
	}

	/**
	 * 请求验证码图片
	 */
	public void requestForImagecodeBoc() {
		isRequestingCode = false;
		bocImageCodeBtn.setText(R.string.imagecodeloading);
		bocImageCodeBtn
				.setBackgroundResource(R.drawable.selector_for_image_code);
		LinearLayout bocImageCodeLl = (LinearLayout) findViewById(R.id.bocImageCodeLl);
		if (bocImageCodeLl.getVisibility() == View.VISIBLE) {
			// 如果验证码布局显示则请求验证码
			HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE,
					ConstantGloble.GO_METHOD_GET, null, this,
					"imagecodeCallBackMethodBoc");
		}
	}

	/**
	 * 验证码请求回调
	 * 
	 * @param resultObj
	 *            请求成功 返回参数
	 */
	public void imagecodeCallBackMethodBoc(Object resultObj) {
		// 取消通讯框
		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		bocImageCodeBtn.setBackgroundDrawable(imageCodeDrawable);
		bocImageCodeBtn.setText("");
		isRequestingCode = false;
		isCodeSucceed = true;
	}

	/**
	 * 请求卡类型
	 * 
	 * @param cardNum
	 */
	private void requestCardType(String cardNum) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.PSNACCBOCNETQRYCARDTYPE);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTNUMBER, cardNum);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "cardTypeCallBack");
	}

	/** 卡类型返回 */
	@SuppressWarnings("unchecked")
	public void cardTypeCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		String type = (String) result.get(Comm.ACCOUNT_TYPE);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(type))
			return;
		cardType = BocnetDataCenter.cardType.get(type);
		BocnetDataCenter.getInstance().setCardTypeStr(type);
		if (cardType.equals("1")) {
			findViewById(R.id.layout_creditcard).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_debitcard).setVisibility(View.GONE);
			// editYear.setText("");editMonth.setText("");
			// mSipBxoCVV2.clearText();
			bocImageCode.setText("");
		}
		if (cardType.equals("2")) {
			findViewById(R.id.layout_debitcard).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_creditcard).setVisibility(View.GONE);
			mSipBxoPas.clearText();
			bocImageCode.setText("");
		}
	}

	/**
	 * 登录请求
	 */
	private void requestBocnetLogin(String password, String randomNum) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETLOGIN);
		biiRequestBody.setConversationId(bocnetConversation);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.LOGINNAME, editAccNum.getText().toString().trim());
		params.put(Bocnet.VALIDATIONCHAR, bocImageCode.getText().toString());
		// 信用卡
		// if (cardType.equals("1")) {
		// String month = editMonth.getText().toString().trim();
		// if(month.length() == 1)
		// month = "0" + month;
		// params.put(Bocnet.EXPDATE,
		// month+editYear.getText().toString().trim());
		// params.put(Bocnet.CVV2VALUE, password);
		// params.put(Bocnet.CVV2VALUE_RC, randomNum);
		// }
		// 借记卡
		if (!StringUtil.isNull(cardType) && cardType.equals("2")) {
			params.put(Bocnet.ATMPASSWORD, password);
			params.put(Bocnet.ATMPASSWORD_RC, randomNum);
		} else {
			params.put(Bocnet.PHONE_BANK_PASSWORD, password);
			params.put(Bocnet.PHONE_BANK_PASSWORD_RC, randomNum);
		}
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "bocnetLoginCallBack");
	}

	/** 登录返回 */
	@SuppressWarnings("unchecked")
	public void bocnetLoginCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		accountSeq = (String) result.get(Bocnet.ACCOUNTSEQ);
		// 登录失败
		if (StringUtil.isNull(accountSeq)) {
			mImageCodeLayout.setVisibility(View.VISIBLE);
			return;
		}
		BocnetDataCenter.getInstance().setAccountSeq(accountSeq);
		requestLoginInfo();
	}

	/**
	 * 请求登录信息
	 */
	public void requestLoginInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODLOGININFO);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "loginInfoCallBack");
	}

	/** 登录信息返回 */
	@SuppressWarnings("unchecked")
	public void loginInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))
			return;
		BocnetDataCenter.getInstance().setLoginInfo(result);
		if (cardType.equals("1")) {//信用卡
			
			
			requestPsnAccBocnetQryCrcdPoint();

			
//			requestCrcdDetail(); //sunh 2015/6/23
			
			//			requestDebitDetail();
		}
		if (cardType.equals("2")) {// 借记卡
			// requestCrcdDetail();
			requestDebitDetail();
		}
	}

	// 请求信用卡积分
	private void requestPsnAccBocnetQryCrcdPoint() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNACCBOCNETQRYCRCDPOINT);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Bocnet.ACCOUNTSEQ, accountSeq);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnAccBocnetQryCrcdPointCallBack");
	}

	// 请求信用卡积分 回调
	public void requestPsnAccBocnetQryCrcdPointCallBack(Object resultObj) {

		
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			crcdPoint = "-";
		} else {
			crcdPoint = (String) result.get(Crcd.CRCD_CONSUMPTIONPOINT);
		}

		requestPsnAccBocnetQueryGeneralInfo();

	}

	/** 信用卡综合信息查询 */
	private void requestPsnAccBocnetQueryGeneralInfo() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNACCBOCNETQUERYGENERALINFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Bocnet.ACCOUNTSEQ, accountSeq);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryGeneralInfoCallBack");

	}

	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryGeneralInfoCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);

		// TranDataCenter.getInstance().getCrcdGeneralInfo().clear();
		TranDataCenter.getInstance().setCrcdGeneralInfo(result);

		Intent it = new Intent(LoginActivity.this,
				MyCrcdCreditCardAcountActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountSeq);// 账户ID
		// it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);// 卡类型
		it.putExtra(Crcd.CRCD_CRCDPOINT, crcdPoint);// 积分
		startActivity(it);

	}

	/**
	 * 请求信用卡信息
	 */
	private void requestCrcdDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODCRCDDETAIL);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, accountSeq);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "crcdDetailCallBack");
	}

	@SuppressWarnings("unchecked")
	public void crcdDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))
			return;
		BocnetDataCenter.getInstance().setCrcdDetail(result);
		BocnetDataCenter.getInstance()
				.setIntentAction(Bocnet.ACTION_CREDITCARD);
		BocnetDataCenter.getInstance().setDebitCard(false);
		Intent intent = new Intent(this, CreditCardAcountActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 请求借记卡信息
	 */
	private void requestDebitDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODDEBITDETAIL);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, accountSeq);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "debitDetailCallBack");
	}

	@SuppressWarnings("unchecked")
	public void debitDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))
			return;
		BocnetDataCenter.getInstance().setDebitDetail(result);
		BocnetDataCenter.getInstance().setIntentAction(Bocnet.ACTION_DEBITCARD);
		BocnetDataCenter.getInstance().setDebitCard(true);
		Intent intent = new Intent(this, DebitCardAcountActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 安全退出
	 */
	private void requestBocnetLogout() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETLOGOUT);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "bocnetLogoutCallBack");
	}

	public void bocnetLogoutCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BocnetDataCenter.getInstance().clearAllData();
		BaseHttpEngine.showProgressDialog();
		requestConversationPre();
	}

	public static int getRequestRegisteCode() {
		return REQUEST_REGISTE_CODE;
	}

	/**
	 * 开户从引导自助注册页面返回至登录页面需做数据清空，重新请求会话、随机数、验证码
	 */
	public void bocnetOnActivityResult(int requestCode, int resultCode,
			Intent data) {
		switch (resultCode) {
		case RESULT_CANCELED:
			if (isBocnet) {
				editAccNum.setText("");
				// editYear.setText("");
				// editMonth.setText("");
				// mSipBxoCVV2.clearText();
				bocnet_cvv2.clearText();
				mSipBxoPas.clearText();
				bocImageCode.setText("");
				findViewById(R.id.layout_debitcard).setVisibility(View.GONE);
				findViewById(R.id.layout_creditcard).setVisibility(View.GONE);
				BocnetDataCenter.getInstance().clearAllData();
				// 请求验证码，请求随机数
				bocnetConversation = "";
				BaseHttpEngine.showProgressDialog();
				btnCardBankUserOnclick(null);
			}
			break;
		default:
			break;
		}
	}

	
	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	protected void requestEcardoutAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);
		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_BRO };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestEcardoutAccountListCallBack");

	}

	/**
	 * PsnCommonQueryAllChinaBankAccount接口的回调方法，返回结果 *
	 */
	@SuppressWarnings("unchecked")
	public void requestEcardoutAccountListCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		
		
			for(ImageAndText icon:LocalData.deptStorageCashLeftList){
				if(icon.getClassName().equals("com.chinamworld.bocmbci.biz.dept.fiexibleinterest.FiexibleInterestActivity")){
					LocalData.deptStorageCashLeftList.remove(icon);
					break;
				}
				
			}

		for (Map<String, Object> map : result) {
			String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			String eCard = (String) map.get(Ecard.ACC_ECARD_RES);
			if (accType.equals(ConstantGloble.ACC_TYPE_BRO)
					&& "1".equals(eCard)) {
				BaseDroidApp.getInstanse().setEcardMap(map);
				ImageAndText icon = new ImageAndText(R.drawable.icon_left_lhjx, "特色存款",true,false,"deptStorageCash_100");
				icon.setClassName("com.chinamworld.bocmbci.biz.dept.fiexibleinterest.FiexibleInterestActivity");								
				LocalData.deptStorageCashLeftList.add(3, icon);				
				break;
			}
		}
		requestPsnSvrGlobalMsgList();

	}
	
	
	
	
	

}
