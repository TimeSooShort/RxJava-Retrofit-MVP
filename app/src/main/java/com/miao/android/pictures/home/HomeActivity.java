package com.miao.android.pictures.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.miao.android.mylibrary.AppActivity;
import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/5.
 */

public class HomeActivity extends AppActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected BaseFragment getFirstFragment() {
        return new GirlsFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
    }
}
