package com.chinamworld.bocmbci.biz.setting.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.setting.control.SettingControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

public class SettingBaseFragment extends Fragment implements HttpObserver, CFCASipDelegator {

	protected SettingControl settingControl = SettingControl.getInstance();
	
	
	protected String smc;
	protected String smc_RC;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void afterClickDown(SipBox sipbox) {
		sipbox.hideSecurityKeyBoard();
	}

	/**
	 * 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestCommConversationIdCallBack");
	}
	
	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);
	}
	
	/**
	 * 登录后的tokenId
	 */
	public void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSN_GETTOKENID);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "requestPSNGetTokenIdCallBack");
	}

	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String token = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TOKEN_ID, token);
	}
	
	/**
	 * 获取安全因子组合
	 * 
	 * @param serviceId 服务码
	 */
	public void requestGetSecurityFactor(String serviceId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_GETSECURITYFACTOR_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		// PB010C
		Map<String, String> map = new HashMap<String, String>();
		map.put(Acc.ACC_GETSECURITY_SERVICEID_REQ, serviceId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestGetSecurityFactorCallBack");
	}

	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().setSecurityIdList(new ArrayList<String>());
		BaseDroidApp.getInstanse().setSecurityNameList(new ArrayList<String>());
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// combinId = (String) biiResponseBody.getResult();
		if (biiResponseBody.getResult() == null
				|| "".equals(biiResponseBody.getResult()))
			return;
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		// 默认因子
		@SuppressWarnings("unchecked")
		Map<String, String> defaultCombin = (Map<String, String>) result
				.get(Acc.ACC_GETSECURITY_DEFAULTCOMBIN_RES);
		BaseDroidApp.getInstanse().setDefaultCombinId(String.valueOf(-1));
		if (defaultCombin != null
				&& !isContainUsb(defaultCombin.get(Acc.ECURITYID))) {// 不包含USB组合
			BaseDroidApp.getInstanse().setDefaultCombinId(defaultCombin.get(Acc.ECURITYID));
		}
		@SuppressWarnings("unchecked")
		List<Map<String, String>> combinList = (List<Map<String, String>>) result
				.get(Acc.ACC_GETSECURITY_COMBINLIST_RES);
		int length = combinList.size();
		String[] securityNames = new String[length+5];
		String[] securityIds = new String[length+5];
		int index = 0 ;
		if (combinList != null) {
			for (int i = 0; i < length; i++) {
				if (!isContainUsb(combinList.get(i).get(Acc.ECURITYID))) {// 不是USB组合就添加
					if (!BaseDroidApp.getInstanse().getSecurityIdList()
							.contains(combinList.get(i).get(Acc.ECURITYID))) {
						String securityId = combinList.get(i).get(Acc.ECURITYID);
						String securityName = combinList.get(i).get(Acc.TOKENNAME);
						if("32".equals(securityId)){
							securityIds[0]=securityId;
							securityNames[0]=securityName;
						}else if("96".equals(securityId)){
							securityIds[1] = securityId;
							securityNames[1] = "手机交易码";
						}else if("8".equals(securityId)){
							securityIds[2]=securityId;
							securityNames[2]=securityName;
						}else if("40".equals(securityId)){
							securityIds[3]=securityId;
							securityNames[3]=securityName;
						}else if("4".equals(securityId)){
							securityIds[4]=securityId;
							securityNames[4]="中银e盾";
						}else{
							securityIds[5+index]=securityId;
							securityNames[5+index]=securityName;
							index++;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < securityIds.length; i++) {
			if(securityIds[i]!=null){
				BaseDroidApp.getInstanse().getSecurityIdList().add(securityIds[i]);
				BaseDroidApp.getInstanse().getSecurityNameList().add(securityNames[i]);
			}
		}

	}

	/**
	 * 是否包含USB组合
	 * 
	 * @param tokenId
	 * @return
	 */
	private boolean isContainUsb(String tokenId) {
		if (Acc.ECURITY_8.equals(tokenId) || Acc.ECURITY_32.equals(tokenId) || Acc.ECURITY_40.equals(tokenId) 
				|| Acc.ECURITY_4.equals(tokenId) || Acc.ECURITY_12.equals(tokenId) || Acc.ECURITY_36.equals(tokenId) || Acc.ECURITY_96.equals(tokenId))
			return false;
		return true;
	}
	
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
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
		// 加密控件设置随机数
		settingControl.randomNumber = (String) biiResponseBody.getResult();
	}
	
	/**
	 * 
	 * @param sipBox 加密控件
	 * @param randomNumber 加密随机数
	 */
	public void initCodeSipBox(SipBox sipBox,String randomNumber) {
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		// add by 2016年9月12日 luqp 修改
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
	
	/**
	 * 发送手机交易码的请求
	 */
	public void sendSMSCToMobile() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendSMSCToMobileCallback");
	}

	/**
	 * 手机交易吗请求回调
	 * 
	 * @param resultObj
	 */
	public void sendSMSCToMobileCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	/**
	 * 
	 * @param sipBox CFCA控件
	 * @param type 手机交易码or动态口令
	 * @return
	 */
	public boolean checkCFCACode(SipBox sipBox , String type){
		boolean isOK = false;
		if (StringUtil.isNullOrEmpty(sipBox.getText()) || 0 >= sipBox.getText().toString().length()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入"+type);
			return false;
		}

		if (6 != sipBox.getText().toString().length()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(type+"由6位数字组成");
			return false;
		}
		try {
			smc = sipBox.getValue().getEncryptPassword();
			smc_RC = sipBox.getValue().getEncryptRandomNum();
		} catch (CodeException e) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(type+"由6位数字组成");
			return false;
		}
		isOK =true;
		return isOK;
	}
	
	/**
	 * 回调前拦截，主要是统一拦截错误信息给出提示
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		boolean stopFlag = false;
		if (resultObj instanceof BiiResponse) {
			// Bii请求前拦截
			stopFlag = doBiihttpRequestCallBackPre((BiiResponse) resultObj);
		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	}

	/**
	 * Bii请求前拦截-可处理统一的错误弹出框 有返回数据（response）<br>
	 * 对于包含exception 的业务错误提示数据进行统一弹框提示
	 * 
	 * @param BiiResponse resultObj
	 * @return 是否终止业务流程
	 */
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
					// 消除通信框
					BaseHttpEngine.dissMissProgressDialog();
					if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求

						return false;
					}
					BiiError biiError = body.getError();
					// 判断是否存在error
					if (biiError != null) {
						//过滤错误
						LocalData.Code_Error_Message.errorToMessage(body);
						
						
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
																						// 要重新登录
								// TODO 错误码是否显示"("+biiError.getCode()+") "
								BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissErrorDialog();
												ActivityTaskManager.getInstance().removeAllActivity();
//												Intent intent = new Intent();
//												intent.setClass(SettingBaseFragment.this.getActivity(), LoginActivity.class);
//												startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//                                                new LoginTask(SettingBaseFragment.this.getActivity()).exe(new LoginTask.LoginCallback() {
//                                                    @Override
//                                                    public void loginStatua(boolean isLogin) {
//                                                    }
//                                                });
												AbstractLoginTool.Instance.Login(SettingBaseFragment.this.getActivity(), new LoginTask.LoginCallback() {
													public void loginStatua(boolean isLogin) {
													}
												});
											}
										});

							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissErrorDialog();
												if (BaseHttpEngine.canGoBack) {
													getActivity().finish();
													BaseHttpEngine.canGoBack = false;
												}
											}
										});
							}
							return true;
						}
						// 弹出公共的错误框
						BaseDroidApp.getInstanse().dismissErrorDialog();
						BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								if (BaseHttpEngine.canGoBack) {
									getActivity().finish();
									BaseHttpEngine.canGoBack = false;
								}
							}
						});
					} else {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						// 避免没有错误信息返回时给个默认的提示
						BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse().dismissErrorDialog();
										if (BaseHttpEngine.canGoBack) {
											getActivity().finish();
											BaseHttpEngine.canGoBack = false;
										}
									}
								});
					}

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		boolean stopFlag = false;

		if (resultObj instanceof BiiResponse) {
			// Bii请求后拦截
			stopFlag = doBiihttpRequestCallBackAfter((BiiResponse) resultObj);

		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	}

	/**
	 * Bii请求后拦截
	 * 
	 * @param resultObj
	 */
	private boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		return false;
	}

	/**
	 * 因网络问题（超时，无网络等）引起的通信异常的默认回调( 返回码 不是200 )<br>
	 * 子类可重写进行特殊化处理<br>
	 * 
	 * @param 请求失败的接口名称 Method
	 */
	@Override
	public void commonHttpErrorCallBack(final String requestMethod) {
		if (Login.LOGOUT_API.equals(requestMethod)) {// 退出的请求 不做任何处理
			return;
		}

		String message = getResources().getString(R.string.communication_fail);
		BaseDroidApp.getInstanse().showMessageDialog(message, new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				if (BaseHttpEngine.canGoBack || LocalData.queryCardMethod.contains(requestMethod)) {
					getActivity().finish();
					BaseHttpEngine.canGoBack = false;
				}

			}
		});
	}


	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		if (Acc.ACC_GETSECURITYFACTOR_API.equals(requestMethod)) {// 代表是 安全因子的请求
			BaseDroidApp.getInstanse().createDialog("",
					BaseDroidApp.getContext().getResources().getString(R.string.PsnGetSecurityFactorservicenoback));
		} else {
			BaseDroidApp.getInstanse().createDialog("",
					BaseDroidApp.getContext().getResources().getString(R.string.servicenoback), new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							if (BaseHttpEngine.canGoBack) {// 防止白版页面出现
								getActivity().finish();
							}
						}
					});
		}		
	}

	@Override
	public void afterKeyboardHidden(SipBox arg0, int arg1) {
		if (getActivity().findViewById(R.id.rltotal) != null)
			((RelativeLayout) getActivity().findViewById(R.id.rltotal)).scrollTo(0, 0);
	}

	@Override
	public void beforeKeyboardShow(SipBox sipbox, int keyboardHeight) {
		// 判断弹出的安全键盘是否会遮掩输入框
		int[] location = new int[2];
		sipbox.getLocationOnScreen(location);
		int y = location[1];
		// 距底距离
		int bottom = getWindowHeight() - y - sipbox.getHeight();
		if (bottom < keyboardHeight) {// 说明遮掩
			if ((RelativeLayout) getActivity().findViewById(R.id.rltotal) != null) {
				((RelativeLayout) getActivity().findViewById(R.id.rltotal)).scrollTo(0, keyboardHeight - bottom);
			}
		}
	}
	
	/**
	 * 获得屏幕总高度
	 * 
	 * @time 2013-1-18下午02:38:38
	 * @author lichuang
	 */
	public int getWindowHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		display.getMetrics(metrics);
		return getActivity().getWindowManager().getDefaultDisplay().getHeight();
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
	
	protected OnClickListener cancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
}
