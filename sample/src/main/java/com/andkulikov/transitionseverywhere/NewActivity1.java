package com.andkulikov.transitionseverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

import com.transitionseverywhere.ActivityTransitionManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionSet;


public class NewActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTransitionManager.setContentView(this, R.layout.new_activity_1);
    }

    @Override
    public void onBackPressed() {
        ActivityTransitionManager.onGoBack(this);
    }
}
