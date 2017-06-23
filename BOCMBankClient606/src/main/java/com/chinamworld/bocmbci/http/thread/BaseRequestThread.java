/**
 * 文件名	：BaseRequestThread.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.thread;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequest;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * BaseRequestThread</p>
 * 
 * 基础http请求线程</p>
 * 
 * 
 * @author wez
 * 
 */
public abstract class BaseRequestThread extends Thread {

	protected BaseHttpEngine engine;

	protected Handler handler;

	protected String url;

	protected String httpMethod;

	protected Object params;

	protected HttpObserver callbackObject;

	protected String successCallBackMethod;

	protected String httpErrorCodeCallBackMethod;
	/*** 请求的标识 */
	protected String requestType;
	/** 是否正在通信 */
	public static boolean isConnecting = false;
	public static BaseRequestThread currentThread;

	// /** 是否弹出通信提示框 */
	// protected boolean dialogFlag;

	public BaseRequestThread(BaseHttpEngine engine, Handler handler,
			String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod,
			String httpErrorCodeCallBackMethod) {

		super();
		currentThread = this;
		this.engine = engine;
		this.handler = handler;
		this.url = url;
		this.httpMethod = httpMethod;
		this.params = params;
		this.callbackObject = callbackObject;
		this.successCallBackMethod = successCallBackMethod;
		this.httpErrorCodeCallBackMethod = httpErrorCodeCallBackMethod;
		if (params instanceof BiiRequest) {
			if (LocalData.noDissMissMethod.contains(((BiiRequest) params)
					.getMethod())) {
				BaseHttpEngine.dissmissCloseOfProgressDialog();
			}
		}
		// this.dialogFlag = dialogFlag;
	}

	public BaseRequestThread(BaseHttpEngine engine, Handler handler,
			String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod,
			String httpErrorCodeCallBackMethod, String requestType) {
		super();
		currentThread = this;
		this.engine = engine;
		this.handler = handler;
		this.url = url;
		this.httpMethod = httpMethod;
		this.params = params;
		this.callbackObject = callbackObject;
		this.successCallBackMethod = successCallBackMethod;
		this.httpErrorCodeCallBackMethod = httpErrorCodeCallBackMethod;
		// this.dialogFlag = dialogFlag;
		this.requestType = requestType;
	}

	/**
	 * 公共run方法
	 */
	@Override
	public void run() {
		try {
			isConnecting = true;
			HashMap<String, Object> result = getResult();

			// LogGloble.d("info", "getResule------"+isConnecting);
			if (result == null) {
				LogGloble.i("httprequest", "cancled");
				return;
			}
			if (!interrupted()) {// 没有被打断
				if (result.get(ConstantGloble.HTTP_RESPOSE_CONTENT) != null
						&& ((Integer) result
								.get(ConstantGloble.HTTP_RESPOSE_CODE)) == 200) {
					dealSuccessResult(result);
				} else {
					dealFailedResult(result);
				}
			}
			isConnecting = false;
		} catch (Exception e) {
			LogGloble.d("net", "eeeeeeeeeeeeeeeeeeeeeeee   "+e);
			LogGloble.exceptionPrint(e);
			logError(e);
		}

	}

	/**
	 * 记录异常
	 * 
	 * @param e
	 */
	private void logError(Exception e) {
		LogGloble.w(this.getClass().getSimpleName(), engine.getClass()
				.getSimpleName() + " request error", e);
	}

	/**
	 * 处理公共数据
	 * 
	 * @param result
	 */
	private Map<String, Object> dealCommonResult(HashMap<String, Object> result) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (params instanceof BiiRequest) {//传递请求方法到处理结果的地方
			resultMap.put(ConstantGloble.BII_REQUETMETHOD,
					((BiiRequest) params).getMethod());
		}
		Object resultData = result.get(ConstantGloble.HTTP_RESPOSE_CONTENT);
		Integer httpCode = (Integer) result
				.get(ConstantGloble.HTTP_RESPOSE_CODE);
		resultMap
				.put(ConstantGloble.HTTP_RESULT_CODE, String.valueOf(httpCode));// http状态码
		resultMap.put(ConstantGloble.HTTP_RESULT_DATA, resultData);// 结果数据
		resultMap.put(ConstantGloble.HTTP_CALLBACK_OBJECT, callbackObject);// 回调对象

		// 代表返回数据空
		if (result.get(ConstantGloble.HTTP_RESPOSE_NULL) != null) {
			resultMap.put(ConstantGloble.HTTP_RESPOSE_NULL, true);
		}
		return resultMap;
	}

	/**
	 * 处理通信失败数据
	 * 
	 * @param result
	 */
	protected void dealFailedResult(HashMap<String, Object> result) {
		Map<String, Object> resultMap = dealCommonResult(result);
		if (params instanceof BiiRequest) {
			resultMap.put(ConstantGloble.BII_REQUETMETHOD,
					((BiiRequest) params).getMethod());
		}else if(params instanceof Map){
			Map requestMap = (Map)params ;
			if(requestMap.containsKey("method")){
				resultMap.put(ConstantGloble.BII_REQUETMETHOD,
						(requestMap).get("method"));
			}
		}
		resultMap.put(ConstantGloble.HTTP_CALLBACK_METHOD,
				httpErrorCodeCallBackMethod);// 回调方法

		// 代表返回数据空
		if (result.get(ConstantGloble.HTTP_RESPOSE_NULL) != null) {
			resultMap.put(ConstantGloble.HTTP_RESPOSE_NULL, true);
		}
		Message msg = handler.obtainMessage(ConstantGloble.HTTP_STAGE_CODE,
				resultMap);
		if(Comm.AQUIRE_IMAGE_CODE.equals(url)){
			resultMap.put(ConstantGloble.BII_REQUESTCODEERROR, ConstantGloble.BII_IMAGE_CODE_ERROR);
		}
		handler.sendMessage(msg);

	}

	/**
	 * 处理成功数据
	 * 
	 * @param result
	 */
	protected void dealSuccessResult(HashMap<String, Object> result) {
		Map<String, Object> resultMap = dealCommonResult(result);
		resultMap.put(ConstantGloble.HTTP_CALLBACK_METHOD,
				successCallBackMethod);// 回调方法
		if (requestType != null) {// 把请求的标识添加进去
			resultMap.put(ConstantGloble.HTTP_REQUEST_TYPE, requestType);
		}
		Message msg = handler.obtainMessage(ConstantGloble.HTTP_STAGE_CONTENT,
				resultMap);
		// msg.sendToTarget();
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * 取得数据
	 * 
	 */
	protected HashMap<String, Object> getResult() {
		// 发送http请求得到请求的结果
		HashMap<String, Object> result = engine.httpSend(url, httpMethod,
				BaseDroidApp.getContext().getApplicationContext(), params);
		return result;
	}

	public BaseHttpEngine getEngine() {
		return engine;
	}

	public void setEngine(BaseHttpEngine engine) {
		this.engine = engine;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
