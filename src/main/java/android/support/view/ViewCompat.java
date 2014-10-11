package android.support.view;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

public class ViewCompat {
    interface ViewCompatImpl {
        float getTransitionAlpha(View v);

        boolean isLaidOut(View v);

        void setClipBounds(View v, Rect clipBounds);

        void setTransitionAlpha(View v, float alpha);

        String getAlphaProperty();
    }

    static class BaseViewCompatImpl implements ViewCompatImpl {
        @Override
        public float getTransitionAlpha(View v) {
            return v.getAlpha();
        }

        @Override
        public boolean isLaidOut(View v) {
            // TODO: Implement support behavior
            return true;
        }

        @Override
        public void setClipBounds(View v, Rect clipBounds) {
            // TODO: Implement support behavior
        }

        @Override
        public void setTransitionAlpha(View v, float alpha) {
            v.setAlpha(alpha);
        }

        @Override
        public String getAlphaProperty() {
            return "alpha";
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMR2ViewCompatImpl extends BaseViewCompatImpl {
        @Override
        public void setClipBounds(View v, Rect clipBounds) {
            ViewCompatJellybeanMr2.setClipBounds(v, clipBounds);
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatViewCompatImpl extends JellyBeanMR2ViewCompatImpl {
        @Override
        public float getTransitionAlpha(View v) {
            return ViewCompatKitKat.getTransitionAlpha(v);
        }

        @Override
        public boolean isLaidOut(View v) {
            return ViewCompatKitKat.isLaidOut(v);
        }

        @Override
        public void setTransitionAlpha(View v, float alpha) {
            ViewCompatKitKat.setTransitionAlpha(v, alpha);
        }

        @Override
        public String getAlphaProperty() {
            return "transitionAlpha";
        }
    }

    static final ViewCompatImpl IMPL;

    static {
        final int version = VERSION.SDK_INT;
        if (version >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatViewCompatImpl();
        } else if (version >= VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static float getTransitionAlpha(View v) {
        return IMPL.getTransitionAlpha(v);
    }

    public static boolean isLaidOut(View v) {
        return IMPL.isLaidOut(v);
    }

    public static void setClipBounds(View v, Rect clipBounds) {
        IMPL.setClipBounds(v, clipBounds);
    }

    public static void setTransitionAlpha(View v, float alpha) {
        IMPL.setTransitionAlpha(v, alpha);
    }

    public static String getAlphaProperty() {
        return IMPL.getAlphaProperty();
    }
}
