package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitionseverywhere.ChangeTextSize;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

/**
 * Created by GV_FiQst on 1/3/19.
 */
public class ChangeTextSizeSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_text, container, false);

        final ViewGroup transitionsContainer = view.findViewById(R.id.transitions_container);
        final TextView textView = transitionsContainer.findViewById(R.id.text1);

        textView.setText("Sample text");
        textView.setTextSize(14);
        textView.setOnClickListener(new View.OnClickListener() {

            boolean mSecondTextSize;

            @Override
            public void onClick(View v) {
                mSecondTextSize = !mSecondTextSize;
                TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeTextSize());
                textView.setTextSize(mSecondTextSize ? 25 : 14);
            }

        });

        return view;
    }
}
