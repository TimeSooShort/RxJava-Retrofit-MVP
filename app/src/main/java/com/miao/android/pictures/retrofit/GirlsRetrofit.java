package com.miao.android.pictures.retrofit;

import com.miao.android.pictures.app.Contants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/7.
 */

public class GirlsRetrofit {

    private static Retrofit retrofit;

    public static Retrofit getGirlsRetrofit(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        if (retrofit == null){
            synchronized (GirlsRetrofit.class){
                if (retrofit == null){
                    retrofit = new Retrofit.Builder()
                    .baseUrl(Contants.GANHUO_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
                }
            }
        }
        return retrofit;
    }
}
