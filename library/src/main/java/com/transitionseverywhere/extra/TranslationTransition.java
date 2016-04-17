package com.transitionseverywhere.extra;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionValues;
import com.transitionseverywhere.utils.AnimatorUtils;
import com.transitionseverywhere.utils.PointFProperty;

/**
 * This transition tracks changes to the translationX and translationY of
 * target views in the start and end scenes and animates change.
 * <p/>
 * Created by Andrey Kulikov on 13/03/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TranslationTransition extends Transition {

    private static final String TRANSLATION_X = "TranslationTransition:translationX";
    private static final String TRANSLATION_Y = "TranslationTransition:translationY";

    private static final PointFProperty<View> TRANSLATION_PROPERTY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TRANSLATION_PROPERTY = new PointFProperty<View>() {

                @Override
                public void set(View object, PointF value) {
                    object.setTranslationX(value.x);
                    object.setTranslationY(value.y);
                }

                @Override
                public PointF get(View object) {
                    return new PointF(object.getTranslationX(), object.getTranslationY());
                }
            };
        } else {
            TRANSLATION_PROPERTY = null;
        }
    }

    public TranslationTransition() {
    }

    public TranslationTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view != null) {
            transitionValues.values.put(TRANSLATION_X, transitionValues.view.getTranslationX());
            transitionValues.values.put(TRANSLATION_Y, transitionValues.view.getTranslationY());
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues != null && endValues != null && TRANSLATION_PROPERTY != null) {
            return AnimatorUtils.ofPointF(endValues.view, TRANSLATION_PROPERTY, getPathMotion(),
                    (float) startValues.values.get(TRANSLATION_X),
                    (float) startValues.values.get(TRANSLATION_Y),
                    (float) endValues.values.get(TRANSLATION_X),
                    (float) endValues.values.get(TRANSLATION_Y));
        } else {
            return null;
        }
    }

}