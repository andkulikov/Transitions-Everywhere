package android.support.compat;

import android.animation.Animator;
import android.animation.Animator.AnimatorPauseListener;
import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;

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

    @TargetApi(VERSION_CODES.KITKAT)
    static class KitKatAnimatorCompatImpl extends BaseAnimatorCompatImpl {
        @Override
        public void addPauseListener(Animator animator, final AnimatorPauseListener listener) {
            animator.addPauseListener(new Animator.AnimatorPauseListener() {
                @Override
                public void onAnimationPause(Animator animation) {
                    listener.onAnimationPause(animation);
                }

                @Override
                public void onAnimationResume(Animator animation) {
                    listener.onAnimationResume(animation);
                }
            });
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

    static final AnimatorCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 19) {
            IMPL = new KitKatAnimatorCompatImpl();
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
}
