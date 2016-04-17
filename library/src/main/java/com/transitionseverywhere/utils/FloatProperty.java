package com.transitionseverywhere.utils;

import android.annotation.SuppressLint;

/**
 * It's great idea to extend it for any properties that works with float fields
 * because ObjectAnimator have optimizations for FloatProperty to avoid autoboxing.
 *
 * Created by Andrey Kulikov on 17/04/16.
 */
@SuppressLint("NewApi, Override")
public abstract class FloatProperty<T> extends android.util.FloatProperty<T> {

    public FloatProperty() {
        super(null);
        // null instead of name here because it's used only for calling setter
        // and getter via reflection. but we have our own overridden set and get.
    }

    /**
     * Just default realisation. Some of properties can have no getter. Override for real getter
     */
    @Override
    public Float get(T object) {
        return null;
    }

}
