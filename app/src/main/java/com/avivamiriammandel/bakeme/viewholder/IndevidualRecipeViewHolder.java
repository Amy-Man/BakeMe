package com.avivamiriammandel.bakeme.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class IndevidualRecipeViewHolder extends ChildViewHolder {

    public ConstraintLayout constraintLayout;
    public CardView cardView;

    public TextView textIngredient;
    public TextView textQuantity;
    public TextView textUnit;


    public IndevidualRecipeViewHolder(View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.constraint_ingredient);
        cardView = itemView.findViewById(R.id.card_ingredient);
        textIngredient = itemView.findViewById(R.id.ingredient_name);
        textQuantity = itemView.findViewById(R.id.ingredient_quantity);
        textUnit = itemView.findViewById(R.id.ingredient_unit);
    }

    public void setTextIngredient(TextView textIngredient) {
        this.textIngredient = textIngredient;
    }

    public void setTextQuantity(TextView textQuantity) {
        this.textQuantity = textQuantity;
    }

    public void setTextUnit(TextView textUnit) {
        this.textUnit = textUnit;
    }
}
