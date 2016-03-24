package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

/**
 * Created by Andrey Kulikov on 20/03/16.
 */
public class AutoTransitionSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autotransition, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final TextView text = (TextView) transitionsContainer.findViewById(R.id.text);

        transitionsContainer.findViewById(R.id.button).setOnClickListener(new VisibleToggleClickListener() {

            @Override
            protected void changeVisibility(boolean visible) {
                TransitionManager.beginDelayedTransition(transitionsContainer);
                // it is the same as
                // TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
                // where AutoTransition is the set of Fade and ChangeBounds transitions
                text.setVisibility(visible ? View.VISIBLE : View.GONE);
            }

        });

        return view;
    }

}
