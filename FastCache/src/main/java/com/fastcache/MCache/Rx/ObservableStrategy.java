package com.fastcache.MCache.Rx;


import com.fastcache.MCache.FastCache;
import com.fastcache.MCache.Model.Record;

import java.lang.reflect.Type;

import io.reactivex.Observable;


public interface ObservableStrategy {

    <T> Observable<Record<T>> execute(FastCache rxCache, String key, Observable<T> source, Type type);
}