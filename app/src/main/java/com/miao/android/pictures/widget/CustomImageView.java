package com.miao.android.pictures.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/12/9.
 */

/**
 * 失败了， 该种方法好像不适合网络加载图片情况
 */

public class CustomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener{

    private static final String TAG = "CustomImageView";

    //用于判断是否第一次加载
    private boolean mFirst = false;

    private Matrix mScaleMatrix;


    //图片初始化比例
    private float mInitScale;

    //图片双击放大比例
    private float mMidScale;
    //图片最大放大比例
    private float mMaxScale;
    //最小缩放比例
    private float mMinScale;
    //最大缩放比例
    private float mMaxOverScale;

    //--------自由移动----
    private int mLastPointerCount;
    private float mLastX;
    private float mLastY;
    private boolean isCanDrag;
    private int mTouchSlop;
    //检查左右是否能移动
    private boolean isCheckLeftAndRight;
    //检查上下是否能移动
    private boolean isCheckTopAndBottom;

    //--------双击
    private GestureDetector mGestureDetector;
    //是否正在自动放大缩小
    private boolean isAutoScale;

    //---------惯性滑动
    private VelocityTracker mVelocityTracker;
    private FlingRunable mFlingRunable;

    /**
     * 捕获用户多指触控时缩放的比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    public CustomImageView(Context context) {
        super(context, null, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setScaleType(ScaleType.MATRIX);

        mScaleMatrix = new Matrix();

        mScaleGestureDetector = new ScaleGestureDetector(context, this);

        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale)
                    return true;
                float x = e.getX();
                float y = e.getY();

                if (getScale() < mMidScale){
                    post(new AutoScaleRunnable(mMidScale, x, y));
                }else{
                    post(new AutoScaleRunnable(mInitScale, x, y));
                }
                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!mFirst){
            mFirst = true;

            int width = getWidth();
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null){
                return;
            }

            //得到图片原始宽高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            //图片缩放值
            float scale = 1.0f;

            if ((dw > width) && (dh < height)){
                scale = width * 1.0f / dw;
            }else if ((dw < width) && (dh > height)){
                scale = height * 1.0f / dh;
            }else{
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            //图片左上角在控件（0，0）位置
            int dx = width/2 - dw/2;
            int dy = height/2 - dh/2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(scale, scale, width/2, height/2);
            setImageMatrix(mScaleMatrix);

            mInitScale = scale;
            mMinScale = scale/4;
            mMidScale = scale*2;
            mMaxScale = scale*4;
            mMaxOverScale = mMaxScale*5;
        }
    }

    /**
     * 自动放大缩小，每次只让scale变化一定值，从而实现平滑的双击变化效果
     */
    private class AutoScaleRunnable implements Runnable{

        private float mTargetScale;
        private float tempScale;

        //放大缩小的中心
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            mTargetScale = targetScale;
            this.x = x;
            this.y = y;

