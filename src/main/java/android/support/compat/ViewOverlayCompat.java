package android.support.compat;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.view.ViewOverlayPreJellybean;
import android.view.ViewGroup;

public abstract class ViewOverlayCompat {

    interface ViewOverlayCompatImpl {
        void addOverlay(ViewGroup sceneRoot, Drawable drawable);

        void removeOverlay(ViewGroup sceneRoot, Drawable drawable);
    }

    static class BaseViewOverlayCompatImpl implements ViewOverlayCompatImpl {
        @Override
        public void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.addDrawable(drawable);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.removeDrawable(drawable);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMR2ViewCompatImpl implements ViewOverlayCompatImpl {
        @Override
        public void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
            sceneRoot.getOverlay().add(drawable);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
            sceneRoot.getOverlay().remove(drawable);
        }
    }

    private static final ViewOverlayCompatImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewCompatImpl();
        } else {
            IMPL = new BaseViewOverlayCompatImpl();
        }
    }

    public static void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
        IMPL.addOverlay(sceneRoot, drawable);
    }

    public static void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
        IMPL.removeOverlay(sceneRoot, drawable);
    }
}
