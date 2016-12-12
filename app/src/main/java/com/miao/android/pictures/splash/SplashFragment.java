package com.miao.android.pictures.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miao.android.mylibrary.ActivityManager;
import com.miao.android.mylibrary.BaseFragment;
import com.miao.android.pictures.R;
import com.miao.android.pictures.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/12/12.
 */

public class SplashFragment extends BaseFragment implements ISplash.SplashView{

    @BindView(R.id.splash_image)
    ImageView mSplashImage;

    private Unbinder mUnbinder;
    private ISplash.SplashPresenter mPresenter;

    private ScaleAnimation mScaleAnimation;

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new SplashPresenter(SplashFragment.this);

        initAnim();

        mPresenter.start();
    }

    private void initAnim() {
        mScaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setFillAfter(true);
        mScaleAnimation.setDuration(3000);
        mSplashImage.startAnimation(mScaleAnimation);

        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(mActivity, HomeActivity.class));
                ActivityManager.getInstance().finishActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.splash_fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showGirl(String url) {
        Glide.with(mActivity)
                .load(url)
                .animate(mScaleAnimation)
                .into(mSplashImage);
    }

    @Override
    public void showGirl() {
        Glide.with(mActivity)
                .load(R.drawable.welcome)
                .animate(mScaleAnimation)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mSplashImage);
    }
}
