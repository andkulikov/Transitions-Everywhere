package android.support.view;

import android.graphics.drawable.Drawable;

public abstract class ViewOverlay {
	public abstract void add(Drawable drawable);

	public abstract void clear();

	public abstract void remove(Drawable drawable);
}
