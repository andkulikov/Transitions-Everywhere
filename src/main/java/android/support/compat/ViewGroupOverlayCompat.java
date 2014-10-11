package android.support.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.view.ViewOverlayPreJellybean;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class ViewGroupOverlayCompat {

    interface ViewGroupOverlayCompatImpl {
        void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY);

        void removeOverlay(ViewGroup sceneRoot, View overlayView);

        void addOverlayIfNeeded(View view);
    }

    static class BaseViewGroupOverlayCompatImpl implements ViewGroupOverlayCompatImpl {

        @Override
        public void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.addView(overlayView, screenX, screenY);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, View overlayView) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.removeView(overlayView);
        }

        @Override
        public void addOverlayIfNeeded(View view) {
            while (view != null && !(view.getId() == android.R.id.content)) {
                view = (View) view.getParent();
            }
            if (view != null && view instanceof FrameLayout) {
                FrameLayout contentLayout = (FrameLayout) view;
                if (contentLayout != null) {
                    ViewOverlayPreJellybean viewOverlay = null;
                    for (int i = 0; i < contentLayout.getChildCount(); i++) {
                        View child = contentLayout.getChildAt(i);
                        if (child instanceof ViewOverlayPreJellybean) {
                            viewOverlay = (ViewOverlayPreJellybean) child;
                            break;
                        }
                    }

                    if (viewOverlay == null) {
                        viewOverlay = new ViewOverlayPreJellybean(view.getContext());
                        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        params.gravity = Gravity.FILL;
                        contentLayout.addView(viewOverlay, params);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMR2ViewGroupCompatImpl implements ViewGroupOverlayCompatImpl {
        @Override
        public void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            if (screenX != 0 && screenY != 0) {
                int[] loc = new int[2];
                sceneRoot.getLocationOnScreen(loc);
                overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
                overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
            }
            sceneRoot.getOverlay().add(overlayView);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, View overlayView) {
            sceneRoot.getOverlay().remove(overlayView);
        }

        @Override
        public void addOverlayIfNeeded(View v) {
            // do nothing
        }

    }

    static final ViewGroupOverlayCompatImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewGroupCompatImpl();
        } else {
            IMPL = new BaseViewGroupOverlayCompatImpl();
        }
    }

    public static void addOverlay(ViewGroup sceneRoot, View overlayView) {
        addOverlay(sceneRoot, overlayView, 0, 0);
    }

    public static void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
        if (overlayView != null) {
            IMPL.addOverlay(sceneRoot, overlayView, screenX, screenY);
        }
    }

    public static void removeOverlay(ViewGroup sceneRoot, View overlayView) {
        if (overlayView != null) {
            IMPL.removeOverlay(sceneRoot, overlayView);
        }
    }

    public static void addOverlayIfNeeded(View view) {
        if (view != null) {
            IMPL.addOverlayIfNeeded(view);
        }
    }
}
