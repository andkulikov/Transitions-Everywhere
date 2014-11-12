package com.github.andkulikov.transitions.everywhere;

import android.app.Activity;
import android.os.Bundle;
import android.transitions.everywhere.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andrey Kulikov on 11.11.14.
 */
public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    public void button2Click(View view) {
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.content));
        View reviews = findViewById(R.id.reviews);
        if (reviews.getVisibility() == View.VISIBLE) {
            reviews.setVisibility(View.GONE);
        } else {
            reviews.setVisibility(View.VISIBLE);
        }
    }

}
