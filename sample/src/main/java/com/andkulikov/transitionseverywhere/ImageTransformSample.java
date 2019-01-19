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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

/**
 * Created by Andrey Kulikov on 20/03/16.
 */
public class ImageTransformSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_transform, container, false);

        final ViewGroup transitionsContainer = view.findViewById(R.id.transitions_container);
        final ImageView imageView = transitionsContainer.findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {

            boolean mExpanded;

            @Override
            public void onClick(View v) {
                mExpanded = !mExpanded;

                TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                    .addTransition(new ChangeBounds())
                    .addTransition(new ChangeImageTransform()));

                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = mExpanded ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(params);

                imageView.setScaleType(mExpanded ? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.FIT_CENTER);
            }
        });

        return view;
    }
}
