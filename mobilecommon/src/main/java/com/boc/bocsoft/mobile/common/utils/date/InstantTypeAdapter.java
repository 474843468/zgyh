package com.boc.bocsoft.mobile.common.utils.date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import org.threeten.bp.Instant;

/**
 * Created by md101 on 10/18/15.
 */
public class InstantTypeAdapter
        extends com.boc.bocsoft.mobile.common.utils.gson.GsonTypeAdapter<Instant> {

    public InstantTypeAdapter() {
    }

    @Override
    public JsonElement serialize(final Instant src, final Type typeOfSrc,
            final JsonSerializationContext context) {
        String formatted = String.valueOf(src.toEpochMilli());
        return new JsonPrimitive(formatted);
    }

    @Override
    public Instant deserialize(final JsonElement json, final Type typeOfT,
            final JsonDeserializationContext context) throws JsonParseException {
        //        return mDateTimeFormatter.parse(json.getAsString(), LocalDateTime.FROM);
        try {
            return Instant.ofEpochMilli(Long.valueOf(json.getAsString()));
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}