package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.view.View;

import java.lang.reflect.Method;

@TargetApi(VERSION_CODES.KITKAT)
class ViewUtilsKitKat extends ViewUtils.ViewUtilsJellyBeanMR2 {

    private static final Method METHOD_getTransitionAlpha = ReflectionUtils.getMethod(View.class, "getTransitionAlpha");
    private static final Method METHOD_setTransitionAlpha = ReflectionUtils.getMethod(View.class, "setTransitionAlpha",
            float.class);

    @Override
    public float getTransitionAlpha(View v) {
        return (Float) ReflectionUtils.invoke(v, 1, METHOD_getTransitionAlpha);
    }

    @Override
    public boolean isLaidOut(View v, boolean defaultValue) {
        return v.isLaidOut();
    }

    @Override
    public void setTransitionAlpha(View v, float alpha) {
        ReflectionUtils.invoke(v, null, METHOD_setTransitionAlpha, alpha);
    }

    @Override
    public boolean isTransitionAlphaCompatMode() {
        return false;
    }

    @Override
    public String getAlphaProperty() {
        return "transitionAlpha";
    }

}
