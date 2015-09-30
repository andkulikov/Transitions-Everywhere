package com.andkulikov.transitionseverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.transitionseverywhere.ActivityTransitionManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    // We transition between these Scenes
    private Scene mScene1;
    private Scene mScene2;
    private Scene mScene3;

    /**
     * A custom TransitionManager
     */
    private TransitionManager mTransitionManagerForScene3;

    /**
     * Transitions take place in this ViewGroup. We retain this for the dynamic transition on scene 4.
     */
    private ViewGroup mSceneRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_transitions);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.select_scene);
        radioGroup.setOnCheckedChangeListener(this);
        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);

        // A Scene can be instantiated from a live view hierarchy.
        mScene1 = new Scene(mSceneRoot, mSceneRoot.findViewById(R.id.container));

        // You can also inflate a generate a Scene from a layout resource file.
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene2, this);

        // Another scene from a layout resource file.
        mScene3 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene3, this);

        findViewById(R.id.open_new_activity_1).setOnClickListener(this);
        findViewById(R.id.open_new_activity_2).setOnClickListener(this);

        // We create a custom TransitionManager for Scene 3, in which ChangeBounds, Fade and
        // ChangeImageTransform take place at the same time.
        mTransitionManagerForScene3 = TransitionInflater.from(this)
                .inflateTransitionManager(R.anim.scene3_transition_manager, mSceneRoot);
    }

    @Override
    public void onCheckedChanged(final RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.select_scene_1: {
                // You can start an automatic transition with TransitionManager.go().
                TransitionManager.go(mScene1);
                break;
            }
            case R.id.select_scene_2: {
                TransitionSet set = new TransitionSet();
                Slide slide = new Slide(Gravity.LEFT);
                slide.addTarget(R.id.transition_title);
                set.addTransition(slide);
                set.addTransition(new ChangeBounds());
                set.addTransition(new ChangeImageTransform());
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set.setDuration(350);
                TransitionManager.go(mScene2, set);
                break;
            }
            case R.id.select_scene_3: {
                // You can also start a transition with a custom TransitionManager.
                mTransitionManagerForScene3.transitionTo(mScene3);
                break;
            }
            case R.id.select_scene_4: {
                // Alternatively, transition can be invoked dynamically without a Scene.
                // For this, we first call TransitionManager.beginDelayedTransition().
                TransitionManager.beginDelayedTransition(mSceneRoot, new ChangeBounds());
                // Then, we can just change view properties as usual.
                View square = mSceneRoot.findViewById(R.id.transition_square);
                ViewGroup.LayoutParams params = square.getLayoutParams();
                int newSize = getResources().getDimensionPixelSize(R.dimen.square_size_expanded);
                params.width = newSize;
                params.height = newSize;
                square.setLayoutParams(params);
                square.invalidate();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_new_activity_1:
                TransitionSet set = new TransitionSet();
                Slide slide = new Slide(Gravity.LEFT);
                slide.addTarget(R.id.transition_title);
                set.addTransition(slide);
                set.addTransition(new ChangeImageTransform());
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setReparent(true);
                set.addTransition(changeBounds);
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set.setDuration(1000);
                ActivityTransitionManager.startActivity(MainActivity.this, NewActivity1.class, set);
                break;
            case R.id.open_new_activity_2:
//                View view = findViewById(R.id.transition_image);
//                TransitionSet set = new TransitionSet();
//                Slide slide = new Slide(Gravity.LEFT);
//                slide.addTarget(R.id.transition_title);
//                set.addTransition(slide);
//                set.addTransition(new ChangeBounds());
//                set.addTransition(new ChangeImageTransform());
//                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
//                set.setDuration(350);
//                ActivityTransitionManager.startActivity(MainActivity.this, NewActivity2.class, view, set);
                break;
        }
    }
}
