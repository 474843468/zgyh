/**
 * 文件名 ：BiiHttpEngine.java
 * 
 * 创建日期 ：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通
 * 
 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreProtocolPNames;

import android.content.Context;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequest;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SDcardLogUtil;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * BiiHttpEngine</p>
 * 
 * Bii 专用 http引擎</p>
 * 
 * 对应:CommonRequestThread</p>
 * 
 * 返回数据类型:BiiResponse</p>
 * 
 * @author wez
 * 
 */
public class BiiHttpEngine extends BaseHttpEngine {

	public static String cookieCurrent = "";

	private static final String TAG = "BiiHttpEngine"; // add by wez 2011.01.06
	private static String requestString;

	/**
	 * do get
	 * 
	 * @param url
	 * @param context
	 * @param params
	 * @param response
	 * @throws Exception
	 */
	@Override
	protected HttpResponse doGet(String url, Context context, Object params)
			throws Exception {
		return null;
	}

	/**
	 * 
	 * 设置 httpPost Entity
	 * 
	 */
	protected void httpPostEntitySet(Object params) {
		if (params == null) {
			return;
		}

		try {
			String jsonString = MyJSON.toJSONString(params);

			jsonString = removeStr(jsonString, "toCspParamsMap");
			LogGloble.w(TAG, "上送数据  ： " + jsonString);
			requestString = jsonString;

			requestString = jsonString;
			// TODO 这个地方的_bfwajax.do 需要提到常量文件 note by wjp
			if (BiiApi.BASE_API.equals("_bfwajax.do")
					&& !HttpManager.newUrlFlag) {// 代表新接口)
				jsonString = "json=" + URLEncoder.encode(jsonString, "UTF-8");
			}

			StringEntity entity = new StringEntity((String) jsonString,
					ConstantGloble.DEFAULT_ENCODE);

			entity.setContentEncoding(ConstantGloble.DEFAULT_ENCODE);
			httpPost.setEntity(entity);
			// 如果是wms基金接口,发送ajax请求AG", msg)
			if (((BiiRequest) params).getParams() == null) {// 代表新接口规范
				if (((BiiRequest) params).getRequest().get(0).getParams()
						.containsKey(BiiApi.TAG_WMS)) {
					httpPost.setHeader("x-requested-with", "XMLHttpRequest");
				}
			}

		} catch (UnsupportedEncodingException e) {
			LogGloble.w(TAG, "UnsupportedEncodingException", e);
		}

	}

	/** 民生解析中，发送csp数据是拼接的字符串。由于Json格式化后导致数据不在是完整的符合条件的Json格式，因此需要重新处理数据 */
	private String removeStr(String jsonStr, String key) {
		if (BTCCMWApplication.flag == false)
			return jsonStr;
		int nIndex = jsonStr.indexOf("\"" + key + "\":\"{");
		if (nIndex < 0)
			return jsonStr;
		String tmp = jsonStr.substring(nIndex);
		nIndex = tmp.indexOf("}\"");
		tmp = tmp.substring(0, nIndex + 2);
		String tmp1 = tmp.substring(0, nIndex + 1);
		jsonStr = jsonStr.replace(tmp, tmp1);
		jsonStr = jsonStr.replace("\"" + key + "\":\"{", "\"" + key + "\":{");

		jsonStr = jsonStr.replace("\\\"", "\"");
		jsonStr = jsonStr.replace("\"[", "[").replace("]\"", "]");

		return jsonStr;
	}
	/**
	 * 转换Response</p>
	 * 
	 * 得到的是BiiResponse
	 * 
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected void convertResponse(HttpResponse response,
			HashMap<String, Object> result) throws Exception {
		// 获取响应的实体流
		InputStream is = response.getEntity().getContent();
		try {
			String stringContent = inputStream2String(is);
			// String stringContent =
			// EntityUtils.toString(response.getEntity());
			LogGloble.w(TAG, "返回数据：" + stringContent);
			if (BTCCMWApplication.flag) {
				BaseHttpEngine.dissMissProgressDialog();
				Map<String, Object> resultmap = MyJSON.parseObject(
						stringContent.replace("_isException_", "biiexception"),
						HashMap.class);
				BTCCMWApplication.resultmap = resultmap;
				BTCCMWApplication.flag = false;
			}

			if(BTCCMWApplication.flag){
				BaseHttpEngine.dissMissProgressDialog();
				Map<String, Object> resultmap  = MyJSON.parseObject(stringContent.replace("_isException_", "biiexception"), HashMap.class);
				BTCCMWApplication.resultmap = resultmap;
				BTCCMWApplication.flag = false;
			}
			
			// LogGloble.i(TAG, "response1 === " +
			// stringContent.substring(0,stringContent.length()/2));
			// LogGloble.i(TAG, "response2 === " +
			// stringContent.substring(stringContent.length()/2,stringContent.length()-1));
			if ("".equals(stringContent) || null == stringContent) {
				result.put(ConstantGloble.HTTP_RESPOSE_NULL, true);
			}
			// TODO 将请求数据保存写入到SD卡 上线时注意关闭
			// if (ConstantGloble.IS_WRITE_LOG_FILE) {
			// SDcardLogUtil.processException(stringContent, (String)
			// result.get(ConstantGloble.BII_REQUETMETHOD));
			// }
			if (SystemConfig.WRITETOFILE) {
				// SDcardLogUtil.processException(stringContent, (String)
				// result.get(ConstantGloble.BII_REQUETMETHOD));
				SDcardLogUtil.logWriteToFile(requestString, "request"
						+ (String) result.get(ConstantGloble.BII_REQUETMETHOD),
						"BIILog");
				SDcardLogUtil.logWriteToFile(stringContent, "response"
						+ (String) result.get(ConstantGloble.BII_REQUETMETHOD),
						"BIILog");
			}
			// System.out.println(stringContent);
			// 转换为BiiResponse
			BiiResponse biiResponse = null;
			if (StringUtil.isNullOrEmpty(stringContent)) {
				biiResponse = new BiiResponse();
			} else {
				biiResponse = MyJSON.parseObject(
						stringContent.replace("_isException_", "biiexception"),
						BiiResponse.class);

				if (biiResponse.getResponse() != null
						&& biiResponse.getResponse().size() > 0) {
					biiResponse
							.getResponse()
							.get(0)
							.setMethod(
									(String) result
											.get(ConstantGloble.BII_REQUETMETHOD));

				}
				if (stringContent.contains("_isException_")) {// 代表新接口
					List<BiiResponseBody> bodys = new ArrayList<BiiResponseBody>();
					BiiResponseBody body = new BiiResponseBody();
					// 传递requestMethod
					body.setMethod((String) result
							.get(ConstantGloble.BII_REQUETMETHOD));
					body.setResult(biiResponse.getResult());
					if (biiResponse.isBiiexception()) {// 有异常
						// code不该为null如果为null设置为空字符串.判断服务器错误码是否为null可以通过biiResponse.getCode()
						String errorCode = biiResponse.getCode() != null ? biiResponse.getCode() : "";
						/**T53添加硬件绑定+手机交易码后，需要对此错误码进行拦截，并直接引导用户进入绑定页面*/
						if("SafetyCombin.noSafetyCombins".equals(errorCode) && body.getMethod().equals(Acc.ACC_GETSECURITYFACTOR_API)){
							/**
							 * 查询版客户类型 :1 查询版老客户;2 查询版新客户（非电子卡签约）;3
							 * 查询版新客户（电子卡签约）
							 */
							Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
									.getInstanse().getBizDataMap()
									.get(ConstantGloble.BIZ_LOGIN_DATA);
							if (!resultMap.isEmpty()) {
								String qryCustType = (String) resultMap
										.get("qryCustType");
								// 是否需要翻译
//							if ( BaseDroidApp.getInstanse().getIsTransNoSafetyCombinMessage() || !(  "1".equals(qryCustType)|| "2".equals(qryCustType) || "3".equals(qryCustType)*/))
								if ( BaseDroidApp.getInstanse()
										.getIsTransNoSafetyCombinMessage() || !("2".equals(qryCustType) && "10".equals(BaseDroidApp.getInstanse().getSegmentInfo()))) {
									BaseDroidApp.getInstanse().setNoSafetyCombinsMsg(biiResponse.getMessage());
									
									body.setStatus(ConstantGloble.STATUS_SUCCESS);
									body.setResult(null);
								}
							}

						}
						else {
							body.setStatus(ConstantGloble.STATUS_FAIL);
						}

