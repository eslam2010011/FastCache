package com.fastcache.MCache;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {CacheEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "cache";

    public abstract CacheEntityDao cacheEntityDao();
    private static AppDatabase INSTANCE;

    public   static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context,
                                    AppDatabase.class,
                                    DB_NAME).allowMainThreadQueries()
                                    .addCallback(onOpenCallback)
                                    .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback onOpenCallback =
            new Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
//                    initializeData();
                }};



}
