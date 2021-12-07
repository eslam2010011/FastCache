package com.fastcache.MCache;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ListenableWorker;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.fastcache.MCache.Model.Record;
import com.fastcache.MCache.Rx.ObservableStrategy;
import com.fastcache.MCache.Sync.DBSync;
import com.fastcache.MCache.callback.GetCallback;
import com.fastcache.MCache.callback.RemoveCallback;
import com.fastcache.MCache.callback.SaveCallback;
import com.fastcache.MCache.db.Persistence;
import com.fastcache.MCache.utils.Constant;
import com.fastcache.MCache.utils.GsonUtils;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;

public class FastCache {
    static Persistence persistence;
    private static FastCache mFastCache;
    DBSync dbSync;
    Context context;
    Gson gson = new Gson();

    public static FastCache getFastCache(Context context) {
        if (mFastCache == null) {
            mFastCache = new FastCache(context);
        }

        return mFastCache;
    }

    public static void config(Persistence mpersistence) {
        if (mpersistence == null) {
            persistence = mpersistence;
        }
    }


    public FastCache(Context context) {
        this.context = context;
        persistence = new RoomImpl(context);
        dbSync = new DBSync(persistence);
        //    Observable<Record<List<User>>> observable = load2Observable("test", User.class);
    }

    public <T> ObservableTransformer<T, Record<T>> transformObservable(final String key, final Type type, final ObservableStrategy strategy) {
        return new ObservableTransformer<T, Record<T>>() {
            @Override
            public ObservableSource<Record<T>> apply(Observable<T> upstream) {
                return strategy.execute(FastCache.this, key, upstream, type);
            }
        };
    }


    public <T> Observable<?> getRx(final String key, final Type type) {
        final Record<T> record = persistence.retrieve(key, type);
        return record != null ? Observable.create(new ObservableOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(ObservableEmitter<Record<T>> emitter) throws Exception {
                emitter.onNext(record);
                emitter.onComplete();
            }
        }) : Observable.empty();
    }

    public <T> Record<T> get(final String key, final Type type) {
        return persistence.retrieve(key, type);
    }

    public <T> T get(final String key, final Type type, GetCallback<T> getCallback) {
        return persistence.retrieve(key, type, getCallback);
    }




    public <T> Optional<T> get2(final String key, final Type type) {
        return (Optional<T>) Optional.fromNullable(GsonUtils.fromJson(persistence.getStringData(key), type));
    }

    public <T> Optional<T> get2(final String key) {
        return (Optional<T>) Optional.fromNullable(persistence().getDataList(key));
    }

    private Persistence persistence() {
        return persistence;
    }


    public <T> void save(String key, T value, SaveCallback<T> saveCallback) {
        persistence().save(key, value, Constant.NEVER_EXPIRE, saveCallback);
    }

    public <T> void save(String key, T value, long expireTime, SaveCallback<T> saveCallback) {
        persistence().save(key, value, expireTime, saveCallback);
    }

    public <T> void save(String key, Single<T> call, SaveCallback<T> saveCallback) {
        persistence().save(key, call, saveCallback);
    }

    public <T, K> void save(String key, T value, Single<K> call, SaveCallback<K> saveCallback) {
        persistence().save(key, value, call, saveCallback);
    }

    public <K> void sync(String key, Single<K> call, SaveCallback<K> saveCallback) {
        persistence().Sync(key, call, saveCallback);
    }

    public void remove(String key, RemoveCallback removeCallback) {
        persistence().evict(key, removeCallback);
    }

    public void removeById(Long id, RemoveCallback removeCallback) {
        persistence().evictById(id, removeCallback);
    }

    public <K> void update(String key, Single<K> call, SaveCallback<K> saveCallback) {
        new UnsupportedOperationException("Unsupported update");
    }

    public void syncWork(Class<? extends ListenableWorker> syncDataWorker) {
        dbSync.Sync(context, syncDataWorker);
    }

    public void syncWork(Class<? extends ListenableWorker> syncDataWorker, Constraints constraints) {
        dbSync.Sync(context, constraints, syncDataWorker);
    }
}
