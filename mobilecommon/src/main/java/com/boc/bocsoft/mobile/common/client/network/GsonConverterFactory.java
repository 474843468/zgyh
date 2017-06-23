package com.boc.bocsoft.mobile.common.client.network;

import com.google.gson.Gson;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * gson转换器
 * Created by XieDu on 2016/4/19.
 */
public class GsonConverterFactory extends Converter.Factory {

    private Gson gson;

    public GsonConverterFactory() {
        gson = new Gson();
    }

    public GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public <D> Converter<D, RequestBody> getRequestConverter(Class<D> clazz) {
        return new GsonRequestConverter<>(gson.getAdapter(clazz));
    }

    @Override
    public <R> Converter<Response, Observable<R>> getResponseConverter(Class<R> clazz) {
        return new GsonResponseConverter<>(gson.getAdapter(clazz));
    }

}