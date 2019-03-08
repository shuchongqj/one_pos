package com.gzdb.supermarket.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by zhumg on 7/25.
 */
public class GsonUtil {

    static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    static {
        GSON_BUILDER.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String toJson(Object obj) {
        return GSON_BUILDER.create().toJson(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> classOfT) throws JsonSyntaxException {
        return GSON_BUILDER.create().fromJson(jsonStr, classOfT);
    }

    public static <T> T fromJson(String jsonStr, Type typeOfT) throws JsonSyntaxException {
        return GSON_BUILDER.create().fromJson(jsonStr, typeOfT);
    }

}
