package com.miao.android.pictures.girl;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.miao.android.pictures.R;
import com.miao.android.pictures.bean.GirlsBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/11.
 */

public class GirlViewAdapter extends PagerAdapter {

    private ArrayList<GirlsBean.ResultsBean> girlsDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;

    public GirlViewAdapter(Context context, ArrayList<GirlsBean.ResultsBean> datas) {
        girlsDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return girlsDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String imageUrl = girlsDatas.get(position).getUrl();
        View view = mInflater.inflate(R.layout.item_girl_detail, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.custom_image);
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem(){
        return mCurrentView;
    }

}
