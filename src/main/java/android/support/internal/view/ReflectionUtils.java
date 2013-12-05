package android.support.internal.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

public class ReflectionUtils {
	public static <E> E safeInvokeMethod(Object target, String name) {
		return safeInvokeMethod(target, name, new Class[0], new Object[0]);
	}

	public static <E> E safeInvokeMethod(Object target, String name, Class<?>[] parameterTypes, Object[] args) {
		try {
			Method method = target.getClass().getMethod(name, parameterTypes);
			method.setAccessible(true);

			Log.v("ReflectionUtils", "Invoking method " + name);

			return (E) method.invoke(target, args);
		}
		catch (NoSuchMethodException e) {
			Log.e("ReflectionUtils", "Error invoking method", e);
		}
		catch (InvocationTargetException e) {
			Log.e("ReflectionUtils", "Error invoking method", e);
		}
		catch (IllegalAccessException e) {
			Log.e("ReflectionUtils", "Error invoking method", e);
		}

		return null;
	}
}
