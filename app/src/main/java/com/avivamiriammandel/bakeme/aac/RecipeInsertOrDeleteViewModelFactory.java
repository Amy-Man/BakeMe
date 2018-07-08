package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.avivamiriammandel.bakeme.model.Recipe;


public class RecipeInsertOrDeleteViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    Application application;
    Recipe recipe;

    public RecipeInsertOrDeleteViewModelFactory(Application application, Recipe recipe) {
        this.application = application;
        this.recipe = recipe;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeInsertOrDeleteViewModel(application, recipe);
    }
}
