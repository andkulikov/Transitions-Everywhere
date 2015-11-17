package com.transitionseverywhere.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ActivityTransitionOptions {
    public static final String ARRAY_KEY = "ARRAY_KEY";

    public static Bundle fromView(View fromView) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARRAY_KEY, getViewsWithId(fromView));
        return bundle;
    }

    public static ArrayList<TransitionData> fromTransitionBundle(Bundle bundle) {
        return bundle.getParcelableArrayList(ARRAY_KEY);
    }

    public static ArrayList<TransitionData> getViewsWithId(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return null;
        }
        ArrayList<TransitionData> viewDatas = new ArrayList<>();
        int groupTransitionName = view.getId();
        if (groupTransitionName > 0) {
            int[] screenLocation = new int[2];
            view.getLocationOnScreen(screenLocation);
            int statusBarHeight = getStatusBarHeight(view.getResources());
            final TransitionData viewData = new TransitionData(view.getId(),
                    screenLocation[0],
                    screenLocation[1] - statusBarHeight, view.getWidth(), view.getHeight());
            viewDatas.add(viewData);
        }
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    View child = ((ViewGroup) view).getChildAt(i);
                    ArrayList<TransitionData> childDatas = getViewsWithId(child);
                    if (childDatas != null) {
                        for (TransitionData data : childDatas) {
                            viewDatas.add(data);
                        }
                    }
                }

            }
        }
        return viewDatas;
    }

    private static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
