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
        return 8;
    }

    @Override
    public String getTitleForPosition(int index) {
        switch (index) {
            case 0: return "Simple animations with AutoTransition";
            case 1: return "Slide transition";
            case 2: return "Scale transition";
            case 3: return "ChangeImageTransform transition";
            case 4: return "Path motion";
            case 5: return "Explode transition and epicenter";
            case 6: return "Recolor transitions";
            case 7: return "Scene to scene transitions";
        }
        return null;
    }

    private Fragment createFragmentForPosition(int index) {
        switch (index) {
            case 0: return new AutoTransitionSample();
            case 1: return new SlideSample();
            case 2: return new ScaleSample();
            case 3: return new ImageTransformSample();
            case 4: return new PathMotionSample();
            case 5: return new ExplodeAndEpicenterExample();
            case 6: return new RecolorSample();
            case 7: return new ScenesSample();
        }
        return null;
    }

}
