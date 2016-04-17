package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.transitionseverywhere.Crossfade;

public class ViewGroupOverlayUtils {

    static class BaseViewGroupOverlayUtils {

        public void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            if (viewOverlay != null) {
                viewOverlay.addView(overlayView, screenX, screenY);
            }
        }

        public void removeOverlay(ViewGroup sceneRoot, View overlayView) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            if (viewOverlay != null) {
                viewOverlay.removeView(overlayView);
            }
        }

        public void moveViewInOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            if (viewOverlay != null) {
                viewOverlay.moveView(overlayView, screenX, screenY);
            }
        }

        public void initializeOverlay(ViewGroup sceneRoot) {
            ViewOverlayPreJellybean.getOverlay(sceneRoot);
        }

        public int[] getLocationOnScreenOfOverlayView(ViewGroup sceneRoot, View overlayView) {
            int[] location = new int[2];
            overlayView.getLocationOnScreen(location);
            return location;
        }

        public void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                        BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay((ViewGroup) view.getParent());
                if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                    viewOverlay.addDrawable(endDrawable);
                }
                viewOverlay.addDrawable(startDrawable);
            }
        }

        public void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                           BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay((ViewGroup) view.getParent());
                viewOverlay.removeDrawable(startDrawable);
                if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                    viewOverlay.removeDrawable(endDrawable);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class JellyBeanMR2ViewGroupUtils extends BaseViewGroupOverlayUtils {

        @Override
        public void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            moveViewInOverlay(sceneRoot, overlayView, screenX, screenY);
            sceneRoot.getOverlay().add(overlayView);
        }

        @Override
        public void removeOverlay(ViewGroup sceneRoot, View overlayView) {
            sceneRoot.getOverlay().remove(overlayView);
        }

        @Override
        public void moveViewInOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
            if (screenX != 0 || screenY != 0) {
                int[] loc = new int[2];
                sceneRoot.getLocationOnScreen(loc);
                overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
                overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
            }
        }

        @Override
        public void initializeOverlay(ViewGroup sceneRoot) {
            // do nothing
        }

        @Override
        public void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                        BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.add(endDrawable);
            }
            overlay.add(startDrawable);
        }

        @Override
        public int[] getLocationOnScreenOfOverlayView(ViewGroup sceneRoot, View overlayView) {
            int[] location = new int[2];
            sceneRoot.getLocationOnScreen(location);
            location[0] += overlayView.getLeft();
            location[1] += overlayView.getTop();
            return location;
        }

        @Override
        public void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                           BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            overlay.remove(startDrawable);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.remove(endDrawable);
            }
        }

        private static ViewOverlay getViewOverlay(boolean useParentOverlay, View view) {
            return useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
        }

    }

    private static final BaseViewGroupOverlayUtils IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewGroupUtils();
        } else {
            IMPL = new BaseViewGroupOverlayUtils();
        }
    }

    public static void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
        if (overlayView != null) {
            IMPL.addOverlay(sceneRoot, overlayView, screenX, screenY);
        }
    }

    public static void initializeOverlay(ViewGroup sceneRoot) {
        IMPL.initializeOverlay(sceneRoot);
    }

    public static void removeOverlay(ViewGroup sceneRoot, View overlayView) {
        if (overlayView != null) {
            IMPL.removeOverlay(sceneRoot, overlayView);
        }
    }

    public static void moveViewInOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY) {
        if (overlayView != null) {
            IMPL.moveViewInOverlay(sceneRoot, overlayView, screenX, screenY);
        }
    }

    public static int[] getLocationOnScreenOfOverlayView(ViewGroup sceneRoot, View overlayView) {
        if (overlayView != null) {
            return IMPL.getLocationOnScreenOfOverlayView(sceneRoot, overlayView);
        } else {
            return new int[2];
        }
    }

    public static void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                             BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
        if (view != null) {
            IMPL.addCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
        }
    }

    public static void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
        if (view != null) {
            IMPL.removeCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
        }
    }
}
