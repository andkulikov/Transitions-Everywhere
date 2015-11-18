package com.transitionseverywhere.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.view.ViewGroup;

import com.transitionseverywhere.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
public class ViewGroupUtils {

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class BaseViewGroupUtils {

        private static Field sFieldLayoutSuppressed;
        private static LayoutTransition sEmptyLayoutTransition;
        private static final Method METHOD_LAYOUT_TRANSITION_CANCEL =
                ReflectionUtils.getPrivateMethod(LayoutTransition.class, "cancel");

        public void suppressLayout(final ViewGroup group, boolean suppress) {
            if (sEmptyLayoutTransition == null) {
                sEmptyLayoutTransition = new LayoutTransition() {
                    @Override
                    public boolean isChangingLayout() {
                        return true;
                    }
                };
                sEmptyLayoutTransition.setAnimator(LayoutTransition.APPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, null);
                sEmptyLayoutTransition.setAnimator(LayoutTransition.CHANGING, null);
            }
            if (suppress) {
                cancelLayoutTransition(group);
                LayoutTransition layoutTransition = group.getLayoutTransition();
                if (layoutTransition != null && layoutTransition != sEmptyLayoutTransition) {
                    group.setTag(R.id.group_layouttransition_backup, group.getLayoutTransition());
                }
                group.setLayoutTransition(sEmptyLayoutTransition);
            } else {
                group.setLayoutTransition(null);
                if (sFieldLayoutSuppressed == null) {
                    sFieldLayoutSuppressed = ReflectionUtils.getPrivateField(ViewGroup.class,
                            "mLayoutSuppressed");
                }
                Boolean suppressed = (Boolean) ReflectionUtils.getFieldValue(group,
                        Boolean.FALSE, sFieldLayoutSuppressed);
                if (!Boolean.FALSE.equals(suppressed)) {
                    ReflectionUtils.setFieldValue(group, sFieldLayoutSuppressed, false);
                    group.requestLayout();
                }
                final LayoutTransition layoutTransition = (LayoutTransition)
                        group.getTag(R.id.group_layouttransition_backup);
                if (layoutTransition != null) {
                    group.setTag(R.id.group_layouttransition_backup, null);
                    group.post(new Runnable() {
                        @Override
                        public void run() {
                            group.setLayoutTransition(layoutTransition);
                        }
                    });
                }
            }
        }

        public boolean cancelLayoutTransition(ViewGroup group) {
            if (group != null) {
                final LayoutTransition layoutTransition = group.getLayoutTransition();
                if (layoutTransition != null && layoutTransition.isRunning() &&
                        METHOD_LAYOUT_TRANSITION_CANCEL != null) {
                    ReflectionUtils.invoke(group.getLayoutTransition(), null, METHOD_LAYOUT_TRANSITION_CANCEL);
                    return true;
                }
            }
            return false;
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMr2ViewGroupUtils extends BaseViewGroupUtils {

        private static final Method METHOD_suppressLayout = ReflectionUtils.getMethod(ViewGroup.class,
                "suppressLayout", boolean.class);

        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            ReflectionUtils.invoke(group, null, METHOD_suppressLayout, suppress);
        }
    }

    private static final BaseViewGroupUtils IMPL;

    static {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMr2ViewGroupUtils();
        } else {
            IMPL = new BaseViewGroupUtils();
        }
    }

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        if (group != null) {
            IMPL.suppressLayout(group, suppress);
        }
    }

    /**
     * @return is cancel performed
     */
    public static boolean cancelLayoutTransition(ViewGroup group) {
        return IMPL.cancelLayoutTransition(group);
    }
}
