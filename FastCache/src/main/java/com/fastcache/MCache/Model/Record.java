package com.fastcache.MCache.Model;

import com.fastcache.MCache.utils.Constant;
import com.fastcache.MCache.utils.GsonUtils;


public final class Record<T> {

    private final Source from;
    private final String key;
    private final T data;
    private final long createTime;
    private final long expireTime;

    public Record(Source from,String key,T value) {

        this(from,key,value,System.currentTimeMillis());
    }

    public Record(Source from,String key,T value,long createTime) {

        this(from,key,value,createTime, Constant.NEVER_EXPIRE);
    }

    public Record(Source from,String key,T value,long createTime,long expireTime) {

        this.from = from;
        this.key = key;
        this.data = value;
        this.createTime = createTime;
        this.expireTime = expireTime;
    }

    public Source getFrom() {
        return from;
    }

    public String getKey() {
        return key;
    }

    public T getData() {
        return data;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }


    public boolean isExpired() {
        if (isNeverExpire()) {
            return false;
        }
        return createTime + expireTime < System.currentTimeMillis();
    }

    public boolean isNeverExpire() {

        return expireTime == Constant.NEVER_EXPIRE;
    }


    public long ttl() {

        if (isNeverExpire()) {

            return Constant.NEVER_EXPIRE;
        }

        if (isExpired()) {

            return Constant.HAS_EXPIRED;
        }

        return getExpireTime()- (System.currentTimeMillis() - getCreateTime());
    }

    public String toString() {
        return GsonUtils.toJson(this);
    }
}