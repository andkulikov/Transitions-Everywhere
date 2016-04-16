package com.andkulikov.transitionseverywhere;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/**
 * Created by Andrey Kulikov on 25/03/16.
 */
public class ExplodeAndEpicenterExample extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(container.getContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 4));
        recyclerView.setAdapter(new Adapter());
        return recyclerView;
    }

    private void letsExplodeIt(View clickedView) {
        final Rect viewRect = new Rect();
        clickedView.getGlobalVisibleRect(viewRect);

        TransitionSet set = new TransitionSet()
            .addTransition(new Explode().setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition transition) {
                    return viewRect;
                }
            }).excludeTarget(clickedView, true))
            .addTransition(new Fade().addTarget(clickedView))
            .addListener(new Transition.TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getActivity().onBackPressed();
                }
            });

        TransitionManager.beginDelayedTransition(recyclerView, set);
        recyclerView.setAdapter(null);
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
