package com.boc.bocsoft.mobile.common.client.network;

import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by XieDu on 2016/4/19.
 */
public class GsonResponseConverter<T> implements Converter<Response, Observable<T>> {
    private final TypeAdapter<T> adapter;

    GsonResponseConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Observable<T> convert(Response response) {
        try {
            String result = response.body().string();
            LoggerUtils.thread();
            LoggerUtils.Info("response:" + result);
            if (StringUtils.isEmpty(result)) {
                HttpException exception = new HttpException("通信错误");
                exception.setType(HttpException.ExceptionType.BII_NULL);
                return Observable.error(exception);
            }
            T data = adapter.fromJson(result);
            return Observable.just(data);
        } catch (IOException e) {
            LoggerUtils.Error(e.getMessage());
            HttpException exception = new HttpException(e.getMessage());
            exception.setType(HttpException.ExceptionType.NETWORK);
            return Observable.error(exception);
        }
    }

    //    @Override
    //    public Observable<T> convert(Response response, TypeToken type) {
    //        try {
    //            String result = response.body().string();
    //            System.out.println("response:" + result);
    //            T data = adapter.fromJson(result);
    //
    //            return Observable.just(data);
    //        } catch (IOException e) {
    //            HttpException exception = new HttpException(e.getMessage());
    //            exception.setType(HttpException.ExceptionType.NETWORK);
    //            return Observable.error(exception);
    //        }
    //    }
}
