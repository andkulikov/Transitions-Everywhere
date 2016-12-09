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

            boolean mToRightAnimation;

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer,
                    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

                mToRightAnimation = !mToRightAnimation;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
                params.gravity = mToRightAnimation ? (Gravity.RIGHT | Gravity.BOTTOM) :
                    (Gravity.LEFT | Gravity.TOP);
                button.setLayoutParams(params);
            }

        });

        return view;
    }


}
