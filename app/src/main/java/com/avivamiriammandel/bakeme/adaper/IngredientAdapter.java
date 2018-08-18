package com.avivamiriammandel.bakeme.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Ingredient;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.text.DecimalFormat;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> listOfIngredients;
    private static final String TAG = IngredientAdapter.class.getSimpleName();
    public IngredientAdapter(Context context, List<Ingredient> listOfIngredients) {
        this.context = context;
        this.listOfIngredients = listOfIngredients;

    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_card, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
       Ingredient ingredient = listOfIngredients.get(position);
        final DecimalFormat format = new DecimalFormat("##");
       holder.ingredientQuantity.setText(format.format(ingredient.getQuantity()));
       holder.ingredientMeasure.setText(ingredient.getMeasure());
       holder.ingredientDescription.setText(ingredient.getIngredient());



       holder.ingredientDescription.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
       }

    @Override
    public int getItemCount() {
        if (listOfIngredients != null) {
            Log.d(TAG, "getItemCount: " + listOfIngredients.size());
            return listOfIngredients.size();
        }
        else
            return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CardView cardView;
        public TextView ingredientQuantity, ingredientMeasure, ingredientDescription;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.ingredient_list_card_constraint);
            cardView = itemView.findViewById(R.id.ingredient_list_card_root);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientMeasure = itemView.findViewById(R.id.ingredient_measure);
            ingredientDescription = itemView.findViewById(R.id.ingredient_description);

        }
    }
}
