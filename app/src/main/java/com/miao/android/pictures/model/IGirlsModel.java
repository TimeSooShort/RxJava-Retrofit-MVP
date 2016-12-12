package com.miao.android.pictures.model;

import com.miao.android.pictures.bean.GirlsBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface IGirlsModel {

    interface LoadGirlsCallback {
        void loadedGirls(GirlsBean dataBean);

        void requestError();
    }

    void getGirls(int page, int count, LoadGirlsCallback callback);

    void getGirl(int page, int count, LoadGirlsCallback callback);
}
