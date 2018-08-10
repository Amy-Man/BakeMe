package com.avivamiriammandel.bakeme.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.dummy.DummyContent;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Client;
import com.avivamiriammandel.bakeme.rest.Service;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipesActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;
    public RecipeApiRepository recipeApiRepository;
    public RecipeDBRepository recipeDBRepository;
    private RecipesViewModel recipesViewModel;
    private RecipesFromApiViewModel recipesFromApiViewModel;
    private RecipesInsertViewModel recipesInsertViewModel;
    private Context context;
    private List<Recipe> recipeListFromDB;
    private List<Recipe> recipeListFromApi;
    private LifecycleOwner lifecycleOwner = RecipesActivity.this;
    private RecipeAdapter adapter;
    private static final String TAG = RecipesActivity.class.getSimpleName();
    public static Boolean hasNoDatabase = true;
    private SharedPreferences sharedPreferences;
    private MyApplication myApplication;
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private Service apiService;
    private SharedPreferences.Editor editor;
    private Boolean insertCompleted;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        context = RecipesActivity.this;
        lifecycleOwner = RecipesActivity.this;
        final LiveData<List<Recipe>> recipeList;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        myApplication = new MyApplication();
        recipeDao = AppDatabase.getInstance(context).recipeDao();
        appExecutors = AppExecutors.getInstance();
        apiService = Client.getClient().create(Service.class);
        recipeApiRepository = RecipeApiRepository.getInstance(apiService);
        recipeDBRepository = RecipeDBRepository.getInstance(myApplication,recipeDao,appExecutors );

        if (hasNoDatabase) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = sharedPreferences.edit();
            if (sharedPreferences.contains("hasDB")) {
                hasNoDatabase = false;
            }
        }

 /*       FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        final RecyclerView recyclerView = findViewById(R.id.recipe_list);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (hasNoDatabase) {
            recipesFromApiViewModel = ViewModelProviders.of
                    (this).get(RecipesFromApiViewModel.class);
            recipesFromApiViewModel.getRecipesListFromApi().observe(this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(@Nullable List<Recipe> recipeList) {
                           if (recipeList != null) {
                               recipeListFromApi = recipeList;
                                Log.d(TAG, "onChanged: " + recipeListFromApi);
                                RecipesInsertViewModelFactory modelFactory
                                        = new RecipesInsertViewModelFactory(getApplication(), recipeListFromApi);
                                recipesInsertViewModel = ViewModelProviders.of(RecipesActivity.this, modelFactory)
                                        .get(RecipesInsertViewModel.class);

                                recipesInsertViewModel.InsertTheRecipes(recipeListFromApi).observe
                                        (RecipesActivity.this, new Observer<Boolean>() {
                                            @Override
                                            public void onChanged(@Nullable Boolean insertCompleted) {
                                                hasNoDatabase = false;
                                                editor = sharedPreferences.edit();
                                                editor.putBoolean("hasDB", true).apply();
                                                Log.d(TAG, "onCreate: " + insertCompleted);

                                            }
                                        });
                            }
                        }
                    });

                 }
                    recipesViewModel = ViewModelProviders.of(RecipesActivity.this).get(RecipesViewModel.class);
                    recipesViewModel.getRecipesListFromDB().observe(RecipesActivity.this, new Observer<List<Recipe>>() {
                        @Override
                        public void onChanged(@Nullable List<Recipe> recipeList) {
                            recipeListFromDB = recipeList;
                            Log.d(TAG, "onCreate: " + recipeListFromDB);
                            adapter = new RecipeAdapter(context, recipeListFromDB);
                            recyclerView.setAdapter(adapter);
                        }
                    });


    }
}


