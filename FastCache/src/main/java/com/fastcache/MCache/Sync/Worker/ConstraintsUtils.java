package com.fastcache.MCache.Sync.Worker;

import androidx.work.Constraints;
import androidx.work.NetworkType;

public class ConstraintsUtils {

    public static Constraints requireInternet() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

}