package com.chinamworld.bocmbci.biz.audio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class AudioKeyBaseActivity extends SettingBaseActivity implements AudioKeyObserver {

	private static final String TAG = "AudioKeyBaseActivity";
	
	protected LayoutInflater mInflater;
	protected Button ibBack;
	protected Button ibRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		initLeftSideList(this, LocalData.settingManagerlistData); // 加载左边菜单栏
		mInflater = LayoutInflater.from(this);
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibRight = (Button) this.findViewById(R.id.ib_top_right_btn);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
//	@Override
//	protected void setSelectedMenu(int clickIndex) {
//		super.setSelectedMenu(clickIndex);
//	}
	
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		
	}
	
	protected void setOnShowAllTextListener(Context context,TextView... tvs){
		for(TextView tv : tvs)
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, tv);
	}
	
	/**
	 *  修改PIN码过程中，提示用户对修改PIN码操作进行核对的事件
	 *
	 *  @param remainNumber 剩余的修改PIN码操作可重试次数
	 *  @param sessionId   调用修改PIN码方法时传入的会话conversationId
	 */
	@Override
	public void keyModifyPinNeedConfirm(int remainNumber, String sessionId) {
		LogGloble.i(TAG, "keyModifyPinNeedConfirm");
		LogGloble.i(TAG, "remainNumber : " + remainNumber + ",sessionId : " + sessionId);
	}
	
	/**
	 *  修改PIN码过程中，用户点击key设备“OK”键，确认修改的事件
	 *  @param sessionId   调用修改PIN码方法时传入的会话ID
	 */
	@Override
	public void keyModifyPinDidConfirm(String sessionId) {
		LogGloble.i(TAG, "keyModifyPinDidConfirm");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}
	

	/**
	 *  key设备修改PIN码成功的回调
	 *
	 *  @param sessionId 调用修改PIN码方法时传入的会话conversationId
	 */
	@Override
	public void keyDidModifyPINSuccess(String sessionId) {
		LogGloble.i(TAG, "keyDidModifyPINSuccess");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}

	/**
	 *  修改PIN码过程中，用户点击key设备“C”键，取消修改操作的事件
	 *  @param sessionId   调用修改PIN码方法时传入的会话ID
	 */
	@Override
	public void keyModifyPinDidCancel(String sessionId) {
		LogGloble.i(TAG, "keyModifyPinDidCancel");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}
	
	/**
	 *  key设备修改PIN码失败的回调
	 *
	 *  @param errorId 修改PIN操作时的错误码
	 *  @param sessionId   调用修改PIN码方法时传入的conversationId
	 */
	@Override
	public void keyDidModifyPINFailWithError(int errorId, String sessionId) {
		LogGloble.i(TAG, "keyDidModifyPINFailWithError");
		LogGloble.i(TAG, "errorId : " + errorId + ",sessionId : " + sessionId);
//		dealWithError(errorId);
	}
	
	/**
	 *  操作PIN码过程中（签名），PIN码输入预警提醒时，用户点击key设备“C”键确认的回调
	 *  @param sessionId   操作PIN码过程中（签名）传入的会话ID
	 */
	@Override
	public void keyPinWarningDidCancel(String sessionId) {
		LogGloble.i(TAG, "keyPinWarningDidCancel");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}

	/**
	 *  操作PIN码过程中（签名），PIN码输入预警提醒时，用户点击key设备“OK”键确认的回调
	 *  @param sessionId   操作PIN码过程中（签名）传入的会话conversationId
	 */
	@Override
	public void keyPinWarningDidConfirm(String sessionId) {
		LogGloble.i(TAG, "keyPinWarningDidConfirm");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}

	/**
	 *  操作PIN码过程中（签名),PIN码输入预警提醒时,出现错误的回调（如设备断开，没电等情况）
	 *  @param errorId 错误码
	 *  @param sessionId   操作PIN码过程中（签名）传入的会话conversationId
	 */
	@Override
	public void keyPinWarningFailWithError(int errorId, String sessionId) {
		LogGloble.i(TAG, "keyPinWarningFailWithError");
		LogGloble.i(TAG, "errorId : " + errorId + ",sessionId : " + sessionId);
//		dealWithError(errorId);
	}

	/**
	 * 操作PIN码过程中（签名），当用户PIN码输入错误次数达到预警值时，提示用户点按Key设备“OK“或”C“键的回调
	 * 如果点击"ok"会继续执行keySignNeedConfirm
	 * @param remainNumber 剩余的PIN码可输入次数
	 * @param sessionId conversationId
	 */
	@Override
	public void keyPinWarningNeedConfirm(final String remainNumber, String sessionId) {
		LogGloble.i(TAG, "keyPinWarningNeedConfirm");
		LogGloble.i(TAG, "remainNumber : " + remainNumber + ",sessionId : " + sessionId);
	}

	/**
	 *  签名过程中，提示用户对签名信息进行核对的事件
	 *  @param sessionID   调用签名方法时传入的会话conversationId
	 */
	@Override
	public void keySignNeedConfirm(String sessionId) {
		LogGloble.i(TAG, "keySignNeedConfirm");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}
	
	/**
	 *  签名过程中，用户点击key设备“OK”键，完成对签名信息核对的事件
	 *  @param sessionID   调用签名方法时传入的会话conversationId
	 */
	@Override
	public void keySignDidConfirm(String sessionId) {
		LogGloble.i(TAG, "keySignDidConfirm");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}
	
	/**
	 *  key设备签名成功，并返回签名后的结果
	 *
	 *  @param signedData 经过Base64后的签名结果数据
	 *  @param sessionId  conversationId
	 */
	@Override
	public void keyDidSignSuccess(String signedData, String sessionId) {
		LogGloble.i(TAG, "keyDidSignSuccess");
		LogGloble.i(TAG, "signSuccessData : " + signedData + ",sessionId : " + sessionId);
	}
	
	/**
	 *  签名过程中，用户点击key设备“C”键，取消签名操作的事件
	 *  @param sessionID   调用签名方法时传入的会话conversationId
	 */
	@Override
	public void keySignDidCancel(String sessionId) {
		LogGloble.i(TAG, "keySignDidCancel");
		LogGloble.i(TAG, "sessionId : " + sessionId);
	}
	
	/**
	 *  key设备签名时出错的回调
	 *
	 *  @param errorId 签名出错时的错误码
	 *  @param sessionId conversationId
	 */
	@Override
	public void keyDidSignFailWithError(int errorId, String sessionId) {
		LogGloble.i(TAG, "keyDidSignFailWithError");
//		dealWithError(errorId);
	}

	@Override
	public void deviceDisconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void successCallback(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void commonErrorHandle(int errorId) {
		super.commonErrorHandle(errorId);
	}
}
