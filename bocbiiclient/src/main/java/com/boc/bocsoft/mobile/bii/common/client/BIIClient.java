package com.boc.bocsoft.mobile.bii.common.client;

import com.boc.bocsoft.mobile.bii.common.global.BIIGlobalConst;
import com.boc.bocsoft.mobile.bii.common.model.BIIRequest;
import com.boc.bocsoft.mobile.bii.common.model.BIIResponse;
import com.boc.bocsoft.mobile.bii.common.response.BIIBeanParser;
import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.common.client.CookieStore;
import com.boc.bocsoft.mobile.common.client.network.GsonConverterFactory;
import com.boc.bocsoft.mobile.common.client.network.Method;
import com.boc.bocsoft.mobile.common.client.network.RequestInfo;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.google.gson.Gson;
import java.util.HashMap;
import okhttp3.Response;
import rx.Observable;

/**
 * 处理网络请求
 * Created by XieDu on 2016/3/8.
 */
public enum BIIClient {

    instance;//单例对象

    // gson解析用
    private Gson gson;
    private BaseHttpClient httpClient;

    /**
     * 构造函数
     */
    BIIClient() {
        gson = GsonUtils.getGson();
        httpClient = getHttpClient();
    }

    public Observable<Response> get(String url) {
        RequestInfo requestInfo = new RequestInfo(httpClient).url(url).method(Method.GET);
        return httpClient.execute(requestInfo);
    }

    public <T, R> Observable<R> post(String method, T params,
            final Class<? extends BIIResponse<R>> clazz) {
        String url = BIIClientConfig.getBiiUrl();
        return post(url, method, params, clazz);
    }

    public <R> Observable<R> post(String method, final Class<? extends BIIResponse<R>> clazz) {
        return post(method, null, clazz);
    }

    public <T, R> Observable<R> post(String url, String method, T params,
            final Class<? extends BIIResponse<R>> clazz) {
        String rquestString =
                (params == null ? gson : GsonUtils.getGson(gson, params.getClass())).toJson(
                        new BIIRequest<>(method, params == null ? new Object() : params));
        LoggerUtils.thread();
        LoggerUtils.Info("request:" + rquestString);
        RequestInfo postRequest = new RequestInfo(httpClient).url(url)
                                                             .method(Method.POST)
                                                             .addFormParam("json", rquestString)
                                                             .addHeader("Connection", "Keep-Alive")
                                                             .addHeader("User-Agent",
                                                                     "X-ANDR|1.5.24")
                                                             .addHeader("Accept-Encoding", "")
                                                             .addHeader("Content-Type",
                                                                     "application/x-www-form-urlencoded;charset=UTF-8")
                                                             .addHeader("bfw-ctrl", "json")
                                                             .addHeader("Accept-Language", "zh-cn");
        return httpClient.execute(postRequest, clazz).flatMap(new BIIBeanParser<R>());
    }

    public BaseHttpClient getHttpClient() {
        if (null == httpClient) {

            httpClient =
                    new BaseHttpClient.HttpClientBuilder().setConnectTimeout(BIIGlobalConst.TIMEOUT)
                                                          .setReadTimeout(BIIGlobalConst.TIMEOUT)
                                                          .setWriteTimeout(BIIGlobalConst.TIMEOUT)
                                                          .setConverterFactory(
                                                                  new GsonConverterFactory(gson))
                                                          .build();
        }
        return httpClient;
    }

    public static void config(BiiConfigInterface param) {
        BIIClientConfig.setConfig(param);
    }

    /**
     * 保存cookies
     */
    public void saveCookies(HashMap<String, HashMap<String, String>> cookiesMap) {
        httpClient.saveCookies(cookiesMap);
    }

    /**
     * 清楚cookies
     */
    public void clearCookies() {
        httpClient.clearCookies();
    }

    /**
     * 获取cookies
     */
    public CookieStore getCookies() {
        return httpClient.getCookies();
    }
}
