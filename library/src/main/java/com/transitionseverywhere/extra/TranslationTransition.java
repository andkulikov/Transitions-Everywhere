/*
 * Copyright (C) 2016 Andrey Kulikov (andkulikov@gmail.com)
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

    private void captureValues(android.support.transition.TransitionValues transitionValues) {
        if (transitionValues.view != null) {
            transitionValues.values.put(TRANSLATION_X, transitionValues.view.getTranslationX());
            transitionValues.values.put(TRANSLATION_Y, transitionValues.view.getTranslationY());
        }
    }

    @Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   android.support.transition.TransitionValues startValues,
                                   android.support.transition.TransitionValues endValues) {
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