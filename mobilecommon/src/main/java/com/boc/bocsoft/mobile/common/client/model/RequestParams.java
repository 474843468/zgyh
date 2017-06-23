package com.boc.bocsoft.mobile.common.client.model;



import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by XieDu on 2016/3/7.
 */
public class RequestParams {
    protected ConcurrentHashMap<String, String> headers;

    protected ConcurrentHashMap<String, String> urlParams;


    public RequestParams() {
        headers = new ConcurrentHashMap<String, String>();
        urlParams = new ConcurrentHashMap<String, String>();
    }

    public ConcurrentHashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(ConcurrentHashMap<String, String> headers) {
        this.headers = headers;
    }

    public ConcurrentHashMap<String, String> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(ConcurrentHashMap<String, String> urlParams) {
        this.urlParams = urlParams;
    }

    public void putHeader(String key, String value) {
        if (value == null) {
            value = "";
        }

        if (!StringUtils.isEmpty(key)) {
            headers.put(key, value);
        }
    }

    public void putAllHeaders(Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            urlParams.putAll(headers);
        }
    }

    public void putUrlParam(String key, String value) {
        if (value == null) {
            value = "";
        }

        if (!StringUtils.isEmpty(key)) {
            urlParams.put(key, value);
        }
    }

    public void putAllUrlParams(Map<String, String> urlParams) {
        if (urlParams != null && !urlParams.isEmpty()) {
            urlParams.putAll(urlParams);
        }
    }

    public void clear() {
        headers.clear();
        urlParams.clear();
    }

}
