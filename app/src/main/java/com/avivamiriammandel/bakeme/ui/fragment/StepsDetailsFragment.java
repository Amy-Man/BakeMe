package com.avivamiriammandel.bakeme.ui.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.adaper.StepAdapter;
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.activity.RecipesActivity;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesFromApiViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesInsertViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesViewModel;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

import static android.net.Uri.parse;

public class StepsDetailsFragment extends Fragment {
    private List<Recipe> recipes;
    public RecipeApiRepository recipeApiRepository;
    public RecipeDBRepository recipeDBRepository;
    private RecipesViewModel recipesViewModel;
    private RecipesFromApiViewModel recipesFromApiViewModel;
    private RecipesInsertViewModel recipesInsertViewModel;
    private Context context;
    private Step step;
    private List<Recipe> recipeListFromApi;
    private LifecycleOwner lifecycleOwner = StepsDetailsFragment.this;
    private static final String TAG = StepsDetailsFragment.class.getSimpleName();
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    public static Boolean hasNoDatabase = true;
    private SharedPreferences sharedPreferences;
    private MyApplication myApplication;
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private Service apiService;
    private SharedPreferences.Editor editor;
    private Boolean insertCompleted;
    com.google.android.exoplayer2.ui.PlayerView playerView;
    SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    public ConstraintLayout constraintLayout;
    public CardView cardView;
    public TextView stepId, stepShortDescription, stepDescription, stepVideoUrl, stepImageUrl;
    public ImageView thumbnail;
    private ComponentListener componentListener;
    private MediaSourceEventListener mediaSourceEventListener;
    private VideoRendererEventListener videoRendererEventListener;
    private AudioRendererEventListener audioRendererEventListener;
    Toolbar toolbar;



    public StepsDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.steps_details_list_card, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
        step = StepTypeConverter.stringToStep(bundle.getString(getString(R.string.steps_bundle)));
            Log.d(TAG, "onCreateView: "+ step.getThumbnailURL());
        context = StepsDetailsFragment.this.getContext();

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

            initViews();

            return rootView;
        } else {
            throw new NullPointerException(TAG);
        }
    }

    private void initViews() {
        toolbar.setTitle(step.getShortDescription());
        player = ExoPlayerFactory.newSimpleInstance(context.getApplicationContext(), new DefaultTrackSelector());
        playerView.setPlayer(player);
        stepDescription.setText(step.getDescription());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (step.getThumbnailURL().contains("mp4")) {
                initializePlayer(step.getThumbnailURL());
                loadThumbnail(step.getVideoURL());
            } else {
                loadThumbnail(step.getThumbnailURL());
                initializePlayer(step.getVideoURL());
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        if ((Util.SDK_INT <= 23) || (player == null)) {
            if (step.getThumbnailURL().contains("mp4")) {
                initializePlayer(step.getThumbnailURL());
                loadThumbnail(step.getVideoURL());
            } else {
                loadThumbnail(step.getThumbnailURL());
                initializePlayer(step.getVideoURL());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((Util.SDK_INT <= 23) || (player == null)) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer(String videoUrlString) {
        if (player == null) {
            if (videoUrlString.isEmpty()) {
                Toast.makeText(getContext(), "no video avalable", Toast.LENGTH_LONG).show();
                playerView.setVisibility(View.INVISIBLE);


            } else if (videoUrlString.contains("mp4")) {
                Log.d(TAG, "initializePlayer: " + videoUrlString);
                playerView.setVisibility(View.VISIBLE);
                String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context.getApplicationContext(), userAgent);

                ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));
                player.setSeekParameters(SeekParameters.DEFAULT);
                // Bind the player to the view.


                player.prepare(mediaSource);
                player.setPlayWhenReady(true);

                TrackSelection.Factory adaptiveTrackSelectionfactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
                player = ExoPlayerFactory.newSimpleInstance((new DefaultRenderersFactory(getContext())),
                        (new DefaultTrackSelector(adaptiveTrackSelectionfactory)),
                        (new DefaultLoadControl()));

                player.addListener(componentListener);


                playerView.setPlayer(player);
                player.setPlayWhenReady(playWhenReady);
                player.seekTo(currentWindow, playbackPosition);

                mediaSource.addEventListener(null, mediaSourceEventListener);


                        /*new DefaultDataSourceFactory(getContext(), userAgent),
                        new DefaultExtractorsFactory(), null, null, null);
*/
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);

            } else if (thumbnail == null){
                loadThumbnail(step.getVideoURL());
                playerView.setVisibility(View.INVISIBLE);
            }

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
                    .error(R.drawable.cake_error)
                    .into(thumbnail);
        } catch (final IllegalArgumentException e) {
            Log.e(TAG, getString(R.string.on_bind_view_holder) + e.getMessage());
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

