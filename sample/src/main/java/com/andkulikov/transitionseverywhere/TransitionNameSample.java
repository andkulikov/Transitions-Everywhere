/*
 * Copyright (C) 2016 Andrey Kulikov (andkulikov@gmail.com)
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
package com.andkulikov.transitionseverywhere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey Kulikov on 12/05/16.
 */
public class TransitionNameSample extends Fragment {

    @Nullable
    @Override
    @SuppressLint("DefaultLocale")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_names, container, false);

        final ViewGroup layout = (ViewGroup) view.findViewById(R.id.transitions_container);
        final Button button = (Button) view.findViewById(R.id.button1);

        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titles.add(String.format("Item %d", i + 1));
        }

        createViews(inflater, layout, titles);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(layout, new ChangeBounds());
                Collections.shuffle(titles);
                createViews(inflater, layout, titles);
            }

        });

        return view;
    }

    private static void createViews(LayoutInflater inflater, ViewGroup layout, List<String> titles) {
        layout.removeAllViews();
        for (String title : titles) {
            TextView textView = (TextView) inflater.inflate(R.layout.fragment_names_item, layout, false);
            textView.setText(title);
            TransitionManager.setTransitionName(textView, title);
            layout.addView(textView);
        }
    }


}
