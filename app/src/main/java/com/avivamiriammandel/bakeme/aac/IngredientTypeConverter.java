package com.avivamiriammandel.bakeme.aac;

import android.arch.persistence.room.TypeConverter;

import com.avivamiriammandel.bakeme.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class IngredientTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String ingredients) {
        if (ingredients == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredients, listType);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredients){
        return gson.toJson(ingredients);
    }
}
