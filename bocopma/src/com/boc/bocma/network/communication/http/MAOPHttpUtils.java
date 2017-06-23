package com.boc.bocma.network.communication.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.MAHttpContext;
import com.boc.bocma.tools.LogUtil;
//import com.boc.bocop.sdk.api.bean.oauth.ContainerInfo;

public class MAOPHttpUtils {
	// 请求列表
	private static List<HttpRequestBase> requests = new ArrayList<HttpRequestBase>();
	private static DefaultHttpClient httpClient;
	// 请求是否可以中断
	private static boolean isCallCancelAllRequest;
	
    public static String getStringFromWeb(String url, String method, Map<String, String> head, String body) throws IOException {
    	getHttpClient();
        HttpRequestBase httpRequest = new HttpPost(url);
        if (method.equals(HttpPut.METHOD_NAME)) {
            httpRequest = new HttpPut(url);
        } else if (method.equals(HttpGet.METHOD_NAME)) {
            httpRequest = new HttpGet(url);
        }
        httpRequest.addHeader("Connection", "Keep-Alive");
        httpRequest.addHeader("Content-Type", "application/json");
        httpRequest.addHeader("Accept", "application/json");
        for (String key : head.keySet()) {
            httpRequest.addHeader(key, head.get(key));
        }

        if (!method.equals(HttpGet.METHOD_NAME)) {
            StringEntity entity = new StringEntity(body, HTTP.UTF_8);
            ((HttpEntityEnclosingRequestBase)httpRequest).setEntity(entity);
        }
        requests.add(httpRequest);
        logHeader(httpRequest);
        
      
        // request with HttpClient
        HttpResponse response = httpClient.execute(httpRequest);
        // read
        String jsonResponse = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            jsonResponse = buildResponseFromHttpResponse(response);
        } else {
            //TODO:处理网络异常
            LogUtil.e("网络异常");
            jsonResponse = buildResponseFromHttpResponse(response);
        }
        CookieStore cookies = ((AbstractHttpClient) httpClient)
                .getCookieStore();
        MAHttpContext.cookieStore = cookies;

        log("response", jsonResponse);
        return jsonResponse;
    }
    /**
	 * 获取单例httppclinet对象
	 * add by lxw
	 * @return
	 */
	private static synchronized DefaultHttpClient getHttpClient() {

		if (null == httpClient) {
	        // generate a HttpClient
	        httpClient = new DefaultHttpClient();
	        // 请求超时
	        httpClient.getParams().setParameter(
	                CoreConnectionPNames.CONNECTION_TIMEOUT, MAHttpContext.OP_TIMEOUT);
	        // 读取超时
	        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
	                MAHttpContext.TIMEOUT);

	        // 添加Cookie
	        if (MAHttpContext.cookieStore != null) {
	            httpClient.setCookieStore(MAHttpContext.cookieStore);

	        }
		}
		return httpClient;
	}
	
    private static String buildResponseFromHttpResponse(HttpResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
//        Header[] hander = response.getAllHeaders();
//        for(Header h:hander){
//        	if(h.getName().equals("Set-Cookie")){
//        		ContainerInfo.setSessionCookie(h.getValue());
//        	}
//        }
        
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            builder.append(s);
        }
        bufferedReader.close();
        return builder.toString();
    }
    
    private static void logHeader(HttpRequestBase request) throws IOException {
        LogUtil.d("Url: " + request.getURI().toString() + "\n"
                + "Method: " + request.getMethod());
        JSONObject headers = new JSONObject();
        try {
            for (Header header : request.getAllHeaders()) {
                headers.putOpt(header.getName() , header.getValue());
            }
            LogUtil.d("head: " + headers.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
	public static void cancelAllRequest() {
		isCallCancelAllRequest = true;
		for (HttpRequestBase request : requests) {
			if (null != request && !request.isAborted())
				request.abort();
		}
		requests.clear();
		isCallCancelAllRequest = false;
	}
	
	public static void resetHttpClient() {
		if (httpClient != null) {
			cancelAllRequest();
			httpClient.getCookieStore().clear();
			httpClient = null;
			System.gc();
		}
	}
	
    private static String streamToString(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            builder.append(s);
        }
        bufferedReader.close();
        return builder.toString();
    }
    
    private static void log(String type, String response) throws IOException {
        LogUtil.d(type + ": " + response);
    }
}
