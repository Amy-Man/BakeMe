package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;


public class RecipesInsertViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    Application application;
    List<Recipe> recipeList;
    private static final String TAG = RecipesInsertViewModelFactory.class.getSimpleName();

    public RecipesInsertViewModelFactory(Application application, List<Recipe> recipeList) {
        this.application = application;
        this.recipeList = recipeList;
        Log.d(TAG, "RecipesInsertViewModelFactory: "+ this.recipeList);
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipesInsertViewModel(application, recipeList);
    }
}
