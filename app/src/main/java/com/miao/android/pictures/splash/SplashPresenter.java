package com.miao.android.pictures.splash;

import com.miao.android.pictures.bean.GirlsBean;
import com.miao.android.pictures.model.GirlsModel;
import com.miao.android.pictures.model.IGirlsModel;

/**
 * Created by Administrator on 2016/12/12.
 */

public class SplashPresenter implements ISplash.SplashPresenter {

    private ISplash.SplashView mView;
    private IGirlsModel mModel;

    public SplashPresenter(ISplash.SplashView view) {
        mView = view;
        mModel = new GirlsModel();
    }

    @Override
    public void start() {
        mModel.getGirl(1, 1, new IGirlsModel.LoadGirlsCallback() {
            @Override
            public void loadedGirls(GirlsBean dataBean) {
                mView.showGirl(dataBean.getResults().get(0).getUrl());
            }

            @Override
            public void requestError() {
                mView.showGirl();
            }
        });
    }
}
