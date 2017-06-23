package com.boc.bocsoft.mobile.wfss.common.client;

import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.common.client.network.GsonConverterFactory;
import com.boc.bocsoft.mobile.common.client.network.Method;
import com.boc.bocsoft.mobile.common.client.network.RequestInfo;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;
import com.boc.bocsoft.mobile.wfss.common.model.WFSSResponse;
import com.boc.bocsoft.mobile.wfss.common.response.WFSSBeanParser;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Response;
import okhttp3.MediaType;
import rx.Observable;

/**
 * 处理网络请求
 * Created by XieDu on 2016/3/8.
 */
public enum WFSSClient {

    instance;//单例对象

    // gson解析用
    private Gson gson;
    private BaseHttpClient httpClient;

    /**
     * 构造函数
     */
     WFSSClient() {
        gson = GsonUtils.getGson();
        httpClient = getHttpClient();
    }

    public Observable<Response>get (String url) {
        RequestInfo requestInfo=new RequestInfo(httpClient).url(url).method(Method.GET);
        return httpClient.execute(requestInfo);
    }

    public <T, R> Observable<R> post(String method, T params, final Class<? extends WFSSResponse<R>> clazz) {
        String url = WFSSClientConfig.getBiiUrl();
        return post(url, method, params, clazz);
    }

    public <T, R> Observable<R> post(String method, final Class<? extends WFSSResponse<R>> clazz) {

        return post(method, new Object(), clazz);
    }

    public <T, R> Observable<R> post(String url, String method, T wfssRequest, final Class<? extends WFSSResponse<R>> clazz) {
        String rquestString = GsonUtils.getGson(gson, wfssRequest.getClass()).toJson(wfssRequest);
        LoggerUtils.Info("访问的地址:" + url + method);
        LoggerUtils.Info("request:" + rquestString);
        RequestInfo postRequest = new RequestInfo(httpClient).url(url + method).method(Method.POST).content(MediaType.parse("JSON"), rquestString)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Accept-Encoding", "")
                .addHeader("chnflg", "sjyh")
                .addHeader("clientType", "android")
                .addHeader("Accept-Language", "zh-cn");
        return httpClient.execute(postRequest, clazz)
                .flatMap(new WFSSBeanParser<R>());
    }

    public BaseHttpClient getHttpClient() {
        if (null == httpClient) {
            httpClient = new BaseHttpClient.HttpClientBuilder()
                    .setConnectTimeout(WFSSGlobalConst.TIMEOUT)
                    .setReadTimeout(WFSSGlobalConst.TIMEOUT)
                    .setWriteTimeout(WFSSGlobalConst.TIMEOUT)
                    .setConverterFactory(new GsonConverterFactory(gson))
                    .build();

        }
        return httpClient;
    }

    public static void config(WFSSConfigInterface param){
        WFSSClientConfig.setConfig(param);
    }

    /**
     * 保存cookies
     * @param cookiesMap
     */
    public void saveCookies(HashMap<String, HashMap<String, String>> cookiesMap){
        httpClient.saveCookies(cookiesMap);
    }

    /**
     * 清楚cookies
     */
    public void clearCookies(){
        httpClient.clearCookies();
    }
}
