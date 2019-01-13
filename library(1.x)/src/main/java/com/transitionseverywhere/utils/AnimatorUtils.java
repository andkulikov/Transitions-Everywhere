/*
 * Copyright (C) 2014 Andrey Kulikov (andkulikov@gmail.com)
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
package com.transitionseverywhere.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorPauseListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Property;
import android.view.View;

import com.transitionseverywhere.PathMotion;

public class AnimatorUtils {

    static class BaseAnimatorCompat {

        public void addPauseListener(@NonNull Animator animator, @NonNull AnimatorPauseListener listener) {
        }

        public void pause(@NonNull Animator animator) {
        }

        public void resume(@NonNull Animator animator) {
        }

        @Nullable
        public <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, float startLeft,
                                     float startTop, float endLeft, float endTop) {
            return null;
        }

        @Nullable
        public <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, @Nullable Path path) {
            return null;
        }

        public boolean isAnimatorStarted(@NonNull Animator anim) {
            return false;
        }

        public boolean hasOverlappingRendering(@NonNull View view) {
            return true;
        }

        @Nullable
        public ObjectAnimator ofFloat(@Nullable View view, @NonNull Property<View, Float> property,
                                      float startFraction, float endFraction) {
            return null;
        }

        @Nullable
        public ObjectAnimator ofInt(@Nullable View view, @NonNull Property<View, Integer> property,
                                    float startFraction, float endFraction) {
            return null;
        }
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    static class IceCreamSandwichAnimatorCompat extends BaseAnimatorCompat {

        @Override
        public void pause(@NonNull Animator animator) {
            animator.cancel();
        }

        @Override
        @Nullable
        public <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, float startLeft,
                                     float startTop, float endLeft, float endTop) {
            return PointFAnimator.ofPointF(target, property, startLeft, startTop, endLeft, endTop);
        }

        @Override
        @Nullable
        public <T> Animator ofPointF(@Nullable final T target, @NonNull final PointFProperty<T> property, final @Nullable Path path) {
            return PathAnimatorCompat.ofPointF(target, property, path);
        }

        @Override
        public boolean isAnimatorStarted(@NonNull Animator anim) {
            return anim.isStarted();
        }

        @Override
        @Nullable
        public ObjectAnimator ofFloat(@Nullable View view, @NonNull Property<View, Float> property,
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
        @Nullable
        public ObjectAnimator ofInt(@Nullable View view, @NonNull Property<View, Integer> property,
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
        public boolean hasOverlappingRendering(@NonNull View view) {
            return view.hasOverlappingRendering();
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatAnimatorCompat extends JellyBeanCompat {
        @Override
        public void addPauseListener(@NonNull Animator animator, @NonNull final AnimatorPauseListener listener) {
            animator.addPauseListener(listener);
        }

        @Override
        public void pause(@NonNull Animator animator) {
            animator.pause();
        }

        @Override
        public void resume(@NonNull Animator animator) {
            animator.resume();
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    static class LollipopAnimatorCompat extends KitKatAnimatorCompat {

        @Override
        public <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, Path path) {
            return ObjectAnimator.ofObject(target, property, null, path);
        }

    }

    @NonNull
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

    public static void addPauseListener(@NonNull Animator animator, @NonNull AnimatorPauseListener listener) {
        IMPL.addPauseListener(animator, listener);
    }

    public static void pause(@NonNull Animator animator) {
        IMPL.pause(animator);
    }

    public static void resume(@NonNull Animator animator) {
        IMPL.resume(animator);
    }

    @Nullable
    public static <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property,
                                        float startLeft, float startTop,
                                        float endLeft, float endTop) {
        return IMPL.ofPointF(target, property, startLeft, startTop, endLeft, endTop);
    }

    @Nullable
    public static <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, @Nullable Path path) {
        if (path != null) {
            return IMPL.ofPointF(target, property, path);
        } else {
            return null;
        }
    }

    @Nullable
    public static <T> Animator ofPointF(@Nullable T target, @NonNull PointFProperty<T> property, @Nullable PathMotion pathMotion,
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

    public static boolean isAnimatorStarted(@NonNull Animator anim) {
        return IMPL.isAnimatorStarted(anim);
    }

    public static boolean hasOverlappingRendering(@NonNull View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    @Nullable
    public static ObjectAnimator ofFloat(@Nullable View view, @NonNull Property<View, Float> property,
                                         float startFraction, float endFraction) {
        return IMPL.ofFloat(view, property, startFraction, endFraction);
    }

    @Nullable
    public static ObjectAnimator ofInt(@Nullable View view, @NonNull Property<View, Integer> property,
                                       float startFraction, float endFraction) {
        return IMPL.ofInt(view, property, startFraction, endFraction);
    }

}
