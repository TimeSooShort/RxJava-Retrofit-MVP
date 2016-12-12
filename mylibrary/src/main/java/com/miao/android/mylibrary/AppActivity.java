package com.miao.android.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/12/5.
 */

public abstract class AppActivity extends BaseActivity {

    protected abstract BaseFragment getFirstFragment();

    protected void handleIntent(Intent intent){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (getIntent() != null){
            handleIntent(getIntent());
        }

        if (getSupportFragmentManager().getFragments() == null){
            BaseFragment firstFragment = getFirstFragment();
            if (firstFragment != null){
                addFragment(firstFragment);
            }
        }
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
