package com.andkulikov.transitionseverywhere;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ListFragment.SampleListProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListFragment listFragment = new ListFragment();
        listFragment.setSampleListListener(this);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, listFragment)
            .commit();
    }

    @Override
    public void onSampleSelected(int index) {
        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.container, createFragmentForPosition(index))
            .addToBackStack(String.valueOf(index))
            .commit();
    }

    @Override
    public int getSampleCount() {
        return 4;
    }

    @Override
    public String getTitleForPosition(int index) {
        switch (index) {
            case 0: return "Simple animations with AutoTransition";
            case 1: return "Scene to scene transitions";
            case 2: return "Slide transition";
            case 3: return "ChangeImageTransform transition";
        }
        return null;
    }

    private Fragment createFragmentForPosition(int index) {
        switch (index) {
            case 0: return new AutoTransitionSample();
            case 1: return new ScenesSample();
            case 2: return new SlideSample();
            case 3: return new ImageTransformSample();
        }
        return null;
    }

}
