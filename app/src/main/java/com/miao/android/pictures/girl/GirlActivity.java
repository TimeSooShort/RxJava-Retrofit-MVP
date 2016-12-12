package com.miao.android.pictures.girl;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miao.android.mylibrary.AppActivity;
import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/11.
 */

public class GirlActivity extends AppActivity {

    @BindView(R.id.girl_toolbar)
    Toolbar mGirlToolbar;

    private GirlFragment mGirlFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        mGirlFragment = GirlFragment.getInstance(getIntent().getParcelableArrayListExtra("girls"),
                getIntent().getIntExtra("current", 0));
        return mGirlFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_girl;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.girl_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mGirlToolbar.setTitle(R.string.meizhi);
        setSupportActionBar(mGirlToolbar);
        mGirlToolbar.setNavigationIcon(R.drawable.ic_back);
        mGirlToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
