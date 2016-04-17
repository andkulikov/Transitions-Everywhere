package com.transitionseverywhere.utils;

import android.animation.LayoutTransition;
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

import com.transitionseverywhere.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey Kulikov on 15.11.14.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ViewOverlayPreJellybean extends FrameLayout {

    private static final Field FIELD_VIEW_PARENT = ReflectionUtils.getPrivateField(View.class, "mParent");

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

    @Override
    public void addView(View child, int left, int top) {
        if (child.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) child.getParent();
            LayoutTransition layoutTransition = null;
            if (parent.getLayoutTransition() != null) {
                layoutTransition = parent.getLayoutTransition();
                parent.setLayoutTransition(null);
            }
            parent.removeView(child);
            if (layoutTransition != null) {
                parent.setLayoutTransition(layoutTransition);
            }

            if (child.getParent() != null) {
                // LayoutTransition will cause the child to delay removal - cancel it
                ViewGroupUtils.cancelLayoutTransition(parent);
                // fail-safe if view is still attached for any reason
                if (child.getParent() != null && FIELD_VIEW_PARENT != null) {
                    ReflectionUtils.setFieldValue(child, FIELD_VIEW_PARENT, null);
                }
            }
            if (child.getParent() != null) {
                return;
            }
        }
        child.setTag(R.id.overlay_layout_params_backup, child.getLayoutParams());
        addView(child, initParams(child, left, top));
        invalidate();
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        ViewUtils.setLayoutParamsSilently(view, (ViewGroup.LayoutParams)
                view.getTag(R.id.overlay_layout_params_backup));
        view.setTag(R.id.overlay_layout_params_backup, null);
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
        if (drawable != null) {
            this.mDrawableOverlays.add(drawable);
            invalidate();
        }
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

    public static ViewOverlayPreJellybean getOverlay(ViewGroup sceneRoot) {
        if (sceneRoot != null) {
            ViewGroup group = sceneRoot;
            while (group.getId() != android.R.id.content && group.getParent() != null &&
                    group.getParent() instanceof ViewGroup) {
                group = (ViewGroup) group.getParent();
            }
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof ViewOverlayPreJellybean) {
                    return (ViewOverlayPreJellybean) child;
                }
            }
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.FILL;
            ViewOverlayPreJellybean viewOverlay = new ViewOverlayPreJellybean(sceneRoot.getContext());
            group.addView(viewOverlay, params);
            return viewOverlay;
        } else {
            return null;
        }
    }

}
