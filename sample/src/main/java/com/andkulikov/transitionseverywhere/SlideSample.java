package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Andrey Kulikov on 20/03/16.
 */
public class SlideSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final TextView text = (TextView) transitionsContainer.findViewById(R.id.text);

        transitionsContainer.findViewById(R.id.button).setOnClickListener(new VisibleToggleClickListener() {
            @Override
            protected void changeVisibility(boolean visible) {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                text.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });

        return view;
    }

}
