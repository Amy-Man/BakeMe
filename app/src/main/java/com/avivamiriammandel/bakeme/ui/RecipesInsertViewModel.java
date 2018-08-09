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

public class RecipesInsertViewModel extends AndroidViewModel {
    private static final String TAG = RecipesInsertViewModel.class.getSimpleName();

    private Context context = getApplication().getApplicationContext();
    private List<Recipe> recipesForInsert;
    private MutableLiveData<Boolean> insertSuccess = new MutableLiveData<>();


    private MyApplication application = new MyApplication();
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private RecipeDBRepository  recipeDBRepository;




    public RecipesInsertViewModel(@NonNull Application application, List<Recipe> recipesForInsert) {
        super(application);
        recipeDao = AppDatabase.getInstance(application).recipeDao();
        appExecutors = AppExecutors.getInstance();
        recipeDBRepository = RecipeDBRepository.getInstance(application, recipeDao, appExecutors);

        this.recipesForInsert = recipesForInsert;
        InsertTheRecipes(this.recipesForInsert);
    }

    public LiveData<Boolean> InsertTheRecipes(final List<Recipe> recipesForInsert) {
        insertSuccess.postValue(false);
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDBRepository.insertRecipes(recipesForInsert);
            }
        });
        insertSuccess.postValue(true);
        return insertSuccess;
    }
}
