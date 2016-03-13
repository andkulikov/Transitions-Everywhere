package com.transitionseverywhere.extra;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.R;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionUtils;
import com.transitionseverywhere.TransitionValues;
import com.transitionseverywhere.Visibility;
import com.transitionseverywhere.utils.AnimatorUtils;

/**
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and scales views up or down. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are
 * limited as described in {@link Visibility#onDisappear(android.view.ViewGroup,
 * TransitionValues, int, TransitionValues, int)}.
 * <p/>
 * Created by Andrey Kulikov on 13/03/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Scale extends Visibility {

    private float mDisappearedScale = 0f;

    public Scale() {
    }

    /**
     * @param disappearedScale Value of scale on start of appearing or in finish of disappearing.
     *                         Default value is 0. Can be useful for mixing some Visibility
     *                         transitions, for example Scale and Fade
     */
    public Scale(float disappearedScale) {
        setDisappearedScale(disappearedScale);
    }

    /**
     * @param disappearedScale Value of scale on start of appearing or in finish of disappearing.
     *                         Default value is 0. Can be useful for mixing some Visibility
     *                         transitions, for example Scale and Fade
     * @return This Scale object.
     */
    public Scale setDisappearedScale(float disappearedScale) {
        if (disappearedScale < 0f) {
            throw new IllegalArgumentException("disappearedScale cannot be negative!");
        }
        mDisappearedScale = disappearedScale;
        return this;
    }

    public Scale(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Scale);
        setDisappearedScale(a.getFloat(R.styleable.Scale_disappearedScale, mDisappearedScale));
        a.recycle();
    }

    private Animator createAnimation(final View view, float startFraction, float endFraction) {
        return TransitionUtils.mergeAnimators(
                AnimatorUtils.ofFloat(view, View.SCALE_X, startFraction, endFraction),
                AnimatorUtils.ofFloat(view, View.SCALE_Y, startFraction, endFraction));
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             TransitionValues startValues,
                             TransitionValues endValues) {
        return createAnimation(view, mDisappearedScale, 1f);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        Animator animator = createAnimation(view, 1f, mDisappearedScale);
        if (animator != null) {
            final float initialScaleX = view.getScaleX();
            final float initialScaleY = view.getScaleY();
            addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    view.setScaleX(initialScaleX);
                    view.setScaleY(initialScaleY);
                }
            });
        }
        return animator;
    }

}
