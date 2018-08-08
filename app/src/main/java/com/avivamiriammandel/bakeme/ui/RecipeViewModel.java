package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.model.Recipe;


public class RecipeViewModel extends AndroidViewModel {

    private RecipeDBRepository recipeApiRepository;
    private LiveData<Recipe> recipe;


    public RecipeViewModel(@NonNull Application application, int recipeId) {
        super(application);


    }

    private LiveData<Recipe> loadRecipe(int recipeIdForQuery){
        return recipeApiRepository.loadRecipe(recipeIdForQuery);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

}
