package com.fastcache.MCache.converter;



import com.fastcache.MCache.utils.GsonUtils;

import java.lang.reflect.Type;


public class GsonConverter extends AbstractConverter {

    public GsonConverter() {
    }

   /* public GsonConverter(Encryptor encryptor) {
        super(encryptor);
    }*/

    @Override
    public <T> T fromJson(String json, Type type) {
        return GsonUtils.fromJson(json, type);
    }

    @Override
    public String toJson(Object data) {
        return GsonUtils.toJson(data);
    }
}