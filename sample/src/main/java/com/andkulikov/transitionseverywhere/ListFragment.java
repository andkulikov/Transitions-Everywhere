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
