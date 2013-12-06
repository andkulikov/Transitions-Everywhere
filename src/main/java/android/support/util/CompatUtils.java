/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.util;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class CompatUtils {
    private static final String TAG = CompatUtils.class.getSimpleName();

    private CompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static Class<?> getClass(final String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Method getMethod(final Class<?> targetClass, final String name,
            final Class<?>... parameterTypes) {
        if (targetClass == null || TextUtils.isEmpty(name)) return null;
        try {
            return targetClass.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return null;
    }

    public static Field getField(final Class<?> targetClass, final String name) {
        if (targetClass == null || TextUtils.isEmpty(name)) return null;
        try {
            return targetClass.getField(name);
        } catch (SecurityException e) {
            // ignore
        } catch (NoSuchFieldException e) {
            // ignore
        }
        return null;
    }

    public static Constructor<?> getConstructor(final Class<?> targetClass,
            final Class<?> ... types) {
        if (targetClass == null || types == null) return null;
        try {
            return targetClass.getConstructor(types);
        } catch (SecurityException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return null;
    }

    public static Object newInstance(final Constructor<?> constructor, final Object ... args) {
        if (constructor == null) return null;
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            Log.e(TAG, "Exception in newInstance", e);
        }
        return null;
    }

    public static Object invoke(final Object receiver, final Object defaultValue,
            final Method method, final Object... args) {
        if (method == null) return defaultValue;
        try {
            return method.invoke(receiver, args);
        } catch (Exception e) {
            Log.e(TAG, "Exception in invoke", e);
        }
        return defaultValue;
    }

    public static Object getFieldValue(final Object receiver, final Object defaultValue,
            final Field field) {
        if (field == null) return defaultValue;
        try {
            return field.get(receiver);
        } catch (Exception e) {
            Log.e(TAG, "Exception in getFieldValue", e);
        }
        return defaultValue;
    }

    public static void setFieldValue(final Object receiver, final Field field, final Object value) {
        if (field == null) return;
        try {
            field.set(receiver, value);
        } catch (Exception e) {
            Log.e(TAG, "Exception in setFieldValue", e);
        }
    }
}
