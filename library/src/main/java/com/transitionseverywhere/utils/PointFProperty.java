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

    public PointFProperty(String name) {
        super(PointF.class, name);
    }
}
