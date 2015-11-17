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
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.utils.AnimatorUtils;
import com.transitionseverywhere.utils.BitmapUtil;
import com.transitionseverywhere.utils.ViewGroupOverlayUtils;
import com.transitionseverywhere.utils.ViewGroupUtils;
import com.transitionseverywhere.utils.ViewUtils;

/**
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes. Visibility is determined not just by the
 * {@link View#setVisibility(int)} state of views, but also whether
 * views exist in the current view hierarchy. The class is intended to be a
 * utility for subclasses such as {@link Fade}, which use this visibility
 * information to determine the specific animations to run when visibility
 * changes occur. Subclasses should implement one or both of the methods
 * {@link #onAppear(ViewGroup, TransitionValues, int, TransitionValues, int)},
 * {@link #onDisappear(ViewGroup, TransitionValues, int, TransitionValues, int)} or
 * {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)},
 * {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class Visibility extends Transition {

    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    protected static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    protected static final String PROPNAME_BITMAP = "android:bitmap";

    /**
     * Mode used in {@link #setMode(int)} to make the transition
     * operate on targets that are appearing. Maybe be combined with
     * {@link #MODE_OUT} to target Visibility changes both in and out.
     */
    public static final int MODE_IN = 0x1;

    /**
     * Mode used in {@link #setMode(int)} to make the transition
     * operate on targets that are disappearing. Maybe be combined with
     * {@link #MODE_IN} to target Visibility changes both in and out.
     */
    public static final int MODE_OUT = 0x2;


    private static final String[] sTransitionProperties = {
            PROPNAME_VISIBILITY,
            PROPNAME_PARENT,
    };

    private static class VisibilityInfo {
        boolean visibilityChange;
        boolean fadeIn;
        int startVisibility;
        int endVisibility;
        ViewGroup startParent;
        ViewGroup endParent;
    }

    private int mMode = MODE_IN | MODE_OUT;

    private int mForcedStartVisibility = -1;
    private int mForcedEndVisibility = -1;

    public Visibility() {}

    public Visibility(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VisibilityTransition);
        int mode = a.getInt(R.styleable.VisibilityTransition_transitionVisibilityMode, 0);
        a.recycle();
        if (mode != 0) {
            setMode(mode);
        }
    }

    /**
     * Changes the transition to support appearing and/or disappearing Views, depending
     * on <code>mode</code>.
     *
     * @param mode The behavior supported by this transition, a combination of
     *             {@link #MODE_IN} and {@link #MODE_OUT}.
     * @attr ref android.R.styleable#VisibilityTransition_transitionVisibilityMode
     */
    public void setMode(int mode) {
        if ((mode & ~(MODE_IN | MODE_OUT)) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        mMode = mode;
    }

    /**
     * Returns whether appearing and/or disappearing Views are supported.
     *
     * Returns whether appearing and/or disappearing Views are supported. A combination of
     *         {@link #MODE_IN} and {@link #MODE_OUT}.
     * @attr ref android.R.styleable#VisibilityTransition_transitionVisibilityMode
     */
    public int getMode() {
        return mMode;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues, int forcedVisibility) {
        int visibility;
        if (forcedVisibility != -1) {
            visibility = forcedVisibility;
        } else {
            visibility = transitionValues.view.getVisibility();
        }
        transitionValues.values.put(PROPNAME_VISIBILITY, visibility);
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] loc = new int[2];
        transitionValues.view.getLocationOnScreen(loc);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, loc);
        transitionValues.values.put(PROPNAME_BITMAP, BitmapUtil.createBitmap(transitionValues.view));
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues, mForcedStartVisibility);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues, mForcedEndVisibility);
    }

    /** @hide */
    @Override
    public void forceVisibility(int visibility, boolean isStartValue) {
        if (isStartValue) {
            mForcedStartVisibility = visibility;
        } else {
            mForcedEndVisibility = visibility;
        }
    }

    /**
     * Returns whether the view is 'visible' according to the given values
     * object. This is determined by testing the same properties in the values
     * object that are used to determine whether the object is appearing or
     * disappearing in the {@link
     * Transition#createAnimator(ViewGroup, TransitionValues, TransitionValues)}
     * method. This method can be called by, for example, subclasses that want
     * to know whether the object is visible in the same way that Visibility
     * determines it for the actual animation.
     *
     * @param values The TransitionValues object that holds the information by
     *               which visibility is determined.
     * @return True if the view reference by <code>values</code> is visible,
     * false otherwise.
     */
    public boolean isVisible(TransitionValues values) {
        if (values == null) {
            return false;
        }
        int visibility = (Integer) values.values.get(PROPNAME_VISIBILITY);
        View parent = (View) values.values.get(PROPNAME_PARENT);

        return visibility == View.VISIBLE && parent != null;
    }

    private static VisibilityInfo getVisibilityChangeInfo(TransitionValues startValues,
                                                   TransitionValues endValues) {
        final VisibilityInfo visInfo = new VisibilityInfo();
        visInfo.visibilityChange = false;
        visInfo.fadeIn = false;
        if (startValues != null && startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visInfo.startVisibility = (Integer) startValues.values.get(PROPNAME_VISIBILITY);
            visInfo.startParent = (ViewGroup) startValues.values.get(PROPNAME_PARENT);
        } else {
            visInfo.startVisibility = -1;
            visInfo.startParent = null;
        }
        if (endValues != null && endValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visInfo.endVisibility = (Integer) endValues.values.get(PROPNAME_VISIBILITY);
            visInfo.endParent = (ViewGroup) endValues.values.get(PROPNAME_PARENT);
        } else {
            visInfo.endVisibility = -1;
            visInfo.endParent = null;
        }
        if (startValues != null && endValues != null) {
            if (visInfo.startVisibility == visInfo.endVisibility &&
                    visInfo.startParent == visInfo.endParent) {
                return visInfo;
            } else {
                if (visInfo.startVisibility != visInfo.endVisibility) {
                    if (visInfo.startVisibility == View.VISIBLE) {
                        visInfo.fadeIn = false;
                        visInfo.visibilityChange = true;
                    } else if (visInfo.endVisibility == View.VISIBLE) {
                        visInfo.fadeIn = true;
                        visInfo.visibilityChange = true;
                    }
                    // no visibilityChange if going between INVISIBLE and GONE
                } else if (visInfo.startParent != visInfo.endParent) {
                    if (visInfo.endParent == null) {
                        visInfo.fadeIn = false;
                        visInfo.visibilityChange = true;
                    } else if (visInfo.startParent == null) {
                        visInfo.fadeIn = true;
                        visInfo.visibilityChange = true;
                    }
                }
            }
        } else if (startValues == null && visInfo.endVisibility == View.VISIBLE) {
            visInfo.fadeIn = true;
            visInfo.visibilityChange = true;
        } else if (endValues == null && visInfo.startVisibility == View.VISIBLE) {
            visInfo.fadeIn = false;
            visInfo.visibilityChange = true;
        }
        return visInfo;
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        VisibilityInfo visInfo = getVisibilityChangeInfo(startValues, endValues);
        if (visInfo.visibilityChange
                && (visInfo.startParent != null || visInfo.endParent != null)) {
            if (visInfo.fadeIn) {
                return onAppear(sceneRoot, startValues, visInfo.startVisibility,
                        endValues, visInfo.endVisibility);
            } else {
                return onDisappear(sceneRoot, startValues, visInfo.startVisibility,
                        endValues, visInfo.endVisibility
                );
            }
        }
        return null;
    }

    /**
     * The default implementation of this method calls
     * {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)}.
     * Subclasses should override this method or
     * {@link #onAppear(ViewGroup, View, TransitionValues, TransitionValues)}.
     * if they need to create an Animator when targets appear.
     * The method should only be called by the Visibility class; it is
     * not intended to be called from external classes.
     *
     * @param sceneRoot The root of the transition hierarchy
     * @param startValues The target values in the start scene
     * @param startVisibility The target visibility in the start scene
     * @param endValues The target values in the end scene
     * @param endVisibility The target visibility in the end scene
     * @return An Animator to be started at the appropriate time in the
     * overall transition for this scene change. A null value means no animation
     * should be run.
     */
    public Animator onAppear(ViewGroup sceneRoot,
                             TransitionValues startValues, int startVisibility,
                             TransitionValues endValues, int endVisibility) {
        if ((mMode & MODE_IN) != MODE_IN || endValues == null) {
            return null;
        }
        if (startValues == null) {
            VisibilityInfo parentVisibilityInfo = null;
            View endParent = (View) endValues.view.getParent();
            TransitionValues startParentValues = getMatchedTransitionValues(endParent,
                    false);
            TransitionValues endParentValues = getTransitionValues(endParent, false);
            parentVisibilityInfo =
                    getVisibilityChangeInfo(startParentValues, endParentValues);
            if (parentVisibilityInfo.visibilityChange) {
                return null;
            }
        }
        final boolean isForcedVisibility = mForcedStartVisibility != -1 ||
                mForcedEndVisibility != -1;
        if (isForcedVisibility && !ViewUtils.isTransitionAlphaCompatMode()) {
            // Make sure that we reverse the effect of onDisappear's setTransitionAlpha(0)
            ViewUtils.setTransitionAlpha(endValues.view, 1);
        }
        return onAppear(sceneRoot, endValues.view, startValues, endValues);
    }

    /**
     * The default implementation of this method returns a null Animator. Subclasses should
     * override this method to make targets appear with the desired transition. The
     * method should only be called from
     * {@link #onAppear(ViewGroup, TransitionValues, int, TransitionValues, int)}.
     *
     * @param sceneRoot The root of the transition hierarchy
     * @param view The View to make appear. This will be in the target scene's View hierarchy and
     *             will be VISIBLE.
     * @param startValues The target values in the start scene
     * @param endValues The target values in the end scene
     * @return An Animator to be started at the appropriate time in the
     * overall transition for this scene change. A null value means no animation
     * should be run.
     */
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                             TransitionValues endValues) {
        return null;
    }

    /**
     * Subclasses should override this method or
     * {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}
     * if they need to create an Animator when targets disappear.
     * The method should only be called by the Visibility class; it is
     * not intended to be called from external classes.
     * <p>
     * The default implementation of this method attempts to find a View to use to call
     * {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)},
     * based on the situation of the View in the View hierarchy. For example,
     * if a View was simply removed from its parent, then the View will be added
     * into a {@link android.view.ViewGroupOverlay} and passed as the <code>view</code>
     * parameter in {@link #onDisappear(ViewGroup, View, TransitionValues, TransitionValues)}.
     * If a visible View is changed to be {@link View#GONE} or {@link View#INVISIBLE},
     * then it can be used as the <code>view</code> and the visibility will be changed
     * to {@link View#VISIBLE} for the duration of the animation. However, if a View
     * is in a hierarchy which is also altering its visibility, the situation can be
     * more complicated. In general, if a view that is no longer in the hierarchy in
     * the end scene still has a parent (so its parent hierarchy was removed, but it
     * was not removed from its parent), then it will be left alone to avoid side-effects from
     * improperly removing it from its parent. The only exception to this is if
     * the previous {@link Scene} was {@link Scene#getSceneForLayout(ViewGroup, int,
     * android.content.Context) created from a layout resource file}, then it is considered
     * safe to un-parent the starting scene view in order to make it disappear.</p>
     *
     * @param sceneRoot The root of the transition hierarchy
     * @param startValues The target values in the start scene
     * @param startVisibility The target visibility in the start scene
     * @param endValues The target values in the end scene
     * @param endVisibility The target visibility in the end scene
     * @return An Animator to be started at the appropriate time in the
     * overall transition for this scene change. A null value means no animation
     * should be run.
     */
    public Animator onDisappear(final ViewGroup sceneRoot,
                                TransitionValues startValues, int startVisibility,
                                TransitionValues endValues, int endVisibility) {
        if ((mMode & MODE_OUT) != MODE_OUT) {
            return null;
        }

        View startView = (startValues != null) ? startValues.view : null;
        View endView = (endValues != null) ? endValues.view : null;
        View overlayView = null;
        View viewToKeep = null;
        if (endView == null || endView.getParent() == null) {
            if (endView != null) {
                // endView was removed from its parent - add it to the overlay
                overlayView = endView;
            } else if (startView != null) {
                // endView does not exist. Use startView only under certain
                // conditions, because placing a view in an overlay necessitates
                // it being removed from its current parent
                if (startView.getParent() == null) {
                    // no parent - safe to use
                    overlayView = startView;
                } else if (startView.getParent() instanceof View) {
                    View startParent = (View) startView.getParent();
                    TransitionValues startParentValues = getTransitionValues(startParent, true);
                    TransitionValues endParentValues = getMatchedTransitionValues(startParent,
                            true);
                    VisibilityInfo parentVisibilityInfo =
                            getVisibilityChangeInfo(startParentValues, endParentValues);
                    if (!parentVisibilityInfo.visibilityChange) {
                        overlayView = TransitionUtils.copyViewImage(sceneRoot, startView,
                                startParent);
                    } else if (startParent.getParent() == null) {
                        int id = startParent.getId();
                        if (id != View.NO_ID && sceneRoot.findViewById(id) != null
                                && mCanRemoveViews) {
                            // no parent, but its parent is unparented  but the parent
                            // hierarchy has been replaced by a new hierarchy with the same id
                            // and it is safe to un-parent startView
                            overlayView = startView;
                        }
                    }
                }
            }
        } else {
            // visibility change
            if (endVisibility == View.INVISIBLE) {
                viewToKeep = endView;
            } else {
                // Becoming GONE
                if (startView == endView) {
                    viewToKeep = endView;
                } else {
                    overlayView = startView;
                }
            }
        }
        final int finalVisibility = endVisibility;

        if (overlayView != null) {
            // TODO: Need to do this for general case of adding to overlay
            int[] screenLoc = (int[]) startValues.values.get(PROPNAME_SCREEN_LOCATION);
            ViewGroupOverlayUtils.addOverlay(sceneRoot, overlayView, screenLoc[0], screenLoc[1]);
            Animator animator = onDisappear(sceneRoot, overlayView, startValues, endValues);
            if (animator == null) {
                ViewGroupOverlayUtils.removeOverlay(sceneRoot, overlayView);
            } else {
                final View finalOverlayView = overlayView;
                addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        ViewGroupOverlayUtils.removeOverlay(sceneRoot, finalOverlayView);
                    }
                });
            }
            if (isReverse()) {
                startValues.view.setVisibility(View.GONE);
            }
            return animator;
        }

        if (viewToKeep != null) {
            int originalVisibility = -1;
            final boolean isForcedVisibility = mForcedStartVisibility != -1 ||
                    mForcedEndVisibility != -1;
            if (!isForcedVisibility) {
                originalVisibility = viewToKeep.getVisibility();
                ViewUtils.setTransitionVisibility(viewToKeep, View.VISIBLE);
            }
            Animator animator = onDisappear(sceneRoot, viewToKeep, startValues, endValues);
            if (animator != null) {
                DisappearListener disappearListener = new DisappearListener(viewToKeep,
                        finalVisibility, isForcedVisibility);
                animator.addListener(disappearListener);
                AnimatorUtils.addPauseListener(animator, disappearListener);
                addListener(disappearListener);
            } else if (!isForcedVisibility) {
                ViewUtils.setTransitionVisibility(viewToKeep, originalVisibility);
            }
            return animator;
        }
        return null;
    }

    @Override
    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues newValues) {
        if (startValues == null && newValues == null) {
            return false;
        }
        if (startValues != null && newValues != null &&
                newValues.values.containsKey(PROPNAME_VISIBILITY) !=
                        startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            // The transition wasn't targeted in either the start or end, so it couldn't
            // have changed.
            return false;
        }
        VisibilityInfo changeInfo = getVisibilityChangeInfo(startValues, newValues);
        return changeInfo.visibilityChange && (changeInfo.startVisibility == View.VISIBLE ||
                changeInfo.endVisibility == View.VISIBLE);
    }

    /**
     * The default implementation of this method returns a null Animator. Subclasses should
     * override this method to make targets disappear with the desired transition. The
     * method should only be called from
     * {@link #onDisappear(ViewGroup, TransitionValues, int, TransitionValues, int)}.
     *
     * @param sceneRoot The root of the transition hierarchy
     * @param view The View to make disappear. This will be in the target scene's View
     *             hierarchy or in an {@link android.view.ViewGroupOverlay} and will be
     *             VISIBLE.
     * @param startValues The target values in the start scene
     * @param endValues The target values in the end scene
     * @return An Animator to be started at the appropriate time in the
     * overall transition for this scene change. A null value means no animation
     * should be run.
     */
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                                TransitionValues endValues) {
        return null;
    }

    private static class DisappearListener extends AnimatorListenerAdapter
            implements TransitionListener {
        private final boolean mIsForcedVisibility;
        private final View mView;
        private final int mFinalVisibility;
        private final ViewGroup mParent;

        private boolean mLayoutSuppressed;
        private boolean mFinalVisibilitySet;
        boolean mCanceled = false;

        public DisappearListener(View view, int finalVisibility, boolean isForcedVisibility) {
            mView = view;
            mIsForcedVisibility = isForcedVisibility;
            mFinalVisibility = finalVisibility;
            mParent = (ViewGroup) view.getParent();
            // Prevent a layout from including mView in its calculation.
            suppressLayout(true);
        }

        @Override
        public void onAnimationPause(Animator animation) {
            if (!mCanceled && !mIsForcedVisibility) {
                ViewUtils.setTransitionVisibility(mView, mFinalVisibility);
            }
        }

        @Override
        public void onAnimationResume(Animator animation) {
            if (!mCanceled && !mIsForcedVisibility) {
                ViewUtils.setTransitionVisibility(mView, View.VISIBLE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mCanceled = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            hideViewWhenNotCanceled();
        }

        @Override
        public void onTransitionStart(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        @Override
        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!mCanceled) {
                if (mIsForcedVisibility) {
                    if (!ViewUtils.isTransitionAlphaCompatMode()) {
                        ViewUtils.setTransitionAlpha(mView, 0);
                    }
                } else if (!mFinalVisibilitySet) {
                    // Recreate the parent's display list in case it includes mView.
                    ViewUtils.setTransitionVisibility(mView, mFinalVisibility);
                    if (mParent != null) {
                        mParent.invalidate();
                    }
                    mFinalVisibilitySet = true;
                }
            }
            // Layout is allowed now that the View is in its final state
            suppressLayout(false);
        }

        private void suppressLayout(boolean suppress) {
            if (mLayoutSuppressed != suppress && mParent != null && !mIsForcedVisibility) {
                mLayoutSuppressed = suppress;
                ViewGroupUtils.suppressLayout(mParent, suppress);
            }
        }
    }
}
