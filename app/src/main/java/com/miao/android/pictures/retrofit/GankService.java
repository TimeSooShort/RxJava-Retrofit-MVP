package com.miao.android.pictures.retrofit;

import com.miao.android.pictures.bean.GirlsBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface GankService {
    @GET("api/data/{type}/{count}/{page}")
    Observable<GirlsBean> getGirlsData(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page);
}
