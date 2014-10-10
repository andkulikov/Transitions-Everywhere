package android.support.view;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build.VERSION_CODES;
import android.view.View;

@TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
public class ViewCompatJellybeanMr2 {
    public static void setClipBounds(View v, Rect clipBounds) {
        v.setClipBounds(clipBounds);
    }
}
