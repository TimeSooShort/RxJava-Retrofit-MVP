package com.miao.android.pictures.splash;

import com.miao.android.mylibrary.AppActivity;
import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;

/**
 * Created by Administrator on 2016/12/12.
 */

public class SplashActivity extends AppActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return new SplashFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.splash_container;
    }
}
