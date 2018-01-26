package com.borunovv.kongregate;

import com.borunovv.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper to parse json.
 *
 * @author borunovv
 */
class JsonReader {
    private Map<String, Object> params = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    JsonReader(String json) throws IOException {
        this.params = (Map<String, Object>) JsonUtils.fromJson(json, Map.class);
    }

    private JsonReader(Map<String, Object> params) {
        this.params = params;
    }

    boolean hasParam(String name) {
        return params.containsKey(name);
    }

    boolean ensureBooleanParam(String name) throws KongregateException {
        ensureParamExists(name);
        if (!(params.get(name) instanceof Boolean)) {
            throw new KongregateException("Expected param '" + name + "' type - Boolean.");
        }
        return (Boolean) params.get(name);
    }

    long ensureLongParam(String name) throws KongregateException {
        ensureParamExists(name);
        if (!(params.get(name) instanceof Number)) {
            throw new KongregateException("Expected param '" + name + "' type - Number.");
        }
        return ((Number) params.get(name)).longValue();
    }

    String ensureStringParam(String name) throws KongregateException {
        ensureParamExists(name);
        return params.get(name).toString();
    }

    // Note: Can return null
    Object tryGetParam(String name) {
        return params.get(name);
    }

    // Note: Can return null
    @SuppressWarnings("unchecked")
    JsonReader tryGetObject(String name) {
        Object obj = tryGetParam(name);
        if (obj == null) return null;
        return new JsonReader((Map<String, Object>) obj);
    }

    // Note: Can return null
    @SuppressWarnings("unchecked")
    List<JsonReader> tryGetObjectsList(String name) {
        Object obj = tryGetParam(name);
        if (obj == null) return null;
        List<Map<String, Object>> objList = (List<Map<String, Object>>) obj;
        List<JsonReader> result = new ArrayList<>(objList.size());
        for (Map<String, Object> item : objList) {
            result.add(new JsonReader(item));
        }
        return result;
    }

    private void ensureParamExists(String name) throws KongregateException {
        if (!hasParam(name)) {
            throw new KongregateException("Expected param '" + name + "'");
        }
    }
}
