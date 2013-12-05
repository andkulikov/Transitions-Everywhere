package android.support.view;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.internal.view.ReflectionUtils;
import android.support.internal.view.ViewGroupOverlayImpl;
import android.support.internal.view.ViewGroupOverlayWrapper;
import android.view.ViewGroup;

public class ViewGroupCompat extends ViewCompat {
	interface ViewGroupCompatImpl {
		ViewGroupOverlay getSupportOverlay(ViewGroup group);

		void suppressLayout(ViewGroup group, boolean suppress);
	}

	static class BaseViewGroupCompatImpl implements ViewGroupCompatImpl {
		@Override
		public ViewGroupOverlay getSupportOverlay(ViewGroup group) {
			return new ViewGroupOverlayImpl(group);
		}

		@Override
		public void suppressLayout(ViewGroup group, boolean suppress) {
		}
	}

	@TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
	static class JellyBeanMR2ViewGroupCompatImpl extends BaseViewGroupCompatImpl {
		@Override
		public ViewGroupOverlay getSupportOverlay(ViewGroup group) {
			return new ViewGroupOverlayWrapper(group);
		}
	}

	@TargetApi(VERSION_CODES.KITKAT)
	static class KitKatViewGroupCompatImpl extends BaseViewGroupCompatImpl {
		@Override
		public void suppressLayout(ViewGroup group, boolean suppress) {
			ReflectionUtils.safeInvokeMethod(group, "suppressLayout",
					new Class<?>[] { boolean.class },
					new Object[] { suppress });
		}
	}

	static final ViewGroupCompatImpl IMPL;

	static {
		final int version = VERSION.SDK_INT;
		if (version >= VERSION_CODES.KITKAT) {
			IMPL = new KitKatViewGroupCompatImpl();
		}
		else if (version >= VERSION_CODES.JELLY_BEAN_MR2) {
			IMPL = new JellyBeanMR2ViewGroupCompatImpl();
		}
		else {
			IMPL = new BaseViewGroupCompatImpl();
		}
	}

	public static ViewGroupOverlay getSupportOverlay(ViewGroup group) {
		return IMPL.getSupportOverlay(group);
	}

	public static void suppressLayout(ViewGroup group, boolean suppress) {
		IMPL.suppressLayout(group, suppress);
	}
}
