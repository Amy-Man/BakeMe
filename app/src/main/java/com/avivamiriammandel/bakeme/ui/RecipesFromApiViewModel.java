package com.avivamiriammandel.bakeme.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Client;
import com.avivamiriammandel.bakeme.rest.Service;

import java.util.List;

public class RecipesFromApiViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = RecipesFromApiViewModel.class.getSimpleName();
    private RecipeApiRepository recipeApiRepository;
    private LiveData<List<Recipe>> recipeListFromApi;
    private MutableLiveData<Boolean> recipesReceived = new MutableLiveData<>();
    private Service apiService;


    public RecipesFromApiViewModel(Application application) {
        super(application);
        apiService = Client.getClient().create(Service.class);
        recipeApiRepository = RecipeApiRepository.getInstance(apiService);
        Log.d(TAG, "RecipesFromApiViewModel: + get repository " + recipeApiRepository);
        recipesReceived.setValue(false);
        recipeListFromApi = recipeApiRepository.getRecipes();
        Log.d(TAG, "setRecipesListFromApi: " + recipeListFromApi);


    }


    public LiveData<List<Recipe>> setRecipesListFromApi() {
         recipeListFromApi = recipeApiRepository.getRecipes();
        Log.d(TAG, "setRecipesListFromApi: " + recipeListFromApi);
        return recipeListFromApi;
    }
    public LiveData<List<Recipe>> getRecipesListFromApi () {
        return recipeListFromApi;
    }
    public LiveData<Boolean> getStatus() {
        return  recipesReceived;
    }

}