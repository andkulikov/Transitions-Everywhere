package com.transitionseverywhere;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

/**
 * Animates changes of textSize on all views that are instances of TextView
 */
public class ChangeTextSize extends Transition {

    private static final String PROP_NAME = "ChangeTextSize.textSize";

    public ChangeTextSize() {
        super();
    }

    public ChangeTextSize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROP_NAME, ((TextView) transitionValues.view).getTextSize());
        }
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        if (startValues == null || endValues == null || !(startValues.view instanceof TextView) || !(endValues.view instanceof TextView))
            return null;

        float start = (float) startValues.values.get(PROP_NAME);
        float end = (float) endValues.values.get(PROP_NAME);
        if (start == end) return null;

        final TextView view = (TextView) endValues.view;
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, (Float) animation.getAnimatedValue());
            }
        });
        return animator;
    }
}
