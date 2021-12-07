package com.fastcache.MCache.utils;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RetryWithDelayOrInternet implements Function<Flowable<? extends Throwable>, Flowable<?>> {
public static boolean isInternetUp;
private int retryCount;
public RetryWithDelayOrInternet(){
    /*ReactiveNetwork
            .observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
        @Override
        public void onSubscribe(Disposable d) {
        }
        @Override
        public void onNext(Boolean aBoolean) {
            isInternetUp=aBoolean;
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    });*/

}

@Override
public Flowable<?> apply(final Flowable<? extends Throwable> attempts) {
    return Flowable.fromPublisher(new Publisher<Object>() {
        @Override
        public void subscribe(Subscriber<? super Object> s) {
            while (true) {
                retryCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    attempts.subscribe(s);
                    break;
                }
                if (isInternetUp || retryCount == 15) {
                    retryCount = 0;
                    s.onNext(new Object());
                }
            }
        }
    })
            .subscribeOn(Schedulers.single());
}}