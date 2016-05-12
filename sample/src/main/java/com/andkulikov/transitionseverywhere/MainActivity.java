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
        return 13;
    }

    @Override
    public String getTitleForPosition(int index) {
        switch (index) {
            case 0: return "Simple animations with AutoTransition";
            case 1: return "Interpolator, duration, start delay";
            case 2: return "Path motion";
            case 3: return "Slide transition";
            case 4: return "Scale transition";
            case 5: return "Explode transition and epicenter";
            case 6: return "Transition names";
            case 7: return "ChangeImageTransform transition";
            case 8: return "Recolor transition";
            case 9: return "Rotate transition";
            case 10: return "Change text transition";
            case 11: return "Custom transition";
            case 12: return "Scene to scene transitions";
        }
        return null;
    }

    private Fragment createFragmentForPosition(int index) {
        switch (index) {
            case 0: return new AutoTransitionSample();
            case 1: return new InterpolatorDurationStartDelaySample();
            case 2: return new PathMotionSample();
            case 3: return new SlideSample();
            case 4: return new ScaleSample();
            case 5: return new ExplodeAndEpicenterExample();
            case 6: return new TransitionNameSample();
            case 7: return new ImageTransformSample();
            case 8: return new RecolorSample();
            case 9: return new RotateSample();
            case 10: return new ChangeTextSample();
            case 11: return new CustomTransitionSample();
            case 12: return new ScenesSample();
        }
        return null;
    }

}
