package com.avivamiriammandel.bakeme.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.model.Recipe;

public class RecipeInsertOrDeleteViewModel extends AndroidViewModel {

    private static final String TAG = RecipeInsertOrDeleteViewModel.class.getSimpleName();
    private RecipeApiRepository recipeApiRepository;
    private Recipe recipeForInsertOrDelete;

    public RecipeInsertOrDeleteViewModel(@NonNull Application application, Recipe recipeForInsertOrDelete) {
        super(application);
        //this.recipeApiRepository = new RecipeApiRepository(application);
        this.recipeForInsertOrDelete = recipeForInsertOrDelete;
        Log.d(TAG, "RecipeInsertOrDeleteViewModel: " + recipeForInsertOrDelete);
    }
}

    /*public void deleteRecipe() {
        recipeApiRepository.deleteRecipe(recipeForInsertOrDelete);
    }

    public void InsertRecipe() {
        recipeApiRepository.insertRecipe(recipeForInsertOrDelete);
        Log.d(TAG, "InsertRecipe: success " + recipeForInsertOrDelete);
    }

}
*/