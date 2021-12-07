package com.fastcache.MCache;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
 @Dao
public interface CacheEntityDao {
 //------------------------query------------------------
     @Query("SELECT * FROM cache_entity")
    List<CacheEntity> getAll();

     @Query("SELECT * FROM cache_entity WHERE `key` IN (:keys)")
    List<CacheEntity> loadAllByKeys(String... keys);

     @Query("SELECT * FROM cache_entity WHERE `key` = :key")
    CacheEntity findByKey(String key);

    @Query("SELECT * FROM cache_entity WHERE `id` = :id")
    CacheEntity findById(Long id);
    @Query("SELECT * FROM cache_entity WHERE `key` = :key And `id` = :id")
    CacheEntity findByIdAndKey(String key,Long id);

    @Query("SELECT * FROM cache_entity WHERE `key` = :key")
    List<CacheEntity> findByKeyList(String key);

//-----------------------insert----------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CacheEntity cacheEntity);

     @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CacheEntity... cacheEntities);
     @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CacheEntity> cacheEntities);

    //---------------------update------------------------

    @Update()
    int update(CacheEntity cacheEntity);
     @Update()
    int updateAll(CacheEntity... cacheEntitys);
     @Update()
    int updateAll(List<CacheEntity> cacheEntitys);

    //-------------------delete-------------------

    @Delete
    int delete(CacheEntity cacheEntity);
     @Delete
    int deleteAll(List<CacheEntity> cacheEntitys);
     @Delete
    int deleteAll(CacheEntity... cacheEntitys);

     @Query("DELETE FROM cache_entity WHERE `key` = :key")
    int deleteBykey(String key);

    @Query("DELETE FROM cache_entity WHERE `id` = :id")
    int deleteById(Long id);
}