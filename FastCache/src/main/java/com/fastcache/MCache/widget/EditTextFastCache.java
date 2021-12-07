package com.fastcache.MCache.widget;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.fastcache.MCache.CacheEntity;
import com.fastcache.MCache.FastCache;
import com.fastcache.MCache.Model.Record;
import com.fastcache.MCache.callback.SaveCallback;


public class EditTextFastCache extends EditText implements TextWatcher {



    public EditTextFastCache(Context context) {
        super(context);
        init();
    }

    public EditTextFastCache(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public EditTextFastCache(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditTextFastCache(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
         geTextFastCache();
         addTextChangedListener(this);

     }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length()>0){

        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void geTextFastCache(){
        Record<String> gettext= FastCache.getFastCache(getContext()).get(getId()+"",String.class);
   if (gettext!=null){
       setText(gettext.getData());
       Log.d("EditTextFastCache",gettext.getData());

   }else {
       Log.d("EditTextFastCache","null");

   }
    }
}
