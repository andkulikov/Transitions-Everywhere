package android.support.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephane on 11/10/13.
 */
public class ViewOverlayCompat extends View {

    public ViewOverlayCompat(Context context) {
        super(context);
        init();
    }

    public ViewOverlayCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewOverlayCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        viewOverlays = new ArrayList<ViewWithBounds>();
        drawableOverlays = new ArrayList<Drawable>();
    }

    private List<ViewWithBounds> viewOverlays;

    public void addView(View view, int left, int top) {
        this.viewOverlays.add(new ViewWithBounds(view, left, top));
        invalidate();
    }

    public synchronized void removeView(View v) {
        int i = -1;
        for (ViewWithBounds viewWithBounds : viewOverlays) {
            i++;
            if (viewWithBounds.getView().equals(v))
                break;
        }
        if (i > -1 && i < viewOverlays.size()) {
            viewOverlays.remove(i);
            invalidate();
        }
    }

    private List<Drawable> drawableOverlays;

    public void addDrawable(Drawable drawable) {
        this.drawableOverlays.add(drawable);
        invalidate();
    }

    public synchronized void removeDrawable(Drawable drawable) {
        drawableOverlays.remove(drawable);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int parentLeft = ((View) getParent()).getLeft();
        int top = ((View) getParent()).getTop();

        for (ViewWithBounds viewWithBounds : viewOverlays) {
            Bitmap bitmap = viewWithBounds.getBitmap();
            if (bitmap != null) {
                float alpha1 = viewWithBounds.view.getAlpha();
                int alpha = (int) (alpha1 * 255);
                mPaint.setAlpha(alpha);
                canvas.drawBitmap(bitmap, viewWithBounds.left - parentLeft, viewWithBounds.top - top, mPaint);
            }
        }

        for (Drawable drawable : drawableOverlays) {
            drawable.draw(canvas);
        }


        ViewCompat.postInvalidateOnAnimation(this);

    }

    private class ViewWithBounds {
        private View view;
        private Bitmap bitmap;
        public int left;
        public int top;

        private ViewWithBounds(View view, int left, int top) {
            this.view = view;
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            this.bitmap = bitmap;
            this.left = left;
            this.top = top;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public View getView() {
            return view;
        }
    }
}
