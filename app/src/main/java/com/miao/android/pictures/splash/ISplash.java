package com.miao.android.pictures.splash;

/**
 * Created by Administrator on 2016/12/12.
 */

public interface ISplash {

    interface SplashView{
        void showGirl(String url);

        //无网络情况下
        void showGirl();
    }

    interface SplashPresenter{
        void start();
    }
}
