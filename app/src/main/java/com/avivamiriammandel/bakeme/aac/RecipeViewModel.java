package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.model.Recipe;


public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<Recipe> recipe;
    private int recipeIdForQuery;

    public RecipeViewModel(@NonNull Application application, int recipeId) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
        this.recipeIdForQuery = recipeId;
    }

    private LiveData<Recipe> loadRecipe(int recipeId){
        return recipeRepository.loadRecipe(recipeIdForQuery);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

}
