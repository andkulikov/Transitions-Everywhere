package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Andrey Kulikov on 24/03/16.
 */
public class PathMotionSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final View button = transitionsContainer.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer,
                    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
                params.gravity = params.gravity == (Gravity.LEFT | Gravity.TOP) ?
                    (Gravity.BOTTOM | Gravity.RIGHT) : (Gravity.LEFT | Gravity.TOP);
                button.setLayoutParams(params);
            }

        });

        return view;
    }


}
