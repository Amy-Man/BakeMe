package com.avivamiriammandel.bakeme.aac;

import android.arch.persistence.room.TypeConverter;

import com.avivamiriammandel.bakeme.model.Recipe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static Recipe stringToRecipe(String recipes) {
        if (recipes == null) {
            return null;
        }
        Type listType = new TypeToken<Recipe>() {}.getType();
        return gson.fromJson(recipes, listType);
    }

    @TypeConverter
    public static String recipeToString(Recipe recipe){
        return gson.toJson(recipe);
    }
}
