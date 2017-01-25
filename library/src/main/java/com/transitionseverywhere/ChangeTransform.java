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
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.utils.MatrixUtils;
import com.transitionseverywhere.utils.ViewUtils;

/**
 * This Transition captures scale and rotation for Views before and after the
 * scene change and animates those changes during the transition.
 *
 * A change in parent is handled as well by capturing the transforms from
 * the parent before and after the scene change and animating those during the
 * transition.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ChangeTransform extends Transition {

    private static final String TAG = "ChangeTransform";

    private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
    private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
    private static final String PROPNAME_PARENT = "android:changeTransform:parent";
    private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
    private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX =
            "android:changeTransform:intermediateParentMatrix";
    private static final String PROPNAME_INTERMEDIATE_MATRIX =
            "android:changeTransform:intermediateMatrix";

    private static final String[] sTransitionProperties = {
            PROPNAME_MATRIX,
            PROPNAME_TRANSFORMS,
            PROPNAME_PARENT_MATRIX,
    };

    private static final Property<View, Matrix> ANIMATION_MATRIX_PROPERTY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ANIMATION_MATRIX_PROPERTY = new Property<View, Matrix>(Matrix.class, "animationMatrix") {
                @Override
                public Matrix get(View object) {
                    return null;
                }

                @Override
                public void set(View object, Matrix value) {
                    ViewUtils.setAnimationMatrix(object, value);
                }
            };
        } else {
            ANIMATION_MATRIX_PROPERTY = null;
        }
    }

    private boolean mUseOverlay = true;
    private boolean mReparent = true;
    private Matrix mTempMatrix = new Matrix();

    public ChangeTransform() {}

    public ChangeTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeTransform);
        mUseOverlay = a.getBoolean(R.styleable.ChangeTransform_reparentWithOverlay, true);
        mReparent = a.getBoolean(R.styleable.ChangeTransform_reparent, true);
        a.recycle();
    }

    /**
     * Returns whether changes to parent should use an overlay or not. When the parent
     * change doesn't use an overlay, it affects the transforms of the child. The
     * default value is <code>true</code>.
     *
     * <p>Note: when Overlays are not used when a parent changes, a view can be clipped when
     * it moves outside the bounds of its parent. Setting
     * {@link android.view.ViewGroup#setClipChildren(boolean)} and
     * {@link android.view.ViewGroup#setClipToPadding(boolean)} can help. Also, when
     * Overlays are not used and the parent is animating its location, the position of the
     * child view will be relative to its parent's final position, so it may appear to "jump"
     * at the beginning.</p>
     *
     * @return <code>true</code> when a changed parent should execute the transition
     * inside the scene root's overlay or <code>false</code> if a parent change only
     * affects the transform of the transitioning view.
     * @attr ref android.R.styleable#ChangeTransform_reparentWithOverlay
     */
    public boolean getReparentWithOverlay() {
        return mUseOverlay;
    }

    /**
     * Sets whether changes to parent should use an overlay or not. When the parent
     * change doesn't use an overlay, it affects the transforms of the child. The
     * default value is <code>true</code>.
     *
     * <p>Note: when Overlays are not used when a parent changes, a view can be clipped when
     * it moves outside the bounds of its parent. Setting
     * {@link android.view.ViewGroup#setClipChildren(boolean)} and
     * {@link android.view.ViewGroup#setClipToPadding(boolean)} can help. Also, when
     * Overlays are not used and the parent is animating its location, the position of the
     * child view will be relative to its parent's final position, so it may appear to "jump"
     * at the beginning.</p>
     *
     * @return <code>true</code> when a changed parent should execute the transition
     * inside the scene root's overlay or <code>false</code> if a parent change only
     * affects the transform of the transitioning view.
     * @attr ref android.R.styleable#ChangeTransform_reparentWithOverlay
     */
    public void setReparentWithOverlay(boolean reparentWithOverlay) {
        mUseOverlay = reparentWithOverlay;
    }

    /**
     * Returns whether parent changes will be tracked by the ChangeTransform. If parent
     * changes are tracked, then the transform will adjust to the transforms of the
     * different parents. If they aren't tracked, only the transforms of the transitioning
     * view will be tracked. Default is true.
     *
     * @return whether parent changes will be tracked by the ChangeTransform.
     * @attr ref android.R.styleable#ChangeTransform_reparent
     */
    public boolean getReparent() {
        return mReparent;
    }

    /**
     * Sets whether parent changes will be tracked by the ChangeTransform. If parent
     * changes are tracked, then the transform will adjust to the transforms of the
     * different parents. If they aren't tracked, only the transforms of the transitioning
     * view will be tracked. Default is true.
     *
     * @param reparent Set to true to track parent changes or false to only track changes
     *                 of the transitioning view without considering the parent change.
     * @attr ref android.R.styleable#ChangeTransform_reparent
     */
    public void setReparent(boolean reparent) {
        mReparent = reparent;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(android.support.transition.TransitionValues transitionValues) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        View view = transitionValues.view;
        if (view.getVisibility() == View.GONE) {
            return;
        }
        transitionValues.values.put(PROPNAME_PARENT, view.getParent());
        Transforms transforms = new Transforms(view);
        transitionValues.values.put(PROPNAME_TRANSFORMS, transforms);
        Matrix matrix = view.getMatrix();
        if (matrix == null || matrix.isIdentity()) {
            matrix = null;
        } else {
            matrix = new Matrix(matrix);
        }
        transitionValues.values.put(PROPNAME_MATRIX, matrix);
        if (mReparent) {
            Matrix parentMatrix = new Matrix();
            ViewGroup parent = (ViewGroup) view.getParent();
            ViewUtils.transformMatrixToGlobal(parent, parentMatrix);
            parentMatrix.preTranslate(-parent.getScrollX(), -parent.getScrollY());
            transitionValues.values.put(PROPNAME_PARENT_MATRIX, parentMatrix);
            transitionValues.values.put(PROPNAME_INTERMEDIATE_MATRIX,
                    view.getTag(R.id.transitionTransform));
            transitionValues.values.put(PROPNAME_INTERMEDIATE_PARENT_MATRIX,
                    view.getTag(R.id.parentMatrix));
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
        if (startValues == null || endValues == null ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ||
                !startValues.values.containsKey(PROPNAME_PARENT) ||
                !endValues.values.containsKey(PROPNAME_PARENT)) {
            return null;
        }

        ViewGroup startParent = (ViewGroup) startValues.values.get(PROPNAME_PARENT);
        ViewGroup endParent = (ViewGroup) endValues.values.get(PROPNAME_PARENT);
        boolean handleParentChange = mReparent && !parentsMatch(startParent, endParent);

        Matrix startMatrix = (Matrix) startValues.values.get(PROPNAME_INTERMEDIATE_MATRIX);
        if (startMatrix != null) {
            startValues.values.put(PROPNAME_MATRIX, startMatrix);
        }

        Matrix startParentMatrix = (Matrix)
                startValues.values.get(PROPNAME_INTERMEDIATE_PARENT_MATRIX);
        if (startParentMatrix != null) {
            startValues.values.put(PROPNAME_PARENT_MATRIX, startParentMatrix);
        }

        // First handle the parent change:
        if (handleParentChange) {
            setMatricesForParent(startValues, endValues);
        }

        // Next handle the normal matrix transform:
        ObjectAnimator transformAnimator = createTransformAnimator(startValues, endValues,
                handleParentChange);

        if (handleParentChange && transformAnimator != null && mUseOverlay) {
            createGhostView(sceneRoot, startValues, endValues);
        }

        return transformAnimator;
    }

    private ObjectAnimator createTransformAnimator(android.support.transition.TransitionValues startValues,
                                                   android.support.transition.TransitionValues endValues,
                                                   final boolean handleParentChange) {
        Matrix startMatrix = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        Matrix endMatrix = (Matrix) endValues.values.get(PROPNAME_MATRIX);

        if (startMatrix == null) {
            startMatrix = MatrixUtils.IDENTITY_MATRIX;
        }

        if (endMatrix == null) {
            endMatrix = MatrixUtils.IDENTITY_MATRIX;
        }

        if (startMatrix.equals(endMatrix)) {
            return null;
        }

        final Transforms transforms = (Transforms) endValues.values.get(PROPNAME_TRANSFORMS);

        // clear the transform properties so that we can use the animation matrix instead
        final View view = endValues.view;
        setIdentityTransforms(view);

        ObjectAnimator animator = ObjectAnimator.ofObject(view, ANIMATION_MATRIX_PROPERTY,
                new MatrixUtils.MatrixEvaluator(), startMatrix, endMatrix);

        final Matrix finalEndMatrix = endMatrix;

        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            private boolean mIsCanceled;
            private Matrix mTempMatrix = new Matrix();

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mIsCanceled) {
                    if (handleParentChange && mUseOverlay) {
                        setCurrentMatrix(finalEndMatrix);
                    } else {
                        view.setTag(R.id.transitionTransform, null);
                        view.setTag(R.id.parentMatrix, null);
                    }
                }
                ANIMATION_MATRIX_PROPERTY.set(view, null);
                transforms.restore(view);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                ValueAnimator animator = (ValueAnimator) animation;
                Matrix currentMatrix = (Matrix) animator.getAnimatedValue();
                setCurrentMatrix(currentMatrix);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                setIdentityTransforms(view);
            }

            private void setCurrentMatrix(Matrix currentMatrix) {
                mTempMatrix.set(currentMatrix);
                view.setTag(R.id.transitionTransform, mTempMatrix);
                transforms.restore(view);
            }
        };

        animator.addListener(listener);
        animator.addPauseListener(listener);
        return animator;
    }

    private boolean parentsMatch(ViewGroup startParent, ViewGroup endParent) {
        boolean parentsMatch = false;
        if (!isValidTarget(startParent) || !isValidTarget(endParent)) {
            parentsMatch = startParent == endParent;
        } else {
            android.support.transition.TransitionValues endValues = getMatchedTransitionValues(startParent, true);
            if (endValues != null) {
                parentsMatch = endParent == endValues.view;
            }
        }
        return parentsMatch;
    }

    private void createGhostView(final ViewGroup sceneRoot,
                                 android.support.transition.TransitionValues startValues,
                                 android.support.transition.TransitionValues endValues) {
        View view = endValues.view;

        Matrix endMatrix = (Matrix) endValues.values.get(PROPNAME_PARENT_MATRIX);
        Matrix localEndMatrix = new Matrix(endMatrix);
        ViewUtils.transformMatrixToLocal(sceneRoot, localEndMatrix);

        Transition outerTransition = this;
        while (outerTransition.mParent != null) {
            outerTransition = outerTransition.mParent;
        }

        View ghostView = ViewUtils.addGhostView(view, sceneRoot, localEndMatrix);
        if (ghostView != null) {
            GhostListener listener = new GhostListener(view, ghostView, endMatrix);
            outerTransition.addListener(listener);
        }

        if (startValues.view != endValues.view) {
            view.setAlpha(0);
        }
        view.setAlpha(1);
    }

    private void setMatricesForParent(android.support.transition.TransitionValues startValues,
                                      android.support.transition.TransitionValues endValues) {
        Matrix endParentMatrix = (Matrix) endValues.values.get(PROPNAME_PARENT_MATRIX);
        endValues.view.setTag(R.id.parentMatrix, endParentMatrix);

        Matrix toLocal = mTempMatrix;
        toLocal.reset();
        endParentMatrix.invert(toLocal);

        Matrix startLocal = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        if (startLocal == null) {
            startLocal = new Matrix();
            startValues.values.put(PROPNAME_MATRIX, startLocal);
        }

        Matrix startParentMatrix = (Matrix) startValues.values.get(PROPNAME_PARENT_MATRIX);
        startLocal.postConcat(startParentMatrix);
        startLocal.postConcat(toLocal);
    }

    private static void setIdentityTransforms(View view) {
        setTransforms(view, 0, 0, 0, 1, 1, 0, 0, 0);
    }

    private static void setTransforms(View view, float translationX, float translationY,
                                      float translationZ, float scaleX, float scaleY, float rotationX,
                                      float rotationY, float rotationZ) {
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
        ViewUtils.setTranslationZ(view, translationZ);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setRotationX(rotationX);
        view.setRotationY(rotationY);
        view.setRotation(rotationZ);
    }

    private static class Transforms {
        public final float translationX;
        public final float translationY;
        public final float translationZ;
        public final float scaleX;
        public final float scaleY;
        public final float rotationX;
        public final float rotationY;
        public final float rotationZ;

        public Transforms(View view) {
            translationX = view.getTranslationX();
            translationY = view.getTranslationY();
            translationZ = ViewUtils.getTranslationZ(view);
            scaleX = view.getScaleX();
            scaleY = view.getScaleY();
            rotationX = view.getRotationX();
            rotationY = view.getRotationY();
            rotationZ = view.getRotation();
        }

        public void restore(View view) {
            setTransforms(view, translationX, translationY, translationZ, scaleX, scaleY,
                    rotationX, rotationY, rotationZ);
        }

        @Override
        public boolean equals(Object that) {
            if (!(that instanceof Transforms)) {
                return false;
            }
            Transforms thatTransform = (Transforms) that;
            return thatTransform.translationX == translationX &&
                    thatTransform.translationY == translationY &&
                    thatTransform.translationZ == translationZ &&
                    thatTransform.scaleX == scaleX &&
                    thatTransform.scaleY == scaleY &&
                    thatTransform.rotationX == rotationX &&
                    thatTransform.rotationY == rotationY &&
                    thatTransform.rotationZ == rotationZ;
        }
    }

    private static class GhostListener extends Transition.TransitionListenerAdapter {
        private View mView;
        private View mGhostView;
        private Matrix mEndMatrix;

        public GhostListener(View view, View ghostView, Matrix endMatrix) {
            mView = view;
            mGhostView = ghostView;
            mEndMatrix = endMatrix;
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            transition.removeListener(this);
            ViewUtils.removeGhostView(mView);
            mView.setTag(R.id.transitionTransform, null);
            mView.setTag(R.id.parentMatrix, null);
        }

        @Override
        public void onTransitionPause(Transition transition) {
            mGhostView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTransitionResume(Transition transition) {
            mGhostView.setVisibility(View.VISIBLE);
        }
    }
}
