package com.boc.bocsoft.mobile.common.client.network;



import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.common.utils.RxUtils;

import java.io.File;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 设置请求参数
 */
public class RequestInfo {
    private HttpUrl url;
    private Method method;
    /**
     * 上传的内容，可以是普通文本，也可以是json转成的文本
     */
    private String content;
    /**
     * 上传的MIME类型
     */
    private MediaType contentType;
    private HttpUrl.Builder urlBuilder;
    private MultipartBody.Builder multipartBuilder;
    private FormBody.Builder formBuilder;
    private Request.Builder requestBuilder;
    private RequestBody body;

    private Converter.Factory converterFactory;


    public RequestInfo(BaseHttpClient baseHttpClient) {
        requestBuilder = new Request.Builder();
        converterFactory = baseHttpClient.getConverterFactory();
    }

    public RequestInfo url(String url) {
        if (url == null || (this.url = HttpUrl.parse(url)) == null)
            throw new IllegalArgumentException("Illegal URL: " + url);
        urlBuilder = this.url.newBuilder();
        return this;
    }


    public RequestInfo method(Method method) {
        this.method = method;
        return this;
    }


    public RequestInfo addHeader(String key, String value) {
        requestBuilder.addHeader(key, value);
        return this;
    }

    public RequestInfo content(MediaType contentType, String content) {
        this.contentType = contentType;
        this.content = content;
        body = RequestBody.create(contentType, content);
        return this;
    }

    ///**
    // * 默认以json方式post上传
    // *
    // * @param object
    // * @return
    // */
//    public <O> RequestInfo object(O object) {
//        Converter<O, RequestBody> requestBodyConverter = converterFactory.getRequestConverter();
//        try {
//            body = requestBodyConverter.convert(object);
//        } catch (IOException e) {
//            throw new RuntimeException("Error convert:" + object, e);
//        }
//        return this;
//    }

    public RequestInfo addQueryParam(String name, String value) {
        urlBuilder.addQueryParameter(name, value);
        return this;
    }

    /**
     * @param name
     * @param value
     * @param encoded 参数是否已经预编码过了
     * @return
     */
    public RequestInfo addFormParam(String name, String value, boolean encoded) {
        if (formBuilder == null)
            formBuilder = new FormBody.Builder();
        if (encoded)
            formBuilder.addEncoded(name, value);
        else
            formBuilder.add(name, value);
        return this;
    }

    /**
     * @param name
     * @param value
     * @return
     */
    public RequestInfo addFormParam(String name, String value) {
        return addFormParam(name, value, false);
    }

    /**
     * 添加要上传的文件
     *
     * @param file
     * @return
     */
    public RequestInfo addMultiPart(File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            throw new IllegalArgumentException("Illegal File:" + file);
        }
        getMultipartBuilder();
        RequestBody requestBody = RequestBody.create(MediaType.parse(RxUtils.guessMimeType(file.getPath())), file);
        multipartBuilder.addFormDataPart("file", file.getName(), requestBody);
        return this;
    }

    /**
     * 添加要上传的文件列表
     *
     * @param files
     * @return
     */
    public RequestInfo addMultiPart(List<File> files) {
        for (File file : files) {
            addMultiPart(file);
        }
        return this;
    }

    public RequestInfo addMultiPart(String name, String value) {
        getMultipartBuilder();
        multipartBuilder.addFormDataPart(name, value);
        return this;
    }

    private void getMultipartBuilder() {
        if (multipartBuilder == null) {
            multipartBuilder = new MultipartBody.Builder();
            multipartBuilder.setType(MultipartBody.FORM);
        }
    }


    /**
     * 根据不同的请求方式和参数,生成Request对象
     */
    public Request generateRequest() {
        if (url == null)
            throw new IllegalArgumentException("Illegal URL: " + url);
        if (method == null) {
            method = Method.GET;
        }
        switch (method) {
            case GET:
            case DELETE:
            case HEAD:
                url = urlBuilder.build();
                break;
            case POST:
            case PUT:
            case PATCH:
                if (body == null) {
                    if (formBuilder != null) {
                        body = formBuilder.build();
                    } else if (multipartBuilder != null) {
                        body = multipartBuilder.build();
                    }
                }
                break;
        }
        return requestBuilder
                .url(url)
                .method(method.name(), body)
                .build();
    }

    public RequestInfo setConverterFactory(Converter.Factory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }
}