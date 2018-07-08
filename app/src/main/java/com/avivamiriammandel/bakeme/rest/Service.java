package com.avivamiriammandel.bakeme.rest;

import android.arch.lifecycle.LiveData;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    String api_path = "topher/2017/May/59121517_baking/baking.json";
    @GET(api_path)
    Call<List<Recipe>> getRecipes();

}
