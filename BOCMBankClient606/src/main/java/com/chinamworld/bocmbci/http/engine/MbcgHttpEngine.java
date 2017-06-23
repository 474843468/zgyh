/**
 * 文件名	：CommonHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * MbcgHttpEngine</p>
 * 
 * MBCManager</p>
 * 
 * 对应:CommonRequestThread</p>
 * 
 * 返回数据类型:String</p>
 * 
 * @author wez
 * 
 */
public class MbcgHttpEngine extends CommonHttpEngine {

	private static final String TAG = "MbcgHttpEngine"; // add by wez

	@Override
	protected void httpPostHeaderSet() {
		httpPost.setHeader("Content-Type", "json");
	}

	// 2011.01.06
	@SuppressWarnings("unchecked")
	protected void httpPostEntitySet(Object params) {
		if (params == null) {
			return;
		}
		try {
			// 转换请求参数
			StringEntity stringEntiry = new StringEntity(MyJSON.toJSONString(params), ConstantGloble.DEFAULT_ENCODE);
			stringEntiry.setContentEncoding(ConstantGloble.DEFAULT_ENCODE);
			httpPost.setEntity(stringEntiry);
		} catch (ClassCastException e) {
			LogGloble.e(TAG, "ClassCastException", e);
		} catch (UnsupportedEncodingException e) {
			LogGloble.e(TAG, "UnsupportedEncodingException", e);
		}

	}
}
