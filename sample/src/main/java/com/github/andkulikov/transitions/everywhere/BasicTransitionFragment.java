/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.andkulikov.transitions.everywhere;

import android.os.Bundle;
import android.transitions.everywhere.ChangeBounds;
import android.transitions.everywhere.ChangeImageTransform;
import android.transitions.everywhere.Explode;
import android.transitions.everywhere.Scene;
import android.transitions.everywhere.TransitionManager;
import android.transitions.everywhere.TransitionSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class BasicTransitionFragment extends Fragment
        implements RadioGroup.OnCheckedChangeListener {

    // We transition between these Scenes
    private Scene mScene1;
    private Scene mScene2;
    private Scene mScene3;

    /** A custom TransitionManager */
    private TransitionManager mTransitionManagerForScene3;

    /** Transitions take place in this ViewGroup. We retain this for the dynamic transition on scene 4. */
    private ViewGroup mSceneRoot;

    public static BasicTransitionFragment newInstance() {
        return new BasicTransitionFragment();
    }

    public BasicTransitionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_transition, container, false);
        assert view != null;
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.select_scene);
        radioGroup.setOnCheckedChangeListener(this);
        mSceneRoot = (ViewGroup) view.findViewById(R.id.scene_root);


        // A Scene can be instantiated from a live view hierarchy.
        mScene1 = new Scene(mSceneRoot, (ViewGroup) mSceneRoot.findViewById(R.id.container));



        // You can also inflate a generate a Scene from a layout resource file.
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene2, getActivity());


        // Another scene from a layout resource file.
        mScene3 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene3, getActivity());


        // We create a custom TransitionManager for Scene 3, in which ChangeBounds and Fade
        // take place at the same time.
//        mTransitionManagerForScene3 = TransitionInflater.from(getActivity())
//                .inflateTransitionManager(R.transition.scene3_transition_manager, mSceneRoot);


        return view;
    }

    @Override
    public void onCheckedChanged(final RadioGroup group, int checkedId) {
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new Explode());
        transition.addTransition(new ChangeBounds());
        transition.addTransition(new ChangeImageTransform());
//        transition.addTransition(new Rotate());
//        transition.addTransition(new ChangeImageTransform());
        transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
        switch (checkedId) {
            case R.id.select_scene_1: {

                // You can start an automatic transition with TransitionManager.go().
                TransitionManager.go(mScene1, transition);

                break;
            }
            case R.id.select_scene_2: {
                TransitionManager.go(mScene2, transition);
                break;
            }
            case R.id.select_scene_3: {

                // You can also start a transition with a custom TransitionManager.
//                Transition transition = new Slide(Gravity.RIGHT);
////                mTransitionManagerForScene3.transitionTo(mScene3, );
//                transition.setEpicenterCallback(new Transition.EpicenterCallback() {
//                    @Override
//                    public Rect onGetEpicenter(Transition transition) {
//                        Rect rect = new Rect();
//                        rect.left = group.getLeft() / 2;
//                        rect.top = group.getTop() * 2 / 3;
//                        rect.bottom = rect.top + 50;
//                        rect.right = rect.left + 80;
//                        return rect;
//                    }
//                });
                TransitionManager.go(mScene3, transition);

                break;
            }
            case R.id.select_scene_4: {

                // Alternatively, transition can be invoked dynamically without a Scene.
                // For this, we first call TransitionManager.beginDelayedTransition().
                TransitionManager.beginDelayedTransition(mSceneRoot, transition);
                // Then, we can just change view properties as usual.
                View square = mSceneRoot.findViewById(R.id.transition_square);
                ViewGroup.LayoutParams params = square.getLayoutParams();
                int newSize = getResources().getDimensionPixelSize(R.dimen.square_size_expanded);
                params.width = newSize;
                params.height = newSize;
                square.setLayoutParams(params);
                break;
            }
        }
    }

}
