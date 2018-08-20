package com.avivamiriammandel.bakeme.adaper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.StepListTypeConverter;
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.ui.fragment.StepsDetailsFragment;
import com.danikula.videocache.HttpProxyCacheServer;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

public class StepsDetailsAdapter extends PagerAdapter {

    Context context;
    Bundle bundle;
    List<Step> stepList;
    Step step;
    int position;
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
    private StepsDetailsAdapter.ComponentListener componentListener;
    private MediaSourceEventListener mediaSourceEventListener;
    private VideoRendererEventListener videoRendererEventListener;
    private AudioRendererEventListener audioRendererEventListener;
    Toolbar toolbar;
    com.google.android.exoplayer2.ui.AspectRatioFrameLayout frameForPlayer;
    Activity callingActivity;
    String stepListString;


    public StepsDetailsAdapter(Context context, Bundle bundle, Activity callingActivity) {
        this.context = context;
        this.bundle = bundle;
        this.callingActivity = callingActivity;
        if (bundle != null) {
            stepListString = bundle.getString(context.getString(R.string.steps_bundle));
            stepList = StepListTypeConverter.stringToStepList(stepListString);
//            position = bundle.getInt(context.getString(R.string.steps_position));
//            callingActivity = (bundle.getParcelable(context.getString(R.string.bundle_activity)));
        }
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup parent, int position) {
        step = stepList.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.steps_details_list_card, parent,false);

        toolbar = rootView.findViewById(R.id.toolbar);
        constraintLayout = rootView.findViewById(R.id.step_detail_list_card_constraint);
        cardView = rootView.findViewById(R.id.step_detail_list_card_root);
        stepId = rootView.findViewById(R.id.step_detail_id);
        stepShortDescription = rootView.findViewById(R.id.step_detail_short_description);
        stepDescription = rootView.findViewById(R.id.step_detail_description);
        stepVideoUrl = rootView.findViewById(R.id.step_detail_video_url);
        stepImageUrl = rootView.findViewById(R.id.step_detail_image_url);
        playerView = rootView.findViewById(R.id.step_detail_video);
        thumbnail = rootView.findViewById(R.id.step_detail_thumbnail);
        frameForPlayer = rootView.findViewById(R.id.frame_for_player);

        initViews();

        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup parent, int position, @NonNull Object object) {
        releasePlayer();
        parent.removeView((View) object);
    }


    private void initViews() {
        toolbar.setTitle(step.getShortDescription());
        player = ExoPlayerFactory.newSimpleInstance(context.getApplicationContext(), new DefaultTrackSelector());

        stepDescription.setText(step.getDescription());
        if (step.getThumbnailURL().contains("mp4")) {
            initializePlayer(step.getThumbnailURL());
            loadThumbnail(step.getVideoURL());
        } else {
            initializePlayer(step.getVideoURL());
            loadThumbnail(step.getThumbnailURL());
        }
    }

    private void initializePlayer(String videoUrlString) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float dpWidth = displayMetrics.widthPixels;
        final int dp = Math.round(dpWidth);

        frameForPlayer.setAspectRatio(Float.valueOf("1.5"));
        frameForPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        ViewGroup.LayoutParams layoutParams = frameForPlayer.getLayoutParams();
        layoutParams.height = dp;
        layoutParams.width = dp;
        frameForPlayer.setLayoutParams(layoutParams);
        playerView.setPlayer(player);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


        if (videoUrlString.isEmpty()) {
            Toast.makeText(context, "no video avalable", Toast.LENGTH_LONG).show();
            playerView.setVisibility(View.INVISIBLE);

        } else if (videoUrlString.contains("mp4")) {

            Log.d(TAG, "initializePlayer: " + videoUrlString);
            playerView.setVisibility(View.VISIBLE);


            String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context.getApplicationContext(), userAgent);


            TrackSelection.Factory adaptiveTrackSelectionfactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            player = ExoPlayerFactory.newSimpleInstance((new DefaultRenderersFactory(context)),
                    (new DefaultTrackSelector(adaptiveTrackSelectionfactory)),
                    (new DefaultLoadControl()));

            playerView.setPlayer(player);
            HttpProxyCacheServer proxy = MyApplication.getProxy(context);
            String proxyUrl = proxy.getProxyUrl(videoUrlString);

            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));

            player.setSeekParameters(SeekParameters.DEFAULT);

            //player.setRepeatMode(Player.REPEAT_MODE_ONE);

            player.prepare(mediaSource, true, false);
            Log.d(TAG, "initializePlayer: " + mediaSource);
            componentListener = new StepsDetailsAdapter.ComponentListener();
            player.addListener(componentListener);


            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = Long.valueOf("0");
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

                        /*new DefaultDataSourceFactory(getContext(), userAgent),
                        new DefaultExtractorsFactory(), null, null, null);
*/
               /* player.prepare(mediaSource);
                player.setPlayWhenReady(true);*/


        }


    }


    private void loadThumbnail(String thumbnailUrlString) {
        try {
            GlideApp.with(context)
                    .load(thumbnailUrlString)
                    .listener(GlidePalette.with(thumbnailUrlString)
                            .use(GlidePalette.Profile.VIBRANT_DARK)
                            .intoBackground(toolbar)
                            .crossfade(true)
                    )
                    .placeholder(R.drawable.cake_loading)
                    .error(R.drawable.cake_step_error)
                    .into(thumbnail);
        } catch (final IllegalArgumentException e) {
            Log.e(TAG, context.getString(R.string.on_bind_view_holder) + e.getMessage());
        }
        ((AppCompatActivity) callingActivity).setSupportActionBar(toolbar);
        ((AppCompatActivity) callingActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setElevation(10.f);


    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.release();
            player.stop();
            player = null;

        }
    }

    /*private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }
*/

    private void hideSystemUI() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private class ComponentListener implements Player.EventListener, VideoRendererEventListener, AudioRendererEventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE: {
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                }
                case Player.STATE_BUFFERING: {
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                }
                case Player.STATE_READY: {
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                }
                case Player.STATE_ENDED: {
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                }
                default: {
                    stateString = "UNKNOWN_STATE             -";
                    break;
                }
            }
            Log.d(TAG, "onPlayerStateChanged: to" + stateString + "playWhenReady" + playWhenReady);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {

        }

        @Override
        public void onAudioSessionId(int audioSessionId) {

        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {

        }
    }
}
