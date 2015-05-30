package android.transitions.everywhere.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorPauseListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeConverter;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.transitions.everywhere.Transition;
import android.transitions.everywhere.TransitionUtils;
import android.util.Property;
import android.view.View;

public class AnimatorUtils {

    interface AnimatorCompatImpl {
        void addPauseListener(Animator animator, AnimatorPauseListener listener);

        void pause(Animator animator);

        void resume(Animator animator);

        ObjectAnimator ofInt(Transition transition, Object target,
                             String xPropertyName, String yPropertyName,
                             int startLeft, int startTop, int endLeft, int endTop);

        ObjectAnimator ofFloat(Transition transition, Object target,
                               String xPropertyName, String yPropertyName,
                               float startLeft, float startTop, float endLeft, float endTop);

        Animator ofInt(Transition transition, Object target, Property propertyX, Property propertyY,
                       int startX, int startY, int endX, int endY);

        <V> PropertyValuesHolder pvhOfObject(Property<?, V> property,
                                             TypeConverter<PointF, V> converter, Path path);

        boolean isAnimatorStarted(Animator anim);

        boolean hasOverlappingRendering(View view);
    }

    static class BaseAnimatorCompatImpl implements AnimatorCompatImpl {
        @Override
        public void addPauseListener(Animator animator, AnimatorPauseListener listener) {
        }

        @Override
        public void pause(Animator animator) {
        }

        @Override
        public void resume(Animator animator) {
        }

        @Override
        public ObjectAnimator ofInt(Transition transition, Object target, String xPropertyName,
                                    String yPropertyName, int startLeft, int startTop,
                                    int endLeft, int endTop) {
            return null;
        }

        @Override
        public ObjectAnimator ofFloat(Transition transition, Object target, String xPropertyName,
                                      String yPropertyName, float startLeft, float startTop,
                                      float endLeft, float endTop) {
            return null;
        }

        @Override
        public Animator ofInt(Transition transition, Object target, Property propertyX,
                              Property propertyY, int startX, int startY, int endX, int endY) {
            return null;
        }

        @Override
        public <V> PropertyValuesHolder pvhOfObject(Property<?, V> property,
                                                    TypeConverter<PointF, V> converter, Path path) {
            return null;
        }

        @Override
        public boolean isAnimatorStarted(Animator anim) {
            return false;
        }

