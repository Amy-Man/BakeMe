package com.avivamiriammandel.bakeme.error;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.avivamiriammandel.bakeme.model.Recipe;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import static com.avivamiriammandel.bakeme.rest.Client.getClient;


/**
 * Created by aviva.miriam on 25 מרץ 2018.
 */

public class ErrorUtils {
    public static ApiError parseError(@NonNull Response<MutableLiveData<List<Recipe>>> response) {
        Converter<ResponseBody, ApiError> converter =
                getClient()
                        .responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
            if (null != response.errorBody()) {
                error = converter.convert(response.errorBody());
                return error;
            }
        } catch (IOException e) {
            return new ApiError();
        }
        return new ApiError();
    }
}
