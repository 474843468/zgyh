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

import com.chinamworld.bocmbci.biz.push.FLogGloble;
import com.chinamworld.bocmbci.biz.push.PushCount;
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
/**
 * 消息推送P402
 */
public class PushRequestThread extends BaseRequestThread {

	private static final String TAG = PushRequestThread.class.getSimpleName();

	public PushRequestThread(BaseHttpEngine engine, Handler handler, String url, String httpMethod, Object params,
			HttpObserver callbackObject, String successCallBackMethod, String httpErrorCodeCallBackMethod) {
		super(engine, handler, url, httpMethod, params, callbackObject, successCallBackMethod,
				httpErrorCodeCallBackMethod);
	}

	@Override
	public void run() {
		try {
			HashMap<String, Object> result = getResult();
			if (result == null) {
				LogGloble.i("httprequest", "cancled");
				return;
			}

			if (result.get(ConstantGloble.HTTP_RESPOSE_CONTENT) != null
					&& ((Integer) result.get(ConstantGloble.HTTP_RESPOSE_CODE)) == 200) {
				dealSuccessResult(result);
			} else {
				dealFailedResult(result);
			}
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}

	}

	/**
	 * 轮询不处理错误情况
	 */
	@Override
	protected void dealFailedResult(HashMap<String, Object> result) {
		// Do nothing
		FLogGloble.i(TAG, "dealFailedResult:" + PushCount.getPushCountInfo());
	}

}
