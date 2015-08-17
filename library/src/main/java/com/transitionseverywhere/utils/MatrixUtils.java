package com.transitionseverywhere.utils;

import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by Andrey Kulikov on 19.10.14.
 */
public class MatrixUtils {

    public static Matrix IDENTITY_MATRIX = new Matrix() {
        void oops() {
            throw new IllegalStateException("Matrix can not be modified");
        }

        @Override
        public void set(Matrix src) {
            oops();
        }

        @Override
        public void reset() {
            oops();
        }

        @Override
        public void setTranslate(float dx, float dy) {
            oops();
        }

        @Override
        public void setScale(float sx, float sy, float px, float py) {
            oops();
        }

        @Override
        public void setScale(float sx, float sy) {
            oops();
        }

        @Override
        public void setRotate(float degrees, float px, float py) {
            oops();
        }

        @Override
        public void setRotate(float degrees) {
            oops();
        }

        @Override
        public void setSinCos(float sinValue, float cosValue, float px, float py) {
            oops();
        }

        @Override
        public void setSinCos(float sinValue, float cosValue) {
            oops();
        }

        @Override
        public void setSkew(float kx, float ky, float px, float py) {
            oops();
        }

        @Override
        public void setSkew(float kx, float ky) {
            oops();
        }

        @Override
        public boolean setConcat(Matrix a, Matrix b) {
            oops();
            return false;
        }

        @Override
        public boolean preTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @Override
        public boolean preScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preScale(float sx, float sy) {
            oops();
            return false;
        }

        @Override
        public boolean preRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preRotate(float degrees) {
            oops();
            return false;
        }

        @Override
        public boolean preSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preSkew(float kx, float ky) {
            oops();
            return false;
        }

        @Override
        public boolean preConcat(Matrix other) {
            oops();
            return false;
        }

        @Override
        public boolean postTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @Override
        public boolean postScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postScale(float sx, float sy) {
            oops();
            return false;
        }

        @Override
        public boolean postRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postRotate(float degrees) {
            oops();
            return false;
        }

        @Override
        public boolean postSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postSkew(float kx, float ky) {
            oops();
            return false;
        }

        @Override
        public boolean postConcat(Matrix other) {
            oops();
            return false;
        }

        @Override
        public boolean setRectToRect(RectF src, RectF dst, Matrix.ScaleToFit stf) {
            oops();
            return false;
        }

        @Override
        public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex,
                                     int pointCount) {
            oops();
            return false;
        }

        @Override
        public void setValues(float[] values) {
            oops();
        }
    };

    private static final Field FIELD_DRAW_MATRIX = ReflectionUtils.getPrivateField(ImageView.class, "mDrawMatrix");

    public static void animateTransform(ImageView imageView, Matrix matrix) {
        Drawable drawable = imageView.getDrawable();
        if (matrix == null || drawable.getIntrinsicWidth() == -1
                || drawable.getIntrinsicHeight() == -1) {
            drawable.setBounds(0, 0, imageView.getWidth(), imageView.getHeight());
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            Matrix drawMatrix = imageView.getImageMatrix();
            if (drawMatrix.isIdentity()) {
                drawMatrix = new Matrix();
                ReflectionUtils.setFieldValue(imageView, FIELD_DRAW_MATRIX, drawMatrix);
            }
            drawMatrix.set(matrix);
        }
        imageView.invalidate();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static class MatrixEvaluator implements TypeEvaluator<Matrix> {

        float[] mTempStartValues = new float[9];

        float[] mTempEndValues = new float[9];

        Matrix mTempMatrix = new Matrix();

        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            startValue.getValues(mTempStartValues);
            endValue.getValues(mTempEndValues);
            for (int i = 0; i < 9; i++) {
                float diff = mTempEndValues[i] - mTempStartValues[i];
                mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
            }
            mTempMatrix.setValues(mTempEndValues);
            return mTempMatrix;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static class NullMatrixEvaluator implements TypeEvaluator<Matrix> {
        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            return null;
        }
    }

}
