package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;


public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    Application application;
    int recipeid;

    public RecipeViewModelFactory(Application application, int recipeid) {
        this.application = application;
        this.recipeid = recipeid;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeViewModel(application, recipeid);
    }
}
