package com.transitionseverywhere.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorPauseListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build.VERSION_CODES;
import android.util.Property;
import android.view.View;

import com.transitionseverywhere.PathMotion;

public class AnimatorUtils {

    static class BaseAnimatorCompat {

        public void addPauseListener(Animator animator, AnimatorPauseListener listener) {
        }

        public void pause(Animator animator) {
        }

        public void resume(Animator animator) {
        }

        public <T> Animator ofPointF(T target, PointFProperty<T> property, float startLeft,
                                     float startTop, float endLeft, float endTop) {
            return null;
        }

        public <T> Animator ofPointF(T target, PointFProperty<T> property, Path path) {
            return null;
        }

        public boolean isAnimatorStarted(Animator anim) {
            return false;
        }

        public boolean hasOverlappingRendering(View view) {
            return true;
        }

        public ObjectAnimator ofFloat(View view, Property<View, Float> property,
                                      float startFraction, float endFraction) {
            return null;
        }

        public ObjectAnimator ofInt(View view, Property<View, Integer> property,
                                    float startFraction, float endFraction) {
            return null;
        }
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    static class IceCreamSandwichAnimatorCompat extends BaseAnimatorCompat {

        @Override
        public void pause(Animator animator) {
            animator.cancel();
        }

        @Override
        public <T> Animator ofPointF(T target, PointFProperty<T> property, float startLeft,
                                     float startTop, float endLeft, float endTop) {
            return PointFAnimator.ofPointF(target, property, startLeft, startTop, endLeft, endTop);
        }

        @Override
        public <T> Animator ofPointF(final T target, final PointFProperty<T> property, final Path path) {
            return PathAnimatorCompat.ofPointF(target, property, path);
        }

        @Override
        public boolean isAnimatorStarted(Animator anim) {
            return anim.isStarted();
        }

        @Override
        public ObjectAnimator ofFloat(View view, Property<View, Float> property,
                                      float startFraction, float endFraction) {
            float start = property.get(view) * startFraction;
            float end = property.get(view) * endFraction;
            if (start == end) {
                return null;
            }
            property.set(view, start);
            return ObjectAnimator.ofFloat(view, property, end);
        }

        @Override
        public ObjectAnimator ofInt(View view, Property<View, Integer> property,
                                    float startFraction, float endFraction) {
            int start = (int) (property.get(view) * startFraction);
            int end = (int) (property.get(view) * endFraction);
            if (start == end) {
                return null;
            }
            property.set(view, start);
            return ObjectAnimator.ofInt(view, property, end);
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class JellyBeanCompat extends IceCreamSandwichAnimatorCompat {
        @Override
        public boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatAnimatorCompat extends JellyBeanCompat {
        @Override
        public void addPauseListener(Animator animator, final AnimatorPauseListener listener) {
            animator.addPauseListener(listener);
        }

        @Override
        public void pause(Animator animator) {
            animator.pause();
        }

        @Override
        public void resume(Animator animator) {
            animator.resume();
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    static class LollipopAnimatorCompat extends KitKatAnimatorCompat {

        @Override
        public <T> Animator ofPointF(T target, PointFProperty<T> property, Path path) {
            return ObjectAnimator.ofObject(target, property, null, path);
        }

    }

    private static final BaseAnimatorCompat IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= VERSION_CODES.LOLLIPOP) {
            IMPL = new LollipopAnimatorCompat();
        } else if (version >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatAnimatorCompat();
        } else if (version >= VERSION_CODES.JELLY_BEAN) {
            IMPL = new JellyBeanCompat();
        } else if (version >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            IMPL = new IceCreamSandwichAnimatorCompat();
        } else {
            IMPL = new BaseAnimatorCompat();
        }
    }

    public static void addPauseListener(Animator animator, AnimatorPauseListener listener) {
        IMPL.addPauseListener(animator, listener);
    }

    public static void pause(Animator animator) {
        IMPL.pause(animator);
    }

    public static void resume(Animator animator) {
        IMPL.resume(animator);
    }

    public static <T> Animator ofPointF(T target, PointFProperty<T> property,
                                        float startLeft, float startTop,
                                        float endLeft, float endTop) {
        return IMPL.ofPointF(target, property, startLeft, startTop, endLeft, endTop);
    }

    public static <T> Animator ofPointF(T target, PointFProperty<T> property, Path path) {
        if (path != null) {
            return IMPL.ofPointF(target, property, path);
        } else {
            return null;
        }
    }

    public static <T> Animator ofPointF(T target, PointFProperty<T> property, PathMotion pathMotion,
                                        float startLeft, float startTop, float endLeft, float endTop) {
        if (startLeft != endLeft || startTop != endTop) {
            if (pathMotion == null || pathMotion.equals(PathMotion.STRAIGHT_PATH_MOTION)) {
                return ofPointF(target, property, startLeft, startTop, endLeft, endTop);
            } else {
                return ofPointF(target, property, pathMotion.getPath(startLeft, startTop,
                        endLeft, endTop));
            }
        } else {
            return null;
        }
    }

    public static boolean isAnimatorStarted(Animator anim) {
        return IMPL.isAnimatorStarted(anim);
    }

    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    public static ObjectAnimator ofFloat(View view, Property<View, Float> property,
                                         float startFraction, float endFraction) {
        return IMPL.ofFloat(view, property, startFraction, endFraction);
    }

    public static ObjectAnimator ofInt(View view, Property<View, Integer> property,
                                       float startFraction, float endFraction) {
        return IMPL.ofInt(view, property, startFraction, endFraction);
    }

}
