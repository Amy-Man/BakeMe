package com.avivamiriammandel.bakeme.adaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.ui.fragment.StepsDetailsFragment;

public class StepDetailAdapter extends FragmentStatePagerAdapter {

    int count;
    String stepString;
    Context context;
    Bundle bundle;

    public StepDetailAdapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        this.context = context;
        this.bundle = bundle;
        if (this.bundle != null) {

            stepString = this.bundle.getString(this.context.getString(R.string.steps_bundle));


            String countString = bundle.getString(this.context.getString(R.string.steps_position));

            count = Integer.parseInt(countString);
        }
    }


    @Override
    public Fragment getItem(int position) {
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();

        Bundle arguments = new Bundle();
        arguments.putString(context.getString(R.string.steps_bundle), stepString);
        position = position++;
        stepsDetailsFragment.setArguments(arguments);
        return stepsDetailsFragment;
    }

    @Override
    public int getCount() {
        return count;
    }
}
