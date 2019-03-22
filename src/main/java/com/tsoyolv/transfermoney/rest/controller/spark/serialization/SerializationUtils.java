package com.tsoyolv.transfermoney.rest.controller.spark.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class SerializationUtils {

    public static JsonElement serializeObject(Object object) {
        return new Gson().toJsonTree(object);
    }

    public static <T> T deserializeObject(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
