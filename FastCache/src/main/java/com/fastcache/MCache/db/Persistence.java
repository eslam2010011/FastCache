package com.fastcache.MCache.db;


import com.fastcache.MCache.CacheEntity;
import com.fastcache.MCache.CacheEntityDao;
import com.fastcache.MCache.Model.Record;
import com.fastcache.MCache.callback.GetCallback;
import com.fastcache.MCache.callback.RemoveCallback;
import com.fastcache.MCache.callback.SaveCallback;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Single;


public interface Persistence {

    <T> Record<T> retrieve(String key, Type type);

    <T> T retrieve(String key, Type type, GetCallback<T> getCallback);

    String getStringData(String key);


    <T> T getDataList(String key);

    <T> void save(String key, T value, SaveCallback<T> saveCallback);

    <T> void save(String key, Single<T> call, SaveCallback<T> saveCallback);

    <T, K> void save(String key, T value, Single<K> call, SaveCallback<K> saveCallback);

    <K> void Sync(String key, Single<K> call, SaveCallback<K> saveCallback);


    <T> void save(String key, T value, long expireTime, SaveCallback<T> saveCallback);

    <T> void update(String key, T value, SaveCallback<T> saveCallback);


    List<String> allKeys();


    List<CacheEntity> getAll(String key);

    boolean containsKey(String key);

    void evict(String key, RemoveCallback removeCallback);


    void evictById(Long id, RemoveCallback removeCallback);


    void evictAll();

    CacheEntityDao getDb();


}