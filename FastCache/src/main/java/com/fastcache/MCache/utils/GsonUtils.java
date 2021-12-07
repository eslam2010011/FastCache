package com.fastcache.MCache.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;


public class GsonUtils {

    private static Gson gson;

    static {
        gson = new Gson();
    }

    private GsonUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T fromJson(String json, Type type) {

        return gson.fromJson(json,type);
    }

    public static String toJson(Object data){

        return gson.toJson(data);
    }
}