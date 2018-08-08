package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipesViewModel.class.getSimpleName();

    private LiveData<List<Recipe>> recipesListFromDB;

    private Application application;



    /*private static final MutableLiveData MUTABLE_LIVE_DATA = new MutableLiveData();

    {
        MUTABLE_LIVE_DATA.setValue(null);
    }*/

/*
    public RecipesViewModel(@NonNull RecipesViewModel recipesViewModel,
                            @NonNull Application application) {
        super(application);
        recipesListFromDB = RecipeDBRepository.getInstance()
                .getRecipes();
    }

    public LiveData<List<Recipe>> getRecipesFromApi() {
        return recipesListFromApi;
    }
*/


}