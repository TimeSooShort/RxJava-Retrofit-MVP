package com.miao.android.pictures.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;
import com.miao.android.pictures.bean.GirlsBean;
import com.miao.android.pictures.girl.GirlActivity;
import com.miao.android.pictures.presenter.GirlsContract;
import com.miao.android.pictures.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/12/5.
 */

public class GirlsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, GirlsContract.homeView {

    @BindView(R.id.network_error_layout)
    ViewStub mNetworkErrorLayout;
    @BindView(R.id.easy_recycler)
    EasyRecyclerView mEasyRecycler;

    private Unbinder mUnbinder;

    private GirlsAdapter adapter;

    private ArrayList<GirlsBean.ResultsBean> dataList;

    private View mView;

    private GirlsContract.IHomePresenter mPresenter;

    private int page = 1;

    @Override
    public void onRefresh() {
        mPresenter.start();
        page = 1;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new HomePresenter(this);

        initRecyclerView();

        mPresenter.start();
    }

    private void initRecyclerView() {

        dataList = new ArrayList<>();

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mEasyRecycler.setLayoutManager(manager);
        adapter = new GirlsAdapter(getActivity());
        mEasyRecycler.setAdapterWithProgress(adapter);

        adapter.setMore(R.layout.load_more_layout, this);
        adapter.setNoMore(R.layout.load_no_more);
        adapter.setError(R.layout.error_layout);

        adapter.setOnMyItemClickListener(new GirlsAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                Intent intent = new Intent(mActivity, GirlActivity.class);
                intent.putParcelableArrayListExtra("girls", dataList);
                intent.putExtra("current", position);
                //过渡动画
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                        holder.itemView, holder.itemView.getWidth()/2,
                        holder.itemView.getHeight()/2, 0, 0);
                startActivity(intent, optionsCompat.toBundle());
            }
        });

        mEasyRecycler.setRefreshListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        if (dataList.size() % 20 == 0){
            page++;
            mPresenter.requestGirls(page, 20, false);
        }
    }

    @Override
    public void refresh(List<GirlsBean.ResultsBean> girlsList) {
        dataList.clear();
        dataList.addAll(girlsList);
        adapter.clear();
        adapter.addAll(girlsList);
    }

    @Override
    public void load(List<GirlsBean.ResultsBean> girlsList) {
        dataList.addAll(girlsList);
        adapter.addAll(girlsList);
    }

    @Override
    public void showHaveNetwork() {
        if (mView != null){
            mView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoNetwork() {
        if (mView != null){
            mView.setVisibility(View.VISIBLE);
            return;
        }

        mView = mNetworkErrorLayout.inflate();
    }
}
