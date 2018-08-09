package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipesViewModel.class.getSimpleName();

    private Context context = getApplication().getApplicationContext();
    private LiveData<List<Recipe>> recipesListFromDB;
    private List<Recipe> recipesList;

    private MyApplication application = new MyApplication();
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private RecipeDBRepository recipeDBRepository;



    public RecipesViewModel(@NonNull Application application) {
        super(application);
        getRecipesFromDB();


    }

    private void getRecipesFromDB() {
        recipeDao = AppDatabase.getInstance(context).recipeDao();
        appExecutors = AppExecutors.getInstance();
        recipeDBRepository = RecipeDBRepository.getInstance(application, recipeDao, appExecutors);
        setRecipesList();
        setRecipesListFromDB();
    }

    private void setRecipesList() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipesList = recipeDBRepository.loadRecipesList();
            }
        });

    }
    private void setRecipesListFromDB() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipesListFromDB = recipeDBRepository.loadAllRecipesWithLiveData();
            }
        });

    }

    public List<Recipe> getRecipesList() {
        return recipesList;
    }

    public LiveData<List<Recipe>> getRecipesListFromDB() {
        return recipesListFromDB;
    }
}