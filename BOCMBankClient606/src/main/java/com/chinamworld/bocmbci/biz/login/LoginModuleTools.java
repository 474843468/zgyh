package com.chinamworld.bocmbci.biz.login;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.boc.commonlib.model.IActionCallBack;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.debitcard.DebitCardAcountActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdCreditCardAcountActivity;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.infoserve.push.PushReceiver;
import com.chinamworld.bocmbci.biz.login.findpwd.FindPwdActivity;
import com.chinamworld.bocmbci.biz.login.reg.RegisteVerifyActivity;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.HttpHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录模块公共入口
 * @author yuht
 *
 */
public class LoginModuleTools extends   HttpHandle {

	public Activity mActivity;
	String crcdPoint;// 信用卡积分
	String accountSeq;
	public LoginModuleTools(Activity activity){
		super(activity);
		mActivity = activity;
		CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity

	}

	public void gotoBocnetLoginModule(Activity activity, Map<String, Object> loginInfo,String accountSeq,boolean isDebit) {
		// TODO Auto-generated method stub
		this.accountSeq=accountSeq;
		BocnetDataCenter.getInstance().setLoginInfo(loginInfo);
		BocnetDataCenter.getInstance().setAccountSeq(accountSeq);
		if(isDebit){
			requestDebitDetail();
		}else{
			requestPsnAccBocnetQryCrcdPoint();
		}
	}

	public void gotoForgetPasswordModule(Activity arg0) {
		// TODO Auto-generated method stub


		BaseDroidApp.getInstanse().setMainItemAutoClick(false);
		// 申请conversationId
//		requsetFindpwdConversationId();
		Intent intent = new Intent();
		intent.setClass(mActivity, FindPwdActivity.class);
		mActivity.startActivityForResult(intent, 1003);
	}


	public void gotoRegistModule(Activity arg0) {
		// TODO Auto-generated method stub
//		requsetRegisterConversationId();
		Intent intent = new Intent();
		intent.setClass(mActivity, RegisteVerifyActivity.class);
		mActivity.startActivityForResult(intent, 1004);
	}


	private void requestDebitDetail() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, accountSeq);
		httpTools.requestHttp(Bocnet.METHODDEBITDETAIL,params, new IHttpResponseCallBack<Map<String, Object>>() {
			@Override
			public void httpResponseSuccess(Map<String, Object> result, String method) {
				// TODO Auto-generated method stub
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result))
					return;
				BocnetDataCenter.getInstance().setDebitDetail(result);
				BocnetDataCenter.getInstance().setIntentAction(Bocnet.ACTION_DEBITCARD);
				BocnetDataCenter.getInstance().setDebitCard(true);
				Intent intent = new Intent(mActivity, DebitCardAcountActivity.class);
				mActivity.startActivityForResult(intent, 0);
			}

		});
	}


	// 请求信用卡积分
	private void requestPsnAccBocnetQryCrcdPoint() {
		HashMap<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Bocnet.ACCOUNTSEQ, accountSeq);
		httpTools.requestHttp(Crcd.CRCD_PSNACCBOCNETQRYCRCDPOINT, paramsmap, new IHttpResponseCallBack<Map<String, Object>>() {
			public void httpResponseSuccess(Map<String, Object> result, String method) {

			if (StringUtil.isNullOrEmpty(result)) {
				crcdPoint = "-";
			} else {
				crcdPoint = (String) result.get(Crcd.CRCD_CONSUMPTIONPOINT);
			}

			requestPsnAccBocnetQueryGeneralInfo();
				}
			});
		httpTools.registAllErrorCode(Crcd.CRCD_PSNACCBOCNETQRYCRCDPOINT);
	}


	/** 信用卡综合信息查询 */
	private void requestPsnAccBocnetQueryGeneralInfo() {
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Bocnet.ACCOUNTSEQ, accountSeq);
		httpTools.requestHttp(Crcd.CRCD_PSNACCBOCNETQUERYGENERALINFO,paramsmap, new IHttpResponseCallBack<Map<String, Object>>(){
			@Override
			public void httpResponseSuccess(Map<String, Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				TranDataCenter.getInstance().setCrcdGeneralInfo(result);
				Intent it = new Intent(mActivity,
						MyCrcdCreditCardAcountActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountSeq);// 账户ID
				it.putExtra(Crcd.CRCD_CRCDPOINT, crcdPoint);// 积分
				mActivity.startActivity(it);
			}
		} );
	}
	/**
	 * 请求conversationId找回密码*/
	/*private void requsetFindpwdConversationId(){
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttp("PSNCreatConversationLoginPre", "requsetFindpwdConversationIdCallBack", null);
	}

	public void requsetFindpwdConversationIdCallBack(Object resultObj){
		String loginPreConversationId = (String)httpTools.getResponseResult(resultObj);
		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);
		requestFindPwdRandomNumber();
	}*/
	/**
	 * 请求随机数--重置密码*/
