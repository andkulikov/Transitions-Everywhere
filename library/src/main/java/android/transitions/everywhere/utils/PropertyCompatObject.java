package android.transitions.everywhere.utils;

import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Andrey Kulikov on 24.10.14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PropertyCompatObject<T, F> extends AnimatorListenerAdapter {

    private T mObject;

    public PropertyCompatObject() {
    }

    public PropertyCompatObject(T object) {
        mObject = object;
    }

    public T getObject() {
        return mObject;
    }

    public String getProperty() {
        return "value";
    }

    public void setValue(F value) {
        // do nothing
    }

    public F getValue() {
        return null;
    }

}
