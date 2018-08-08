package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipesInsertViewModel extends AndroidViewModel {

    private static final String TAG = RecipesInsertViewModel.class.getSimpleName();
    private RecipeApiRepository recipeApiRepository;
    private List<Recipe> recipesForInsert;
    MutableLiveData<Boolean> recipesInserted;

    public RecipesInsertViewModel(@NonNull Application application, List<Recipe> recipesForInsert) {
        super(application);
        //this.recipeApiRepository = new RecipeApiRepository(application);
        this.recipesForInsert = recipesForInsert;

        Log.d(TAG, "RecipeInsertOrDeleteViewModel: " + recipesForInsert);
    }

    /*public LiveData<Boolean> InsertListOfRecipes() {
        recipeApiRepository.insertListOfRecipes(recipesForInsert);
        recipesInserted = recipeApiRepository.recipesInserted;
        Log.d(TAG, "InsertRecipe: success " + recipesForInsert);
        return recipesInserted;
    }
*/
}