            if (getScale() > mTargetScale){
                tempScale = SMALLER;
            }
            if (getScale() < mTargetScale){
                tempScale = BIGGER;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();
            //判断重复条件
            if ((tempScale > 1.0f && currentScale < mTargetScale)
                    || (tempScale < 1.0f && currentScale > mTargetScale)){
                //直到达到目标缩放值前每16毫秒缩放一次
                postDelayed(this, 16);
            }else{
                //由于浮点数计算过程中的误差，保证缩放值与目标一致
                float scale = mTargetScale/currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }

    private class FlingRunable implements Runnable{

        private Scroller mScroller;
        private int mCurrentX, mCurrentY;

        public FlingRunable(Context context) {
            mScroller = new Scroller(context);
        }

        public void cancelFling(){
            mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY){
            RectF rectF = getMatrixRectF();
            if (rectF == null){
                return;
            }
            final int startX = Math.round(-rectF.left);
            final int minX, minY, maxX, maxY;
            if (rectF.width() > viewWidth){
                minX = 0;
                maxX = Math.round(rectF.width() - viewWidth);
            }else{
                minX = maxX = startX;
            }

            final int startY = Math.round(-rectF.top);
            if (rectF.height() > viewHeight){
                minY = 0;
                maxY = Math.round(rectF.height() - viewHeight);
            }else{
                minY = maxY = startY;
            }

            mCurrentX = startX;
            mCurrentY = startY;

            if (startX != maxX || startY != maxY){
                mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        /**
         * 由Scroller来计算坐标位置，每16毫秒调用直到滑动结束
         */
        @Override
        public void run() {
            if (mScroller.isFinished()){
                return;
            }
            if (mScroller.computeScrollOffset()){
                //每次getCurrX（）得到的值=mStartX + 从开始到现在的时间移动的距离
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

                //Scroller是计算位置的工具类，真正移动图片要靠Matrix
                mScaleMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                checkBorderWhenTranslate();
                setImageMatrix(mScaleMatrix);

                mCurrentX = newX;
                mCurrentY = newY;

                postDelayed(this, 16);
            }
        }
    }

    /**
     * 获得图片当前的缩放比例
     * @return
     */
    private float getScale(){
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        //放大手势值大于1
        float mScaleFactor= scaleGestureDetector.getScaleFactor();
        float scale = getScale();

        if (getDrawable() == null){
            return true;
        }

        if (((mScaleFactor > 1.0f)&&(scale*mScaleFactor < mMaxOverScale))
                || ((mScaleFactor < 1.0f)&&(scale*mScaleFactor > mMinScale))){
            if (scale*mScaleFactor > mMaxOverScale + 0.01f){
                mScaleFactor = mMaxOverScale/scale;
            }
            if (scale * mScaleFactor < mMinScale + 0.01f){
                mScaleFactor = mMinScale/scale;
            }

            mScaleMatrix.postScale(mScaleFactor, mScaleFactor, scaleGestureDetector.getFocusX(),
                    scaleGestureDetector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /**
     * 通过矩形来操作图片
     * @return
     */
    private RectF getMatrixRectF(){
        //获得当前图片的矩阵
        Matrix matrix = mScaleMatrix;
        //创建一个矩形
        RectF rectF = new RectF();
        //得到当前图片
        Drawable d = getDrawable();
        if (d != null){
            //使得矩形的宽高与当前图片大小一致
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            //将矩阵映射到矩形
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 当缩放时检查边界并使图片居中
     */
    private void checkBorderAndCenterWhenScale() {
        if (getDrawable() == null){
            return;
        }
        //水平竖直方向上的偏移量
        float delX = 0.0f;
        float delY = 0.0f;

        int width = getWidth();
        int height = getHeight();

        RectF rectF = getMatrixRectF();
        //当图片宽度大于控件宽度时
        if (rectF.width() >= width){
            //左侧白边
            if (rectF.left > 0){
                delX = -rectF.left;
            }
            //右侧白边
            if (rectF.right < width){
                delX = width - rectF.right;
            }
        }
        if (rectF.height() >= height){
            if (rectF.top > 0){
                delY = -rectF.top;
            }
            if (rectF.bottom < height){
                delY = height - rectF.bottom;
            }
        }
        //水平居中
        if (rectF.width() < width){
            delX = width/2f - rectF.right + rectF.width()/2f;
        }

        //竖直居中
        if (rectF.height() < height){
            delY = height/2f - rectF.bottom + rectF.height()/2f;
        }
        mScaleMatrix.postTranslate(delX, delY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (mGestureDetector.onTouchEvent(motionEvent)){
            return true;
        }

        //将事件传递给ScaleGestureDetector
        mScaleGestureDetector.onTouchEvent(motionEvent);

        float x = 0.0f;
        float y = 0.0f;

        int mPointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < mPointerCount; i++){
            x += motionEvent.getX(i);
            y += motionEvent.getY(i);
        }

        x /= mPointerCount;
        y /= mPointerCount;

        if (mPointerCount != mLastPointerCount){
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        mLastPointerCount = mPointerCount;
        RectF rectF = getMatrixRectF();
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01){
                    if (getParent() instanceof ViewPager){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                mVelocityTracker = VelocityTracker.obtain();
                if (mVelocityTracker != null){
                    mVelocityTracker.addMovement(motionEvent);
                }

                if (mFlingRunable != null){
                    mFlingRunable.cancelFling();
                    mFlingRunable = null;
                }
                isCanDrag = false;

                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01){
                    if (getParent() instanceof ViewPager){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag){
                    isCanDrag = isMotionEvent(dx, dy);
                }

                if (isCanDrag){
                    if (getDrawable() != null){

                        if (mVelocityTracker != null){
                            mVelocityTracker.addMovement(motionEvent);
                        }

                        isCheckLeftAndRight = true;
                        isCheckTopAndBottom = true;
                        //图片宽小于控件宽时禁止移动
                        if (rectF.width() < getWidth()){
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        if (rectF.height() < getHeight()){
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mLastPointerCount = 0;

                if (getScale() < mInitScale){
                    post(new AutoScaleRunnable(mInitScale, getWidth()/2, getHeight()/2));
                }
                if (getScale() > mMaxScale){
                    post(new AutoScaleRunnable(mMaxScale, getWidth()/2, getHeight()/2));
                }

                if (isCanDrag){
                    if (mVelocityTracker != null){
                        mVelocityTracker.addMovement(motionEvent);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        final float vX = mVelocityTracker.getXVelocity();
                        final float vY = mVelocityTracker.getYVelocity();

                        mFlingRunable = new FlingRunable(getContext());
                        mFlingRunable.fling(getWidth(), getHeight(), (int)-vX, (int)-vY);

                        post(mFlingRunable);
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:

                if (mVelocityTracker != null){
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                }

                break;
        }
        return true;
    }

    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0.0f;
        float deltaY = 0.0f;

        //解决左右出现白边的情况
        if (isCheckLeftAndRight){
            if (rectF.left < 0){
                deltaX = -rectF.left;
            }
            if (rectF.right < getWidth()){
                deltaX = getWidth() - rectF.right;
            }
        }

        //解决上下出现白边的情况
        if (isCheckTopAndBottom){
            if (rectF.top > 0){
                deltaY = -rectF.top;
            }
            if (rectF.bottom < getHeight()){
                deltaY = getHeight() - rectF.bottom;
            }
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 判断是否是移动操作
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMotionEvent(float dx, float dy){
        return Math.sqrt(dx*dx + dy*dy) > mTouchSlop;
    }
}
