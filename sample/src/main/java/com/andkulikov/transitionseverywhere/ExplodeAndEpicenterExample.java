package com.andkulikov.transitionseverywhere;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/**
 * Created by Andrey Kulikov on 25/03/16.
 */
public class ExplodeAndEpicenterExample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_explode, container, false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View clickedView) {
                    TransitionSet set = new TransitionSet()
                        .addTransition(new Explode().setEpicenterCallback(new Transition.EpicenterCallback() {
                            @Override
                            public Rect onGetEpicenter(Transition transition) {
                                return new Rect(clickedView.getLeft(), clickedView.getTop(),
                                    clickedView.getRight(), clickedView.getBottom());
                            }
                        }).excludeTarget(clickedView, true))
                        .addTransition(new Fade().addTarget(clickedView))
                        .addListener(new Transition.TransitionListenerAdapter() {
                            @Override
                            public void onTransitionEnd(Transition transition) {
                                getActivity().onBackPressed();
                            }
                        });

                    TransitionManager.beginDelayedTransition(layout, set);

                    layout.removeAllViews();
                }
            });
        }
        return layout;
    }

}
