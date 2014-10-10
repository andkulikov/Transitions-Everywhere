package android.support.view;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.support.util.CompatUtils;
import android.view.View;

import java.lang.reflect.Method;

@TargetApi(VERSION_CODES.KITKAT)
public class ViewCompatKitKat {
    private static final Method METHOD_getTransitionAlpha = CompatUtils.getMethod(View.class, "getTransitionAlpha");
    private static final Method METHOD_setTransitionAlpha = CompatUtils.getMethod(View.class, "setTransitionAlpha",
            float.class);

    public static float getTransitionAlpha(View v) {
        return (Float) CompatUtils.invoke(v, 1, METHOD_getTransitionAlpha);
    }

    public static boolean isLaidOut(View v) {
        return v.isLaidOut();
    }

    public static void setTransitionAlpha(View v, float alpha) {
        CompatUtils.invoke(v, null, METHOD_setTransitionAlpha, alpha);
    }
}
