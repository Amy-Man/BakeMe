package com.avivamiriammandel.bakeme.aac;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
    private Service apiService;

    private static class SingletonHelper {

        private static final RecipeApiRepository INSTANCE = new RecipeApiRepository();
    }

    public static RecipeApiRepository getInstance() {

        return SingletonHelper.INSTANCE;
    }

    public RecipeApiRepository() {

        apiService = Client.getClient().create(Service.class);
    }

    public LiveData<List<Recipe>> getRecipes() {

        final MutableLiveData<List<Recipe>> recipeListFromApi = new MutableLiveData<>();
        apiService.getRecipes()
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        if (response.isSuccessful()) {
                            recipeListFromApi.setValue(response.body());
                            Log.d(TAG, "onResponse: " + recipeListFromApi);
                        } else {
                            recipeListFromApi.setValue(null);
                            ApiError apiError = ErrorUtils.parseError(response);
                            Log.e(TAG, "" + apiError.getMessage() + " " + apiError.getStatusCode() + " " + apiError.getEndpoint());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        recipeListFromApi.setValue(null);
                        Log.e(TAG, "on Failure" + t.getMessage());
                    }
                });
    return recipeListFromApi;
    }
}