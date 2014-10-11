package android.support.compat;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.ViewGroup;

public class ViewGroupCompat {
    interface ViewGroupCompatImpl {
        void suppressLayout(ViewGroup group, boolean suppress);
    }

    static class BaseViewGroupCompatImpl implements ViewGroupCompatImpl {
        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            // TODO: Implement support behavior
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatViewGroupCompatImpl extends BaseViewGroupCompatImpl {
        @Override
        public void suppressLayout(ViewGroup group, boolean suppress) {
            ViewGroupCompatKitKat.suppressLayout(group, suppress);
        }
    }

    static final ViewGroupCompatImpl IMPL;

    static {
        final int version = VERSION.SDK_INT;
        if (version >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatViewGroupCompatImpl();
        } else {
            IMPL = new BaseViewGroupCompatImpl();
        }
    }

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        IMPL.suppressLayout(group, suppress);
    }
}
