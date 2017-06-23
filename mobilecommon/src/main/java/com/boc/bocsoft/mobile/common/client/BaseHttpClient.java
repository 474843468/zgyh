package com.boc.bocsoft.mobile.common.client;

import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.common.client.model.ClientConfig;
import com.boc.bocsoft.mobile.common.client.network.Converter;
import com.boc.bocsoft.mobile.common.client.network.GsonConverterFactory;
import com.boc.bocsoft.mobile.common.client.network.RequestInfo;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * 执行网络请求
 * Created by XieDu on 2016/2/23.
 */
public class BaseHttpClient {

    private OkHttpClient client;
    private ClientConfig clientConfig;
    private CookiesManager cookiesManager;
    private Converter.Factory converterFactory;

    public BaseHttpClient() {
        this(new HttpClientBuilder());
    }

    public BaseHttpClient(HttpClientBuilder httpClientBuilder) {
        this.clientConfig = httpClientBuilder.clientConfig;
        this.cookiesManager = httpClientBuilder.cookiesManager;
        this.converterFactory = httpClientBuilder.converterFactory;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        //管理cookie
        client = okHttpClientBuilder.cookieJar(cookiesManager)
                .connectTimeout(clientConfig.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(clientConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(clientConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .hostnameVerifier(clientConfig.getHostnameVerifier())
                                    .retryOnConnectionFailure(false)
                // 忽略主机端证书
                .sslSocketFactory(getIgnoreSSLSocktFactory())
                .build();
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
    }

    /**
     * 保存cookie
     */
    public void saveCookies(HashMap<String, HashMap<String, String>> cookitesMap){
        cookiesManager.saveCookies(cookitesMap);
    }

    /**
     * 清除cookie
     */
    public void clearCookies(){
        cookiesManager.clearCookies();
    }

    /**
     * 获取cookies
     * @return ookies
     */
    public CookieStore getCookies() {
        return cookiesManager.getCookies();
    }

    public  Observable<Response> execute(final RequestInfo requestInfo) {
        Request request = requestInfo.generateRequest();
        final Call call = client.newCall(request);
        return Observable.create(new OnSubscribe<Response>() {

            @Override
            public void call(final Subscriber<? super Response> subj) {
                subj.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        call.cancel();
                    }
                }));

                if (!subj.isUnsubscribed()) {
                    //call.enqueue(new Callback() {
                    //    @Override
                    //    public void onFailure(Call call, IOException e) {
                    //        if (!subj.isUnsubscribed()) {
                    //            subj.onError(createNetworkException(e.getMessage()));
                    //        }
                    //        if (!subj.isUnsubscribed()) {
                    //            subj.onCompleted();
                    //        }
                    //    }
                    //
                    //    @Override
                    //    public void onResponse(Call call, Response response) throws IOException {
                    //        if (!subj.isUnsubscribed()) {
                    //            subj.onStart();
                    //            if (response.isSuccessful()) {
                    //                subj.onNext(response);
                    //            } else {
                    //                subj.onError(createNetworkException(response.message()));
                    //            }
                    //        }
                    //        if (!subj.isUnsubscribed()) {
                    //            subj.onCompleted();
                    //        }
                    //    }
                    //});

                    try {
                        Response response = call.execute();
                        //LoggerUtils.Log(" call.execute():"+response+"   Thread:"+Thread.currentThread());
                        if (!subj.isUnsubscribed()) {
                            subj.onStart();
                            if (response.isSuccessful()) {
                                subj.onNext(response);
                            } else {
                                subj.onError(createNetworkException(response.message()));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (!subj.isUnsubscribed()) {
                            subj.onError(createNetworkException(e));
                    }
                    }
                    if (!subj.isUnsubscribed()) {
                        subj.onCompleted();
                    }
                }
            }
        });
    }

    public <D> Observable<D> execute(final RequestInfo requestInfo, final Class<D> clazz) {
        return execute(requestInfo).flatMap(new Func1<Response, Observable<D>>() {
            @Override
            public Observable<D> call(Response response) {
                Converter<Response, Observable<D>> responseConverter = converterFactory.getResponseConverter(clazz);
                try {
                    return responseConverter.convert(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Observable.error(createNetworkException(e));
                }
            }
        });
    }

    /**
     * 创建网络类型的异常
     *
     * @param message 网络异常信息
     * @return HttpException异常
     */
    private HttpException createNetworkException(String message) {
        //SocketTimeoutException 异常可能没有message 此时程序可能会崩溃
        if(message == null){
            message = "";
        }
        HttpException exception = new HttpException(message);
        exception.setType(HttpException.ExceptionType.NETWORK);
        return exception;
    }

    /**
     * 创建网络类型的异常
     *
     * @param cause 网络异常
     * @return HttpException异常
     */
    private HttpException createNetworkException(Throwable cause) {
        HttpException exception = new HttpException(cause);
        exception.setType(HttpException.ExceptionType.NETWORK);
        return exception;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public HttpClientBuilder builder() {
        return new HttpClientBuilder();
    }

    public static class HttpClientBuilder {
        ClientConfig clientConfig;
        CookiesManager cookiesManager;
        Converter.Factory converterFactory;

        public HttpClientBuilder() {
            clientConfig = new ClientConfig();
            cookiesManager = new CookiesManager();
            converterFactory = new GsonConverterFactory();
        }

        public BaseHttpClient build() {
            return new BaseHttpClient(this);
        }

        public HttpClientBuilder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            clientConfig.setHostnameVerifier(hostnameVerifier);
            return this;
        }

        public HttpClientBuilder setReadTimeout(int readTimeout) {
            clientConfig.setReadTimeout(readTimeout);
            return this;
        }

        public HttpClientBuilder setWriteTimeout(int writeTimeout) {
            clientConfig.setWriteTimeout(writeTimeout);
            return this;
        }

        public HttpClientBuilder setConnectTimeout(int connectTimeout) {
            clientConfig.setConnectTimeout(connectTimeout);
            return this;
        }

        public HttpClientBuilder setCookiesManager(CookiesManager cookiesManager) {
            this.cookiesManager = cookiesManager;
            return this;
        }

        public HttpClientBuilder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }
    }

    /**
     * 忽略主机端证书，ssl测试环境时使用
     * @return SSLSocketFactory
     */
    private SSLSocketFactory getIgnoreSSLSocktFactory() {

        // 初始化工作
        SSLSocketFactory sslSocketFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
            return sslContext.getSocketFactory();
        }catch (Exception exception){
            return null;
        }

    }



}
