package com.avivamiriammandel.bakeme.adaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.StepListTypeConverter;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.ui.fragment.StepsDetailsFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

public class StepDetailAdapter extends FragmentStatePagerAdapter {

    Context context;
    Bundle bundle;
    List<Step> stepList;
    Step step;
    String stepString;
    int stepPosition;
    private static final String TAG = StepsDetailsFragment.class.getSimpleName();
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    com.google.android.exoplayer2.ui.PlayerView playerView;
    SimpleExoPlayer player;
    Integer currentWindow = 0;
    Long playbackPosition = Long.valueOf("0");
    private boolean playWhenReady;
    public ConstraintLayout constraintLayout;
    public CardView cardView;
    public TextView stepId, stepShortDescription, stepDescription, stepVideoUrl, stepImageUrl;
    public ImageView thumbnail;
    //private StepsDetailsAdapter.ComponentListener componentListener;
    private MediaSourceEventListener mediaSourceEventListener;
    private VideoRendererEventListener videoRendererEventListener;
    private AudioRendererEventListener audioRendererEventListener;
    Toolbar toolbar;
    com.google.android.exoplayer2.ui.AspectRatioFrameLayout frameForPlayer;
    Activity callingActivity;
    String stepListString;


    public StepDetailAdapter(FragmentManager fm, Context context, Bundle bundle, Activity callingActivity) {
        super(fm);
        this.context = context;
        this.bundle = bundle;
        this.context = context;
        this.callingActivity = callingActivity;

        if (bundle != null) {
            stepListString = bundle.getString(context.getString(R.string.steps_bundle));
            stepList = StepListTypeConverter.stringToStepList(stepListString);
            stepPosition = bundle.getInt(context.getString(R.string.steps_position));
        }
    }


    @Override
    public Fragment getItem(int position) {

        stepString = StepTypeConverter.stepToString(stepList.get(position));
        Bundle arguments = new Bundle();
        arguments.putString(context.getString(R.string.steps_bundle), stepString);
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(arguments);
        return stepsDetailsFragment;
    }

    @Override
    public int getCount() {
        return stepList.size();
    }
}
