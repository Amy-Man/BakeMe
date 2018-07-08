package com.avivamiriammandel.bakeme.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class RecipeListViewHolder extends GroupViewHolder {
    public ConstraintLayout constraintLayout;
    public CardView cardView;

    public ImageView imageView;
    public TextView textView;

    public RecipeListViewHolder(View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.root_recipe_list_card);
        cardView = itemView.findViewById(R.id.recipe_list_card);
        imageView = itemView.findViewById(R.id.recipe_thumbnail);
        textView = itemView.findViewById(R.id.recipe_detail);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
