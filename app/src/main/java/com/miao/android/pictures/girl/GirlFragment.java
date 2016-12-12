package com.miao.android.pictures.girl;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;
import com.miao.android.pictures.bean.GirlsBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/12/11.
 */

public class GirlFragment extends BaseFragment {

    @BindView(R.id.girl_view_pager)
    ViewPager mGirlViewPager;

    private Unbinder mUnbinder;

    private ArrayList<GirlsBean.ResultsBean> datas;
    private int current;

    private GirlViewAdapter mAdapter;

    public static GirlFragment getInstance(ArrayList<Parcelable> datas, int current){
        Bundle args = new Bundle();
        args.putParcelableArrayList("girls", datas);
        args.putInt("current", current);
        GirlFragment fragment = new GirlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null){
            datas = bundle.getParcelableArrayList("girls");
            current = bundle.getInt("current");
        }

        mAdapter = new GirlViewAdapter(mActivity, datas);
        mGirlViewPager.setAdapter(mAdapter);
        mGirlViewPager.setCurrentItem(current);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.girl_viewpager;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
