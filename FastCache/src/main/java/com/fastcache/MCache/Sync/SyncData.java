package com.fastcache.MCache.Sync;

import io.reactivex.Single;

public class SyncData<T> {


    String key;

    Single<T> single;

    long lastSyncTimestamp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Single<T> getSingle() {
        return single;
    }

    public void setSingle(Single<T> single) {
        this.single = single;
    }

    public long getLastSyncTimestamp() {
        return lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }
}
