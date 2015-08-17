package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;

public class ViewOverlayUtils {

    static class BaseViewOverlayUtils {

        public void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.addDrawable(drawable);
        }

        public void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.removeDrawable(drawable);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMR2ViewUtils extends BaseViewOverlayUtils {
        @Override
        public void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
            sceneRoot.getOverlay().add(drawable);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
            sceneRoot.getOverlay().remove(drawable);
        }
    }

    private static final BaseViewOverlayUtils IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewUtils();
        } else {
            IMPL = new BaseViewOverlayUtils();
        }
    }

    public static void addOverlay(ViewGroup sceneRoot, Drawable drawable) {
        IMPL.addOverlay(sceneRoot, drawable);
    }

    public static void removeOverlay(ViewGroup sceneRoot, Drawable drawable) {
        IMPL.removeOverlay(sceneRoot, drawable);
    }
}
