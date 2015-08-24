package com.andkulikov.transitionseverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

import com.transitionseverywhere.ActivityTransitionManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionSet;


public class NewActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getIntent().getExtras();
        if (params != null) {
            TransitionSet set = new TransitionSet();
            set.addTransition(new ChangeBounds());
            set.setDuration(350);
            ActivityTransitionManager.setContentView(this, R.layout.new_activity_2, params, set);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityTransitionManager.onGoBack(this);
    }
}
