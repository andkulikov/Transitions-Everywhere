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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andrey Kulikov on 20/03/16.
 */
public class ListFragment extends Fragment {

    private SampleListProvider mSampleProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        view.setAdapter(new Adapter());
        return view;
    }

    public void setSampleListListener(SampleListProvider sampleSelectedListener) {
        mSampleProvider = sampleSelectedListener;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.getTitle().setText(mSampleProvider.getTitleForPosition(position));
            holder.getTitle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSampleProvider.onSampleSelected(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSampleProvider == null ? 0 : mSampleProvider.getSampleCount();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(TextView textView) {
            super(textView);
        }

        public TextView getTitle() {
            return (TextView) itemView;
        }
    }

    public interface SampleListProvider {
        void onSampleSelected(int index);

        String getTitleForPosition(int index);

        int getSampleCount();
    }
}
