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
        Bundle params = getIntent().getExtras();
        if (params != null) {
            TransitionSet set = new TransitionSet();
            Slide slide = new Slide(Gravity.LEFT);
            slide.addTarget(R.id.transition_title);
            set.addTransition(slide);
            set.addTransition(new ChangeBounds());
            set.addTransition(new ChangeImageTransform());
            set.setOrdering(TransitionSet.ORDERING_TOGETHER);
            set.setDuration(350);
            ActivityTransitionManager.setContentView(this, R.layout.new_activity_1, params, set);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityTransitionManager.onGoBack(this);
    }
}
