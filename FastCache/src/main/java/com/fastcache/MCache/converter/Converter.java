package com.fastcache.MCache.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;


public interface Converter {


    <T> T read(InputStream source, Type type);

    void writer(OutputStream sink, Object data);


    <T> T fromJson(String json, Type type);

    String toJson(Object data);


    String converterName();
}