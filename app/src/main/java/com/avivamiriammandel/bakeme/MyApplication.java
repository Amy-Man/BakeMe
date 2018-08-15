package com.avivamiriammandel.bakeme;


import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    /*RecipeDao recipeDao = AppDatabase.getInstance(getApplicationContext()).recipeDao();
    AppExecutors appExecutors =  AppExecutors.getInstance();
*/
    public void onCreate() {
        super.onCreate();
        Context context = getBaseContext();

        Stetho.initializeWithDefaults(this);

    /*    getApiRepositoryInstance();
        getDBRepositoryInstance();

    }

    public RecipeDBRepository getDBRepositoryInstance() {
        return RecipeDBRepository.getInstance(MyApplication.this, recipeDao, appExecutors);
    }
    public RecipeApiRepository getApiRepositoryInstance() {
        return RecipeApiRepository.getInstance();*/
    }

}
