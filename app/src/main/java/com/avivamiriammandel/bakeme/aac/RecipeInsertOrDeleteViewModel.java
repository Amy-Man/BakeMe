package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.model.Recipe;

public class RecipeInsertOrDeleteViewModel extends AndroidViewModel {

    private static final String TAG = RecipeListViewModel.class.getSimpleName();
    private RecipeRepository recipeRepository;
    private Recipe recipeForInsertOrDelete;

    public RecipeInsertOrDeleteViewModel(@NonNull Application application, Recipe recipeForInsertOrDelete) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
        this.recipeForInsertOrDelete = recipeForInsertOrDelete;
        
    }

    public void deleteRecipe() {
        recipeRepository.deleteRecipe(recipeForInsertOrDelete);
    }

    public void InsertRecipe() {
        recipeRepository.insertRecipe(recipeForInsertOrDelete);
        Log.d(TAG, "InsertRecipe: success");
    }

}
