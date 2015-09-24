package com.transitionseverywhere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.transitionseverywhere.activity.TransitionData;
import com.transitionseverywhere.utils.ViewGroupOverlayUtils;

import java.util.ArrayList;

public class ActivityTransitionManager extends TransitionManager {
    private static final String LOG_TAG = "ActivityTransitionManager";
    private static Transition transition;
    private static Transition transitionClone = null;
    private static ArrayList<TransitionData> transitionData;

    public static void startActivity(Activity activity, Class<?> cls) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        startActivity(activity, cls, content, sDefaultTransition);
    }

    public static void startActivity(Activity activity, Class<?> cls, Transition transition) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        startActivity(activity, cls, content, transition);
    }

    public static void startActivity(Activity activity, Class<?> cls, View content, Transition transition) {
        Intent intent = new Intent(activity, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (isTransitionsAllowed()) {

                if (transition != null) {
                    ActivityTransitionManager.transition = transition;
                    transitionClone = transition.clone();
                }
            }
            sceneChangeSetup((ViewGroup) content, transitionClone);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void setContentView(Activity activity, int layoutId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            final Scene scene = Scene.getSceneForLayout(content, layoutId, activity);
            scene.enter();
            activityChangeRunTransition(content, transitionClone, false);
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
        transitionClone.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
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
        activityChangeRunTransition(content, transitionClone, true);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected static void activityChangeRunTransition(final ViewGroup sceneRoot,
                                                      final Transition transition,
                                                      boolean isReverse) {
        if (transition != null && sceneRoot != null) {
            ViewGroupOverlayUtils.initializeOverlay(sceneRoot);
            MultiListener listener = new MultiListener(transition, sceneRoot, true);
            if (isReverse) {
                listener.run(true);
            } else {
                sceneRoot.addOnAttachStateChangeListener(listener);
                sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
            }
        }
    }


//    private static void startTransition(Activity activity, int layoutId, Bundle bundle,
//                                        Transition transition) {
//        ViewGroup decorView = (ViewGroup) activity.findViewById(android.R.id.content);
//        FrameLayout phantomView = new FrameLayout(activity.getApplicationContext());
//        phantomView.setId(R.id.phantom_view);
//        ActivityTransitionManager.transitionData = ActivityTransitionOptions.fromTransitionBundle(bundle);
//        for (TransitionData data : transitionData) {
//            View view = new View(activity.getApplicationContext());
//            setParamsForView(view, phantomView, data);
//        }
//        activity.setContentView(phantomView);
//        Scene newScene = Scene.getSceneForLayout(decorView, layoutId, activity);
//        changeScene(newScene, transition);
//    }
//
//    private static void exitTransition(final Activity activity) {
//        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
//        FrameLayout phantomView = new FrameLayout(activity.getApplicationContext());
//        for (TransitionData data : transitionData) {
//            View view = activity.findViewById(data.getId());
//            if (view != null) {
//                if (view instanceof ImageView) {
//                    ImageView imageView = new ImageView(activity);
//                    imageView.setImageDrawable(((ImageView) view).getDrawable());
//                    setParamsForView(imageView, phantomView, data);
//                } else if (view instanceof TextView) {
//                    TextView textView = new TextView(activity);
//                    textView.setText(((TextView) view).getText());
//                    textView.setTextColor(((TextView) view).getTextColors());
//                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((TextView) view).getTextSize());
//                    setParamsForView(textView, phantomView, data);
//                } else {
//                    View temp = new View(activity);
//                    setParamsForView(temp, phantomView, data);
//                    if (Build.VERSION.SDK_INT >= 16) {
//                        temp.setBackground(view.getBackground());
//                    } else {
//                        temp.setBackgroundDrawable(view.getBackground());
//                    }
//                }
//            }
//        }
//        Scene exitScene = Scene.getSceneForView(content, phantomView);
//        transition.addListener(new Transition.TransitionListener() {
//            @Override
//            public void onTransitionStart(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionEnd(Transition transition) {
//                activity.finish();
//                activity.overridePendingTransition(0, 0);
//            }
//
//            @Override
//            public void onTransitionCancel(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionPause(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionResume(Transition transition) {
//
//            }
//        });
//        changeScene(exitScene, transition);
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private static void setParamsForView(View view, ViewGroup viewGroup, TransitionData data) {
//        view.setId(data.getId());
//        view.setLeft(data.getLeft());
//        view.setTop(data.getTop());
//        view.setRight(data.getLeft() + data.getWidth());
//        view.setBottom(data.getTop() + data.getHeight());
//        viewGroup.addView(view);
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.width = data.getWidth();
//        layoutParams.height = data.getHeight();
//        //for debug
//        layoutParams.setMargins(data.getLeft(), data.getTop(), 0, 0);
//    }


}
