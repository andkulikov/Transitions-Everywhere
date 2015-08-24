package com.transitionseverywhere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.transitionseverywhere.activity.ActivityTransitionOptions;
import com.transitionseverywhere.activity.TransitionData;

import java.util.ArrayList;

public class ActivityTransitionManager extends TransitionManager {
    private static final String LOG_TAG = "ActivityTransitionManager";
    private static Transition transition;
    private static ArrayList<TransitionData> transitionData;

    public static void startActivity(Activity activity, Class<?> cls) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        startActivity(activity, cls, content);
    }

    public static void startActivity(Activity activity, Class<?> cls, View content) {
        Intent intent = new Intent(activity, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Bundle transitionBundle = ActivityTransitionOptions.fromView(content);
            intent.putExtras(transitionBundle);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void setContentView(Activity activity, int layoutId, Bundle bundle) {
        setContentView(activity, layoutId, bundle, sDefaultTransition);
    }

    public static void setContentView(Activity activity, int layoutId, Bundle bundle,
                                      Transition transition) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActivityTransitionManager.transition = transition;
            startTransition(activity, layoutId, bundle, transition);
        } else {
            activity.setContentView(layoutId);
        }
    }

    public static void onGoBack(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            exitTransition(activity);
        } else {
            activity.finish();
        }
    }

    private static void startTransition(Activity activity, int layoutId, Bundle bundle,
                                        Transition transition) {
        ViewGroup decorView = (ViewGroup) activity.findViewById(android.R.id.content);
        FrameLayout phantomView = new FrameLayout(activity.getApplicationContext());
        phantomView.setId(R.id.phantom_view);
        ActivityTransitionManager.transitionData = ActivityTransitionOptions.fromTransitionBundle(bundle);
        for (TransitionData data : transitionData) {
            View view = new View(activity.getApplicationContext());
            setParamsForView(view, phantomView, data);
        }
        activity.setContentView(phantomView);
        Scene newScene = Scene.getSceneForLayout(decorView, layoutId, activity);
        changeScene(newScene, transition);
    }

    private static void exitTransition(final Activity activity) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        FrameLayout phantomView = new FrameLayout(activity.getApplicationContext());
        for (TransitionData data : transitionData) {
            View view = activity.findViewById(data.getId());
            if (view != null) {
                if (view instanceof ImageView) {
                    ImageView imageView = new ImageView(activity);
                    imageView.setImageDrawable(((ImageView) view).getDrawable());
                    setParamsForView(imageView, phantomView, data);
                } else if (view instanceof TextView) {
                    TextView textView = new TextView(activity);
                    textView.setText(((TextView) view).getText());
                    textView.setTextColor(((TextView) view).getTextColors());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((TextView) view).getTextSize());
                    setParamsForView(textView, phantomView, data);
                } else {
                    View temp = new View(activity);
                    setParamsForView(temp, phantomView, data);
                    if (Build.VERSION.SDK_INT >= 16) {
                        temp.setBackground(view.getBackground());
                    } else {
                        temp.setBackgroundDrawable(view.getBackground());
                    }
                }
            }
        }
        Scene exitScene = Scene.getSceneForView(content, phantomView);
        changeScene(exitScene, transition);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }, transition.getDuration());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void setParamsForView(View view, ViewGroup viewGroup, TransitionData data) {
        view.setId(data.getId());
        view.setLeft(data.getLeft());
        view.setTop(data.getTop());
        view.setRight(data.getLeft() + data.getWidth());
        view.setBottom(data.getTop() + data.getHeight());
        viewGroup.addView(view);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = data.getWidth();
        layoutParams.height = data.getHeight();
        //for debug
        layoutParams.setMargins(data.getLeft(), data.getTop(), 0, 0);
    }
}
