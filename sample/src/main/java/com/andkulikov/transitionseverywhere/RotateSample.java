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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Andrey Kulikov on 17/04/16.
 */
public class RotateSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rotate, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final View icon = transitionsContainer.findViewById(R.id.icon);

        icon.setOnClickListener(new View.OnClickListener() {

            boolean mRotated;

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Rotate());

                mRotated = !mRotated;
                icon.setRotation(mRotated ? 135 : 0);
            }

        });

        return view;
    }

}