//	private void requestFindPwdRandomNumber(){
//		String loginPreConversationId = (String)LoginDataCenter.getInstance().getConversationId();
//		httpTools.requestHttpWithConversationId("PSNGetRandomLoginPre", null, loginPreConversationId, new IHttpResponseCallBack<String>() {
//
//			@Override
//			public void httpResponseSuccess(String result, String method) {
//				// TODO Auto-generated method stub
//				BaseHttpEngine.dissMissProgressDialog();
//				String randomNumber = (String)result;
//
//				LoginDataCenter.getInstance().setRandomNumber(randomNumber);
//
//				Intent intent = new Intent();
//				intent.setClass(mActivity, FindPwdActivity.class);
//				mActivity.startActivityForResult(intent, 1003);
//			}
//		});
//	}

//	/**
//	 * 请求conversationId ---找回密码
//	 */
//	private void requsetRegisterConversationId() {
//		// 展示通信框
//		BaseHttpEngine.showProgressDialog();
//		httpTools.requestHttp(Login.AQUIRE_CONVERSATION_ID_API, "requsetRegisterConversationIdCallBack", null);
//	}
	/**
	 * 请求conversationId返回 --- 找回密码
	 *
	 * @param resultObj
	 */
//	public void requsetRegisterConversationIdCallBack(Object resultObj) {
//		String loginPreConversationId = (String) httpTools.getResponseResult(resultObj);
//		LoginDataCenter.getInstance().setConversationId(loginPreConversationId);
//		requestRegisterRandomNumber();
//	}
	/**
	 * 请求随机数---自助注册
	 */
//	private void requestRegisterRandomNumber() {
//		String conversationId = (String)LoginDataCenter.getInstance()
//				.getConversationId();
//		// 获取 随机数
//		httpTools.requestHttpWithConversationId(Login.AQUIRE_RANDOM_NUMBER_API, null, conversationId, new IHttpResponseCallBack<String>() {
//
//			@Override
//			public void httpResponseSuccess(String result,
//											String method) {
//				// TODO Auto-generated method stub
//				BaseHttpEngine.dissMissProgressDialog();
//				String recoderNumber = (String)result;
//				LoginDataCenter.getInstance().setRandomNumber(recoderNumber);
//
//				Intent intent = new Intent();
//				intent.setClass(mActivity, RegisteVerifyActivity.class);
//				mActivity.startActivityForResult(intent, 1004);
//			}
//		});
//	}

	IActionCallBack bindingDeviceForPushServiceCallback;
	/***
	 * 消息推送绑定设备信息
	 * @param activity
     */
	public void BindingDeviceForPushService(final Activity activity,IActionCallBack callback) {
		bindingDeviceForPushServiceCallback = callback;
		PushManager.getInstance(activity).setPushDevice(null);
		InfoServeDataCenter.getInstance().setIsRefreshMessages(true);
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttpWithConversationId("PSNGetTokenId", null,null, new IHttpResponseCallBack<String>() {
			@Override
			public void httpResponseSuccess(String token, String s) {
				String conversionId = (String) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID);
				// 绑定设备信息
				Map<String, Object> paramsmap = new HashMap<String, Object>();
				paramsmap.put(Push.DEVICE_INFO, PushDevice.load().getDeviceId());
				paramsmap.put(Push.DEVICE_STYLE, "03");
				paramsmap.put(Push.DEVICE_SUB_STYLE, "01");
				paramsmap.put(Push.PUSHADDRESS, PushReceiver.deviceId);
				paramsmap.put(Push.BINDFLAG, "A");
				paramsmap.put(Comm.TOKEN_REQ, token);
				paramsmap.put("conversationId", conversionId);
				httpTools.requestHttp(Push.PSN_SET_MOBILE_INFO, paramsmap, new IHttpResponseCallBack() {
					@Override
					public void httpResponseSuccess(Object o, String s) {
						BaseHttpEngine.dissMissProgressDialog();
						PushDevice pd = PushDevice.load();
						PushManager.getInstance(activity).setPushDevice(pd);
						PushManager.getInstance(activity).restartPushService();
						if(bindingDeviceForPushServiceCallback != null)
							bindingDeviceForPushServiceCallback.callBack(null);
//						Toast.makeText(mActivity,"SetMobileInfo Success", Toast.LENGTH_LONG).show();
					}
				});
				httpTools.registAllErrorCode(Push.PSN_SET_MOBILE_INFO);
			}
		});
	}

	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		if(Push.PSN_SET_MOBILE_INFO.equals(method)){
			if(bindingDeviceForPushServiceCallback != null)
				bindingDeviceForPushServiceCallback.callBack(null);
		}
		return super.doHttpErrorHandler(method, biiError);
	}
}
