package com.boc.bocsoft.mobile.common.utils.date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by md101 on 10/18/15.
 */
public class LocalDateTypeAdapter
        extends com.boc.bocsoft.mobile.common.utils.gson.GsonTypeAdapter<LocalDate> {
    /**
     * Formatter.
     */
    private final List<DateTimeFormatter> formats;

    public LocalDateTypeAdapter() {
        this(DateFormatters.DATE_TIME_FORMATTERS);
    }

    public LocalDateTypeAdapter(DateTimeFormatter... dateTimeFormatters) {
        formats = Arrays.asList(dateTimeFormatters);
    }

    @Override
    public JsonElement serialize(final LocalDate src, final Type typeOfSrc,
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
    public LocalDate deserialize(final JsonElement json, final Type typeOfT,
            final JsonDeserializationContext context) throws JsonParseException {
        //        return mDateTimeFormatter.parse(json.getAsString(), LocalDateTime.FROM);
        JsonParseException exception = null;
        final String value = json.getAsString();
        for (DateTimeFormatter format : formats)
            try {
                synchronized (format) {
                    return format.parse(value, LocalDate.FROM);
                }
            } catch (Exception e) {
                exception = new JsonParseException(exception);
            }
        throw exception;
    }
}