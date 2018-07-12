package com.avivamiriammandel.bakeme.aac;

import android.arch.lifecycle.LiveData;

import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

public class ListOfRecipesWithBoolean extends Recipe {
    List<Recipe> recipeList;
    Boolean recipesInserted;

    public ListOfRecipesWithBoolean(List<Recipe> recipeList, Boolean recipesInserted) {
        super();
        this.recipeList = recipeList;
        this.recipesInserted = recipesInserted;
    }
}