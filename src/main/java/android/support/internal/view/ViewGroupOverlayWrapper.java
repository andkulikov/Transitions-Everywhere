package android.support.internal.view;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.support.view.ViewCompat;
import android.support.view.ViewGroupOverlay;
import android.support.view.ViewOverlay;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(VERSION_CODES.KITKAT)
public class ViewGroupOverlayWrapper extends ViewGroupOverlay {
	private ViewOverlay mViewOverlay;

	public ViewGroupOverlayWrapper(ViewGroup group) {
		mViewOverlay = ViewCompat.getSupportOverlay(group);
	}

	@Override
	public void add(View view) {

	}

	@Override
	public void remove(View view) {

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
