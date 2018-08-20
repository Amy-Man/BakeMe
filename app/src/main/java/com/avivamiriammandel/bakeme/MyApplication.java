package com.avivamiriammandel.bakeme;


import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    /*RecipeDao recipeDao = AppDatabase.getInstance(getApplicationContext()).recipeDao();
    AppExecutors appExecutors =  AppExecutors.getInstance();
*/


        private HttpProxyCacheServer proxy;

        public static HttpProxyCacheServer getProxy(Context context) {
            MyApplication app = (MyApplication) context.getApplicationContext();
            return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
        }

        private HttpProxyCacheServer newProxy() {
            return new HttpProxyCacheServer(this);
        }


    public void onCreate() {
        super.onCreate();
        Context context = getBaseContext();

        Stetho.initializeWithDefaults(this);

    }



    /*    getApiRepositoryInstance();
        getDBRepositoryInstance();

    }

    public RecipeDBRepository getDBRepositoryInstance() {
        return RecipeDBRepository.getInstance(MyApplication.this, recipeDao, appExecutors);
    }
    public RecipeApiRepository getApiRepositoryInstance() {
        return RecipeApiRepository.getInstance();*/
    }

