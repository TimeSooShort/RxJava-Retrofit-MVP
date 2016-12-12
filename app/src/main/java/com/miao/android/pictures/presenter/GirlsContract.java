package com.miao.android.pictures.presenter;

import com.miao.android.pictures.bean.GirlsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface GirlsContract {

    interface homeView{
        void refresh(List<GirlsBean.ResultsBean> girlsList);

        void load(List<GirlsBean.ResultsBean> girlsList);

        void showHaveNetwork();

        void showNoNetwork();
    }

    interface IHomePresenter{
        void start();

        void requestGirls(int page, int count, boolean isRefreshing);
    }
}
