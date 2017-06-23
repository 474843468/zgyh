package com.boc.bocsoft.mobile.common.client.network;

import okhttp3.Response;
import rx.Observable;

/**
 * 解析数据
 * Created by XieDu on 2016/3/11.
 */
public interface DataParser<T> {
    public Observable<T> parseNetworkResponse(Response response);
}