package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipesViewModel.class.getSimpleName();
    public RecipeRepository recipeRepository;
    private LiveData<List<Recipe>> recipesListFromApi;
    private LiveData<List<Recipe>> recipesListFromDB;


    public RecipesViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);

    }

    public LiveData<List<Recipe>> getRecipesListFromApi() {
        //do
        recipesListFromApi = recipeRepository.getRecipesFromApi();
        //while (recipesListFromApi == null);
        return  recipesListFromApi;

    }

    public LiveData<List<Recipe>> getRecipesListFromDB() {
        recipesListFromDB = recipeRepository.loadAllRecipes();
        if (recipesListFromDB != null)
            Log.d(TAG, "getRecipesListFromDB: " + recipesListFromDB);
        return recipesListFromDB;
    }
}
