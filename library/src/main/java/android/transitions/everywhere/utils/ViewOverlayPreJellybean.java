package android.transitions.everywhere.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey Kulikov on 15.11.14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ViewOverlayPreJellybean extends FrameLayout {

    private List<Drawable> mDrawableOverlays;

    public ViewOverlayPreJellybean(Context context) {
        super(context);
        init();
    }

    public ViewOverlayPreJellybean(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewOverlayPreJellybean(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawableOverlays = new ArrayList<Drawable>();
    }

    public void addView(View view, int left, int top) {
        addView(view, initParams(view, left, top));
        invalidate();
    }

    public void moveView(View view, int left, int top) {
        if (view.getParent() == this) {
            view.setLayoutParams(initParams(view, left, top));
        }
    }

    private LayoutParams initParams(View view, int left, int top) {
        int[] loc = new int[2];
        getLocationOnScreen(loc);

        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        left -= loc[0];
        top -= loc[1];
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        view.setLeft(left);
        view.setTop(top);
        if (view.getMeasuredWidth() != 0) {
            layoutParams.width = view.getMeasuredWidth();
            view.setRight(left + layoutParams.width);
        }
        if (view.getMeasuredHeight() != 0) {
            layoutParams.height = view.getMeasuredHeight();
            view.setBottom(top + layoutParams.height);
        }
        return layoutParams;
    }

    public synchronized void addDrawable(Drawable drawable) {
        this.mDrawableOverlays.add(drawable);
        invalidate();
    }

    public synchronized void removeDrawable(Drawable drawable) {
        mDrawableOverlays.remove(drawable);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Drawable drawable : mDrawableOverlays) {
            drawable.draw(canvas);
        }
    }

    public static ViewOverlayPreJellybean getOverlay(View sceneRoot) {
        View group = sceneRoot;
        while (group != null && !(group.getId() == android.R.id.content)) {
            group = (View) group.getParent();
        }

        ViewOverlayPreJellybean viewOverlayPreJellybean = null;
        if (group != null) {
            for (int i = 0; i < ((FrameLayout) group).getChildCount(); i++) {
                View childAt = ((FrameLayout) group).getChildAt(i);
                if (childAt instanceof ViewOverlayPreJellybean) {
                    viewOverlayPreJellybean = (ViewOverlayPreJellybean) childAt;
                    break;
                }
            }

            if (viewOverlayPreJellybean == null) {
                viewOverlayPreJellybean = new ViewOverlayPreJellybean(sceneRoot.getContext());
                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.FILL;
                ((FrameLayout) group).addView(viewOverlayPreJellybean, params);
            }
        }
        return viewOverlayPreJellybean;
    }

}
