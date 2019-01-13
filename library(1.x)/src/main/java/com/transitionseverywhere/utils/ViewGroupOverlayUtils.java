/*
 * Copyright (C) 2014 Andrey Kulikov (andkulikov@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.transitionseverywhere.Crossfade;

public class ViewGroupOverlayUtils {

    static class BaseViewGroupOverlayUtils {

        public void addOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView, int screenX, int screenY) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.addView(overlayView, screenX, screenY);
        }

        public void removeOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.removeView(overlayView);
        }

        public void moveViewInOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView, int screenX, int screenY) {
            ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay(sceneRoot);
            viewOverlay.moveView(overlayView, screenX, screenY);
        }

        public void initializeOverlay(@NonNull ViewGroup sceneRoot) {
            ViewOverlayPreJellybean.getOverlay(sceneRoot);
        }

        @NonNull
        public int[] getLocationOnScreenOfOverlayView(@NonNull ViewGroup sceneRoot, @NonNull View overlayView) {
            int[] location = new int[2];
            overlayView.getLocationOnScreen(location);
            return location;
        }

        public void addCrossfadeOverlay(boolean useParentOverlay, @NonNull View view, int fadeBehavior,
                                        @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
            if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ViewOverlayPreJellybean viewOverlay = ViewOverlayPreJellybean.getOverlay((ViewGroup) view.getParent());
                if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                    viewOverlay.addDrawable(endDrawable);
                }
                viewOverlay.addDrawable(startDrawable);
            }
        }

        public void removeCrossfadeOverlay(boolean useParentOverlay, @NonNull View view, int fadeBehavior,
                                           @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
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
        public void addOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView, int screenX, int screenY) {
            moveViewInOverlay(sceneRoot, overlayView, screenX, screenY);
            sceneRoot.getOverlay().add(overlayView);
        }

        @Override
        public void removeOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView) {
            sceneRoot.getOverlay().remove(overlayView);
        }

        @Override
        public void moveViewInOverlay(@NonNull ViewGroup sceneRoot, @NonNull View overlayView, int screenX, int screenY) {
            if (screenX != 0 || screenY != 0) {
                int[] loc = new int[2];
                sceneRoot.getLocationOnScreen(loc);
                overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
                overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
            }
        }

        @Override
        public void initializeOverlay(@NonNull ViewGroup sceneRoot) {
            // do nothing
        }

        @Override
        public void addCrossfadeOverlay(boolean useParentOverlay, @NonNull View view, int fadeBehavior,
                                        @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.add(endDrawable);
            }
            overlay.add(startDrawable);
        }

        @NonNull
        @Override
        public int[] getLocationOnScreenOfOverlayView(@NonNull ViewGroup sceneRoot, @NonNull View overlayView) {
            int[] location = new int[2];
            sceneRoot.getLocationOnScreen(location);
            location[0] += overlayView.getLeft();
            location[1] += overlayView.getTop();
            return location;
        }

        @Override
        public void removeCrossfadeOverlay(boolean useParentOverlay, @NonNull View view, int fadeBehavior,
                                           @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
            ViewOverlay overlay = getViewOverlay(useParentOverlay, view);
            overlay.remove(startDrawable);
            if (fadeBehavior == Crossfade.FADE_BEHAVIOR_REVEAL) {
                overlay.remove(endDrawable);
            }
        }

        @NonNull
        private static ViewOverlay getViewOverlay(boolean useParentOverlay, @NonNull View view) {
            return useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
        }

    }

    @NonNull
    private static final BaseViewGroupOverlayUtils IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new JellyBeanMR2ViewGroupUtils();
        } else {
            IMPL = new BaseViewGroupOverlayUtils();
        }
    }

    public static void addOverlay(@NonNull ViewGroup sceneRoot, @Nullable View overlayView, int screenX, int screenY) {
        if (overlayView != null) {
            IMPL.addOverlay(sceneRoot, overlayView, screenX, screenY);
        }
    }

    public static void initializeOverlay(@NonNull ViewGroup sceneRoot) {
        IMPL.initializeOverlay(sceneRoot);
    }

    public static void removeOverlay(@NonNull ViewGroup sceneRoot, @Nullable View overlayView) {
        if (overlayView != null) {
            IMPL.removeOverlay(sceneRoot, overlayView);
        }
    }

    public static void moveViewInOverlay(@NonNull ViewGroup sceneRoot, @Nullable View overlayView, int screenX, int screenY) {
        if (overlayView != null) {
            IMPL.moveViewInOverlay(sceneRoot, overlayView, screenX, screenY);
        }
    }

    @NonNull
    public static int[] getLocationOnScreenOfOverlayView(@NonNull ViewGroup sceneRoot, @Nullable View overlayView) {
        if (overlayView != null) {
            return IMPL.getLocationOnScreenOfOverlayView(sceneRoot, overlayView);
        } else {
            return new int[2];
        }
    }

    public static void addCrossfadeOverlay(boolean useParentOverlay, @Nullable View view, int fadeBehavior,
                                           @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
        if (view != null) {
            IMPL.addCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
        }
    }

    public static void removeCrossfadeOverlay(boolean useParentOverlay, @Nullable View view, int fadeBehavior,
                                              @NonNull BitmapDrawable startDrawable, @NonNull BitmapDrawable endDrawable) {
        if (view != null) {
            IMPL.removeCrossfadeOverlay(useParentOverlay, view, fadeBehavior, startDrawable, endDrawable);
        }
    }
}
