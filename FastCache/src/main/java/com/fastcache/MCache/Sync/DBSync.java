package com.fastcache.MCache.Sync;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkManager;

import com.fastcache.MCache.Sync.Worker.ConstraintsUtils;
import com.fastcache.MCache.callback.SaveCallback;
import com.fastcache.MCache.db.Persistence;

import io.reactivex.Single;

public class DBSync {
    Persistence persistence;

    public DBSync(Persistence persistence) {
        this.persistence = persistence;
    }

    <K> void Sync(String key, Single<K> call, SaveCallback<K> saveCallback) {
        persistence.Sync(key, call, saveCallback);
    }

    public Operation Sync(Context context, Class<? extends ListenableWorker> worker) {
        OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(worker)
                .setConstraints(ConstraintsUtils.requireInternet())
                .build();
        return WorkManager
                .getInstance(context)
                .enqueue(simpleRequest);
    }

    public Operation Sync(Context context, Constraints constraints, Class<? extends ListenableWorker> worker) {
        OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(worker)
                .setConstraints(constraints)
                .build();
        return WorkManager
                .getInstance(context)
                .enqueue(simpleRequest);
    }


}
