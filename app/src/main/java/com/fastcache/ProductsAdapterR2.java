package com.fastcache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fastcache.MCache.CacheEntity;
import com.fastcache.MCache.FastCache;
import com.fastcache.MCache.callback.RemoveCallback;
import com.fastcache.MCache.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapterR2 extends RecyclerView.Adapter<ProductsAdapterR2.SliderAdapterVH> {

    private Context context;
    private List<CacheEntity> mSliderItems = new ArrayList<>();

    public ProductsAdapterR2(Context context, List<CacheEntity> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
    }

    public void add(CacheEntity cacheEntity){
        mSliderItems.add(cacheEntity);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.r2_layout, null);
        return new SliderAdapterVH(inflate);    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, @SuppressLint("RecyclerView") final int position) {
        CacheEntity sliderItem = mSliderItems.get(position);
        String s= GsonUtils.fromJson(sliderItem.data,String.class);
        viewHolder.spinner_text.setText(s);
        viewHolder.spinner_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastCache.getFastCache(context).removeById(sliderItem.getId(), new RemoveCallback() {
                    @Override
                    public void done() {
                        mSliderItems.remove(position);
                        notifyItemRemoved(position);
                        Log.d("CacheEntity_r",  "removeById");

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSliderItems.size();
    }



    class SliderAdapterVH extends RecyclerView.ViewHolder {

TextView spinner_text;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            spinner_text=itemView.findViewById(R.id.spinner_text);

        }
    }

}