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

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
public class ViewUtils {

    static class BaseViewUtils {

        @Nullable
        private static final Field FIELD_VIEW_FLAGS =
                ReflectionUtils.getPrivateField(View.class, "mViewFlags");

        @Nullable
        private static final Field FIELD_LAYOUT_PARAMS =
                ReflectionUtils.getPrivateField(View.class, "mLayoutParams");

        private static final int VIEW_VISIBILITY_MASK = 0x0000000C;

        @Nullable
        private static final Method METHOD_SET_FRAME =
                ReflectionUtils.getPrivateMethod(View.class, "setFrame", int.class, int.class,
                        int.class, int.class);

        public float getTransitionAlpha(@NonNull View v) {
            return v.getAlpha();
        }

        public boolean isLaidOut(@NonNull View v, boolean defaultValue) {
            return defaultValue;
        }

        public void setClipBounds(@NonNull View v, @Nullable Rect clipBounds) {
            // TODO: Implement support behavior
        }

        @Nullable
        public Rect getClipBounds(@NonNull View v) {
            // TODO: Implement support behavior
            return null;
        }

        public void setTransitionName(@NonNull View v, @Nullable String name) {
            v.setTag(R.id.transitionName, name);
        }

        @Nullable
        public String getTransitionName(@NonNull View v) {
            return (String) v.getTag(R.id.transitionName);
        }

        public void setTranslationZ(@NonNull View view, float z) {
            // do nothing
        }

        public float getTranslationZ(@NonNull View view) {
            return 0;
        }

        @Nullable
        public View addGhostView(@NonNull View view, @NonNull ViewGroup viewGroup, @Nullable Matrix matrix) {
            return null;
        }

        public void removeGhostView(@NonNull View view) {
            // do nothing
        }

        public void transformMatrixToGlobal(@NonNull View view, @NonNull Matrix matrix) {
            // TODO: Implement support behavior
        }

        public void transformMatrixToLocal(@NonNull View v, @NonNull Matrix matrix) {
            // TODO: Implement support behavior
        }

        public void setAnimationMatrix(@NonNull View view, @Nullable Matrix matrix) {
            // TODO: Implement support behavior
        }

        @Nullable
        public Object getWindowId(@NonNull View view) {
            return view.getWindowToken();
        }

        public boolean isRtl(@NonNull View view) {
            return false;
        }

        public void setHasTransientState(@NonNull View view, boolean hasTransientState) {
            // do nothing; API doesn't exist
        }

        public boolean hasTransientState(@NonNull View view) {
            return false;
        }

        public void setTransitionVisibility(@NonNull View v, int visibility) {
            int value = (Integer) ReflectionUtils.getFieldValue(v, 0, FIELD_VIEW_FLAGS);
            value = (value & ~VIEW_VISIBILITY_MASK) | visibility;
            ReflectionUtils.setFieldValue(v, FIELD_VIEW_FLAGS, value);
        }

        public void setLeftTopRightBottom(@NonNull View v, int left, int top, int right, int bottom) {
            ReflectionUtils.invoke(v, null, METHOD_SET_FRAME, left, top, right, bottom);
        }

