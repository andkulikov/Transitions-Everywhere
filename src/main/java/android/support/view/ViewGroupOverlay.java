package android.support.view;

import android.view.View;

public abstract class ViewGroupOverlay extends ViewOverlay {
	public abstract void add(View view);

	public abstract void remove(View view);
}
