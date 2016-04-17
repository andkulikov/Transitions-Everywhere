package android.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Dummy class. Permits to extend same hidden class from android framework.
 * Actually in runtime will be used class from android framework and ObjectAnimator
 * optimizations for FloatProperty will be applied.
 *
 * Created by Andrey Kulikov on 18.08.15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class FloatProperty<T> extends Property<T, Float> {

    public FloatProperty(String name) {
        super(Float.class, name);
    }

    public abstract void setValue(T object, float value);

    @SuppressLint("NewApi")
    @Override
    final public void set(T object, Float value) {
        setValue(object, value);
    }

}
