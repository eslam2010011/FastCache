package com.fastcache;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastcache.MCache.CacheEntity;
import com.fastcache.MCache.FastCache;
import com.fastcache.MCache.callback.RemoveCallback;
import com.fastcache.MCache.callback.SaveCallback;
import com.fastcache.MCache.utils.GsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Button_id, Button_remove,Button_get;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<CacheEntity> list=    (List<CacheEntity>) FastCache.getFastCache(MainActivity.this).get2("Chat").get();
        ProductsAdapterR2 productsAdapterR2=new ProductsAdapterR2(this,list);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productsAdapterR2);
        Button_id = findViewById(R.id.Button_id);
        Button_remove = findViewById(R.id.Button_remove);
        Button_get = findViewById(R.id.Button_get);
        Button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FastCache.getFastCache(MainActivity.this).save("Chat", "Eslam", new SaveCallback<String>() {
                    @Override
                    public void done(CacheEntity t) {
                        productsAdapterR2.add(t);
                        Log.d("CacheEntity", t.getId() + "");
                         Log.d("CacheEntity", t.getCreatedAt() + "");
                    }

                    @Override
                    public void doneOnline(String s) {

                    }

                     @Override
                     public void onError(Throwable t) {

                     }
                 });
            }
        });

        Button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastCache.getFastCache(MainActivity.this).removeById(1616869503306L, new RemoveCallback() {
                    @Override
                    public void done() {
                        Log.d("CacheEntity_r",  "removeById");

                    }
                });
            }
        });
        Button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CacheEntity cacheEntity :list){
                     String s= GsonUtils.fromJson(cacheEntity.data,String.class);
                    Log.d("CacheEntity2", s+ "");

                }


            }
        });

    }
}