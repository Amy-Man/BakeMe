package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class RecipeRepository {
    public Boolean hasNoRecipes = false;
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> recipeListFromDB;
    private MutableLiveData<List<Recipe>> recipeListFromApi;
    private Integer id;
    private List<Recipe> recipeList;
    private LiveData<Recipe> recipeWithLiveData;
    private Recipe recipeForInsertOrDelete;

    RecipeRepository(Application application, Recipe recipe) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.recipeDao = db.recipeDao();
        this.recipeListFromApi = getRecipesFromApi();
        this.recipeListFromDB = recipeDao.loadAllRecipes();
        this.recipeWithLiveData = recipeDao.loadRecipeById(id);
        this.recipeForInsertOrDelete = recipe;
    }

    RecipeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.recipeDao = db.recipeDao();
        this.recipeListFromApi = getRecipesFromApi();
        this.recipeListFromDB = recipeDao.loadAllRecipes();
    }

    public LiveData<List<Recipe>> loadAllRecipes() {
        return recipeDao.loadAllRecipes();
    }

    public  LiveData<Recipe> loadRecipe(int id) {
        return recipeDao.loadRecipeById(id);
    }

    public void insertRecipe(final Recipe recipeForInsert){
       AppExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               recipeDao.insertRecipe(recipeForInsert);
               Log.d(TAG, "run: Insert" );
           }
       });

    }
    public void deleteRecipe(final Recipe recipeForDelete){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.deleteRecipe(recipeForDelete);
            }
        });

    }

    public MutableLiveData<List<Recipe>> getRecipesFromApi() {
        final Service apiService = Client.getClient().create(Service.class);
        final Call<MutableLiveData<List<Recipe>>> call = apiService.getRecipes();
        call.enqueue(new Callback<MutableLiveData<List<Recipe>>>() {
            @Override
            public void onResponse(Call<MutableLiveData<List<Recipe>>> call, Response<MutableLiveData<List<Recipe>>> response) {
                if (response.isSuccessful()) {
                    recipeListFromApi = response.body();
                    Log.d(TAG, "onResponse: " + recipeList);
                    if (recipeList == null) {
                        hasNoRecipes = true;
                        ApiError apiError = ErrorUtils.parseError(response);
                        Log.e(TAG, "" + apiError.getMessage() + " " + apiError.getStatusCode() + " " + apiError.getEndpoint());
                    } else {
                        hasNoRecipes = false;
                    }
                    Log.d(TAG, "onResponse1: " + hasNoRecipes);
                }

            }

            @Override
            public void onFailure(Call<MutableLiveData<List<Recipe>>> call, Throwable t) {
                hasNoRecipes = true;
                Log.d(TAG, "on Failure" + t.getMessage());
            }

        });
        if (hasNoRecipes)
            return null;
        else
            return recipeListFromApi;
    }
}
