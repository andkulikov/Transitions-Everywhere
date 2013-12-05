package android.support.internal.view;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.support.view.ViewOverlay;
import android.view.View;

@TargetApi(VERSION_CODES.KITKAT)
public class ViewOverlayWrapper extends ViewOverlay {
	private android.view.ViewOverlay mViewOverlay;

	public ViewOverlayWrapper(View view) {
		mViewOverlay = view.getOverlay();
	}

	@Override
	public void add(Drawable drawable) {
		mViewOverlay.add(drawable);
	}

	@Override
	public void clear() {
		mViewOverlay.clear();
	}

	@Override
	public void remove(Drawable drawable) {
		mViewOverlay.remove(drawable);
	}
}
