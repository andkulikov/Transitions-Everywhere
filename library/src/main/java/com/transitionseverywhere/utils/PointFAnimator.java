package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;

/**
 * Copyright New Cloud Technology, Ltd, 2014
 * <p/>
 * NOTICE:  The intellectual and technical concepts contained herein are proprietary to New Cloud Technology, Ltd, and is protected by trade secret and copyright law. Dissemination of any of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from New Cloud Technology, Ltd.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PointFAnimator extends BasePointFAnimator {

    private float mStartTop, mStartLeft, mEndTop, mEndLeft;

    protected PointFAnimator(Object target, PointFProperty pointFProperty) {
        super(target, pointFProperty);
    }

    public static <T> PointFAnimator ofPointF(T target, PointFProperty<T> property, float startLeft,
                                              float startTop, float endLeft, float endTop) {
        PointFAnimator animator = null;
        if (target != null && property != null) {
            animator = new PointFAnimator(target, property);
            animator.mStartLeft = startLeft;
            animator.mStartTop = startTop;
            animator.mEndLeft = endLeft;
            animator.mEndTop = endTop;
        }
        return animator;
    }

    protected void applyAnimatedFraction(PointF holder, float fraction) {
        holder.x = interpolate(fraction, mStartLeft, mEndLeft);
        holder.y = interpolate(fraction, mStartTop, mEndTop);
    }

    protected static float interpolate(float fraction, float startValue, float endValue) {
        float diff = endValue - startValue;
        return startValue + (diff * fraction);
    }

}
