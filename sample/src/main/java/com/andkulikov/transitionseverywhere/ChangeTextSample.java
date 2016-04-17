package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.ChangeText;

/**
 * Created by Andrey Kulikov on 17/04/16.
 */
public class ChangeTextSample extends Fragment {

    public static final String TEXT_1 = "Hi, i am text. Tap on me!";
    public static final String TEXT_2 = "Thanks! Another tap?";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_text, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final TextView textView = (TextView) transitionsContainer.findViewById(R.id.text1);

        textView.setText(TEXT_1);
        textView.setOnClickListener(new View.OnClickListener() {

            boolean mSecondText;

            @Override
            public void onClick(View v) {
                mSecondText = !mSecondText;
                TransitionManager.beginDelayedTransition(transitionsContainer,
                    new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                textView.setText(mSecondText ? TEXT_2 : TEXT_1);
            }

        });

        return view;
    }
}
