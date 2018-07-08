package com.avivamiriammandel.bakeme.adaper;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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
import com.avivamiriammandel.bakeme.aac.RecipeListViewModel;
import com.avivamiriammandel.bakeme.aac.RecipeViewModel;
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> listOfRecipes;
    private static final String TAG = RecipeAdapter.class.getSimpleName();

    public RecipeAdapter(Context context, List<Recipe> listOfRecipes) {
        this.context = context;
        this.listOfRecipes = listOfRecipes;

    }

    public RecipeAdapter(Observer<List<Recipe>> observer, List<Recipe> recipes) {
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
       Recipe recipe = listOfRecipes.get(position);
       holder.recipeDetailText.setText(recipe.getName());
        try {

            GlideApp.with(context)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.cake_loading)
                    .error(R.drawable.cake_error)
                    .into(holder.recipeImage);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, R.string.on_bind_view_holder + e.getMessage());
        }


       }

    @Override
    public int getItemCount() {
        if (listOfRecipes != null)
            return listOfRecipes.size();
        else
            return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CardView cardView;

        public ImageView recipeImage;
        public TextView recipeDetailText, recipeServingsText;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.root_recipe_list_card);
            cardView = itemView.findViewById(R.id.recipe_list_card);
            recipeImage = itemView.findViewById(R.id.recipe_thumbnail);
            recipeDetailText = itemView.findViewById(R.id.recipe_detail);
            recipeServingsText = itemView.findViewById(R.id.recipe_number_of_servings);

        }
    }
}
