/**
 * 文件名	：CommonRequestThread.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.thread;

import android.os.Handler;

import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * CommonRequestThread</p>
 * 
 * 对应:CommonHttpEngine</p>
 * 
 * @author wez
 * 
 */
public class CommonRequestThread extends BaseRequestThread{

	
	
	

	public CommonRequestThread(BaseHttpEngine engine, Handler handler,
			String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod,
			String httpErrorCodeCallBackMethod) {
		super(engine, handler, url, httpMethod, params, callbackObject,
				successCallBackMethod, httpErrorCodeCallBackMethod);
	}
	public CommonRequestThread(BaseHttpEngine engine, Handler handler,
			String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod,
			String httpErrorCodeCallBackMethod,String requestType) {
		super(engine, handler, url, httpMethod, params, callbackObject,
				successCallBackMethod, httpErrorCodeCallBackMethod,requestType);
	}


}
