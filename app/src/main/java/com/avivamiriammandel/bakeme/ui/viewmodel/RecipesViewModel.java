package com.avivamiriammandel.bakeme.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipesViewModel.class.getSimpleName();


    private LiveData<List<Recipe>> recipesListFromDB;
    private LiveData<List<Recipe>> recipesList;

    private MyApplication application = new MyApplication();
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private RecipeDBRepository recipeDBRepository;



    public RecipesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipeDao = database.recipeDao();
        appExecutors = AppExecutors.getInstance();
        recipeDBRepository = RecipeDBRepository.getInstance(application, recipeDao, appExecutors);
        recipesList = getRecipesListFromDB();
        }




    public LiveData<List<Recipe>> getRecipesListFromDB() {
          if (recipesListFromDB==null) {
              recipesListFromDB = recipeDBRepository.loadAllRecipesWithLiveData();
          }
                Log.d(TAG, "run: " + recipesListFromDB);

        return recipesListFromDB;
    }

}