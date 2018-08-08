package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.avivamiriammandel.bakeme.error.ApiError;
import com.avivamiriammandel.bakeme.error.ErrorUtils;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Client;
import com.avivamiriammandel.bakeme.rest.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
private List<Recipe> recipesForInsert;
private Integer recipeId;


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

    @WorkerThread
    public LiveData<List<Recipe>> loadAllRecipes() {
        return recipeDao.loadAllRecipes();
    }


    @WorkerThread
    public  LiveData<Recipe> loadRecipe(int recipeId) {
        return recipeDao.loadRecipeById(recipeId);
    }

    @WorkerThread
    public void insertRecipes(final List<Recipe> recipesForInsert){
       // recipeDao.insertRecipe(recipeForInsert);
       appExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               try {
                   recipeDao.insertRecipes(recipesForInsert);
                   Log.d(TAG, "run: Insert");
               }catch (NullPointerException e) {
                   throw new NullPointerException(e + "null Insert");
               }
           }
       });
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
