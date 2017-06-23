package com.boc.bocsoft.mobile.mbcg;

import com.boc.bocsoft.mobile.bocyun.common.global.YunGlobalConst;
import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.common.client.CookieStore;
import com.boc.bocsoft.mobile.common.client.network.GsonConverterFactory;
import com.boc.bocsoft.mobile.common.client.network.Method;
import com.boc.bocsoft.mobile.common.client.network.RequestInfo;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.google.gson.Gson;
import java.util.HashMap;
import okhttp3.MediaType;
import rx.Observable;

public enum MBCGClient {

    instance;//单例对象

    // gson解析用
    private Gson gson;
    private BaseHttpClient httpClient;

    private static String url = "";


    /**
     * 构造函数
     */
     MBCGClient() {
        gson = GsonUtils.getGson();
        httpClient = getHttpClient();
    }


    public <T, R> Observable<R> post(String url,MBCGHeader header ,String method,T params, final Class<? extends MBCGResponse<R>> clazz) {


        MBCGRequest<T> request = new MBCGRequest<>(params);

        request.setHeader(header);
        request.setMethod(method);

        String rquestString = gson.toJson(request);


        LoggerUtils.Info("requestURL:"+url);
        LoggerUtils.Info("request:" + rquestString);//.addFormParam("json", rquestString);
        RequestInfo postRequest = new RequestInfo(httpClient).url(url).method(Method.POST).content(
            MediaType.parse("application/json"),rquestString);

        return httpClient.execute(postRequest, clazz)
                .flatMap(new MBCGBeanParser<R>());
    }

    public BaseHttpClient getHttpClient() {
        if (null == httpClient) {


            httpClient = new BaseHttpClient.HttpClientBuilder()
                    .setConnectTimeout(YunGlobalConst.TIMEOUT)
                    .setReadTimeout(YunGlobalConst.TIMEOUT)
                    .setWriteTimeout(YunGlobalConst.TIMEOUT)
                    .setConverterFactory(new GsonConverterFactory(gson))
                    .build();

        }
        return httpClient;
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

    /**
     * 获取cookies
     */
    public CookieStore getCookies(){
        return httpClient.getCookies();
    }

    public static void config(String url) {
        MBCGClient.url = url;
    }
    public static String url(){
        return url;
    }
}
