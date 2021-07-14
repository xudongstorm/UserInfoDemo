package com.example.userinfodemo.net;

import com.example.userinfodemo.common.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private volatile static RetrofitManager mInstance;
    private Retrofit mRetrofit;
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private RetrofitManager(){
        init();
    }

    public static RetrofitManager getInstance(){
        if(mInstance == null){
            synchronized (RetrofitManager.class){
                if(mInstance == null){
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    private void init(){
        // 创建 OKHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT,  TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
    }

    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    public UsersService createUsersService(){
        return mRetrofit.create(UsersService.class);
    }

}
