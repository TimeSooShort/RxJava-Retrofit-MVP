package com.miao.android.pictures.app;

import android.app.Application;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyApplication extends Application {

    private static MyApplication sMyApplication;

    private static MyApplication getInstance(){
        return sMyApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;


    }
}
