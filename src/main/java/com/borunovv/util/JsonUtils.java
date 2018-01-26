package com.borunovv.util;

import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author borunovv
 */
public class JsonUtils {

    public static String toJson(Object obj) {
        return JSONValue.toJSONString(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (clazz.equals(Map.class)) {
            return (T) JSONValue.parse(json);

        } else if (clazz.equals(List.class)) {
            return (T) JSONValue.parse(json);

        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz.getSimpleName());
        }
    }
}