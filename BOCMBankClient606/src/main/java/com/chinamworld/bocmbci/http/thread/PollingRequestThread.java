/**
 * 文件名	：PollingRequestThread.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.thread;

import java.util.HashMap;

import android.os.Handler;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * PollingRequestThread</p>
 * 
 * 对应:PollingFileHttpEngine</p>
 * 
 * @author wez
 * 
 */
public class PollingRequestThread extends BaseRequestThread {
	// 轮寻标示
	public static boolean pollingFlag = false;
	private int mPullingTime = 60 * 1000;

	public PollingRequestThread(BaseHttpEngine engine, Handler handler,
			String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod,
			String httpErrorCodeCallBackMethod, int pullingTime) {
		super(engine, handler, url, httpMethod, params, callbackObject,
				successCallBackMethod, httpErrorCodeCallBackMethod);
		this.mPullingTime = pullingTime * 1000;
	}

	@Override
	public void run() {
		while (pollingFlag) {
			try {
				HashMap<String, Object> result = getResult();
				if (result == null) {
					LogGloble.i("httprequest", "cancled");
					return;
				}

				if (result.get(ConstantGloble.HTTP_RESPOSE_CONTENT) != null
						&& ((Integer) result
								.get(ConstantGloble.HTTP_RESPOSE_CODE)) == 200) {
					dealSuccessResult(result);
				} else {
					dealFailedResult(result);
				}
				sleep(mPullingTime);
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}

		}

	}

	/**
	 * 轮询不处理错误情况
	 */
	@Override
	protected void dealFailedResult(HashMap<String, Object> result) {
		// Do nothing
	}

}
