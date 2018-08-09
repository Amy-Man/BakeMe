package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

public class RecipeApiRepository {
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RecipeApiRepository sInstance;


    private Service apiService;
    private List<Recipe> recipeListFromApi;

    private RecipeApiRepository(Service apiService) {
        this.apiService = apiService;

    }
    public synchronized static RecipeApiRepository getInstance(
            Service apiService){
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeApiRepository(apiService);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }





    @WorkerThread
    public List<Recipe> getRecipes() {
        apiService = Client.getClient().create(Service.class);
        Log.d(TAG, "getRecipes:  begin getting...");
        Call<List<Recipe>> call = apiService.getRecipes();
        Log.d(TAG, "getRecipes: call "+ call);
        call.enqueue(new Callback<List<Recipe>>() {
                         @Override
                         public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                         Log.d(TAG, "onResponse: " + response);
                         if (response.isSuccessful()) {
                            recipeListFromApi = response.body();
                            Log.d(TAG, "onResponse: " + recipeListFromApi);
                        } else {
                            //recipeListFromApi = null;
                            ApiError apiError = ErrorUtils.parseError(response);
                            Log.d(TAG, "onResponse: error");
                            Log.e(TAG, "" + apiError.getMessage() + " " + apiError.getStatusCode() + " " + apiError.getEndpoint());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        //recipeListFromApi = null;
                        Log.d(TAG, "on Failure" + t.getMessage());
                    }
                });
        Log.d(TAG, "getRecipes: returning from retrofit");
    return recipeListFromApi;
    }
}