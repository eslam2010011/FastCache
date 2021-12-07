package com.fastcache.MCache.utils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class TypeUtils {


    public static Class<?> getRawType(Type type) {

        return TypeToken.get(type).getRawType();
    }


    public static <T> String getClassSimpleName(T t) {

        return t.getClass().getSimpleName();
    }
}