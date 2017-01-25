/*
 * Copyright (C) 2014 The Android Open Source Project
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
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;
import android.view.View;

import com.transitionseverywhere.utils.AnimatorUtils;
import com.transitionseverywhere.utils.PointFProperty;

/**
 * This class is used by Slide and Explode to create an animator that goes from the start
 * position to the end position. It takes into account the canceled position so that it
 * will not blink out or shift suddenly when the transition is interrupted.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TranslationAnimationCreator {

    public static final PointFProperty<View> TRANSLATIONS_PROPERTY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TRANSLATIONS_PROPERTY = new PointFProperty<View>() {
                @Override
                public void set(View view, PointF topLeft) {
                    view.setTranslationX(topLeft.x);
                    view.setTranslationY(topLeft.y);
                }
            };
        } else {
            TRANSLATIONS_PROPERTY = null;
        }
    }

    /**
     * Creates an animator that can be used for x and/or y translations. When interrupted,
     * it sets a tag to keep track of the position so that it may be continued from position.
     *
     * @param view The view being moved. This may be in the overlay for onDisappear.
     * @param values The values containing the view in the view hierarchy.
     * @param viewPosX The x screen coordinate of view
     * @param viewPosY The y screen coordinate of view
     * @param startX The start translation x of view
     * @param startY The start translation y of view
     * @param endX The end translation x of view
     * @param endY The end translation y of view
     * @param interpolator The interpolator to use with this animator.
     * @return An animator that moves from (startX, startY) to (endX, endY) unless there was
     * a previous interruption, in which case it moves from the current position to (endX, endY).
     */
    public static Animator createAnimation(View view, android.support.transition.TransitionValues values, int viewPosX, int viewPosY,
                                    float startX, float startY, float endX, float endY, TimeInterpolator interpolator,
                                    Transition transition) {
        float terminalX = view.getTranslationX();
        float terminalY = view.getTranslationY();
        int[] startPosition = (int[]) values.view.getTag(R.id.transitionPosition);
        if (startPosition != null) {
            startX = startPosition[0] - viewPosX + terminalX;
            startY = startPosition[1] - viewPosY + terminalY;
        }
        // Initial position is at translation startX, startY, so position is offset by that amount
        int startPosX = viewPosX + Math.round(startX - terminalX);
        int startPosY = viewPosY + Math.round(startY - terminalY);

        view.setTranslationX(startX);
        view.setTranslationY(startY);
        Animator anim = AnimatorUtils.ofPointF(view, TRANSLATIONS_PROPERTY,
                startX, startY, endX, endY);

        if (anim != null) {
            TransitionPositionListener listener = new TransitionPositionListener(view, values.view,
                    startPosX, startPosY, terminalX, terminalY);
            transition.addListener(listener);
            anim.addListener(listener);
            AnimatorUtils.addPauseListener(anim, listener);
            anim.setInterpolator(interpolator);
        }
        return anim;
    }

    private static class TransitionPositionListener extends AnimatorListenerAdapter implements
            Transition.TransitionListener {

        private final View mViewInHierarchy;
        private final View mMovingView;
        private final int mStartX;
        private final int mStartY;
        private int[] mTransitionPosition;
        private float mPausedX;
        private float mPausedY;
        private final float mTerminalX;
        private final float mTerminalY;

        private TransitionPositionListener(View movingView, View viewInHierarchy,
                                           int startX, int startY, float terminalX, float terminalY) {
            mMovingView = movingView;
            mViewInHierarchy = viewInHierarchy;
            mStartX = startX - Math.round(mMovingView.getTranslationX());
            mStartY = startY - Math.round(mMovingView.getTranslationY());
            mTerminalX = terminalX;
            mTerminalY = terminalY;
            mTransitionPosition = (int[]) mViewInHierarchy.getTag(R.id.transitionPosition);
            if (mTransitionPosition != null) {
                mViewInHierarchy.setTag(R.id.transitionPosition, null);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (mTransitionPosition == null) {
                mTransitionPosition = new int[2];
            }
            mTransitionPosition[0] = Math.round(mStartX + mMovingView.getTranslationX());
            mTransitionPosition[1] = Math.round(mStartY + mMovingView.getTranslationY());
            mViewInHierarchy.setTag(R.id.transitionPosition, mTransitionPosition);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
        }

        @Override
        public void onAnimationPause(Animator animator) {
            mPausedX = mMovingView.getTranslationX();
            mPausedY = mMovingView.getTranslationY();
            mMovingView.setTranslationX(mTerminalX);
            mMovingView.setTranslationY(mTerminalY);
        }

        @Override
        public void onAnimationResume(Animator animator) {
            mMovingView.setTranslationX(mPausedX);
            mMovingView.setTranslationY(mPausedY);
        }

        @Override
        public void onTransitionStart(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            mMovingView.setTranslationX(mTerminalX);
            mMovingView.setTranslationY(mTerminalY);
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }
    }

}
