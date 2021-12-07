package com.fastcache.MCache.Rx;

import io.reactivex.Single;

public interface MRx {

    <T> void saveRx(Single<T> call);
}
