/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.transitionseverywhere;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.utils.AnimatorUtils;

/**
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and fades views in or out when they become visible
 * or non-visible. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy.
 * <p/>
 * <p>The ability of this transition to fade out a particular view, and the
 * way that that fading operation takes place, is based on
 * the situation of the view in the view hierarchy. For example, if a view was
 * simply removed from its parent, then the view will be added into a {@link
 * android.view.ViewGroupOverlay} while fading. If a visible view is
 * changed to be {@link View#GONE} or {@link View#INVISIBLE}, then the
 * visibility will be changed to {@link View#VISIBLE} for the duration of
 * the animation. However, if a view is in a hierarchy which is also altering
 * its visibility, the situation can be more complicated. In general, if a
 * view that is no longer in the hierarchy in the end scene still has a
 * parent (so its parent hierarchy was removed, but it was not removed from
 * its parent), then it will be left alone to avoid side-effects from
 * improperly removing it from its parent. The only exception to this is if
 * the previous {@link Scene} was
 * {@link Scene#getSceneForLayout(android.view.ViewGroup, int, android.content.Context)
 * created from a layout resource file}, then it is considered safe to un-parent
 * the starting scene view in order to fade it out.</p>
 * <p/>
 * <p>A Fade transition can be described in a resource file by using the
 * tag <code>fade</code>, along with the standard
 * attributes of {@link com.transitionseverywhere.R.styleable#Fade} and
 * {@link com.transitionseverywhere.R.styleable#Transition}.</p>
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Fade extends Visibility {

    static final String PROPNAME_ALPHA = "fade:alpha";

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are appearing. Maybe be combined with
     * {@link #OUT} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_IN}.
     */
    public static final int IN = Visibility.MODE_IN;

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are disappearing. Maybe be combined with
     * {@link #IN} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_OUT}.
     */
    public static final int OUT = Visibility.MODE_OUT;

    /**
     * Constructs a Fade transition that will fade targets in and out.
     */
    public Fade() {
    }

    /**
     * Constructs a Fade transition that will fade targets in
     * and/or out, according to the value of fadingMode.
     *
     * @param fadingMode The behavior of this transition, a combination of
     *                   {@link #IN} and {@link #OUT}.
     */
    public Fade(int fadingMode) {
        setMode(fadingMode);
    }

    public Fade(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fade);
        @VisibilityMode int fadingMode = a.getInt(R.styleable.Fade_fadingMode, getMode());
        a.recycle();
        setMode(fadingMode);
    }

    @Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        if (transitionValues.view != null) {
            transitionValues.values.put(PROPNAME_ALPHA, transitionValues.view.getAlpha());
        }
    }

    /**
     * Utility method to handle creating and running the Animator.
     */
    private Animator createAnimation(final View view, float startAlpha, float endAlpha, android.support.transition.TransitionValues values) {
        final float curAlpha = view.getAlpha();
        startAlpha = curAlpha * startAlpha;
        endAlpha = curAlpha * endAlpha;

        if (values != null && values.values.containsKey(PROPNAME_ALPHA)) {
            float savedAlpha = (Float) values.values.get(PROPNAME_ALPHA);
            // if saved value is not equal curAlpha it means that previous
            // transition was interrupted and in the onTransitionEnd
            // we've applied endListenerAlpha. we should apply proper value to
            // continue animation from the interrupted state
            if (savedAlpha != curAlpha) {
                startAlpha = savedAlpha;
            }
        }

        view.setAlpha(startAlpha);
        final ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.ALPHA, endAlpha);
        final FadeAnimatorListener listener = new FadeAnimatorListener(view, curAlpha);
        anim.addListener(listener);
        addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                view.setAlpha(curAlpha);
            }
        });
        return anim;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             android.support.transition.TransitionValues startValues,
                             android.support.transition.TransitionValues endValues) {
        return createAnimation(view, 0, 1, startValues);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view,
                                android.support.transition.TransitionValues startValues,
                                android.support.transition.TransitionValues endValues) {
        return createAnimation(view, 1, 0, startValues);
    }

    private static class FadeAnimatorListener extends AnimatorListenerAdapter {
        private final View mView;
        private float mEndListenerAlpha;
        private boolean mLayerTypeChanged = false;

        public FadeAnimatorListener(View view, float endListenerAlpha) {
            mView = view;
            mEndListenerAlpha = endListenerAlpha;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            if (AnimatorUtils.hasOverlappingRendering(mView) &&
                    mView.getLayerType() == View.LAYER_TYPE_NONE) {
                mLayerTypeChanged = true;
                mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mView.setAlpha(mEndListenerAlpha);
            if (mLayerTypeChanged) {
                mView.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }

    }

}