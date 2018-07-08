package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.model.Recipe;

public class RecipeInsertOrDeleteViewModel extends AndroidViewModel {

    private static final String TAG = RecipeListViewModel.class.getSimpleName();
    private RecipeRepository recipeRepository;
    private Recipe recipeForInsertOrDelete;

    public RecipeInsertOrDeleteViewModel(@NonNull Application application, Recipe recipe) {
        super(application);
        this.recipeRepository = new RecipeRepository(application, recipe);
        this.recipeForInsertOrDelete = recipe;
    }

    public void deleteRecipe(Recipe recipeForDelete) {
        recipeRepository.deleteRecipe(recipeForInsertOrDelete);
    }

    public void InsertRecipe(Recipe recipeForInsert) {
        recipeRepository.insertRecipe(recipeForInsertOrDelete);
    }

}
