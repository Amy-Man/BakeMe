package com.avivamiriammandel.bakeme.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.model.Recipe;


public class RecipeViewModel extends AndroidViewModel {

    private static final String TAG = RecipesViewModel.class.getSimpleName();

    private Context context = getApplication().getApplicationContext();
    private LiveData<Recipe> recipeFromDB;
    private int recipeId;

    private MyApplication application = new MyApplication();
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;

    private RecipeDBRepository  recipeDBRepository;
    public RecipeViewModel(@NonNull Application application, int recipeId) {
        super(application);
        this.recipeId = recipeId;
        recipeDao = AppDatabase.getInstance(application).recipeDao();
        appExecutors = AppExecutors.getInstance();
        recipeDBRepository = RecipeDBRepository.getInstance(application, recipeDao, appExecutors);

        recipeFromDB = recipeDBRepository.loadRecipe(recipeId);


    }

    public LiveData<Recipe> getRecipe() {
        return recipeFromDB;
    }
}












