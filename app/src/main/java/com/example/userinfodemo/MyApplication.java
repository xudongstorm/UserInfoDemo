package com.example.userinfodemo;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = getApplicationContext();
    }

    public static Context getAppContext(){
        return mApp;
    }
}