        @Override
        public boolean hasOverlappingRendering(View view) {
            return false;
        }
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    static class HoneyCombAnimatorCompatImpl extends BaseAnimatorCompatImpl {
        @Override
        public void pause(Animator animator) {
            animator.cancel();
        }

        @Override
        public ObjectAnimator ofInt(Transition transition, Object target, String xPropertyName,
                                    String yPropertyName, int startLeft, int startTop,
                                    int endLeft, int endTop) {
            int pvhNumber = 0;
            if (startLeft != endLeft) pvhNumber++;
            if (startTop != endTop) pvhNumber++;
            int pvhIndex = 0;
            PropertyValuesHolder pvh[] = new PropertyValuesHolder[pvhNumber];
            if (startLeft != endLeft) {
                pvh[pvhIndex++] = PropertyValuesHolder.ofInt(xPropertyName, startLeft, endLeft);
            }
            if (startTop != endTop) {
                pvh[pvhIndex] = PropertyValuesHolder.ofInt(yPropertyName, startTop, endTop);
            }
            return ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        }

        @Override
        public ObjectAnimator ofFloat(Transition transition, Object target, String xPropertyName,
                                      String yPropertyName, float startLeft, float startTop,
                                      float endLeft, float endTop) {
            int pvhNumber = 0;
            if (startLeft != endLeft) pvhNumber++;
            if (startTop != endTop) pvhNumber++;
            int pvhIndex = 0;
            PropertyValuesHolder pvh[] = new PropertyValuesHolder[pvhNumber];
            if (startLeft != endLeft) {
                pvh[pvhIndex++] = PropertyValuesHolder.ofFloat(xPropertyName, startLeft, endLeft);
            }
            if (startTop != endTop) {
                pvh[pvhIndex] = PropertyValuesHolder.ofFloat(yPropertyName, startTop, endTop);
            }
            return ObjectAnimator.ofPropertyValuesHolder(target, pvh);
        }
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    static class IceCreamSandwichAnimatorCompatImpl extends HoneyCombAnimatorCompatImpl {
        @Override
        public Animator ofInt(Transition transition, Object target, Property propertyX,
                              Property propertyY, int startX, int startY, int endX, int endY) {
            Animator animX = startX == endX ? null :
                    ObjectAnimator.ofInt(target, propertyX, startX, endX);
            Animator animY = startY == endY ? null :
                    ObjectAnimator.ofInt(target, propertyY, startY, endY);
            return TransitionUtils.mergeAnimators(animX, animY);
        }

        @Override
        public boolean isAnimatorStarted(Animator anim) {
            return anim.isStarted();
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    static class JellyBeanCompatImpl extends IceCreamSandwichAnimatorCompatImpl {
        @Override
        public boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatAnimatorCompatImpl extends JellyBeanCompatImpl {
        @Override
        public void addPauseListener(Animator animator, final AnimatorPauseListener listener) {
            animator.addPauseListener(listener);
        }

        @Override
        public void pause(Animator animator) {
            animator.pause();
        }

        @Override
        public void resume(Animator animator) {
            animator.resume();
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    static class LollipopAnimatorCompatImpl extends KitKatAnimatorCompatImpl {

        @Override
        public ObjectAnimator ofInt(Transition transition, Object target, String xPropertyName,
                                    String yPropertyName, int startLeft, int startTop,
                                    int endLeft, int endTop) {
            return ObjectAnimator.ofInt(target, xPropertyName, yPropertyName,
                    transition.getPathMotion().getPath(startLeft, startTop, endLeft, endTop));
        }

        @Override
        public ObjectAnimator ofFloat(Transition transition, Object target, String xPropertyName,
                                      String yPropertyName, float startLeft, float startTop,
                                      float endLeft, float endTop) {
            Path path;
            if (transition == null) {
                path = new Path();
                path.moveTo(startLeft, startTop);
                path.lineTo(endLeft, endTop);
            } else {
                path = transition.getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
            }
            return ObjectAnimator.ofFloat(target, xPropertyName, yPropertyName, path);
        }

        @Override
        public Animator ofInt(Transition transition, Object target, Property propertyX,
                              Property propertyY, int startX, int startY, int endX, int endY) {
            Path positionPath = transition.getPathMotion().getPath(startX, startY, endX, endY);
            return ObjectAnimator.ofInt(target, propertyX, propertyY, positionPath);
        }

        @Override
        public <V> PropertyValuesHolder pvhOfObject(Property<?, V> property, TypeConverter<PointF, V> converter, Path path) {
            return PropertyValuesHolder.ofObject(property, converter, path);
        }

    }

    private static final AnimatorCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= VERSION_CODES.LOLLIPOP) {
            IMPL = new LollipopAnimatorCompatImpl();
        } else if (version >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatAnimatorCompatImpl();
        } else if (version >= VERSION_CODES.JELLY_BEAN) {
            IMPL = new JellyBeanCompatImpl();
        } else if (version >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            IMPL = new IceCreamSandwichAnimatorCompatImpl();
        } else if (version >= VERSION_CODES.HONEYCOMB) {
            IMPL = new HoneyCombAnimatorCompatImpl();
        } else {
            IMPL = new BaseAnimatorCompatImpl();
        }
    }

    public static void addPauseListener(Animator animator, AnimatorPauseListener listener) {
        IMPL.addPauseListener(animator, listener);
    }

    public static void pause(Animator animator) {
        IMPL.pause(animator);
    }

    public static void resume(Animator animator) {
        IMPL.resume(animator);
    }

    public static ObjectAnimator ofInt(Transition transition, Object target,
                                       String xPropertyName, String yPropertyName,
                                       int startLeft, int startTop, int endLeft, int endTop) {
        if (startLeft != endLeft || startTop != endTop) {
            return IMPL.ofInt(transition, target, xPropertyName, yPropertyName, startLeft,
                    startTop, endLeft, endTop);
        } else {
            return null;
        }
    }

    public static ObjectAnimator ofFloat(Transition transition, Object target, String xPropertyName,
                                         String yPropertyName, float startLeft, float startTop,
                                         float endLeft, float endTop) {
        if (startLeft != endLeft || startTop != endTop) {
            return IMPL.ofFloat(transition, target, xPropertyName, yPropertyName, startLeft,
                    startTop, endLeft, endTop);
        } else {
            return null;
        }
    }

    public static Animator ofInt(Transition transition, Object target, Property propertyX,
                                 Property propertyY, int startX, int startY, int endX, int endY) {
        if (startX != endX || startY != endY) {
            return IMPL.ofInt(transition, target, propertyX, propertyY, startX, startY, endX, endY);
        } else {
            return null;
        }
    }

    public static <V> PropertyValuesHolder pvhOfObject(Property<?, V> property,
                                                       TypeConverter<PointF, V> converter, Path path) {
        return IMPL.pvhOfObject(property, converter, path);
    }

    public static boolean isAnimatorStarted(Animator anim) {
        return IMPL.isAnimatorStarted(anim);
    }

    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator ofObject(PropertyCompatObject objectCompat,
                                          TypeEvaluator evaluator, Object... values) {
        return ObjectAnimator.ofObject(objectCompat, objectCompat.getProperty(), evaluator, values);
    }
}
