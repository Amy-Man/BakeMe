package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipeListViewModel.class.getSimpleName();
    private RecipeRepository recipeRepository;
    private List<Recipe> recipesListFromApi;
    private LiveData<List<Recipe>> recipesListFromDB;


    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);

    }

    public List<Recipe> getRecipesListFromApi() {
        do
        recipesListFromApi = recipeRepository.getRecipesFromApi();
        while (recipesListFromApi == null);
        return  recipesListFromApi;

    }

    public LiveData<List<Recipe>> getRecipesListFromDB() {
        recipesListFromDB = recipeRepository.loadAllRecipes();
        return recipesListFromDB;
    }
}
