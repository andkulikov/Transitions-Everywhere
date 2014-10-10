package android.support.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by stephane on 11/10/13.
 */
public class OverlayCompatibilityHelper {

    public static void addViewOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
        int[] loc = new int[2];
        sceneRoot.getLocationOnScreen(loc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
            overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
            sceneRoot.getOverlay().add(overlayView);
        } else {
            ViewOverlayCompat viewOverlayCompat = getViewOverlay(sceneRoot);
            viewOverlayCompat.addView(overlayView, screenX, screenY);
        }
    }

    public static void addViewOverlay(ViewGroup sceneRoot, View overlayView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sceneRoot.getOverlay().add(overlayView);
        } else {
            getViewOverlay(sceneRoot).addView(overlayView, 0, 0);
        }
    }

    public static void removeViewOverlay(ViewGroup finalSceneRoot, View finalOverlayView) {
        if (finalOverlayView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                finalSceneRoot.getOverlay().remove(finalOverlayView);
            } else {
                ViewOverlayCompat viewOverlayCompat = getViewOverlay(finalSceneRoot);
                viewOverlayCompat.removeView(finalOverlayView);
            }
        }
    }

    public static void addViewOverlay(ViewGroup sceneRoot, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sceneRoot.getOverlay().add(drawable);
        } else {
            ViewOverlayCompat viewOverlayCompat = getViewOverlay(sceneRoot);
            viewOverlayCompat.addDrawable(drawable);
        }
    }

    public static void removeViewOverlay(ViewGroup sceneRoot, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sceneRoot.getOverlay().remove(drawable);
        } else {
            ViewOverlayCompat viewOverlayCompat = getViewOverlay(sceneRoot);
            viewOverlayCompat.removeDrawable(drawable);
        }
    }

    public static void addViewOverlayCompat(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        View group = v;
        while (group != null && !(group.getId() == android.R.id.content)) {
            group = (View) group.getParent();
        }

        if (group != null) {
            ViewOverlayCompat viewOverlayCompat = null;
            for (int i = 0; i < ((FrameLayout) group).getChildCount(); i++) {
                View childAt = ((FrameLayout) group).getChildAt(i);
                if (childAt instanceof ViewOverlayCompat) {
                    viewOverlayCompat = (ViewOverlayCompat) childAt;
                    break;
                }
            }

            if (viewOverlayCompat == null) {
                viewOverlayCompat = new ViewOverlayCompat(v.getContext());
                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.FILL;
                ((FrameLayout) group).addView(viewOverlayCompat, params);
            }
        }
    }

    private static ViewOverlayCompat getViewOverlay(ViewGroup sceneRoot) {
        View group = sceneRoot;
        while (group != null && !(group.getId() == android.R.id.content)) {
            group = (View) group.getParent();
        }

        if (group != null) {
            ViewOverlayCompat viewOverlayCompat = null;
            for (int i = 0; i < ((FrameLayout) group).getChildCount(); i++) {
                View childAt = ((FrameLayout) group).getChildAt(i);
                if (childAt instanceof ViewOverlayCompat) {
                    viewOverlayCompat = (ViewOverlayCompat) childAt;
                    break;
                }
            }

            if (viewOverlayCompat == null) {
                viewOverlayCompat = new ViewOverlayCompat(sceneRoot.getContext());
                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.FILL;
                ((FrameLayout) group).addView(viewOverlayCompat, params);
            }
            return viewOverlayCompat;
        }
        return null;
    }
}
