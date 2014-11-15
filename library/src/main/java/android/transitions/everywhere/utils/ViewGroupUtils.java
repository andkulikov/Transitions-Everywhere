package android.transitions.everywhere.utils;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.ViewGroup;

public class ViewGroupUtils {
    interface ViewGroupUtilsImpl {
        void suppressLayout(ViewGroup group, boolean suppress);
    }

    static class BaseViewGroupUtilsImpl implements ViewGroupUtilsImpl {
        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            // TODO: Implement support behavior
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatViewGroupUtilsImpl extends BaseViewGroupUtilsImpl {
        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            ViewGroupUtilsKitKat.suppressLayout(group, suppress);
        }
    }

    private static final ViewGroupUtilsImpl IMPL;

    static {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatViewGroupUtilsImpl();
        } else {
            IMPL = new BaseViewGroupUtilsImpl();
        }
    }

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        IMPL.suppressLayout(group, suppress);
    }
}
