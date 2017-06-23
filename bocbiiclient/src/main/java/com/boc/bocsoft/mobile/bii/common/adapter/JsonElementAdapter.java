package com.boc.bocsoft.mobile.bii.common.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Administrator on 2016/5/19.
 */
public class JsonElementAdapter extends TypeAdapter<JsonElement> {

    @Override
    public JsonElement read(JsonReader reader) throws IOException {
        JsonElement ele = new JsonParser().parse(reader);

        return ele;
    }

    @Override
    public void write(JsonWriter arg0, JsonElement arg1) throws IOException {
        // TODO Auto-generated method stub

    }
}
