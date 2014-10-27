package android.transitions.everywhere.utils;

/**
 * Created by Andrey Kulikov on 24.10.14.
 */
public class PropertyCompatObject<T, F> {

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
