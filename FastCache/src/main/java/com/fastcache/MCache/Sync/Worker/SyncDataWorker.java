package com.fastcache.MCache.Sync.Worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public abstract class SyncDataWorker extends Worker   {
    public static String SyncDataWorker="SyncDataWorker";
     public SyncDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
     }
    @NonNull
    @Override
    public Result doWork() {

        Log.d(SyncDataWorker,"Start");
        sync();
        Log.d(SyncDataWorker,"work_Sync");
        return Result.success();
    }
    protected abstract void sync();


}