package com.avivamiriammandel.bakeme.aac;

import android.arch.persistence.room.TypeConverter;

import com.avivamiriammandel.bakeme.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeListTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Recipe> stringToRecipeList(String recipes) {
        if (recipes == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Recipe>>() {}.getType();
        return gson.fromJson(recipes, listType);
    }

    @TypeConverter
    public static String recipesListToString(List<Recipe> recipes){
        return gson.toJson(recipes);
    }
}
