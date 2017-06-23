/**
 * 文件名	：CommonHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * CommonHttpEngine</p>
 * 
 * 公共http引擎</p>
 * 
 * 对应:CommonRequestThread</p>
 * 
 * 返回数据类型:String</p>
 * 
 * @author wez
 * 
 */
public class CommonHttpEngine extends BaseHttpEngine {

	private static final String TAG = "CommonHttpEngine"; // add by wez
	
	protected boolean hasIMEI = true;// 2011.01.06
	public void setHasIMEI(boolean hasIMEI){
		this.hasIMEI = hasIMEI;
	}
	/**
	 * 拦截处理params
	 * 
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object paramsPre(Object params) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		if(hasIMEI)
			((HashMap<String, String>)params).put(ConstantGloble.IMEI, BaseDroidApp.getInstanse().getImei()); // 上送IMEI
		
		return params;
	}
	
	
	/**
	 * 
	 * 将请求参数map转化为 List<NameValuePair>
	 * 
	 * @author wez
	 * @param parameters
	 * @return List<NameValuePair>
	 */
	@SuppressWarnings("unchecked")
	protected Object convertParameters(Object parameters) {

		List<NameValuePair> result = new ArrayList<NameValuePair>();

		if (!StringUtil.isNullOrEmpty(parameters)) {
			Map<String, String> paramsMap = ((Map<String, String>) parameters);
			if (!StringUtil.isNullOrEmpty(parameters)) {
				for (Entry<String, String> entry : paramsMap.entrySet()) {
					result.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * 设置 httpPost Entity
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void httpPostEntitySet(Object params) {
		if (params == null) {
			return;
		}

		try {
			// 转换请求参数
			List<NameValuePair> listParams = (List<NameValuePair>) convertParameters(params);

			httpPost.setEntity(new UrlEncodedFormEntity(
					(List<NameValuePair>) listParams,
					ConstantGloble.DEFAULT_ENCODE));

		} catch (ClassCastException e) {
			LogGloble.e(TAG, "ClassCastException", e);
		} catch (UnsupportedEncodingException e) {
			LogGloble.e(TAG, "UnsupportedEncodingException", e);
		}

	}

	/**
	 * 
	 * 根据url和参数键值对生成HttpGet
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	protected HttpGet gennerateHttpGet(String url, Object params) {
		// 转换请求参数
		@SuppressWarnings("unchecked")
		List<NameValuePair> listParams = (List<NameValuePair>) convertParameters(params);
		// 添加http参数到url
		String paramUrl = addListParamsToHttpUrl(url, listParams);
		// 实例化一个HttpGet对象的实例
		httpGet = new HttpGet(paramUrl);
		
		String host = httpGet.getURI().getHost();
		String path = httpGet.getURI().getPath();
		
		/**
		 * 处理cookie key
		 */
		dealWithCookieKey(host,path);
		
		return httpGet;
	}


	/**
	 * 转换Response</p>
	 * 
	 * 得到的是String
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected void convertResponse(HttpResponse response,
			HashMap<String, Object> result) throws Exception {
		// 获取响应的实体流
		InputStream is = response.getEntity().getContent();
		try {
			String stringContent = inputStream2String(is);
			LogGloble.i(TAG, "stringContent:" + stringContent);
				Map<String , Object> responceResult = MyJSON.parseObject(stringContent,
						Map.class);
				Object obj = responceResult.get("_isException_");
				responceResult.put(ConstantGloble.HTTP_RESPOSE_CODE, "200");
				if ("true".equals(obj)) {
					result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, "null");
				}else{
					result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, responceResult);
				}
		}catch(Exception e){
			result.put(ConstantGloble.HTTP_RESPOSE_CODE, 200);
			result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, "null");
		}finally {
			if (is != null) {
				is.close();
			}
		}
	}

	@Override
	protected void httpPostHeaderSet() {
		httpPost.setHeader(ConstantGloble.USER_AGENT, getUserAgent());
		String cookie = BaseDroidApp.getInstanse().getAllCookie(httpPost.getURI().getHost());
		httpPost.setHeader(ConstantGloble.COOKIE, cookie);
//		String cookie = BaseDroidApp.getInstanse().getCookie(cookieKey);
//		if (!StringUtil.isNullOrEmpty(cookie)) {
//			LogGloble.i(ConstantGloble.COOKIE, ConstantGloble.COOKIE + ": "
//					+ cookie);
//			httpPost.setHeader(ConstantGloble.COOKIE, cookie);
//		}
	}


	@Override
	//add by liwei for http1.1 消息推送主机自动关闭链接
	protected void httpPostHeaderSetAutoClose() {
		// TODO Auto-generated method stub
		
	}



}
