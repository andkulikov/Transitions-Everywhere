package com.andkulikov.transitionseverywhere;

import android.view.View;

/**
 * Created by Andrey Kulikov on 24/03/16.
 */
public abstract class VisibleToggleClickListener implements View.OnClickListener {

    private boolean mVisible;

    @Override
    public void onClick(View v) {
        mVisible = !mVisible;
        changeVisibility(mVisible);
    }

    protected abstract void changeVisibility(boolean visible);

}
