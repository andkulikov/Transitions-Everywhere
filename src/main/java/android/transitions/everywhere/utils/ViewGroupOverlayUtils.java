package android.transitions.everywhere.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.transitions.everywhere.hidden.Crossfade;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;

public class ViewGroupOverlayUtils {

    interface ViewGroupOverlayCompatImpl {
        void addOverlay(ViewGroup sceneRoot, View overlayView, int screenX, int screenY);

        void removeOverlay(ViewGroup sceneRoot, View overlayView);

        void addOverlayIfNeeded(View view);

        void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                 BitmapDrawable startDrawable, BitmapDrawable endDrawable);

        void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                    BitmapDrawable startDrawable, BitmapDrawable endDrawable);
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

        @Override
        public void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                        BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            //TODO ViewOverlay
        }

        @Override
        public void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                           BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            //TODO ViewOverlay
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

        @Override
        public void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                        BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            overlay.remove(startDrawable);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.remove(endDrawable);
            }
        }

        @Override
        public void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                           BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.add(endDrawable);
            }
            overlay.add(startDrawable);
        }

        private static ViewOverlay getViewOverlay(boolean useParentOverlay, View view) {
            return useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
        }

    }

    private static final ViewGroupOverlayCompatImpl IMPL;

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

    public static void addCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                             BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
        IMPL.addCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
    }

    public static void removeCrossfadeOverlay(boolean useParentOverlay, View view, int fadeBehavior,
                                BitmapDrawable startDrawable, BitmapDrawable endDrawable) {
        IMPL.removeCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
    }
}
