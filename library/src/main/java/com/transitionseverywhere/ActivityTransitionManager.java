package com.transitionseverywhere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.utils.BitmapUtil;
import com.transitionseverywhere.utils.ViewGroupOverlayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivityTransitionManager extends TransitionManager {
    private static final String LOG_TAG = "ActivityTransitionManager";
    private static Transition transitionClone = null;

    public static void startActivity(Activity activity, Class<?> cls) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        startActivity(activity, cls, content, sDefaultTransition);
    }

    public static void startActivity(Activity activity, Class<?> cls, Transition transition) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        startActivity(activity, cls, content, transition);
    }

    public static void startActivity(Activity activity, Class<?> cls, ViewGroup content,
                                     Transition transition) {
        Intent intent = new Intent(activity, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (isTransitionsAllowed()) {
                if (transition != null) {
                    transitionClone = transition.clone();
                }
            }
            sceneChangeSetup(content, transitionClone);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void setContentView(Activity activity, int layoutId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            final Scene scene = Scene.getSceneForLayout(content, layoutId, activity);
            scene.enter();
            activityChangeRunTransition(content, transitionClone);
        } else {
            activity.setContentView(layoutId);
        }
    }

    public static void onGoBack(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            exit(activity);
        } else {
            activity.finish();
        }
    }

    private static void exit(final Activity activity) {
        final ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        transitionClone.setCanRemoveViews(true);
        transitionClone.setIsReverse(true);
        transitionClone.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transitionClone = null;
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        activityChangeRunTransition(content, transitionClone);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected static void activityChangeRunTransition(final ViewGroup sceneRoot,
                                                      final Transition transition) {
        if (transition != null && sceneRoot != null) {
            ViewGroupOverlayUtils.initializeOverlay(sceneRoot);
            MultiListener listener = new MultiListener(transition, sceneRoot, true);
            if (transition.isReverse()) {
                listener.run();
            } else {
                sceneRoot.addOnAttachStateChangeListener(listener);
                sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
            }
        }
    }
}
