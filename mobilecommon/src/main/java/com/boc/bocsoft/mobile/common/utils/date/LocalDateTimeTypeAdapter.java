package com.boc.bocsoft.mobile.common.utils.date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by md101 on 10/18/15.
 */
public class LocalDateTimeTypeAdapter
        extends com.boc.bocsoft.mobile.common.utils.gson.GsonTypeAdapter<LocalDateTime> {
    /**
     * Formatter.
     */
    private final List<DateTimeFormatter> formats;

    public LocalDateTimeTypeAdapter() {
        this(DateFormatters.DATE_TIME_FORMATTERS);
    }

    public LocalDateTimeTypeAdapter(DateTimeFormatter... dateTimeFormatters) {
        formats = Arrays.asList(dateTimeFormatters);
    }

    @Override
    public JsonElement serialize(final LocalDateTime src, final Type typeOfSrc,
            final JsonSerializationContext context) {
        //        return new JsonPrimitive(mDateTimeFormatter.format(src));
        final DateTimeFormatter primary = formats.get(0);
        String formatted;
        synchronized (primary) {
            formatted = primary.format(src);
        }
        return new JsonPrimitive(formatted);
    }

    @Override
    public LocalDateTime deserialize(final JsonElement json, final Type typeOfT,
            final JsonDeserializationContext context) throws JsonParseException {
        //        return mDateTimeFormatter.parse(json.getAsString(), LocalDateTime.FROM);
        JsonParseException exception = null;
        final String value = json.getAsString();
        for (DateTimeFormatter format : formats)
            try {
                    return format.parse(value, LocalDateTime.FROM);
            } catch (Exception e) {
                exception = new JsonParseException(exception);
            }
        try {
                Instant timeStamp=Instant.ofEpochMilli(Long.valueOf(value));
                return LocalDateTime.ofInstant(timeStamp, ZoneId.systemDefault());
        } catch (Exception e) {
            exception = new JsonParseException(exception);
        }
        throw exception;
    }
}