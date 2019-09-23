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
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

import com.transitionseverywhere.utils.TransitionUtils;

/**
 * This transition tracks changes to the translationX and translationY of
 * target views in the start and end scenes and animates change.
 * <p/>
 * Created by Andrey Kulikov on 13/03/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Translation extends Transition {

    private static final String TRANSLATION_X = "Translation:translationX";
    private static final String TRANSLATION_Y = "Translation:translationY";
    private static final String[] sTransitionProperties = {
            TRANSLATION_X,
            TRANSLATION_Y
    };

    @Nullable
    private static final Property<View, PointF> TRANSLATION_PROPERTY;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            TRANSLATION_PROPERTY = new Property<View, PointF>(PointF.class, "translation") {

                @Override
                public void set(@NonNull View object, @NonNull PointF value) {
                    object.setTranslationX(value.x);
                    object.setTranslationY(value.y);
                }

                @Override
                public PointF get(@NonNull View object) {
                    return new PointF(object.getTranslationX(), object.getTranslationY());
                }
            };
        } else {
            TRANSLATION_PROPERTY = null;
        }
    }

    public Translation() {
    }

    public Translation(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    @Nullable
    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(@NonNull TransitionValues transitionValues) {
        transitionValues.values.put(TRANSLATION_X, transitionValues.view.getTranslationX());
        transitionValues.values.put(TRANSLATION_Y, transitionValues.view.getTranslationY());
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues,
                                   @Nullable TransitionValues endValues) {
        if (startValues != null && endValues != null) {
            float startX = (float) startValues.values.get(TRANSLATION_X);
            float startY = (float) startValues.values.get(TRANSLATION_Y);
            float endX = (float) endValues.values.get(TRANSLATION_X);
            float endY = (float) endValues.values.get(TRANSLATION_Y);
            endValues.view.setTranslationX(startX);
            endValues.view.setTranslationY(startY);
            if (Build.VERSION.SDK_INT >= 21 && TRANSLATION_PROPERTY != null) {
                Path path = getPathMotion().getPath(startX, startY, endX, endY);
                return ObjectAnimator.ofObject(endValues.view, TRANSLATION_PROPERTY, null, path);
            } else {
                Animator x = (startX == endX) ? null :
                        ObjectAnimator.ofFloat(endValues.view, View.TRANSLATION_X, startX, endX);
                Animator y = (startY == endY) ? null :
                        ObjectAnimator.ofFloat(endValues.view, View.TRANSLATION_Y, startY, endY);
                return TransitionUtils.mergeAnimators(x, y);
            }
        } else {
            return null;
        }
    }

}