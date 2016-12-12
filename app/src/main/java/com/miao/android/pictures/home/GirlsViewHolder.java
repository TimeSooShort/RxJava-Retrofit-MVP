package com.miao.android.pictures.home;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.miao.android.pictures.R;
import com.miao.android.pictures.bean.GirlsBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public class GirlsViewHolder extends BaseViewHolder<GirlsBean.ResultsBean> {

    private ImageView mImageView;

    public GirlsViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_girl);
        mImageView = $(R.id.girl_img);
    }

    @Override
    public void setData(GirlsBean.ResultsBean data) {
        super.setData(data);
        Glide.with(getContext())
                .load(data.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }
}
