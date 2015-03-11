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

    @TargetApi(VERSION_CODES.HONEYCOMB)
    static class BaseViewGroupUtilsImpl implements ViewGroupUtilsImpl {

        private static final Field FIELD_LAYOUT_SUPPRESSED =
                ReflectionUtils.getPrivateField(ViewGroup.class, "mLayoutSuppressed");

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
                Boolean suppressed = (Boolean) ReflectionUtils.getFieldValue(group,
                        Boolean.FALSE, FIELD_LAYOUT_SUPPRESSED);
                if (!Boolean.FALSE.equals(suppressed)) {
                    ReflectionUtils.setFieldValue(group, FIELD_LAYOUT_SUPPRESSED, false);
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
