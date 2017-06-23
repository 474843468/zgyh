package com.boc.bocsoft.mobile.common.client.network;

import com.google.gson.TypeAdapter;
import okhttp3.RequestBody;

/**
 * Created by XieDu on 2016/4/13.
 */
public class GsonRequestConverter<D> implements Converter<D, RequestBody> {
    private TypeAdapter<D> adapter;

    public GsonRequestConverter(TypeAdapter<D> adapter) {
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(D data) {
        String json = adapter.toJson(data);
        return RequestBody.create(ContentType.JSON, json);
    }
}