						BiiError error = new BiiError();
						error.setCode(errorCode);
						error.setMessage(biiResponse.getMessage());
						error.setType(biiResponse.getType());
						body.setError(error);

					} else {// 没有异常
						body.setStatus(ConstantGloble.STATUS_SUCCESS);

					}
					bodys.add(body);
					biiResponse.setResponse(bodys);
				}
			}
			result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, biiResponse);
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		} finally {
			BaseDroidApp.getInstanse().setIsTransNoSafetyCombinMessage(false);
			if (is != null) {
				is.close();
			}
		}
	}

	@Override
	protected void httpPostHeaderSet() {
		// USER_AGENT
		httpPost.setHeader(ConstantGloble.USER_AGENT, getUserAgent());

		String[] dns = httpPost.getURI().toString().split("//");
		dns = dns[1].split("/");
		String url = dns[0];
		// COOKIE
		String cookie = BaseDroidApp.getInstanse().getAllCookie(url/*
																	 * httpPost.
																	 * getURI
																	 * ().
																	 * toString
																	 * (
																	 * ).getHost
																	 * ()
																	 */);
		httpPost.setHeader(ConstantGloble.COOKIE, cookie);
		// String cookie = BaseDroidApp.getInstanse().getCookie(cookieKey);
		// if (!StringUtil.isNullOrEmpty(cookie)) {
		if (cookie != null && cookie.length() > 0
				&& SystemConfig.BASE_HTTP_URL.contains(url))
			cookieCurrent = cookie;
		// LogGloble.i(ConstantGloble.COOKIE, ConstantGloble.COOKIE + ": " +
		// cookie);
		// httpPost.setHeader(ConstantGloble.COOKIE, cookie);
		// }

		// add by wez 20130320 Bii要求的head
		if (BiiApi.BASE_API.equals(Comm.BFWAJAX) && !HttpManager.newUrlFlag) {// 代表新接口
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
		} else {
			httpPost.setHeader("Content-Type", "text/json");
		}
		httpPost.setHeader("bfw-ctrl", "json");
		httpPost.setHeader("Accept-Language", "zh-cn");// TODO 需要确认是否固定值
	}

	@Override
	// add by liwei for http1.1 消息推送主机自动关闭链接
	protected void httpPostHeaderSetAutoClose() {
		httpPost.setHeader("connection", "close"); //
		httpPost.getParams().setBooleanParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false); // 禁用expect:100-Continue属性
	}

	@Override
	protected HttpGet gennerateHttpGet(String url, Object params) {
		return null;
	}

	@Override
	protected Object convertParameters(Object parameters) {
		return null;
	}



}
