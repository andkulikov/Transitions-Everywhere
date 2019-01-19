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

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

/**
 * Created by Andrey Kulikov on 25/03/16.
 */
public class ExplodeAndEpicenterExample extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(container.getContext());
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 4));
        mRecyclerView.setAdapter(new Adapter());
        return mRecyclerView;
    }

    private void letsExplodeIt(View clickedView) {
        // save rect of view in screen coordinated
        final Rect viewRect = new Rect();
        clickedView.getGlobalVisibleRect(viewRect);

        final Explode explode = new Explode();
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                return viewRect;
            }
        });
        explode.excludeTarget(clickedView, true);
        TransitionSet set = new TransitionSet()
            .addTransition(explode)
            .addTransition(new Fade().addTarget(clickedView))
            .addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    getActivity().onBackPressed();
                }
            });
        TransitionManager.beginDelayedTransition(mRecyclerView, set);

        // remove all views from Recycler View
        mRecyclerView.setAdapter(null);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explode_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickedView) {
                    letsExplodeIt(clickedView);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 32;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

    }

}
