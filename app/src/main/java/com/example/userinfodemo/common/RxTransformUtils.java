package com.example.userinfodemo.common;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxTransformUtils {

    public static <T> ObservableTransformer<T, T> schedule() {
        return resultObservable -> resultObservable
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread(), true);
    }
}
