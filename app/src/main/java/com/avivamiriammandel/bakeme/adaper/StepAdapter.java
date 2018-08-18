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
import android.widget.AdapterView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.model.Ingredient;
import com.avivamiriammandel.bakeme.model.Step;

import java.text.DecimalFormat;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context context;
    private List<Step> listOfSteps;
    private onItemClickListener clickListener;
    private static final String TAG = StepAdapter.class.getSimpleName();

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public StepAdapter(Context context, List<Step> listOfSteps) {
        this.context = context;
        this.listOfSteps = listOfSteps;

    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_list_card, parent, false);
        return new StepViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, int position) {
       Step step = listOfSteps.get(position);

        holder.stepId.setText(String.valueOf(step.getId()));
        holder.stepShortDescription.setText(step.getShortDescription());
        holder.stepDescription.setText(step.getDescription());
        holder.stepVideoUrl.setText(step.getVideoURL());
        holder.stepImageUrl.setText(step.getThumbnailURL());
       }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + listOfSteps);
        if (listOfSteps != null)
            return listOfSteps.size();
        else
            return 0;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CardView cardView;
        public TextView stepId, stepShortDescription, stepDescription, stepVideoUrl, stepImageUrl;

        public StepViewHolder(View itemView, final onItemClickListener clickListener) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.step_list_card_constraint);
            cardView = itemView.findViewById(R.id.step_list_card_root);
            stepId = itemView.findViewById(R.id.step_id);
            stepShortDescription = itemView.findViewById(R.id.step_short_description);
            stepDescription = itemView.findViewById(R.id.step_description);
            stepVideoUrl = itemView.findViewById(R.id.step_video_url);
            stepImageUrl = itemView.findViewById(R.id.step_image_url);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null){
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
