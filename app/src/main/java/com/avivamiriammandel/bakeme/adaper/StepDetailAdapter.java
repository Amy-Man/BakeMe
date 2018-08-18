/*
package com.avivamiriammandel.bakeme.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.ui.fragment.StepsDetailsFragment;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static android.net.Uri.parse;

public class StepDetailAdapter extends RecyclerView.Adapter<StepDetailAdapter.StepDetailViewHolder>
        implements Player.EventListener {

    private Context context;
    private Step step;
    private onItemClickListener clickListener;
    SimpleExoPlayer player;

    private static final String TAG = StepDetailAdapter.class.getSimpleName();

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

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public StepDetailAdapter(Context context, Step step) {
        this.context = context;
        this.step = step;

    }

    @NonNull
    @Override
    public StepDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_details_list_card, parent, false);
        return new StepDetailViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StepDetailViewHolder holder, int position) {


        holder.stepId.setText(String.valueOf(step.getId()));
        holder.stepShortDescription.setText(step.getShortDescription());
        holder.stepDescription.setText(step.getDescription());
        holder.stepVideoUrl.setText(step.getVideoURL());
        holder.stepImageUrl.setText(step.getThumbnailURL());

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(, "BakeMe"));


        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(parse(step.getVideoURL()));
        player.setSeekParameters(SeekParameters.DEFAULT);




        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        TrackSelector trackSelector = new TrackSelector() {
            @Override
            public TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilities, TrackGroupArray trackGroups) throws ExoPlaybackException {
                return null;
            }

            @Override
            public void onSelectionActivated(Object info) {

            }
        };
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        holder.playerView.setPlayer(player);
       }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + step);
        if (step != null)
            return 1;
        else
            return 0;
    }

    public static class StepDetailViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CardView cardView;
        public TextView stepId, stepShortDescription, stepDescription, stepVideoUrl, stepImageUrl;
        public ImageView thumbnail;
        public com.google.android.exoplayer2.ui.PlayerView playerView;

        public StepDetailViewHolder(View itemView, final onItemClickListener clickListener) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.step_list_card_constraint);
            cardView = itemView.findViewById(R.id.step_list_card_root);
            stepId = itemView.findViewById(R.id.step_id);
            stepShortDescription = itemView.findViewById(R.id.step_short_description);
            stepDescription = itemView.findViewById(R.id.step_description);
            stepVideoUrl = itemView.findViewById(R.id.step_video_url);
            stepImageUrl = itemView.findViewById(R.id.step_image_url);
            playerView = itemView.findViewById(R.id.step_detail_video);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener !=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
*/
