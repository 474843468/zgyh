package com.chinamworld.bocmbci.commonlibtools;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.base.application.DexApplication;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.infoserve.push.PushReceiver;
import com.chinamworld.bocmbci.biz.invest.activity.InvesHasOpenActivity;
import com.chinamworld.bocmbci.biz.push.PushDevice;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpRequestHandle;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.HashMap;
import java.util.List;

public class CommonTools extends BaseCommonTools implements IHttpRequestHandle {
	@Override
	public void commonHttpErrorCallBack(String s) {

	}

	Activity activity;
	HttpTools httpTools;
	public CommonTools(){
		instance = this;
		
	}
	
	@Override
	public HashMap<String, HashMap<String, String>> getCookieMap() {
		return BaseDroidApp.cookieMap;
	}

	@Override
	public int getScreenTimeOut() {
		return BaseDroidApp.getInstanse().screenOutTime;
	}

	@Override
	public void setCurrentActivity(Activity activity) {
		CommonApplication.getInstance().setCurrentAct(activity);
	}

	@Override
	public String getdeviceInfo() {
		return PushDevice.load().getDeviceId();
	}

	@Override
	public String getpushAddress() {
	return PushReceiver.deviceId;
	}

	@Override
	public void loginTimeOutHandler(final Activity activity,String message) {
		this.activity = activity; 
//		BaseDroidApp.getInstanse().showMessageDialog(message, new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
				ActivityTaskManager.getInstance().removeAllActivity();
				ActivityTaskManager.getInstance().removeAllSecondActivity();;
//				Intent intent = new Intent();
//				intent.setClass(activity, LoginActivity.class);
//				activity.startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//		new LoginTask(activity).exe(new LoginCallback() {
//			@Override
//			public void loginStatua(boolean isLogin) {
//			}
//		});
		AbstractLoginTool.Instance.Login(activity, new LoginCallback() {
			public void loginStatua(boolean isLogin) {
			}
		});
//			}
//		});
	}

	
	@Override
	public void noOperateTimeOutHandler(Activity activity) {
		this.activity = activity; 
		if(BaseDroidApp.getInstanse().isLogin() == false)
			return;
		httpTools = new HttpTools(activity,null);
		httpTools.requestHttp("", new HashMap<String,Object>(), null);
		httpTools.registAllErrorCode("Login.LOGOUT_API");
		BaseDroidApp.getInstanse().setMainItemAutoClick(false);
	
		// 强制退出
		ActivityTaskManager.getInstance().removeAllActivity();
		BaseDroidApp.getInstanse().clientLogOut();
//		Intent intent1 = new Intent();
//		intent1.setClass(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				LoginActivity.class);
//		intent1.putExtra(ConstantGloble.TIME_OUT_CONFIRM, true);
//		intent1.putExtra(ConstantGloble.BACK_TO_MAIN, true);
//		activity.startActivity(intent1);
//		new LoginTask(activity).exe(new LoginCallback() {
//			@Override
//			public void loginStatua(boolean isLogin) {
//			}
//		});
		AbstractLoginTool.Instance.Login(activity, new LoginTask.LoginCallback(){
			@Override
			public void loginStatua(boolean isLogin) {
			}
		});
	}

	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse resultObj) {
		List<BiiResponseBody> biiResponseBodyList = resultObj.getResponse();

		if(StringUtil.isNullOrEmpty(biiResponseBodyList))
			return false;
		for (BiiResponseBody body : biiResponseBodyList) {
			if(ConstantGloble.STATUS_SUCCESS.equals(body.getStatus()))
				continue;
				// 消除通信框
			BaseHttpEngine.dissMissProgressDialog();
			if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求
				return false;
			}
			if (Login.OUTLAY_CLOSECONVERSATION_ID_API.equals(body
					.getMethod())
					|| Tran.PSNGETTICKETFORMESSAGE.equals(body
							.getMethod())
					|| Tran.INSERTTRANSEQ.equals(body.getMethod())) {// 关闭回话
				return false;
			}
			BiiError biiError = body.getError();
			// 判断是否存在error
			if(biiError == null) {
				return true;
			}
			if(biiError.getCode() == null) {
				// 弹出公共的错误框
				return true;
			}
			// 过滤错误
			LocalData.Code_Error_Message.errorToMessage(body);
		
			if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时,要重新登录
				loginTimeOutHandler(activity,biiError.getMessage());
			//	HttpHandlerTools.a();
				return true;
			} 
			// 非会话超时错误拦截
			if(httpTools != null && httpTools.IsInterceptErrorCode(body.getMethod(),biiError.getCode())) {  // 需要拦截错误码
				return true;
			}
			
			if(biiError.getCode().equals("role.no.invest.service")){
				BaseDroidApp.getInstanse().showErrorDialog(biiError.getMessage(), R.string.cancle,
						R.string.confirm, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
								case CustomDialog.TAG_SURE:
									
									Intent intent = new Intent();
									intent.setClass(activity,
											InvesHasOpenActivity.class);
									activity.startActivity(intent);	
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (BaseHttpEngine.canGoBack) {
										activity.finish();
										BaseHttpEngine.canGoBack = false;
									}
									break;
								case CustomDialog.TAG_CANCLE:
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (BaseHttpEngine.canGoBack) {
										activity.finish();
										BaseHttpEngine.canGoBack = false;
									}

									break;
								}

							}
						});	
			}else{
				BaseDroidApp.getInstanse().dismissErrorDialog();
				BaseDroidApp.getInstanse().createDialog(biiError.getCode(),biiError.getMessage(), new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse()
								.dismissErrorDialog();
						if (BaseHttpEngine.canGoBack) {
							activity.finish();
							BaseHttpEngine.canGoBack = false;
						}
					}
				});
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
		return false;
	}

	@Override
	public Application getCurApplication() {
		return DexApplication.instanse;
	}

	@Override
	public void SetCookie(String cookie, String host) {
		if(cookie == null && host == null) {
			BaseDroidApp.getInstanse().clientLogOut();
			return;
		}

		BaseDroidApp.getInstanse().SetCookie(cookie, host);

		/** 移动支付cookie */
		if (SystemConfig.BASE_HTTP_URL.contains(host))
			BiiHttpEngine.cookieCurrent =  BaseDroidApp.getInstanse().getAllCookie(host);

	}



}
