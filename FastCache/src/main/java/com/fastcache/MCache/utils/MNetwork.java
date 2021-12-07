package com.fastcache.MCache.utils;



import com.fastcache.MCache.db.IMNetwork;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MNetwork implements IMNetwork {
    @Override
    public boolean checkNetwork() {
        final boolean[] checkNetwork = new boolean[1];

        return  checkNetwork[0];
    }
}