        public void setLayoutParamsSilently(@NonNull View view, @Nullable ViewGroup.LayoutParams layoutParams) {
            ReflectionUtils.setFieldValue(view, FIELD_LAYOUT_PARAMS, layoutParams);
        }

    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class ViewUtilsJellyBean extends BaseViewUtils {
        @Override
        public void setHasTransientState(@NonNull View view, boolean hasTransientState) {
            view.setHasTransientState(hasTransientState);
        }

        @Override
        public boolean hasTransientState(@NonNull View view) {
            return view.hasTransientState();
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    static class ViewUtilsJellyBeanMR1 extends ViewUtilsJellyBean {
        @Override
        public boolean isRtl(@NonNull View view) {
            return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    static class ViewUtilsJellyBeanMR2 extends ViewUtilsJellyBeanMR1 {
        @Override
        public void setClipBounds(@NonNull View v, @Nullable Rect clipBounds) {
            v.setClipBounds(clipBounds);
        }

        @Override
        @Nullable
        public Rect getClipBounds(@NonNull View v) {
            return v.getClipBounds();
        }

        @Override
        @Nullable
        public Object getWindowId(@NonNull View view) {
            return view.getWindowId();
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class ViewUtilsKitKat extends ViewUtilsJellyBeanMR2 {
        @Override
        public boolean isLaidOut(@NonNull View v, boolean defaultValue) {
            return v.isLaidOut();
        }
    }

    @NonNull
    private static final BaseViewUtils IMPL;

    static {
        final int version = VERSION.SDK_INT;
        if (version >= VERSION_CODES.LOLLIPOP_MR1) {
            IMPL = new ViewUtilsLollipopMr1();
        } else if (version >= VERSION_CODES.LOLLIPOP) {
            IMPL = new ViewUtilsLollipop();
        } else if (version >= VERSION_CODES.KITKAT) {
            IMPL = new ViewUtilsKitKat();
        } else if (version >= VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new ViewUtilsJellyBeanMR2();
        } else if (version >= VERSION_CODES.JELLY_BEAN_MR1) {
            IMPL = new ViewUtilsJellyBeanMR1();
        } else if (version >= VERSION_CODES.JELLY_BEAN) {
            IMPL = new ViewUtilsJellyBean();
        } else {
            IMPL = new BaseViewUtils();
        }
    }

    public static float getTransitionAlpha(@NonNull View v) {
        return IMPL.getTransitionAlpha(v);
    }

    public static boolean isLaidOut(@NonNull View v, boolean defaultValue) {
        return IMPL.isLaidOut(v, defaultValue);
    }

    public static void setClipBounds(@NonNull View v, @Nullable Rect clipBounds) {
        IMPL.setClipBounds(v, clipBounds);
    }

    @Nullable
    public static Rect getClipBounds(@NonNull View v) {
        return IMPL.getClipBounds(v);
    }

    public static void setTransitionName(@NonNull View v, @Nullable String name) {
        IMPL.setTransitionName(v, name);
    }

    @Nullable
    public static String getTransitionName(@NonNull View v) {
        return IMPL.getTransitionName(v);
    }

    public static float getTranslationZ(@NonNull View view) {
        return IMPL.getTranslationZ(view);
    }

    public static void setTranslationZ(@NonNull View view, float z) {
        IMPL.setTranslationZ(view, z);
    }

    public static void transformMatrixToGlobal(@NonNull View view, @NonNull Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    public static void transformMatrixToLocal(@NonNull View view, @NonNull Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

    public static void setAnimationMatrix(@NonNull View view, @Nullable Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    @Nullable
    public static View addGhostView(@NonNull View view, @NonNull ViewGroup viewGroup, @Nullable Matrix matrix) {
        return IMPL.addGhostView(view, viewGroup, matrix);
    }

    public static void removeGhostView(@NonNull View view) {
        IMPL.removeGhostView(view);
    }

    @Nullable
    public static Object getWindowId(@NonNull View view) {
        return IMPL.getWindowId(view);
    }

    public static boolean isRtl(@NonNull View view) {
        return IMPL.isRtl(view);
    }

    public static boolean hasTransientState(@NonNull View view) {
        return IMPL.hasTransientState(view);
    }

    public static void setHasTransientState(@NonNull View view, boolean hasTransientState) {
        IMPL.setHasTransientState(view, hasTransientState);
    }

    /**
     * Change the visibility of the View without triggering any other changes. This is
     * important for transitions, where visibility changes should not adjust focus or
     * trigger a new layout. This is only used when the visibility has already been changed
     * and we need a transient value during an animation. When the animation completes,
     * the original visibility value is always restored.
     *
     * @param visibility One of View.VISIBLE, View.INVISIBLE, or View.GONE.
     */
    public static void setTransitionVisibility(@NonNull View view, int visibility) {
        IMPL.setTransitionVisibility(view, visibility);
    }

    public static void setLeftTopRightBottom(@NonNull View view, int left, int top, int right, int bottom) {
        IMPL.setLeftTopRightBottom(view, left, top, right, bottom);
    }

    public static void setLayoutParamsSilently(@NonNull View view, @Nullable ViewGroup.LayoutParams layoutParams) {
        IMPL.setLayoutParamsSilently(view, layoutParams);
    }
}
