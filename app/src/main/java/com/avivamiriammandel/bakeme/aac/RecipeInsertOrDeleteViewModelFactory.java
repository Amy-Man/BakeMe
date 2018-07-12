package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.avivamiriammandel.bakeme.model.Recipe;



public class RecipeInsertOrDeleteViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    Application application;
    Recipe recipe;
    private static final String TAG = RecipeInsertOrDeleteViewModelFactory.class.getSimpleName();

    public RecipeInsertOrDeleteViewModelFactory(Application application, Recipe recipe) {
        this.application = application;
        this.recipe = recipe;
        Log.d(TAG, "RecipeInsertOrDeleteViewModelFactory: "+ this.recipe);
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeInsertOrDeleteViewModel(application, recipe);
    }
}
