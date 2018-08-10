package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecipeDBRepository {

private Application application;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RecipeDBRepository sInstance;

    private  RecipeDao recipeDao;
private   AppExecutors appExecutors;

private Recipe recipeForInsertOrDelete;
private LiveData<List<Recipe>> recipeListFromDB;
private LiveData<Recipe> recipeLiveData;
public List<Recipe> recipesForInsert;
public List<Recipe> recipesList;
private Integer recipeId;
private Integer count = 0;
private MutableLiveData<Boolean> recipesInsertedSuccess = new MutableLiveData<>();

private RecipeDBRepository(Application application, RecipeDao recipeDao,
                               AppExecutors appExecutors) {
    this.application = application;
    this.recipeDao = recipeDao;
    this.appExecutors = appExecutors;
}
    public synchronized static RecipeDBRepository getInstance(
            Application application,
            RecipeDao recipeDao,
            AppExecutors appExecutors
            ){
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeDBRepository(application, recipeDao,
                        appExecutors);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    public LiveData<List<Recipe>> loadAllRecipesWithLiveData() {
        return recipeDao.loadAllRecipesWithLiveData();
    }

    @WorkerThread
    public List<Recipe> loadRecipesList() {

    appExecutors.diskIO().execute(new Runnable() {
        @Override
        public void run() {
            recipesList = recipeDao.loadRecipesList();
        }
    });
        return recipesList;
    }


    @WorkerThread
    public  LiveData<Recipe> loadRecipe(int recipeId) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        return recipeDao.loadRecipeById(recipeId);
    }

    @WorkerThread
        public LiveData<Boolean> insertRecipes(final List<Recipe> recipesForInsert){
        try {
            appExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    List<Long> recipesInsertedLong = recipeDao.insertRecipes(recipesForInsert);
                    for (Long recipeInserted : recipesInsertedLong) {
                        if (recipeInserted != -1)
                            count++;
                    }
                    if (count == recipesForInsert.size())
                        recipesInsertedSuccess.postValue(true);
                    Log.d(TAG, "run: Insert all");
                }
            });
        } catch (NullPointerException e) {
                throw new NullPointerException(e + "null Insert");
            }
            return recipesInsertedSuccess;
}

    /*@WorkerThread
    public void deleteRecipe(){
        //recipeDao.deleteRecipe(recipeForDelete);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    recipeDao.deleteRecipe(recipeForDelete);
                } catch (NullPointerException e) {
                    throw new NullPointerException(e + "null Delete");
                }

            }
        });
    }*/

}
