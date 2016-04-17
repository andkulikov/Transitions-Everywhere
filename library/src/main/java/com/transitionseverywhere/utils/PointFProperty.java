package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;

/**
 * Created by Andrey Kulikov on 15.08.15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class PointFProperty<T> extends Property<T, PointF> {

    public PointFProperty() {
        super(PointF.class, null);
        // null instead of name here because it's used only for calling setter
        // and getter via reflection. but we have our own overridden set and get.
    }

    /**
     * Just default realisation. Some of properties can have no getter. Override for real getter
     */
    @Override
    public PointF get(T object) {
        return null;
    }

}
