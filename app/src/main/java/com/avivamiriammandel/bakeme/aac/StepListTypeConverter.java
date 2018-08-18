package com.avivamiriammandel.bakeme.aac;

import android.arch.persistence.room.TypeConverter;

import com.avivamiriammandel.bakeme.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StepListTypeConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Step> stringToStepList(String steps) {
        if (steps == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Step>>() {}.getType();
        return gson.fromJson(steps, listType);
    }

    @TypeConverter
    public static String stepListToString(List<Step> steps){
        return gson.toJson(steps);
    }
}
