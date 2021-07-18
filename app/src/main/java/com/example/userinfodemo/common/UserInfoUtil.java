package com.example.userinfodemo.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.disposables.Disposable;

public class UserInfoUtil {

    public static String mapToJson(Object map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(map);
    }

    public static void unsubscribe(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
