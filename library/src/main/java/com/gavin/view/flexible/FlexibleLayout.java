package com.gavin.view.flexible;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.gavin.view.flexible.callback.OnPullListener;
import com.gavin.view.flexible.callback.OnReadyPullListener;
import com.gavin.view.flexible.util.PullAnimatorUtil;

/**
 * Created by gavin
 * date 2018/6/12
 * 带有下拉放大效果的FrameLayout
 */
public class FlexibleLayout extends LinearLayout implements IFlexible {

    /**
     * 是否允许下拉放大
     */
    private boolean isEnable = true;

    /**
     * 头部高度
     */
    private int mHeaderHeight = 0;

    /**
     * 头部宽度
     */
    private int mHeaderWidth = 0;

    /**
     * 头部size ready
     */
    private boolean mHeaderSizeReady;

    /**
     * 头部
     */
    private View mHeaderView;

    /**
     * 最大下拉高度
     */
    private int mMaxPullHeight = 200;

    /**
     * true 开始下拽
     */
    private boolean mIsBeingDragged;

    /**
     * 准备下拉监听
     */
    private OnReadyPullListener mListener;

    /**
     * 初始坐标
     */
    private float mInitialY, mInitialX;

    /**
     * 下拉监听
     */
    private OnPullListener mOnPullListener;

    public FlexibleLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlexibleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeaderSizeReady = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent");
        if (isEnable && isHeaderReady() && isReady()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    log("onInterceptTouchEvent DOWN");
                    mInitialX = ev.getX();
                    mInitialY = ev.getY();
                    mIsBeingDragged = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    log("onInterceptTouchEvent MOVE");
                    float diffY = ev.getY() - mInitialY;
                    float diffX = ev.getX() - mInitialX;
                    if (diffY > 0 && diffY / Math.abs(diffX) > 2) {
                        mIsBeingDragged = true;
                        log("onInterceptTouchEvent return true");
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        log("onTouchEvent");
        if (isEnable && isHeaderReady() && isReady()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (mIsBeingDragged) {
                        float diffY = ev.getY() - mInitialY;
                        changeHeader((int) diffY);
                        if (mOnPullListener != null) {
                            mOnPullListener.onPull((int) diffY);
                        }
                        log("onTouchEvent return true");
                        //return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mIsBeingDragged) {
                        resetHeader();
                        if (mOnPullListener != null) {
                            mOnPullListener.onRelease();
                        }
                        return true;
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean isReady() {
        return mListener != null && mListener.isReady();
    }

    @Override
    public boolean isHeaderReady() {
        return mHeaderView != null && mHeaderSizeReady;
    }

    @Override
    public void changeHeader(int offsetY) {
        PullAnimatorUtil.pullAnimator(mHeaderView, mHeaderHeight, mHeaderWidth, offsetY, mMaxPullHeight);
    }

    @Override
    public void resetHeader() {
        PullAnimatorUtil.resetAnimator(mHeaderView, mHeaderHeight, mHeaderWidth);
    }


    /**
     * 是否允许下拉放大
     *
     * @param isEnable
     * @return
     */
    public FlexibleLayout setEnable(boolean isEnable) {
        this.isEnable = isEnable;
        return this;
    }

    /**
     * 设置头部
     *
     * @param header
     * @return
     */
    public FlexibleLayout setHeader(View header) {
        mHeaderView = header;
        mHeaderView.post(new Runnable() {
            @Override
            public void run() {
                mHeaderHeight = mHeaderView.getHeight();
                mHeaderWidth = mHeaderView.getWidth();
                mHeaderSizeReady = true;
            }
        });
        return this;
    }

    /**
     * 最大下拉高度
     *
     * @param height
     * @return
     */
    public FlexibleLayout setMaxPullHeight(int height) {
        mMaxPullHeight = height;
        return this;
    }

    /**
     * 监听 是否可以下拉放大
     *
     * @param listener
     * @return
     */
    public FlexibleLayout setReadyListener(OnReadyPullListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置下拉监听
     *
     * @param onPullListener
     * @return
     */
    public FlexibleLayout setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
        return this;
    }

    private void log(String str) {
        //Log.i("FlexibleView", str);
    }
}
