package com.boc.bocsoft.mobile.common.utils.gson;

import com.boc.bocsoft.mobile.common.utils.date.GsonDateFormatUtils;
import com.boc.bocsoft.mobile.common.utils.date.InstantTypeAdapter;
import com.boc.bocsoft.mobile.common.utils.date.LocalDateTimeTypeAdapter;
import com.boc.bocsoft.mobile.common.utils.date.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

/**
 * Gson utilities.
 */
public abstract class GsonUtils {

    private static final Gson GSON = createGson(true);

    private static final Gson GSON_NO_NULLS = createGson(false);

    /**
     * Create the standard {@link Gson} configuration
     *
     * @param serializeNulls whether nulls should be serialized
     * @return created gson, never null
     */
    public static final Gson createGson(final boolean serializeNulls) {
        Map<Type, Object> typeAdapterMap = new HashMap<>();
        typeAdapterMap.put(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        typeAdapterMap.put(LocalDate.class, new LocalDateTypeAdapter());
        typeAdapterMap.put(Instant.class,new InstantTypeAdapter());
        return createGson(serializeNulls, typeAdapterMap);
    }

    public static final Gson createGson(final boolean serializeNulls,
            Map<Type, Object> typeAdapterMap) {
        final GsonBuilder builder = new GsonBuilder();
        for (Map.Entry<Type, Object> entry : typeAdapterMap.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson() {
        return GSON;
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON : GSON_NO_NULLS;
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
    }

    public static Gson getGson(Class<?> clazz) {
        return getGson(GSON, GsonDateFormatUtils.processSerializable(clazz));
    }

    public static Gson getGson(Gson gson,Class<?> clazz) {
        return getGson(gson, GsonDateFormatUtils.processSerializable(clazz));
    }

    private static Gson getGson(Gson gson,Map<Type, Object> typeAdapterMap) {
        return typeAdapterMap != null ? GsonUtils.createGson(true, typeAdapterMap) : gson;
    }
}
