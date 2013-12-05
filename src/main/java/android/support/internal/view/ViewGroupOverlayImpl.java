package android.support.internal.view;

import android.graphics.drawable.Drawable;
import android.support.view.ViewGroupOverlay;
import android.view.View;
import android.view.ViewGroup;

public class ViewGroupOverlayImpl extends ViewGroupOverlay {
	private ViewOverlayImpl mViewOverlayImpl;

	public ViewGroupOverlayImpl(ViewGroup group) {
		mViewOverlayImpl = new ViewOverlayImpl(group.getContext(), group);
	}

	@Override
	public void add(View view) {

	}

	@Override
	public void remove(View view) {

	}

	@Override
	public void add(Drawable drawable) {
		mViewOverlayImpl.add(drawable);
	}

	@Override
	public void clear() {
		mViewOverlayImpl.clear();
	}

	@Override
	public void remove(Drawable drawable) {
		mViewOverlayImpl.remove(drawable);
	}
}
