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
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> listOfRecipes;
    private onItemClickListener clickListener;
    private static final String TAG = RecipeListAdapter.class.getSimpleName();

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecipeListAdapter(Context context, List<Recipe> listOfRecipes) {
        this.context = context;
        this.listOfRecipes = listOfRecipes;

    }

    @NonNull
    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_list_card, parent, false);
        return new RecipeViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeViewHolder holder, int position) {
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
        holder.recipeServingsText.setText(String.format(recipe.getServings().toString()));

       }

    @Override
    public int getItemCount() {
        if (listOfRecipes != null)
            return listOfRecipes.size();
        else
            return 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CardView cardView;

        public ImageView recipeImage;
        public TextView recipeDetailText, recipeServingsText;

        public RecipeViewHolder(View itemView, final onItemClickListener clickListener) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.root_recipe_list_card);
            cardView = itemView.findViewById(R.id.recipe_list_card);
            recipeImage = itemView.findViewById(R.id.recipe_thumbnail);
            recipeDetailText = itemView.findViewById(R.id.recipe_description);
            recipeServingsText = itemView.findViewById(R.id.recipe_number_of_servings);

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
