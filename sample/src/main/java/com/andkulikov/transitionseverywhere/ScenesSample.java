package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/**
 * Created by Andrey Kulikov on 20/03/16.
 */
public class ScenesSample extends Fragment implements RadioGroup.OnCheckedChangeListener {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenes, container, false);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.select_scene);
        radioGroup.setOnCheckedChangeListener(this);
        mSceneRoot = (ViewGroup) view.findViewById(R.id.scene_root);

        // A Scene can be instantiated from a live view hierarchy.
        mScene1 = new Scene(mSceneRoot, mSceneRoot.findViewById(R.id.container));

        // You can also inflate a generate a Scene from a layout resource file.
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene2, getContext());

        // Another scene from a layout resource file.
        mScene3 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene3, getContext());

        // We create a custom TransitionManager for Scene 3, in which ChangeBounds, Fade and
        // ChangeImageTransform take place at the same time.
        mTransitionManagerForScene3 = TransitionInflater.from(getContext())
            .inflateTransitionManager(R.anim.scene3_transition_manager, mSceneRoot);

        return view;
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
        }
    }
}
