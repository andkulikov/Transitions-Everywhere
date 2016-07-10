package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by Andrey Kulikov on 20.10.14.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ViewUtilsLollipop extends ViewUtils.ViewUtilsKitKat {

    private static final Class CLASS_GhostView = ReflectionUtils.getClass("android.view.GhostView");
    private static final Method METHOD_addGhost = ReflectionUtils.getMethod(CLASS_GhostView,
            "addGhost", View.class, ViewGroup.class, Matrix.class);
    private static final Method METHOD_removeGhost = ReflectionUtils.getMethod(CLASS_GhostView,
            "removeGhost", View.class);
    private static final Method METHOD_transformMatrixToGlobal =
            ReflectionUtils.getMethod(View.class, "transformMatrixToGlobal", Matrix.class);
    private static final Method METHOD_transformMatrixToLocal =
            ReflectionUtils.getMethod(View.class, "transformMatrixToLocal", Matrix.class);
    private static final Method METHOD_setAnimationMatrix =
            ReflectionUtils.getMethod(View.class, "setAnimationMatrix", Matrix.class);

    @Override
    public void transformMatrixToGlobal(View view, Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_transformMatrixToGlobal, matrix);
    }

    @Override
    public void transformMatrixToLocal(View view, Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_transformMatrixToLocal, matrix);
    }

    @Override
    public void setAnimationMatrix(View view, Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_setAnimationMatrix, matrix);
    }

    @Override
    public View addGhostView(View view, ViewGroup viewGroup, Matrix matrix) {
        return (View) ReflectionUtils.invoke(null, null, METHOD_addGhost, view, viewGroup, matrix);
    }

    @Override
    public void removeGhostView(View view) {
        ReflectionUtils.invoke(view, null, METHOD_removeGhost, view);
    }

    @Override
    public void setTransitionName(View v, String name) {
        v.setTransitionName(name);
    }

    @Override
    public String getTransitionName(View v) {
        return v.getTransitionName();
    }

    @Override
    public float getTranslationZ(View view) {
        return view.getTranslationZ();
    }

    @Override
    public void setTranslationZ(View view, float z) {
        view.setTranslationZ(z);
    }
}

