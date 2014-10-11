package android.support.compat;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.support.util.CompatUtils;
import android.view.ViewGroup;

import java.lang.reflect.Method;

@TargetApi(VERSION_CODES.KITKAT)
public class ViewGroupCompatKitKat {
    private static final Method METHOD_suppressLayout = CompatUtils.getMethod(ViewGroup.class, "suppressLayout",
            boolean.class);

    public static void suppressLayout(ViewGroup group, boolean suppress) {
        CompatUtils.invoke(group, null, METHOD_suppressLayout, suppress);
    }
}
