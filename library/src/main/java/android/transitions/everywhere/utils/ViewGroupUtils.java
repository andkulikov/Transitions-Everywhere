package android.transitions.everywhere.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.transitions.everywhere.R;
import android.view.ViewGroup;

import java.lang.reflect.Field;

@TargetApi(VERSION_CODES.HONEYCOMB)
public class ViewGroupUtils {

    public static final LayoutTransition EMPTY_LAYOUT_TRANSITION = new LayoutTransition() {
        @Override
        public boolean isChangingLayout() {
            return true;
        }
    };
    static {
        EMPTY_LAYOUT_TRANSITION.setAnimator(LayoutTransition.APPEARING, null);
        EMPTY_LAYOUT_TRANSITION.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
        EMPTY_LAYOUT_TRANSITION.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
        EMPTY_LAYOUT_TRANSITION.setAnimator(LayoutTransition.DISAPPEARING, null);
        EMPTY_LAYOUT_TRANSITION.setAnimator(LayoutTransition.CHANGING, null);
    }

    interface ViewGroupUtilsImpl {
        void suppressLayout(ViewGroup group, boolean suppress);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class BaseViewGroupUtilsImpl implements ViewGroupUtilsImpl {

        private static Field sFieldLayoutSuppressed;

        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            if (suppress) {
                if (group.getLayoutTransition() != null) {
                    group.setTag(R.id.group_layouttransition_cache, group.getLayoutTransition());
                }
                group.setLayoutTransition(EMPTY_LAYOUT_TRANSITION);
            } else {
                group.setLayoutTransition((LayoutTransition) group.getTag(R.id.group_layouttransition_cache));
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
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
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
