package com.gavin.view.flexible.util;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * Created by gavin
 * date 2018/6/12
 */
public class PullAnimatorUtil {

    /**
     * @param headerView
     * @param headerHeight
     * @param offsetY
     */
    public static void pullAnimator(View headerView, int headerHeight, int headerWidth, int offsetY, int maxHeight) {
        if (headerView == null) {
            return;
        }
        int pullOffset = (int) Math.pow(offsetY, 0.9);
        int newHeight = Math.min(maxHeight + headerHeight, pullOffset + headerHeight);
        int newWidth = (int) ((((float) newHeight / headerHeight)) * headerWidth);
        headerView.getLayoutParams().height = newHeight;
        headerView.getLayoutParams().width = newWidth;
        int margin = (newWidth - headerWidth) / 2;
        headerView.setTranslationX(-margin);
        headerView.requestLayout();
    }

    /**
     * 高度重置动画
     *
     * @param headerView
     * @param headerHeight
     */
    public static void resetAnimator(final View headerView, final int headerHeight, int headerWidth) {
        if (headerView == null) {
            return;
        }
        ValueAnimator heightAnimator = ValueAnimator.ofInt(headerView.getLayoutParams().height, headerHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int)animation.getAnimatedValue();
                headerView.getLayoutParams().height = height;
            }
        });
        ValueAnimator widthAnimator = ValueAnimator.ofInt(headerView.getLayoutParams().width, headerWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int)animation.getAnimatedValue();
                headerView.getLayoutParams().width = width;
            }
        });
        ValueAnimator translationAnimator = ValueAnimator.ofInt((int) headerView.getTranslationX(), 0);
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int translation = (int)animation.getAnimatedValue();
                headerView.setTranslationX(translation);
                headerView.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.setDuration(100);
        set.play(heightAnimator).with(widthAnimator).with(translationAnimator);
        set.start();

    }

}
