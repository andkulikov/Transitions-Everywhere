package android.support.compat;

import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(VERSION_CODES.HONEYCOMB)
public class ViewCompat {
    interface ViewCompatImpl {
        float getTransitionAlpha(View v);

        boolean isLaidOut(View v);

        void setClipBounds(View v, Rect clipBounds);

        Rect getClipBounds(View v);

        String getTransitionName(View v);

        void setTransitionAlpha(View v, float alpha);

        String getAlphaProperty();

        void setTranslationZ(View view, float z);

        float getTranslationZ(View view);

        View addGhostView(View view, ViewGroup viewGroup, Matrix matrix);

        void removeGhostView(View view);

        void transformMatrixToGlobal(View view, Matrix matrix);

        void transformMatrixToLocal(View view, Matrix matrix);

        void setAnimationMatrix(View view, Matrix matrix);

        Object getWindowId(View view);
    }

    static class BaseViewCompatImpl implements ViewCompatImpl {
        @Override
        public float getTransitionAlpha(View v) {
            return v.getAlpha();
        }

        @Override
        public boolean isLaidOut(View v) {
            // TODO: Implement support behavior
            return true;
        }

        @Override
        public void setClipBounds(View v, Rect clipBounds) {
            // TODO: Implement support behavior
        }

        @Override
        public Rect getClipBounds(View v) {
            // TODO: Implement support behavior
            return null;
        }

        @Override
        public String getTransitionName(View v) {
            return null;
        }

        @Override
        public void setTransitionAlpha(View v, float alpha) {
            v.setAlpha(alpha);
        }

        @Override
        public String getAlphaProperty() {
            return "alpha";
        }

        @Override
        public void setTranslationZ(View view, float z) {
            // do nothing
        }

        @Override
        public float getTranslationZ(View view) {
            return 0;
        }

        @Override
        public View addGhostView(View view, ViewGroup viewGroup, Matrix matrix) {
            return null;
        }

        @Override
        public void removeGhostView(View view) {
            // do nothing
        }

        @Override
        public void transformMatrixToGlobal(View view, Matrix matrix) {
            // TODO: Implement support behavior
        }

        @Override
        public void transformMatrixToLocal(View v, Matrix matrix) {
            // TODO: Implement support behavior
        }

        @Override
        public void setAnimationMatrix(View view, Matrix matrix) {
            // TODO: Implement support behavior
        }

        @Override
        public Object getWindowId(View view) {
            return null;
        }

    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
    static class ViewCompatJellyBeanMR2 extends BaseViewCompatImpl {
        @Override
        public void setClipBounds(View v, Rect clipBounds) {
            v.setClipBounds(clipBounds);
        }

        @Override
        public Rect getClipBounds(View v) {
            return v.getClipBounds();
        }

        @Override
        public Object getWindowId(View view) {
            return view.getWindowId();
        }
    }

    private static final ViewCompatImpl IMPL;

    static {
        final int version = VERSION.SDK_INT;
        if (version >= VERSION_CODES.LOLLIPOP) {
            IMPL = new ViewCompatLolipop();
        } else if (version >= VERSION_CODES.KITKAT) {
            IMPL = new ViewCompatKitKat();
        } else if (version >= VERSION_CODES.JELLY_BEAN_MR2) {
            IMPL = new ViewCompatJellyBeanMR2();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static float getTransitionAlpha(View v) {
        return IMPL.getTransitionAlpha(v);
    }

    public static boolean isLaidOut(View v) {
        return IMPL.isLaidOut(v);
    }

    public static void setClipBounds(View v, Rect clipBounds) {
        IMPL.setClipBounds(v, clipBounds);
    }

    public static Rect getClipBounds(View v) {
        return IMPL.getClipBounds(v);
    }

    public static void setTransitionAlpha(View v, float alpha) {
        IMPL.setTransitionAlpha(v, alpha);
    }

    public static String getAlphaProperty() {
        return IMPL.getAlphaProperty();
    }

    public static String getTransitionName(View v) {
        return IMPL.getTransitionName(v);
    }

    public static float getTranslationZ(View view) {
        return IMPL.getTranslationZ(view);
    }

    public static void setTranslationZ(View view, float z) {
        IMPL.setTranslationZ(view, z);
    }

    public static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    public static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

    public static void setAnimationMatrix(View view, Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    public static View addGhostView(View view, ViewGroup viewGroup, Matrix matrix) {
        return IMPL.addGhostView(view, viewGroup, matrix);
    }

    public static void removeGhostView(View view) {
        IMPL.removeGhostView(view);
    }

    public static Object getWindowId(View view) {
        return IMPL.getWindowId(view);
    }
}
