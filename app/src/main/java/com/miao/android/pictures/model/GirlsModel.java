package com.miao.android.pictures.model;

import com.miao.android.pictures.bean.GirlsBean;
import com.miao.android.pictures.retrofit.GankService;
import com.miao.android.pictures.retrofit.GirlsRetrofit;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/7.
 */

public class GirlsModel implements IGirlsModel {

    @Override
    public void getGirls(int page, int count, final LoadGirlsCallback callback) {
        GirlsRetrofit.getGirlsRetrofit()
                .create(GankService.class)
                .getGirlsData("福利", count, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError();
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        callback.loadedGirls(girlsBean);
                    }
                });
    }

    @Override
    public void getGirl(int page, int count, LoadGirlsCallback callback) {
        getGirls(page, count, callback);
    }
}
