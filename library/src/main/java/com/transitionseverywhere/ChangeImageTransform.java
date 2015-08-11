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
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.transitionseverywhere.utils.AnimatorUtils;
import com.transitionseverywhere.utils.MatrixUtils;
import com.transitionseverywhere.utils.PropertyCompatObject;

import java.util.Map;

/**
 * This Transition captures an ImageView's matrix before and after the
 * scene change and animates it during the transition.
 * <p/>
 * <p>In combination with ChangeBounds, ChangeImageTransform allows ImageViews
 * that change size, shape, or {@link android.widget.ImageView.ScaleType} to animate contents
 * smoothly.</p>
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChangeImageTransform extends Transition {

    private static final String TAG = "ChangeImageTransform";

    private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
    private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";

    private static final String[] sTransitionProperties = {
            PROPNAME_MATRIX,
            PROPNAME_BOUNDS,
    };

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (!(view instanceof ImageView) || view.getVisibility() != View.VISIBLE) {
            return;
        }
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        Map<String, Object> values = transitionValues.values;

        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);
        values.put(PROPNAME_BOUNDS, bounds);
        Matrix matrix;
        ImageView.ScaleType scaleType = imageView.getScaleType();
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            matrix = imageView.getImageMatrix();
            if (!matrix.isIdentity()) {
                matrix = new Matrix(matrix);
            } else {
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                if (drawableWidth > 0 && drawableHeight > 0) {
                    float scaleX = ((float) bounds.width()) / drawableWidth;
                    float scaleY = ((float) bounds.height()) / drawableHeight;
                    matrix = new Matrix();
                    matrix.setScale(scaleX, scaleY);
                } else {
                    matrix = null;
                }
            }
        } else {
            matrix = new Matrix(imageView.getImageMatrix());
        }
        values.put(PROPNAME_MATRIX, matrix);
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
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    /**
     * Creates an Animator for ImageViews moving, changing dimensions, and/or changing
     * {@link android.widget.ImageView.ScaleType}.
     *
     * @param sceneRoot   The root of the transition hierarchy.
     * @param startValues The values for a specific target in the start scene.
     * @param endValues   The values for the target in the end scene.
     * @return An Animator to move an ImageView or null if the View is not an ImageView,
     * the Drawable changed, the View is not VISIBLE, or there was no change.
     */
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);
        if (startBounds == null || endBounds == null) {
            return null;
        }

        Matrix startMatrix = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        Matrix endMatrix = (Matrix) endValues.values.get(PROPNAME_MATRIX);

        boolean matricesEqual = (startMatrix == null && endMatrix == null) ||
                (startMatrix != null && startMatrix.equals(endMatrix));

        if (startBounds.equals(endBounds) && matricesEqual) {
            return null;
        }

        ImageView imageView = (ImageView) endValues.view;
        Drawable drawable = imageView.getDrawable();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        ObjectAnimator animator;
        if (drawableWidth == 0 || drawableHeight == 0) {
            animator = createMatrixAnimator(imageView, new MatrixUtils.NullMatrixEvaluator(),
                    MatrixUtils.IDENTITY_MATRIX, MatrixUtils.IDENTITY_MATRIX);
        } else {
            if (startMatrix == null) {
                startMatrix = MatrixUtils.IDENTITY_MATRIX;
            }
            if (endMatrix == null) {
                endMatrix = MatrixUtils.IDENTITY_MATRIX;
            }
            MatrixUtils.animateTransform(imageView, startMatrix);
            animator = createMatrixAnimator(imageView, new MatrixUtils.MatrixEvaluator(),
                    startMatrix, endMatrix);
        }
        return animator;
    }

    private ObjectAnimator createMatrixAnimator(ImageView imageView, TypeEvaluator<Matrix> evaluator,
                                                Matrix startMatrix, final Matrix endMatrix) {
        ImageMatrixProperty matrixProperty = new ImageMatrixProperty(imageView);
        ObjectAnimator animator = AnimatorUtils.ofObject(matrixProperty, evaluator,
                startMatrix, endMatrix);
        // save strong reference to object (in animator this target will be saved as a weak
        // reference so it can be garbage-collected)
        animator.addListener(matrixProperty);
        return animator;
    }

    private static class ImageMatrixProperty extends PropertyCompatObject<ImageView, Matrix> {

        public ImageMatrixProperty(ImageView object) {
            super(object);
        }

        @Override
        public void setValue(Matrix value) {
            MatrixUtils.animateTransform(getObject(), value);
        }

    }

}
