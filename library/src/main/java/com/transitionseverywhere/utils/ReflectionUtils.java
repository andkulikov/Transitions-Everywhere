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

package com.transitionseverywhere.utils;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private static final String TAG = ReflectionUtils.class.getSimpleName();

    private ReflectionUtils() {
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

    public static Method getPrivateMethod(final Class<?> targetClass, final String name,
                                   final Class<?>... parameterTypes) {
        if (targetClass == null || TextUtils.isEmpty(name)) return null;
        try {
            Method method = targetClass.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (SecurityException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
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

    public static Field getPrivateField(final Class<?> targetClass, final String name) {
        if (targetClass == null || TextUtils.isEmpty(name)) return null;
        try {
            Field field = targetClass.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (SecurityException e) {
            e.printStackTrace();
            // ignore
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // ignore
        }
        return null;
    }

    public static void setFieldValue(final Object receiver, final Field field, final Object value) {
        if (field == null) return;
        try {
            field.set(receiver, value);
        } catch (Exception e) {
            Log.e(TAG, "Exception in setFieldValue", e);
        }
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

    // Optimizations to avoid creating new objects array in every call of method.invoke(...)
    // caused by "Object... args" arguments definition

    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final Object[] ONE_OBJECT_ARRAY = new Object[1];
    private static final Object[] TWO_OBJECTS_ARRAY = new Object[2];
    private static final Object[] THREE_OBJECTS_ARRAY = new Object[3];
    private static final Object[] FOUR_OBJECTS_ARRAY = new Object[4];

    public static Object invoke(Object receiver, Object defaultValue, Method method) {
        return invoke(receiver, defaultValue, method, EMPTY_ARRAY);
    }

    public static Object invoke(Object receiver, Object defaultValue, Method method, Object firstArg) {
        ONE_OBJECT_ARRAY[0] = firstArg;
        Object result = invoke(receiver, defaultValue, method, ONE_OBJECT_ARRAY);
        ONE_OBJECT_ARRAY[0] = null;
        return result;
    }

    public static Object invoke(Object receiver, Object defaultValue, Method method,
                                Object firstArg, Object secondArg) {
        TWO_OBJECTS_ARRAY[0] = firstArg;
        TWO_OBJECTS_ARRAY[1] = secondArg;
        Object result = invoke(receiver, defaultValue, method, TWO_OBJECTS_ARRAY);
        TWO_OBJECTS_ARRAY[0] = null;
        TWO_OBJECTS_ARRAY[1] = null;
        return result;
    }


    public static Object invoke(Object receiver, Object defaultValue, Method method,
                                Object firstArg, Object secondArg, Object thirdArg) {
        THREE_OBJECTS_ARRAY[0] = firstArg;
        THREE_OBJECTS_ARRAY[1] = secondArg;
        THREE_OBJECTS_ARRAY[2] = thirdArg;
        Object result = invoke(receiver, defaultValue, method, THREE_OBJECTS_ARRAY);
        THREE_OBJECTS_ARRAY[0] = null;
        THREE_OBJECTS_ARRAY[1] = null;
        THREE_OBJECTS_ARRAY[2] = null;
        return result;
    }

    public static Object invoke(Object receiver, Object defaultValue, Method method,
                                Object firstArg, Object secondArg,
                                Object thirdArg, Object fourthArg) {
        FOUR_OBJECTS_ARRAY[0] = firstArg;
        FOUR_OBJECTS_ARRAY[1] = secondArg;
        FOUR_OBJECTS_ARRAY[2] = thirdArg;
        FOUR_OBJECTS_ARRAY[3] = fourthArg;
        Object result = invoke(receiver, defaultValue, method, FOUR_OBJECTS_ARRAY);
        FOUR_OBJECTS_ARRAY[0] = null;
        FOUR_OBJECTS_ARRAY[1] = null;
        FOUR_OBJECTS_ARRAY[2] = null;
        FOUR_OBJECTS_ARRAY[3] = null;
        return result;
    }

}
