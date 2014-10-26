package android.support.compat;

import android.animation.Animator;
import android.animation.Animator.AnimatorPauseListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.transition.Transition;
import android.support.transition.TransitionUtils;
import android.support.util.PropertyCompatObject;
import android.util.Property;

public class AnimatorCompat {
    interface AnimatorCompatImpl {
        void addPauseListener(Animator animator, AnimatorPauseListener listener);

        void pause(Animator animator);

        void resume(Animator animator);
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
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    static class HoneyCombAnimatorCompatImpl extends BaseAnimatorCompatImpl {
        @Override
        public void pause(Animator animator) {
            animator.cancel();
        }
    }

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatAnimatorCompatImpl extends BaseAnimatorCompatImpl {
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

    private static final AnimatorCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= VERSION_CODES.KITKAT) {
            IMPL = new KitKatAnimatorCompatImpl();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator ofInt(Transition transition, Object target,
                                       String xPropertyName, String yPropertyName,
                                       int startLeft, int startTop, int endLeft, int endTop) {
        if (startLeft != endLeft || startTop != endTop) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                return ObjectAnimator.ofInt(target, xPropertyName, yPropertyName,
                        transition.getPathMotion().getPath(startLeft, startTop, endLeft, endTop));
            } else {
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
        } else {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator ofFloat(Transition transition, Object target,
                                       String xPropertyName, String yPropertyName,
                                       float startLeft, float startTop, float endLeft, float endTop) {
        if (startLeft != endLeft || startTop != endTop) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                Path path;
                if (transition == null) {
                    path = new Path();
                    path.moveTo(startLeft, startTop);
                    path.lineTo(endLeft, endTop);
                } else {
                    path = transition.getPathMotion().getPath(startLeft, startTop, endLeft, endTop);
                }
                return ObjectAnimator.ofFloat(target, xPropertyName, yPropertyName, path);
            } else {
                int pvhNumber = 0;
                if (startLeft != endLeft) pvhNumber++;
                if (startTop != endTop) pvhNumber++;
                int pvhIndex = 0;
                PropertyValuesHolder pvh[] = new PropertyValuesHolder[pvhNumber];
                if (startLeft != endLeft) {
                    pvh[pvhIndex++] = PropertyValuesHolder.ofFloat(xPropertyName, startLeft, endLeft);
                }
                if (startTop != endTop) {
                    pvh[pvhIndex++] = PropertyValuesHolder.ofFloat(yPropertyName, startTop, endTop);
                }
                return ObjectAnimator.ofPropertyValuesHolder(target, pvh);
            }
        } else {
            return null;
        }
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Animator ofInt(Transition transition, Object target,
                                       Property propertyX,
                                       Property propertyY,
                                       int startX, int startY, int endX, int endY) {
        if (startX != endX || startY != endY) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                Path positionPath = transition.getPathMotion().getPath(startX, startY, endX, endY);
                return ObjectAnimator.ofInt(target, propertyX, propertyY, positionPath);
            } else {
                Animator animX = startX == endX ? null : ObjectAnimator.ofInt(target, propertyX, startX, endX);
                Animator animY = startY == endY ? null : ObjectAnimator.ofInt(target, propertyY, startY, endY);
                return TransitionUtils.mergeAnimators(animX, animY);
            }
        } else {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator ofObject(PropertyCompatObject objectCompat,
                                          TypeEvaluator evaluator, Object... values) {
        return ObjectAnimator.ofObject(objectCompat, objectCompat.getProperty(), evaluator, values);
    }
}
