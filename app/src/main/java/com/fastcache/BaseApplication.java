package com.fastcache;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.fastcache.MCache.FastCache;

public class BaseApplication extends MultiDexApplication {
  static   Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
     }
}