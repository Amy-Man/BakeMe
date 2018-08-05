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
    private final MutableLiveData<List<Recipe>> recipeListFromApi = new MutableLiveData<>();
    private Integer id;
    private List<Recipe> recipeList;
    private LiveData<Recipe> recipeWithLiveData;
    private Recipe recipeForInsertOrDelete;
    private List<Recipe> recipesForInsert;
    public MutableLiveData<Boolean> recipesInserted = new MutableLiveData<>();
    int count = 0;


   public RecipeRepository(Application application, List<Recipe> recipesForInsert) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.recipeDao = db.recipeDao();
        this.recipeListFromDB = recipeDao.loadAllRecipes();
        this.recipeWithLiveData = recipeDao.loadRecipeById(id);
        this.recipesForInsert = recipesForInsert;

    }

    public RecipeRepository(Application application, Recipe recipeForInsertOrDelete) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.recipeDao = db.recipeDao();
        this.recipeListFromDB = recipeDao.loadAllRecipes();
        this.recipeWithLiveData = recipeDao.loadRecipeById(id);
        this.recipeForInsertOrDelete = recipeForInsertOrDelete;
    }


    public RecipeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.recipeDao = db.recipeDao();
        this.recipeListFromDB = recipeDao.loadAllRecipes();
    }

    public LiveData<List<Recipe>> loadAllRecipes() {
        return recipeDao.loadAllRecipes();
    }

    public  LiveData<Recipe> loadRecipe(int id) {
        return recipeDao.loadRecipeById(id);
    }

    public void insertRecipe(final Recipe recipeForInsert){
       // recipeDao.insertRecipe(recipeForInsert);
       AppExecutors.getInstance().diskIO().execute(new Runnable() {
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
    public LiveData<Boolean> insertListOfRecipes(final List<Recipe> recipesForInsert){
       // recipeDao.insertRecipe(recipeForInsert);
        recipesInserted.setValue(false);

       AppExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               try {
                   List<Long> recipesInsertedLong = recipeDao.insertRecipes(recipesForInsert);
                   for (Long recipeInserted:recipesInsertedLong){
                       if (recipeInserted != -1)
                       count++;
                        }
                   if (count == recipesForInsert.size())
                       recipesInserted.postValue(true);
                   Log.d(TAG, "run: Insert all");
                   }catch (NullPointerException e) {
                   throw new NullPointerException(e + "null Insert");
               }
           }
       });
       return recipesInserted;
    }
    public void deleteRecipe(final Recipe recipeForDelete){
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
    }

    public LiveData<List<Recipe>> getRecipesFromApi() {
        final Service apiService = Client.getClient().create(Service.class);
        final Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipeListFromApi.setValue(response.body());
                    Log.d(TAG, "onResponse: " + recipeListFromApi);
                    if (recipeListFromApi == null) {
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
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
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
