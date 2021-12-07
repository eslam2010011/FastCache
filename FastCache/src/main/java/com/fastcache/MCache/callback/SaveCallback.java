
package com.fastcache.MCache.callback;


import com.fastcache.MCache.CacheEntity;

public interface SaveCallback<T> {
    void done(CacheEntity t);

    void doneOnline(T t);

    void onError(Throwable t);


}