package android.transitions.everywhere.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.ViewGroup;

import java.lang.reflect.Field;

@TargetApi(VERSION_CODES.HONEYCOMB)
public class ViewGroupUtils {
    interface ViewGroupUtilsImpl {
        void suppressLayout(ViewGroup group, boolean suppress);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class BaseViewGroupUtilsImpl implements ViewGroupUtilsImpl {

        private static Field sFieldLayoutSuppressed;

        static LayoutTransition mLayoutTransition = new LayoutTransition() {
            @Override
            public boolean isChangingLayout() {
                return true;
            }
        };
        static {
            mLayoutTransition.setAnimator(LayoutTransition.APPEARING, null);
            mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
            mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
            mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, null);
            mLayoutTransition.setAnimator(LayoutTransition.CHANGING, null);
        }

        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            if (suppress) {
                group.setLayoutTransition(mLayoutTransition);
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
            }
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMr2ViewGroupUtilsImpl extends BaseViewGroupUtilsImpl {
        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            ViewGroupUtilsJellyBeanMr2.suppressLayout(group, suppress);
        }
    }

    private static final ViewGroupUtilsImpl IMPL;

    static {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMr2ViewGroupUtilsImpl();
        } else {
            IMPL = new BaseViewGroupUtilsImpl();
        }
    }

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        if (group != null) {
            IMPL.suppressLayout(group, suppress);
        }
    }
}
