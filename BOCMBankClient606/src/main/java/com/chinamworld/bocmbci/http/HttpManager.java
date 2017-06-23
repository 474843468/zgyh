/**
 * 文件名	：HttpManager.java
 * 创建日期	：2013-03-19
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.BiiHeader;
import com.chinamworld.bocmbci.bii.BiiRequest;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiPollingEngine;
import com.chinamworld.bocmbci.http.engine.CommonHttpEngine;
import com.chinamworld.bocmbci.http.engine.FileHttpEngine;
import com.chinamworld.bocmbci.http.engine.ImageFileHttpEngine;
import com.chinamworld.bocmbci.http.thread.CommonRequestThread;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.http.thread.PushRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 通信管理类
 * 
 * @author wez
 * 
 *         2013-4-2
 * 
 */
public class HttpManager {

	private static final String TAG = "HttpManager";

	private static BiiHeader sharedBiiHeader = new BiiHeader();// 共用的BiiHeader
	/** 轮训线程 */
	public static PollingRequestThread mPollingRequestThread;
	/** 消息推送 */
	public static PushRequestThread mPushRequestThread;
	public static CommonRequestThread thread;
	public static String currentMethod = "";

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void requestBii(final BiiRequestBody biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod) {
		LogGloble.d("time", "biiRequest.getMethod()---  " + biiRequest.getMethod()); 
		if (biiRequest.getParams() != null) {
			if (biiRequest.getParams().containsKey(Comm.Otp) || biiRequest.getParams().containsKey(Comm.Smc)) {
			
				SipBoxUtils.setSipBoxParams(biiRequest.getParams());
//				biiRequest.getParams().put(SipBoxUtils.ACTIV, SipBoxUtils.ACTIV_VALUE);// 控件的属性定义好的
//				biiRequest.getParams().put(SipBoxUtils.STATE, SipBoxUtils.STATE_VALUE);// 控件的属性定义好的
			}
		}
		requestBii(BiiApi.BASE_API_URL, biiRequest, callbackObject, successCallBackMethod);
	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param 请求的标识
	 * 
	 */
	public static void requestBii(final BiiRequestBody biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod, String requestType) {
		requestBii(BiiApi.BASE_API_URL, biiRequest, callbackObject, successCallBackMethod, requestType);
	}
	
	/**
	 * 功能外置-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param 请求的标识
	 * 
	 */
	public static void requestOutlayBii(final BiiRequestBody biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod) {
		requestBii(SystemConfig.Outlay_API_URL, biiRequest, callbackObject, successCallBackMethod);
	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param url 请求地址
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	public static void requestBii_withUrl(String url, final BiiRequestBody biiRequest,
			final HttpObserver callbackObject, final String successCallBackMethod) {
		// 如果是wms基金
		if (url.contains(BiiApi.FINCADDRESS)) {
			StringBuffer urlSb = new StringBuffer();
			urlSb.append(BiiApi.FINCADDRESS);
			urlSb.append(biiRequest.getMethod());
			urlSb.append(".do");
			url = urlSb.toString();
			biiRequest.getParams().put(BiiApi.TAG_WMS, "TAG_WMS");
		}
		requestBii(url, biiRequest, callbackObject, successCallBackMethod);
	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 多个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param biiRequestBodyList 多个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	public static void requestBii(final List<BiiRequestBody> biiRequestBodyList, final HttpObserver callbackObject,
			final String successCallBackMethod) {
		requestBii(BiiApi.BASE_API_URL, biiRequestBodyList, callbackObject, successCallBackMethod);
	}
	
	

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	public static void requestBii(final String url, final BiiRequestBody biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod) {
		// biiRequestBody不能为空
		if (StringUtil.isNullOrEmpty(biiRequest)) {
			LogGloble.w(TAG, "requestBiiWithCallBack -> biiRequestBody is null");
			return;
		}
		LogGloble.i("BiiHttpEngine", "请求URL地址:  " + url);
		LogGloble.i("BiiHttpEngine", "当前请求接口名： " + biiRequest.getMethod());
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		biiRequestBodyList.add(biiRequest); 
		requestBii(url, biiRequestBodyList, callbackObject, successCallBackMethod);
	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 单个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequest 单个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	private static void requestBii(final String url, final BiiRequestBody biiRequest,
			final HttpObserver callbackObject, final String successCallBackMethod, String requestType) {
		// biiRequestBody不能为空
		if (StringUtil.isNullOrEmpty(biiRequest)) {
			LogGloble.w(TAG, "requestBiiWithCallBack -> biiRequestBody is null");
			return;
		}
		LogGloble.i("BiiHttpEngine", "请求URL地址:  " + url);
		LogGloble.i("BiiHttpEngine", "当前请求接口名： " + biiRequest.getMethod());
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		requestBii(url, biiRequestBodyList, callbackObject, successCallBackMethod, requestType);
	}

	public static boolean newUrlFlag = false;

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 多个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequestList 多个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	private static void requestBii(final String url, final List<BiiRequestBody> biiRequestBodyList,
			final HttpObserver callbackObject, final String successCallBackMethod) {

		// biiRequestBody不能为空
		if (StringUtil.isNullOrEmpty(biiRequestBodyList)) {
			LogGloble.w(TAG, "requestBiiWithCallBack -> biiRequestBody is null");
			return;
		}


		// 组装 BiiRequest
		BiiRequest biiRequest = new BiiRequest();
		biiRequest.setHeader(sharedBiiHeader);
		Random random = new Random();
		if (url.contains(BiiApi.FINCADDRESS)) {
			newUrlFlag = true;
		} else {
			newUrlFlag = false;
		}
		// mod by xyl 基金地址 改成老接口规范
		if (BiiApi.BASE_API.equals(Comm.BFWAJAX) && !url.contains(BiiApi.FINCADDRESS)) {// 代表新接口

			if (biiRequestBodyList.size() == 1) {
				BiiRequestBody requestBody = biiRequestBodyList.get(0);
				biiRequest.setMethod(requestBody.getMethod());
				if (requestBody.getParams() != null) {// (请求含有params)将id放到Params里面
					if (requestBody.getConversationId() != null) {
						requestBody.getParams().put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
					}
					if (!requestBody.getParams().containsKey(Comm.ID)) {
						requestBody.getParams().put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
					}
					// 设置参数
					biiRequest.setParams(requestBody.getParams());
				} else {// 请求不包含params
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
					if (requestBody.getConversationId() != null) {
						params.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
					}
					// 设置参数
					biiRequest.setParams(params);
				}
			} else if (biiRequestBodyList.size() > 1) {// 新接口请求多条数据组合
				biiRequest.setMethod("CompositeAjax");
				// 组合请求的params
				Map<String, List<BiiRequest>> maps = new HashMap<String, List<BiiRequest>>();
				List<BiiRequest> request = new ArrayList<BiiRequest>();
				for (int i = 0; i < biiRequestBodyList.size(); i++) {
					// 组装 BiiRequest
					BiiRequest biiRequestitem = new BiiRequest();
//					BiiHeader h = new BiiHeader();
//					h.setAgent("test_");
//					h.setDevice("samsung,GT-S5570,GT-S5570");
					biiRequestitem.setHeader(sharedBiiHeader);
					BiiRequestBody requestBody = biiRequestBodyList.get(i);
					biiRequestitem.setMethod(requestBody.getMethod());
					if (requestBody.getParams() != null) {// (请求含有params)将id放到Params里面
						if (requestBody.getConversationId() != null) {
							requestBody.getParams()
									.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
						}
						requestBody.getParams().put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
						// 设置参数
						biiRequestitem.setParams(requestBody.getParams());
						biiRequestitem.setMethod(requestBody.getMethod());
					} else {// 请求不包含params
						Map<String, Object> params = new HashMap<String, Object>();
						params.put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
						if (requestBody.getConversationId() != null) {
							params.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
						}
						// 设置参数
						biiRequestitem.setParams(params);
					}
					request.add(biiRequestitem);
				}
				maps.put("methods", request);
				biiRequest.setParams(maps);

			}

		} else {// 代表老接口规范
			biiRequest.setRequest(biiRequestBodyList);
		}

		requestBii(url, biiRequest, callbackObject, successCallBackMethod,
				ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK);

	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调</p>
	 * 
	 * 多个BiiRequestBody
	 * 
	 * </p>
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequestList 多个请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	private static void requestBii(final String url, final List<BiiRequestBody> biiRequestBodyList,
			final HttpObserver callbackObject, final String successCallBackMethod, String requestType) {

		// biiRequestBody不能为空
		if (StringUtil.isNullOrEmpty(biiRequestBodyList)) {
			LogGloble.w(TAG, "requestBiiWithCallBack -> biiRequestBody is null");
			return;
		}

		// 组装 BiiRequest
		BiiRequest biiRequest = new BiiRequest();
		biiRequest.setHeader(sharedBiiHeader);

		Random random = new Random();
		if (BiiApi.BASE_API.equals(Comm.BFWAJAX)) {// 代表新接口
			BiiRequestBody requestBody = biiRequestBodyList.get(0);
			biiRequest.setMethod(requestBody.getMethod());
			if (requestBody.getParams() != null) {// 将id放到Params里面
				if (requestBody.getConversationId() != null) {
					requestBody.getParams().put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				requestBody.getParams().put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				// 设置参数
				biiRequest.setParams(requestBody.getParams());
				biiRequest.setMethod(requestBody.getMethod());
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				if (requestBody.getConversationId() != null) {
					params.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				// 设置参数
				biiRequest.setParams(params);
			}

		} else {
			biiRequest.setRequest(biiRequestBodyList);
		}

		requestBii(url, biiRequest, callbackObject, successCallBackMethod,
				ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK, requestType);

	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequest 请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param httpErrorCodeCallBackMethod 失败回调方法名 ( http code error
	 *        500/503/502...)
	 * 
	 */
	private static void requestBii(final String url, final BiiRequest biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod, final String httpErrorCodeCallBackMethod) {
//		LogGloble.i(TAG, "requestBii url: " + url);
//		LogGloble.i(TAG, "requestBii biiRequest: " + JSON.toJSONString(biiRequest));
		final BiiHttpEngine biiHttpEngine = BaseDroidApp.getInstanse().getBiiHttpEngine();
		thread = new CommonRequestThread(biiHttpEngine, httpHandler, url, ConstantGloble.GO_METHOD_POST, biiRequest,
				callbackObject, successCallBackMethod, httpErrorCodeCallBackMethod);
		thread.start();

	}

	/**
	 * 关闭通信
	 */
	public static void stopConnect() {
		try {

			thread.interrupt();

		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
	}

	/**
	 * Bii-Http请求</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请或者 求url
	 * @param biiRequest 请求对象
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param httpErrorCodeCallBackMethod 失败回调方法名 ( http code error
	 *        500/503/502...)
	 * 
	 */
	private static void requestBii(final String url, final BiiRequest biiRequest, final HttpObserver callbackObject,
			final String successCallBackMethod, final String httpErrorCodeCallBackMethod, String requestType) {
		LogGloble.i("BiiHttpEngine", "请求URL地址:  " + url);
		LogGloble.i("BiiHttpEngine", "当前请求接口名： " + biiRequest.getMethod());
		final BiiHttpEngine biiHttpEngine = BaseDroidApp.getInstanse().getBiiHttpEngine();

		new CommonRequestThread(biiHttpEngine, httpHandler, url, ConstantGloble.GO_METHOD_POST, biiRequest,
				callbackObject, successCallBackMethod, httpErrorCodeCallBackMethod, requestType).start();

	}

	/**
	 * 通用Http请求,返回String</p>
	 * 
	 * 暂时关闭,需要时可打开</p>
	 * 
	 * 发起http请求并指定回调</p>
	 * 
	 * @param url http 请或者 求url
	 * @param httpMethod get 或者post请求 GO_METHOD_GET/GO_METHOD_POST
	 * @param params 请求的参数map
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * 
	 */
	public static void requestString(final String url, final String httpMethod, final Map<String, String> params,
			final HttpObserver callbackObject, final String successCallBackMethod) {
		requestString(url, httpMethod, params, callbackObject, successCallBackMethod,
				ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK);
	}

	/**
	 * 通用Http请求,返回String</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请求url
	 * @param httpMethod get 或者post请求 GO_METHOD_GET/GO_METHOD_POST
	 * @param params 请求的参数map
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param httpErrorCodeCallBackMethod 失败回调方法名 ( http code error
	 *        500/503/502...)
	 * 
	 */
	public static void requestString(final String url, final String httpMethod, final Map<String, String> params,
			final HttpObserver callbackObject, final String successCallBackMethod,
			final String httpErrorCodeCallBackMethod) {

		final CommonHttpEngine httpEngine = BaseDroidApp.getInstanse().getCommonHttpEngine();

		new CommonRequestThread(httpEngine, httpHandler, url, httpMethod, params, callbackObject,
				successCallBackMethod, httpErrorCodeCallBackMethod).start();
	}
	
	/**
	 * 通用Http请求,返回String</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请求url
	 * @param httpMethod get 或者post请求 GO_METHOD_GET/GO_METHOD_POST
	 * @param params 请求的参数map
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param httpErrorCodeCallBackMethod 失败回调方法名 ( http code error
	 *        500/503/502...)
	 * 
	 */
	public static void requestString(final String url, final String httpMethod, final Map<String, Object> params,BaseHttpEngine engine,
			final HttpObserver callbackObject, final String successCallBackMethod) {

		new CommonRequestThread(engine, httpHandler, url, httpMethod, params, callbackObject,
				successCallBackMethod, ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK).start();
	}

	/**
	 * 消息推送
	 * 
	 * @param url 请求路径
	 * @param httpMethod 请求方法
	 * @param params 请求参数
	 * @param handler 处理返回数据的Handler
	 * @param pullingTime 轮训时间
	 */
	public static void requestPush(final BiiRequestBody biiRequestBody, Handler handler) {
		BiiPollingEngine biiHttpEngine = new BiiPollingEngine();
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		biiRequestBodyList.add(biiRequestBody);
		// 组装 BiiRequest
		BiiRequest biiRequest = new BiiRequest();
		biiRequest.setHeader(sharedBiiHeader);
		Random random = new Random();
		if (BiiApi.BASE_API.equals(Comm.BFWAJAX)) {// 代表新接口
			BiiRequestBody requestBody = biiRequestBodyList.get(0);
			biiRequest.setMethod(requestBody.getMethod());
			if (requestBody.getParams() != null) {// 将id放到Params里面
				if (requestBody.getConversationId() != null) {
					requestBody.getParams().put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				requestBody.getParams().put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				// 设置参数
				biiRequest.setParams(requestBody.getParams());
				biiRequest.setMethod(requestBody.getMethod());
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				if (requestBody.getConversationId() != null) {
					params.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				// 设置参数
				biiRequest.setParams(params);
			}

		} else {
			biiRequest.setRequest(biiRequestBodyList);
		}

		mPushRequestThread = new PushRequestThread(biiHttpEngine, handler, BiiApi.BASE_PUSH_RUL,
				ConstantGloble.GO_METHOD_POST, biiRequest, null, null, null);
		mPushRequestThread.start();
	}

	/**
	 * 普通轮询请求
	 * 
	 * @param url 请求路径
	 * @param httpMethod 请求方法
	 * @param params 请求参数
	 * @param handler 处理返回数据的Handler 返回的数据格式是String类型的
	 * @param pullingTime 轮训时间 单位为秒 （s）
	 */
	public static void requestPollingComm(final String url, final String httpMethod, final Map<String, String> params,
			Handler handler, int pullingTime) {

		// 返回的是String类型的
		final CommonHttpEngine httpEngine = BaseDroidApp.getInstanse().getCommonHttpEngine();

		new PollingRequestThread(httpEngine, handler, url, httpMethod, params, null, null, null, pullingTime).start();

	}
	
	
	/**
	 * 功能外置轮询请求
	 * 
	 * @param url 请求路径 
	 * @param biiRequestBody 请求参数
	 * @param handler 处理返回数据的Handler
	 * @param pullingTime 轮训时间
	 */
	public static void requestPollingOutlay(final BiiRequestBody biiRequestBody, Handler handler, int pullingTime) {
		requestPollingBase(SystemConfig.Outlay_API_URL, biiRequestBody, handler, pullingTime);
	}

	/**
	 * BII轮询请求
	 * 
	 * @param url 请求路径
	 * @param httpMethod 请求方法
	 * @param params 请求参数
	 * @param handler 处理返回数据的Handler
	 * @param pullingTime 轮训时间
	 */
	public static void requestPollingBii(final BiiRequestBody biiRequestBody, Handler handler, int pullingTime) {
		requestPollingBase(BiiApi.BASE_API_URL, biiRequestBody, handler, pullingTime);
	}
	
	/**
	 * 轮询请求
	 * 
	 * @param url 请求路径
	 * @param httpMethod 请求方法
	 * @param params 请求参数
	 * @param handler 处理返回数据的Handler
	 * @param pullingTime 轮训时间
	 */
	private static void requestPollingBase(String url, final BiiRequestBody biiRequestBody, Handler handler, int pullingTime) {

		final BiiPollingEngine biiHttpEngine = BaseDroidApp.getInstanse().getBiiPollingHttpEngine();
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		biiRequestBodyList.add(biiRequestBody);
		// 组装 BiiRequest
		BiiRequest biiRequest = new BiiRequest();
		biiRequest.setHeader(sharedBiiHeader);
		Random random = new Random();
		if (BiiApi.BASE_API.equals(Comm.BFWAJAX)) {// 代表新接口
			BiiRequestBody requestBody = biiRequestBodyList.get(0);
			biiRequest.setMethod(requestBody.getMethod());
			if (requestBody.getParams() != null) {// 将id放到Params里面
				if (requestBody.getConversationId() != null) {
					requestBody.getParams().put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				requestBody.getParams().put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				// 设置参数
				biiRequest.setParams(requestBody.getParams());
				biiRequest.setMethod(requestBody.getMethod());
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ID, String.valueOf(random.nextInt(1000000000)));
				if (requestBody.getConversationId() != null) {
					params.put(ConstantGloble.CONVERSATION_ID, requestBody.getConversationId());
				}
				// 设置参数
				biiRequest.setParams(params);
			}

		} else {
			biiRequest.setRequest(biiRequestBodyList);
		}

		PollingRequestThread.pollingFlag = true;
		mPollingRequestThread = new PollingRequestThread(biiHttpEngine, handler, url,
				ConstantGloble.GO_METHOD_POST, biiRequest, null, null, ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK,
				pullingTime);

		mPollingRequestThread.start();
	}

	/**
	 * Http请求文件</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请求url
	 * @param httpMethod get 或者post请求 GO_METHOD_GET/GO_METHOD_POST
	 * @param params 请求的参数map
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param httpErrorCodeCallBackMethod 失败回调方法名 ( http code error
	 *        500/503/502...)
	 * 
	 */
	public static void requestFile(final String url, final String httpMethod, final Map<String, String> params,
			final HttpObserver callbackObject, final String successCallBackMethod, final String localFilePath) {

		final FileHttpEngine fileEngine = BaseDroidApp.getInstanse().getFileHttpEngine();

		fileEngine.setFilePath(localFilePath);

		new CommonRequestThread(fileEngine, httpHandler, url, httpMethod, params, callbackObject,
				successCallBackMethod, ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK).start();
	}

	/**
	 * Http请求图片</p>
	 * 
	 * http请求并指定回调
	 * 
	 * @param url http 请求url
	 * @param httpMethod get 或者post请求 GO_METHOD_GET/GO_METHOD_POST
	 * @param params 请求的参数map
	 * @param callbackObject 回调目标对象
	 * @param successCallBackMethod 成功回调方法名 (http code = 200)
	 * @param dialogFlag 是否弹出通信提示框的标识 true为弹出 false 表示不弹出
	 * 
	 */
	public static void requestImage(final String url, final String httpMethod, final Map<String, String> params,
			final HttpObserver callbackObject, final String successCallBackMethod) {

		final ImageFileHttpEngine imageHttpEngine = BaseDroidApp.getInstanse().getImageFileHttpEngine();

		new CommonRequestThread(imageHttpEngine, httpHandler, url, httpMethod, params, callbackObject,
				successCallBackMethod, ConstantGloble.HTTP_ERROR_CODE_DEFAULT_CALLBACK).start();

	}

	/**
	 * http请求返回的Handler
	 * 
	 */
	private static Handler httpHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_CODE);

			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);

			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_OBJECT);

			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_CALLBACK_METHOD);

			// 请求标识
			String requestType = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_REQUEST_TYPE);
			// 代表返回数据空
			if (((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESPOSE_NULL) != null) {
				BaseHttpEngine.dissMissProgressDialog();
				// 统一回调处理返回数据为空的方法
				callbackObject.commonHttpResponseNullCallBack((String) ((Map<String, Object>) msg.obj)
						.get(ConstantGloble.BII_REQUETMETHOD));
				return;

			}
			switch (msg.what) {

			// 正常http请求数据返回
			case ConstantGloble.HTTP_STAGE_CONTENT:

				/**
				 * 执行全局前拦截器
				 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackPre(resultObj)) {
					break;
				}

				/**
				 * 执行callbackObject回调前拦截器
				 */
				if (callbackObject.httpRequestCallBackPre(resultObj)) {
					break;
				}

				Method callbackMethod = null;

				try {
					// 回调
					if (requestType != null) {// 如果传递了标识过来就调用含有标识的回调方法
						callbackMethod = callbackObject.getClass()
								.getMethod(callBackMethod, Object.class, String.class);
						callbackMethod.invoke(callbackObject, resultObj, requestType);
					} else {// 代表没有传递请求的标识
						callbackMethod = callbackObject.getClass().getMethod(callBackMethod, Object.class);
						callbackMethod.invoke(callbackObject, resultObj);
					}
				} catch (Exception e) {
					LogGloble.e(TAG, "SecurityException ", e);
				}

				/**
				 * 执行callbackObject回调后拦截器
				 */
				if (callbackObject.httpRequestCallBackAfter(resultObj)) {
					break;
				}

				/**
				 * 执行全局后拦截器
				 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(resultObj)) {
					break;
				}

				break;

			// 请求失败错误情况处理
			case ConstantGloble.HTTP_STAGE_CODE:

				BaseHttpEngine.dissMissProgressDialog();
				if (((Map<String, Object>) msg.obj).get(ConstantGloble.BII_REQUESTCODEERROR) != null) {
					if (ConstantGloble.BII_IMAGE_CODE_ERROR == (Integer) ((Map<String, Object>) msg.obj)
							.get(ConstantGloble.BII_REQUESTCODEERROR)) {// 代表是验证码请求失败
						((Map<String, Object>) msg.obj).put(ConstantGloble.BII_REQUETMETHOD,
								ConstantGloble.BII_IMAGECODE_METHOD);
					}

				}

				/**
				 * 执行code error 全局前拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject error code 回调前拦截器
				 */
				if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}

				Method httpCodeCallbackMethod = null;
				LogGloble.d("net", "---callBackMethod---  " + callBackMethod);
				try {
					// 回调
					httpCodeCallbackMethod = callbackObject.getClass().getMethod(callBackMethod, String.class);

					httpCodeCallbackMethod.invoke(callbackObject,
							(String) ((Map<String, Object>) msg.obj).get(ConstantGloble.BII_REQUETMETHOD));
				} catch (Exception e) {
					LogGloble.e(TAG, "BIIException ", e);
				}

				/**
				 * 执行code error 全局后拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject code error 后拦截器
				 */
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				break;

			// 消息轮询正常数据返回的处理
			case ConstantGloble.HTTP_STAGE_POLLING_CONTENT:
				// TODO
				// added by nl. 龙虎榜数据 2012/11/2 以下------
				HashMap<String, Object> pollingDataMap = BaseDroidApp.getInstanse().getPollingDataMap();

				pollingDataMap = (HashMap<String, Object>) msg.obj;
				ArrayList<Map<String, String>> poolingMessage = (ArrayList<Map<String, String>>) pollingDataMap
						.get("resInfo");

				if (!StringUtil.isNullOrEmpty(poolingMessage)) {

					BaseDroidApp.getInstanse().getNotification().icon = R.drawable.ic_launcher;

					BaseDroidApp.getInstanse().getNotification().flags = Notification.FLAG_AUTO_CANCEL;

					BaseDroidApp.getInstanse().getNotification().defaults = Notification.DEFAULT_SOUND;

					String title = "您有" + poolingMessage.size() + "转账汇款消息";
					/** 单击通知后的Intent，此例子单击后还是在当前页面 */
					/** 执行通知 */
					Intent intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);

					PendingIntent pdIntent = PendingIntent.getActivity(BaseDroidApp.getInstanse().getCurrentAct(), 0,
							intent, 0);
					/** 设置通知消息 */
					BaseDroidApp
							.getInstanse()
							.getNotification()
							.setLatestEventInfo(BaseDroidApp.getInstanse().getCurrentAct(), "FopAndroid", title,
									pdIntent);
					BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
					if(activity != null)
						activity.getNotificationManager()
							.notify(1, BaseDroidApp.getInstanse().getNotification());
				}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 关闭轮询线程
	 */
	public static void stopPolling() {
		PollingRequestThread.pollingFlag = false;
		if(mPollingRequestThread  != null)
			mPollingRequestThread.interrupt();
	}

	public static BiiHeader getSharedBiiHeader() {
		return sharedBiiHeader;
	}
}
