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
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.utils.AnimatorUtils;
import com.transitionseverywhere.utils.PointFProperty;
import com.transitionseverywhere.utils.RectEvaluator;
import com.transitionseverywhere.utils.ViewGroupUtils;
import com.transitionseverywhere.utils.ViewOverlayUtils;
import com.transitionseverywhere.utils.ViewUtils;

import java.util.Map;

/**
 * This transition captures the layout bounds of target views before and after
 * the scene change and animates those changes during the transition.
 * <p/>
 * <p>A ChangeBounds transition can be described in a resource file by using the
 * tag <code>changeBounds</code>, using its attributes of
 * {@link com.transitionseverywhere.R.styleable#ChangeBounds} along with the other standard
 * attributes of {@link com.transitionseverywhere.R.styleable#Transition}.</p>
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ChangeBounds extends Transition {

    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final String[] sTransitionProperties = {
            PROPNAME_BOUNDS,
            PROPNAME_CLIP,
            PROPNAME_PARENT,
            PROPNAME_WINDOW_X,
            PROPNAME_WINDOW_Y
    };

    private static final PointFProperty<Drawable> DRAWABLE_ORIGIN_PROPERTY;
    private static final PointFProperty<ViewBounds> TOP_LEFT_PROPERTY;
    private static final PointFProperty<ViewBounds> BOTTOM_RIGHT_PROPERTY;
    private static final PointFProperty<View> BOTTOM_RIGHT_ONLY_PROPERTY;
    private static final PointFProperty<View> TOP_LEFT_ONLY_PROPERTY;
    private static final PointFProperty<View> POSITION_PROPERTY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            DRAWABLE_ORIGIN_PROPERTY = new PointFProperty<Drawable>() {

                private Rect mBounds = new Rect();

                @Override
                public void set(Drawable object, PointF value) {
                    object.copyBounds(mBounds);
                    mBounds.offsetTo(Math.round(value.x), Math.round(value.y));
                    object.setBounds(mBounds);
                }

                @Override
                public PointF get(Drawable object) {
                    object.copyBounds(mBounds);
                    return new PointF(mBounds.left, mBounds.top);
                }
            };
            TOP_LEFT_PROPERTY = new PointFProperty<ViewBounds>() {
                @Override
                public void set(ViewBounds viewBounds, PointF topLeft) {
                    viewBounds.setTopLeft(topLeft);
                }
            };
            BOTTOM_RIGHT_PROPERTY = new PointFProperty<ViewBounds>() {
                @Override
                public void set(ViewBounds viewBounds, PointF bottomRight) {
                    viewBounds.setBottomRight(bottomRight);
                }
            };
            BOTTOM_RIGHT_ONLY_PROPERTY = new PointFProperty<View>() {
                @Override
                public void set(View view, PointF bottomRight) {
                    int left = view.getLeft();
                    int top = view.getTop();
                    int right = Math.round(bottomRight.x);
                    int bottom = Math.round(bottomRight.y);
                    ViewUtils.setLeftTopRightBottom(view, left, top, right, bottom);
                }
            };
            TOP_LEFT_ONLY_PROPERTY = new PointFProperty<View>() {
                @Override
                public void set(View view, PointF topLeft) {
                    int left = Math.round(topLeft.x);
                    int top = Math.round(topLeft.y);
                    int right = view.getRight();
                    int bottom = view.getBottom();
                    ViewUtils.setLeftTopRightBottom(view, left, top, right, bottom);
                }
            };
            POSITION_PROPERTY = new PointFProperty<View>() {
                @Override
                public void set(View view, PointF topLeft) {
                    int left = Math.round(topLeft.x);
                    int top = Math.round(topLeft.y);
                    int right = left + view.getWidth();
                    int bottom = top + view.getHeight();
                    ViewUtils.setLeftTopRightBottom(view, left, top, right, bottom);
                }
            };
        } else {
            DRAWABLE_ORIGIN_PROPERTY = null;
            TOP_LEFT_PROPERTY = null;
            BOTTOM_RIGHT_PROPERTY = null;
            BOTTOM_RIGHT_ONLY_PROPERTY = null;
            TOP_LEFT_ONLY_PROPERTY = null;
            POSITION_PROPERTY = null;
        }
    }

    int[] tempLocation = new int[2];
    boolean mResizeClip = false;
    boolean mReparent = false;
    private static final String LOG_TAG = "ChangeBounds";

    private static RectEvaluator sRectEvaluator;

    public ChangeBounds() {}

    public ChangeBounds(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeBounds);
        boolean resizeClip = a.getBoolean(R.styleable.ChangeBounds_resizeClip, false);
        a.recycle();
        setResizeClip(resizeClip);
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    /**
     * When <code>resizeClip</code> is true, ChangeBounds resizes the view using the clipBounds
     * instead of changing the dimensions of the view during the animation. When
     * <code>resizeClip</code> is false, ChangeBounds resizes the View by changing its dimensions.
     *
     * <p>When resizeClip is set to true, the clip bounds is modified by ChangeBounds. Therefore,
     * {@link android.transition.ChangeClipBounds} is not compatible with ChangeBounds
     * in this mode.</p>
     *
     * @param resizeClip Used to indicate whether the view bounds should be modified or the
     *                   clip bounds should be modified by ChangeBounds.
     * @see android.view.View#setClipBounds(android.graphics.Rect)
     * @attr ref android.R.styleable#ChangeBounds_resizeClip
     */
    public void setResizeClip(boolean resizeClip) {
        mResizeClip = resizeClip;
    }

    /**
     * Returns true when the ChangeBounds will resize by changing the clip bounds during the
     * view animation or false when bounds are changed. The default value is false.
     *
     * @return true when the ChangeBounds will resize by changing the clip bounds during the
     * view animation or false when bounds are changed. The default value is false.
     * @attr ref android.R.styleable#ChangeBounds_resizeClip
     */
    public boolean getResizeClip() {
        return mResizeClip;
    }

    /**
     * Setting this flag tells ChangeBounds to track the before/after parent
     * of every view using this transition. The flag is not enabled by
     * default because it requires the parent instances to be the same
     * in the two scenes or else all parents must use ids to allow
     * the transition to determine which parents are the same.
     *
     * @param reparent true if the transition should track the parent
     *                 container of target views and animate parent changes.
     * @deprecated Use {@link android.transition.ChangeTransform} to handle
     * transitions between different parents.
     */
    public void setReparent(boolean reparent) {
        mReparent = reparent;
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (ViewUtils.isLaidOut(view, false) || view.getWidth() != 0 || view.getHeight() != 0) {
            values.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(),
                    view.getRight(), view.getBottom()));
            values.values.put(PROPNAME_PARENT, values.view.getParent());
            if (mReparent) {
                values.view.getLocationInWindow(tempLocation);
                values.values.put(PROPNAME_WINDOW_X, tempLocation[0]);
                values.values.put(PROPNAME_WINDOW_Y, tempLocation[1]);
            }
            if (mResizeClip) {
                values.values.put(PROPNAME_CLIP, ViewUtils.getClipBounds(view));
            }
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

    private boolean parentMatches(View startParent, View endParent) {
        boolean parentMatches = true;
        if (mReparent) {
            TransitionValues endValues = getMatchedTransitionValues(startParent, true);
            if (endValues == null) {
                parentMatches = startParent == endParent;
            } else {
                parentMatches = endParent == endValues.view;
            }
        }
        return parentMatches;
    }

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        if (sRectEvaluator == null) {
            sRectEvaluator = new RectEvaluator();
        }
        Map<String, Object> startParentVals = startValues.values;
        Map<String, Object> endParentVals = endValues.values;
        ViewGroup startParent = (ViewGroup) startParentVals.get(PROPNAME_PARENT);
        ViewGroup endParent = (ViewGroup) endParentVals.get(PROPNAME_PARENT);
        if (startParent == null || endParent == null) {
            return null;
        }
        final View view = endValues.view;
        if (parentMatches(startParent, endParent)) {
            Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
            Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);
            final int startLeft = startBounds.left;
            final int endLeft = endBounds.left;
            final int startTop = startBounds.top;
            final int endTop = endBounds.top;
            final int startRight = startBounds.right;
            final int endRight = endBounds.right;
            final int startBottom = startBounds.bottom;
            final int endBottom = endBounds.bottom;
            final int startWidth = startRight - startLeft;
            final int startHeight = startBottom - startTop;
            final int endWidth = endRight - endLeft;
            final int endHeight = endBottom - endTop;
            Rect startClip = (Rect) startValues.values.get(PROPNAME_CLIP);
            Rect endClip = (Rect) endValues.values.get(PROPNAME_CLIP);
            int numChanges = 0;
            if ((startWidth != 0 && startHeight != 0) || (endWidth != 0 && endHeight != 0)) {
                if (startLeft != endLeft || startTop != endTop) ++numChanges;
                if (startRight != endRight || startBottom != endBottom) ++numChanges;
            }
            if ((startClip != null && !startClip.equals(endClip)) ||
                    (startClip == null && endClip != null)) {
                ++numChanges;
            }
            if (numChanges > 0) {
                Animator anim;
                if (!mResizeClip || (startClip == null && endClip == null)) {
                    ViewUtils.setLeftTopRightBottom(view, startLeft, startTop, startRight, startBottom);
                    if (numChanges == 2) {
                        if (startWidth == endWidth && startHeight == endHeight) {
                            anim = AnimatorUtils.ofPointF(view, POSITION_PROPERTY, getPathMotion(),
                                    startLeft, startTop, endLeft, endTop);
                        } else {
                            ViewBounds viewBounds = new ViewBounds(view);
                            Animator topLeftAnimator = AnimatorUtils.ofPointF(viewBounds,
                                    TOP_LEFT_PROPERTY, getPathMotion(),
                                    startLeft, startTop, endLeft, endTop);
                            Animator bottomRightAnimator = AnimatorUtils.ofPointF(viewBounds,
                                    BOTTOM_RIGHT_PROPERTY, getPathMotion(),
                                    startRight, startBottom, endRight, endBottom);
                            AnimatorSet set = new AnimatorSet();
                            set.playTogether(topLeftAnimator, bottomRightAnimator);
                            set.addListener(viewBounds);
                            anim = set;
                        }
                    } else if (startLeft != endLeft || startTop != endTop) {
                        anim = AnimatorUtils.ofPointF(view, TOP_LEFT_ONLY_PROPERTY, getPathMotion(),
                                startLeft, startTop, endLeft, endTop);
                    } else {
                        anim = AnimatorUtils.ofPointF(view, BOTTOM_RIGHT_ONLY_PROPERTY, getPathMotion(),
                                startRight, startBottom, endRight, endBottom);
                    }
                } else {
                    int maxWidth = Math.max(startWidth, endWidth);
                    int maxHeight = Math.max(startHeight, endHeight);

                    ViewUtils.setLeftTopRightBottom(view, startLeft, startTop, startLeft + maxWidth,
                            startTop + maxHeight);

                    Animator positionAnimator = null;
                    if (startLeft != endLeft || startTop != endTop) {
                        positionAnimator = AnimatorUtils.ofPointF(view, POSITION_PROPERTY, getPathMotion(),
                                startLeft, startTop, endLeft, endTop);
                    }
                    final Rect finalClip = endClip;
                    if (startClip == null) {
                        startClip = new Rect(0, 0, startWidth, startHeight);
                    }
                    if (endClip == null) {
                        endClip = new Rect(0, 0, endWidth, endHeight);
                    }
                    ObjectAnimator clipAnimator = null;
                    if (!startClip.equals(endClip)) {
                        ViewUtils.setClipBounds(view, startClip);
                        clipAnimator = ObjectAnimator.ofObject(view,
                                ChangeClipBounds.VIEW_CLIP_BOUNDS, sRectEvaluator, startClip, endClip);
                        clipAnimator.addListener(new AnimatorListenerAdapter() {
                            private boolean mIsCanceled;

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                mIsCanceled = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (!mIsCanceled) {
                                    ViewUtils.setClipBounds(view, finalClip);
                                    ViewUtils.setLeftTopRightBottom(view, endLeft, endTop,
                                            endRight, endBottom);
                                }
                            }
                        });
                    }
                    anim = TransitionUtils.mergeAnimators(positionAnimator,
                            clipAnimator);
                }
                if (view.getParent() instanceof ViewGroup) {
                    final ViewGroup parent = (ViewGroup) view.getParent();
                    ViewGroupUtils.suppressLayout(parent, true);
                    TransitionListener transitionListener = new TransitionListenerAdapter() {
                        boolean mCanceled = false;

                        @Override
                        public void onTransitionCancel(Transition transition) {
                            ViewGroupUtils.suppressLayout(parent, false);
                            mCanceled = true;
                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            if (!mCanceled) {
                                ViewGroupUtils.suppressLayout(parent, false);
                            }
                        }

                        @Override
                        public void onTransitionPause(Transition transition) {
                            ViewGroupUtils.suppressLayout(parent, false);
                        }

                        @Override
                        public void onTransitionResume(Transition transition) {
                            ViewGroupUtils.suppressLayout(parent, true);
                        }
                    };
                    addListener(transitionListener);
                }
                return anim;
            }
        } else {
            sceneRoot.getLocationInWindow(tempLocation);
            int startX = (Integer) startValues.values.get(PROPNAME_WINDOW_X) - tempLocation[0];
            int startY = (Integer) startValues.values.get(PROPNAME_WINDOW_Y) - tempLocation[1];
            int endX = (Integer) endValues.values.get(PROPNAME_WINDOW_X) - tempLocation[0];
            int endY = (Integer) endValues.values.get(PROPNAME_WINDOW_Y) - tempLocation[1];
            // TODO: also handle size changes: check bounds and animate size changes
            if (startX != endX || startY != endY) {
                final int width = view.getWidth();
                final int height = view.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
                final BitmapDrawable drawable = new BitmapDrawable(
                        sceneRoot.getContext().getResources(), bitmap);
                drawable.setBounds(startX, startY, startX + width, startY + height);
                Animator anim;
                anim = AnimatorUtils.ofPointF(drawable, DRAWABLE_ORIGIN_PROPERTY, getPathMotion(),
                        startX, startY, endX, endY);
                if (anim != null) {
                    final float transitionAlpha = ViewUtils.getTransitionAlpha(view);
                    ViewUtils.setTransitionAlpha(view, 0);
                    ViewOverlayUtils.addOverlay(sceneRoot, drawable);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ViewOverlayUtils.removeOverlay(sceneRoot, drawable);
                            ViewUtils.setTransitionAlpha(view, transitionAlpha);
                        }
                    });
                }
                return anim;
            }
        }
        return null;
    }

    private static class ViewBounds extends AnimatorListenerAdapter {
        private int mLeft;
        private int mTop;
        private int mRight;
        private int mBottom;
        private boolean mIsTopLeftSet;
        private boolean mIsBottomRightSet;
        private View mView;

        public ViewBounds(View view) {
            mView = view;
        }

        public void setTopLeft(PointF topLeft) {
            mLeft = Math.round(topLeft.x);
            mTop = Math.round(topLeft.y);
            mIsTopLeftSet = true;
            if (mIsBottomRightSet) {
                setLeftTopRightBottom();
            }
        }

        public void setBottomRight(PointF bottomRight) {
            mRight = Math.round(bottomRight.x);
            mBottom = Math.round(bottomRight.y);
            mIsBottomRightSet = true;
            if (mIsTopLeftSet) {
                setLeftTopRightBottom();
            }
        }

        private void setLeftTopRightBottom() {
            ViewUtils.setLeftTopRightBottom(mView, mLeft, mTop, mRight, mBottom);
            mIsTopLeftSet = false;
            mIsBottomRightSet = false;
        }
    }

}
