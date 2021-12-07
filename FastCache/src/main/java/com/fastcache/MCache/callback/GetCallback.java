
package com.fastcache.MCache.callback;


import com.fastcache.MCache.CacheEntity;

public interface GetCallback<T> {

    void done(T t, CacheEntity byKey);


}