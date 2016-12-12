package com.miao.android.pictures.presenter;

import com.miao.android.pictures.bean.GirlsBean;
import com.miao.android.pictures.model.GirlsModel;
import com.miao.android.pictures.model.IGirlsModel;

/**
 * Created by Administrator on 2016/12/7.
 */

public class HomePresenter implements GirlsContract.IHomePresenter {

    private GirlsContract.homeView view;
    private IGirlsModel girlsModel;

    public HomePresenter(GirlsContract.homeView view) {
        this.view = view;
        girlsModel = new GirlsModel();
    }

    @Override
    public void start() {
        requestGirls(1, 20, true);
    }

    @Override
    public void requestGirls(int page, int count, final boolean isRefreshing) {
        girlsModel.getGirls(page, count, new IGirlsModel.LoadGirlsCallback() {
            @Override
            public void loadedGirls(GirlsBean dataBean) {
                if (isRefreshing){
                    view.refresh(dataBean.getResults());
                }else{
                    view.load(dataBean.getResults());
                }
                view.showHaveNetwork();
            }

            @Override
            public void requestError() {
                view.showNoNetwork();
            }
        });
    }
}
