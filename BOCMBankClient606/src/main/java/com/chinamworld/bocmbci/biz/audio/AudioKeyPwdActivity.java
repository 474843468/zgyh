package com.chinamworld.bocmbci.biz.audio;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.device.key.KeyInfo;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.AudioKey;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.audio.bocm.BOCMKeyModel;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;

/**
 * 
 * 音频key(中银E盾)密码管理
 *
 */
public class AudioKeyPwdActivity extends AudioKeyBaseActivity implements
		OnClickListener {

	private LinearLayout tabcontent;
	private View view;

	/** 原始密码 */
	private EditText old_pwd;
	/** 新密码 */
	private EditText new_pwd;
	/** 确认新密码 */
	private EditText confirm_new_pwd;
	private Button btn_confirm;
	private TextView new_pwd_tv;
	private TextView confirm_new_pwd_tv;

	private String conversationId;
	private String randomNumber;
	private String oldPwdStr;
	private String newPwdStr;
	private String confirmNewPwdStr;
	private int remainNumbers = 5;
	
	/**P603  首次修改中银E盾密码*/
	private boolean isFlag;
	private LinearLayout old_pwd_layout;
	
	/*603改造 添加中银e盾不连接 页面显示情况**/
	private LinearLayout keyConnectionLayout;
	private TextView keyNotConnectinfo;
	/**判断中银e盾连接标识*/
//	private Boolean isConnection;
	protected AudioKeyManager mAudioKeyManager = AudioKeyManager.getInstance();
	private Button noConnettionBtn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		isFlag = getIntent().getBooleanExtra(ConstantGloble.BACK_TO_MAIN_ACTIVITY, false);
		isFlag = AudioKeyManager.getInstance().isFlag();
//		isConnection = mAudioKeyManager.isConnected();
		setObserver(this);
		init();
	}

	private void init() {
		setTitle(getString(R.string.audio_key_pwd_title));
		ibRight.setVisibility(View.GONE);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.audio_key_pwd_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		setLeftSelectedPosition("settingManager_11");
		
		/**603改造 中银e盾不连接时，页面为提示用户插入中银e盾连接*/
		keyConnectionLayout = (LinearLayout)findViewById(R.id.key_connection);
		keyNotConnectinfo = (TextView)findViewById(R.id.key_no_connection);
		noConnettionBtn = (Button)findViewById(R.id.btn_noconection);
		old_pwd_layout = (LinearLayout)findViewById(R.id.old_pwd_layout);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		confirm_new_pwd = (EditText) findViewById(R.id.confirm_new_pwd);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		noConnettionBtn.setOnClickListener(this);
		if(isFlag){
			ibBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(AudioKeyPwdActivity.this, MainActivity.class);
//					startActivity(intent);
					goToMainActivity();
				}
			});
		}
		initViews();
	}
	/**
	 * 初始化布局
	 */
	private void initViews() {
		notOrConnectionView(mAudioKeyManager.isConnected());
//		if(isFlag){
//			old_pwd_layout.setVisibility(View.GONE);
//			new_pwd_tv = (TextView)findViewById(R.id.new_password_text);
//			confirm_new_pwd_tv = (TextView)findViewById(R.id.confrim_new_password_text);
//			new_pwd_tv.setText("中银e盾密码：");
//			confirm_new_pwd_tv.setText("请确认密码：");
//		}
		old_pwd.setText("");
		old_pwd.setHint(R.string.please_enter_pwd);
		new_pwd.setText("");
		new_pwd.setHint(R.string.please_enter_pwd);
		confirm_new_pwd.setText("");
		confirm_new_pwd.setHint(R.string.please_enter_pwd);
	}
	/**603改造 布局显示*/
	private void notOrConnectionView(Boolean isConnection){
		if(isConnection){
			keyConnectionLayout.setVisibility(View.VISIBLE);
			keyNotConnectinfo.setVisibility(View.GONE);
			btn_confirm.setVisibility(View.VISIBLE);
			noConnettionBtn.setVisibility(View.GONE);
			old_pwd.setFocusable(true);
			old_pwd.setFocusableInTouchMode(true);
			old_pwd.requestFocus();
			old_pwd.findFocus();
			if(isFlag){
				old_pwd_layout.setVisibility(View.GONE);
				new_pwd_tv = (TextView)findViewById(R.id.new_password_text);
				confirm_new_pwd_tv = (TextView)findViewById(R.id.confrim_new_password_text);
				new_pwd.setFocusable(true);
				new_pwd.setFocusableInTouchMode(true);
				new_pwd.requestFocus();
				new_pwd.findFocus();
				new_pwd_tv.setText("中银e盾密码：");
				confirm_new_pwd_tv.setText("请确认密码：");
			}
		}else {
			keyConnectionLayout.setVisibility(View.GONE);
			keyNotConnectinfo.setVisibility(View.VISIBLE);
			btn_confirm.setVisibility(View.GONE);
			noConnettionBtn.setVisibility(View.VISIBLE);
			noConnettionBtn.setClickable(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (!AudioKeyManager.getInstance().hasAudioKey()) {
				BaseDroidApp.getInstanse().showMessageDialog(
						getString(R.string.no_have_audio_key), cancelListener);
				return;
			}
			
			if (BaseDroidApp.getInstanse().isWiredHeadsetOn()) {
				modifyPwd();
			} else {
				BaseDroidApp.getInstanse().showMessageDialog(
						getString(R.string.no_device_connect), cancelListener);
			}
			break;
		case R.id.btn_noconection:
			initViews();
			break;
			
		default:
			break;
		}
	}

	private void modifyPwd() {
		oldPwdStr = old_pwd.getText().toString().trim();
		newPwdStr = new_pwd.getText().toString().trim();
		confirmNewPwdStr = confirm_new_pwd.getText().toString().trim();
		
		if(!isFlag){
			if (!checkPwd(oldPwdStr,
					getResources().getString(R.string.old_pwd_without_colon))) {
				old_pwd.setText("");
				return;
			}
		}
		if (!checkPwd(newPwdStr,
				getResources().getString(R.string.new_pwd_without_colon))) {
			new_pwd.setText("");
			return;
		}
		if (!checkPwd(
				confirmNewPwdStr,
				getResources().getString(
						R.string.confirm_new_pwd_without_colon))) {
			new_pwd.setText("");
			confirm_new_pwd.setText("");
			return;
		}
		if (newPwdStr.equals(oldPwdStr)) {
			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.newpwd_cannot_eq_oldpwd),
					cancelListener);
			new_pwd.setText("");
			confirm_new_pwd.setText("");
			return;
		}
		if (!confirmNewPwdStr.equals(newPwdStr)) {
			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.password_not_eq),
					cancelListener);
			new_pwd.setText("");
			confirm_new_pwd.setText("");
			return;
		}
		BaseHttpEngine.showProgressDialog();
		BaseHttpEngine.dissmissCloseOfProgressDialog();
		requestCommConversationId();
	}

	public boolean checkPwd(String pwd, String lable) {
		String type = "";
		if (getResources().getString(R.string.old_pwd_without_colon).equals(
				lable)) {
			type = "password2";
		} else if (getResources().getString(R.string.new_pwd_without_colon)
				.equals(lable)) {
			if(isFlag) lable="中银e盾密码";
			type = "password3";
		} else if (getResources().getString(
				R.string.confirm_new_pwd_without_colon).equals(lable)) {
			if(isFlag) lable="确认密码";
			else lable = "";
			type = "password4";
		}
		RegexpBean regBean;
		if(lable.equals("确认密码")){
			regBean = new RegexpBean("", pwd, type);
		}else {
			regBean = new RegexpBean(lable, pwd, type);
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(regBean);
		if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
			return false;
		}
		return true;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		conversationId = (String) biiResponseBody.getResult();
		requestForRandomNumber(conversationId);
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
		if(isFlag) oldPwdStr = "88888888";
		modifyPin(this, oldPwdStr, newPwdStr, randomNumber, conversationId,
				this, null);
	}


	@Override
	public void keyModifyPinNeedConfirm(final int remainNumber, String sessionId) {
		super.keyModifyPinNeedConfirm(remainNumber, sessionId);
		setRemainNumbers(remainNumber);
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				BaseHttpEngine.dissMissProgressDialog();
				View view = mInflater.inflate(R.layout.audio_key_modify_pwd_dialog,
						null);
				BaseDroidApp.getInstanse().showDialog(view);				
			}
		});
	}
	
	@Override
	public void keyModifyPinDidConfirm(String sessionId) {
		super.keyModifyPinDidConfirm(sessionId);
	}

	@Override
	public void keyDidModifyPINSuccess(String sessionId) {
		super.keyDidModifyPINSuccess(sessionId);
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.modify_password_success_title),
						getString(R.string.modify_password_success),cancelListener);
				initViews();				
			}
		});
	}
	

	@Override
	public void keyModifyPinDidCancel(String sessionId) {
		super.keyModifyPinDidCancel(sessionId);
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				BaseDroidApp.getInstanse().dismissMessageDialog();		
				initViews();
			}
		});
	}
	
	@Override
	public void keyDidModifyPINFailWithError(final int errorId, String sessionId) {
		super.keyDidModifyPINFailWithError(errorId, sessionId);
		dealWithError(errorId);
	}

	@Override
	public void keyDidSignFailWithError(int errorId, String sessionId) {
		super.keyDidSignFailWithError(errorId, sessionId);
		dealWithError(errorId);
	}

	@Override
	public void keyPinWarningFailWithError(final int errorId, String sessionId) {
		super.keyPinWarningFailWithError(errorId, sessionId);
		dealWithError(errorId);
	}
	
	private void dealWithError(final int errorId) {
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				initViews();
				BaseDroidApp.getInstanse().dismissMessageDialog();
				if (errorId == AudioKey.BOC_PASSWORD_INVALID) {
					String remainNumber = String.valueOf(getRemainNumbers() - 1);
					String errorMsg = getErrorMessage(AudioKey.BOC_PASSWORD_INVALID)
							+ getString(R.string.please_enter_again) + remainNumber
							+ getString(R.string.chances);
					BaseDroidApp.getInstanse().showMessageDialog(errorMsg,cancelListener);
				} else {
					if (errorId != AudioKey.BOC_SUCCESS && errorId != AudioKey.BOC_USER_CANCEL) {
						commonErrorHandle(errorId);
					}
				}
			}
		});
	}
	
	@Override
	public void deviceDisconnect() {
		super.deviceDisconnect();
	}
	
	@Override
	public void successCallback(Object obj) {
		super.successCallback(obj);
		if (obj instanceof BOCMKeyModel) {
			updateViews((BOCMKeyModel) obj);
		} 
	}

	private void updateViews(BOCMKeyModel model){
		KeyInfo mKeyInfo = model.getmKeyInfo();
		if (mKeyInfo.isPinNeedModify()) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().dismissMessageDialog();

			BaseDroidApp.getInstanse().showMessageDialog(
					getString(R.string.need_modify_password),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissMessageDialog();
							isFlag = true;
							AudioKeyManager.getInstance().setFlag(true);
							notOrConnectionView(mAudioKeyManager.isConnected());
							noConnettionBtn.setClickable(true);
						}
					});
		}else {
			AudioKeyManager.getInstance().setFlag(false);
			noConnettionBtn.setClickable(true);
		}
	}
	
	@Override
	public void commonErrorHandle(int errorId) {
		super.commonErrorHandle(errorId);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public int getRemainNumbers() {
		return remainNumbers;
	}

	public void setRemainNumbers(int remainNumbers) {
		this.remainNumbers = remainNumbers;
	}

}
