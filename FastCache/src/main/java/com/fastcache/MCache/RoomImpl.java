package com.fastcache.MCache;

import android.content.Context;
import android.os.AsyncTask;

import com.fastcache.MCache.Model.Record;
import com.fastcache.MCache.Model.Source;
import com.fastcache.MCache.Rx.MRx;
import com.fastcache.MCache.callback.GetCallback;
import com.fastcache.MCache.callback.RemoveCallback;
import com.fastcache.MCache.callback.SaveCallback;
import com.fastcache.MCache.converter.Converter;
import com.fastcache.MCache.converter.GsonConverter;
import com.fastcache.MCache.db.DB;
import com.fastcache.MCache.utils.Constant;
import com.fastcache.MCache.utils.GsonUtils;
import com.fastcache.MCache.utils.MNetwork;
import com.fastcache.MCache.utils.Preconditions;
import com.fastcache.MCache.utils.RetryWithDelayOrInternet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RoomImpl extends MNetwork implements DB , MRx {

    private CacheEntityDao db;
    private Converter converter;

    public RoomImpl(Context context) {
        this(context,new GsonConverter());
    }

    public RoomImpl(Context context, Converter converter) {
        this.db = AppDatabase.getDatabase(context).cacheEntityDao();
        this.converter = converter;
    }
    @Override
    public <T> T retrieve(String key, Type type ,GetCallback<T> getCallback) {
      T fromJson=  GsonUtils.fromJson(getStringData(key),type);
      if (getCallback!=null){
          getCallback.done(fromJson ,db.findByKey(key));
      }
      return fromJson ;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type ) {
       /* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Insert Data
                AppDatabase.getInstance(context).userDao().insert(new User(1,"James","Mathew"));

                // Get Data
                AppDatabase.getInstance(context).userDao().getAllUsers();
            }
        });*/
        CacheEntity entity = db.findByKey(key);
        if (entity==null) return null;
        long timestamp = entity.timestamp;
        long expireTime = entity.expireTime;
        T result = null;
        if (expireTime<0) { // 缓存的数据从不过期

            String json = entity.data;
            result = converter.fromJson(json,type);
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                String json = entity.data;

                result = converter.fromJson(json,type);
            } else {
                evict(key, new RemoveCallback() {
                    @Override
                    public void done() {

                    }
                });
            }
        }
        return result != null ? new Record<>(Source.PERSISTENCE, key, result, timestamp, expireTime) : null;
    }




    @Override
    public String getStringData(String key) {
        CacheEntity entity = db.findByKey(key);
         if (entity==null) return null;
        long timestamp = entity.timestamp;
        long expireTime = entity.expireTime;
        String json = null;
        if (expireTime<0) {
            json = entity.data;
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {

                json = entity.data;
            } else {
                evict(key, new RemoveCallback() {
                    @Override
                    public void done() {

                    }
                });            }
        }

        return json;
    }

    @Override
    public <T> T getDataList(String key) {
         List<CacheEntity> entity = db.findByKeyList(key);
        return (T) entity;
    }

    @Override
    public <T> void save(String key, T value, SaveCallback<T> saveCallback) {
        save(key,value, Constant.NEVER_EXPIRE,saveCallback);
    }

    @Override
    public <T> void save(String key, Single<T> call, final SaveCallback<T> saveCallback) {
        CacheEntity entityv = db.findByKey(key);
        if (entityv!=null){
            db.delete(entityv);
        }
        final CacheEntity entity = new CacheEntity();
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setExpireTime(Constant.NEVER_EXPIRE);
        entity.setType(com.fastcache.MCache.utils.Type.OFFLINE);
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setId(System.currentTimeMillis());
        entity.setUpdateAt(Constant.NEVER_EXPIRE);
        saveCallback.done(entity);
        call.retryWhen(new RetryWithDelayOrInternet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(final T doctorListMain) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                      /*  if (doctorListMain instanceof List  ){
                            List<T> list=new ArrayList<>();
                            for (T t : list){
                                entity.setData(converter.toJson(t));
                                entity.setType(com.fastcache.MCache.utils.Type.Online);
                                entity.setUpdateAt(System.currentTimeMillis());
                                db.insert(entity);
                            }
                        }else {

                        }*/

                        entity.setData(converter.toJson(doctorListMain));
                        entity.setType(com.fastcache.MCache.utils.Type.Online);
                        entity.setUpdateAt(System.currentTimeMillis());
                        db.insert(entity);

                    }
                });
                saveCallback.doneOnline(doctorListMain);
            }

            @Override
            public void onError(Throwable e) {
                saveCallback.onError(e);

            }
        });

    }

    @Override
    public <T, K> void save(String key, T value, Single<K> call, final SaveCallback<K> saveCallback) {
        final CacheEntity entity = new CacheEntity();
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setExpireTime(Constant.NEVER_EXPIRE);
        entity.setData(GsonUtils.toJson(value));
        entity.setId(System.currentTimeMillis());
        entity.setUpdateAt(Constant.NEVER_EXPIRE);
         entity.setType(com.fastcache.MCache.utils.Type.OFFLINE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.insert(entity);
            }
        });
        saveCallback.done(entity);
        call.retryWhen(new RetryWithDelayOrInternet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<K>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(K doctorListMain) {
                CacheEntity entity1 = db.findById(entity.getId());
                entity1.setType(com.fastcache.MCache.utils.Type.Online);
                entity1.setUpdateAt(System.currentTimeMillis());
                db.update(entity1);
                saveCallback.doneOnline(doctorListMain);
            }

            @Override
            public void onError(Throwable e) {
                saveCallback.onError(e);

            }
        });
    }

    @Override
    public <K> void Sync(String key, Single<K> call, final SaveCallback<K> saveCallback) {
        List<CacheEntity> list = db.findByKeyList(key);
        if (list==null) return ;
        for (final CacheEntity entity:list) {
            if (entity.getType()==com.fastcache.MCache.utils.Type.OFFLINE){
                call.retryWhen(new RetryWithDelayOrInternet())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<K>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(K list) {
                        CacheEntity entity1 = db.findById(entity.getId());
                        entity1.setType(com.fastcache.MCache.utils.Type.Online);
                        entity1.setUpdateAt(System.currentTimeMillis());
                        db.update(entity1);
                        if (saveCallback!=null){
                            saveCallback.doneOnline(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }


         }

    }

    @Override
    public <T> void save(String key, T value, long expireTime, SaveCallback<T> saveCallback) {
        CacheEntity entityv = db.findByKey(key);
        if (entityv!=null){
            db.delete(entityv);
        }
        final CacheEntity entity = new CacheEntity();
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setExpireTime(expireTime);
        entity.setData(converter.toJson(value));
        entity.setId(System.currentTimeMillis());
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setType(com.fastcache.MCache.utils.Type.OFFLINE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                entity.setUpdateAt(System.currentTimeMillis());
                db.insert(entity);



            }
        });
        saveCallback.done(entity);
    }

    @Override
    public <T> void update(String key, T value, SaveCallback<T> saveCallback) {
        CacheEntity entity =db.findByKey(key);
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
         entity.setData(converter.toJson(value));
        entity.setCreatedAt(System.currentTimeMillis());
       // db.update(value);
    }


    @Override
    public List<String> allKeys() {
        List<CacheEntity> list = db.getAll();
        List<String> result = new ArrayList<>();
        for (CacheEntity entity:list) {
            result.add(entity.key);
        }
        return result;
    }

    @Override
    public List<CacheEntity> getAll(String key) {
       return db.getAll();
    }

    @Override
    public boolean containsKey(String key) {
        List<String> keys = allKeys();
        return Preconditions.isNotBlank(keys) ? keys.contains(key) : false;
    }

    @Override
    public void evict(String key , RemoveCallback removeCallback) {
        CacheEntity entity = db.findByKey(key);
        if (entity!=null) {
            db.delete(entity);
            removeCallback.done();
        }
    }
    @Override
    public void evictById(Long id , RemoveCallback removeCallback) {
        CacheEntity entity = db.findById(id);
        if (entity!=null) {
            db.delete(entity);
            removeCallback.done();
        }
    }

    @Override
    public void evictAll() {
        db.deleteAll();
    }

    @Override
    public CacheEntityDao getDb() {
        return db;
    }


    @Override
    public <T> void saveRx(Single<T> call) {

    }
}
