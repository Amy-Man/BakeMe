package com.avivamiriammandel.bakeme.ui.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppDatabase;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.IngredientTypeConverter;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.RecipeListTypeConverter;
import com.avivamiriammandel.bakeme.aac.RecipeTypeConverter;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Client;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.fragment.IngredientsFragment;
import com.avivamiriammandel.bakeme.ui.fragment.RecipesListFragment;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesFromApiViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesInsertViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesInsertViewModelFactory;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesViewModel;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {} representing
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
    private BottomNavigationView navigation;
    private Boolean navRecipes, navIngredients, navSteps, addFragment = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        initViews();

        initDB();

        }




        private void loadRecipesListFragment() {
        if (getSupportFragmentManager().getFragments().isEmpty()) {
            addFragment = true;
    }
        String recipeString =  RecipeListTypeConverter.recipesListToString(recipeListFromDB);
        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.recipes_bundle), recipeString);
        RecipesListFragment fragment = new RecipesListFragment();
        fragment.setArguments(arguments);

        if (addFragment) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment)
                    .commit();
            addFragment = false;
        }else  {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        }
    }


    public void initViews(){
        context = RecipesActivity.this;
        lifecycleOwner = RecipesActivity.this;
        final LiveData<List<Recipe>> recipeList;

        myApplication = new MyApplication();
        recipeDao = AppDatabase.getInstance(context).recipeDao();
        appExecutors = AppExecutors.getInstance();
        apiService = Client.getClient().create(Service.class);
        recipeApiRepository = RecipeApiRepository.getInstance(apiService);
        recipeDBRepository = RecipeDBRepository.getInstance(myApplication,recipeDao,appExecutors );
    }

    public void initDB() {
        if (hasNoDatabase) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Log.d(TAG, "initDB: " + sharedPreferences);
            editor = sharedPreferences.edit();
            if ((sharedPreferences.contains("hasDB")) && (sharedPreferences.getBoolean("hasDB", true))) {
                hasNoDatabase = false;
            }
        }
        Log.d(TAG, "initDB: " + hasNoDatabase);
        if (hasNoDatabase) {
            recipesFromApiViewModel = ViewModelProviders.of
                    (this).get(RecipesFromApiViewModel.class);
            recipesFromApiViewModel.getRecipesListFromApi().observe(this, new Observer<List<Recipe>>() {
                        @Override
                        public void onChanged(@Nullable List<Recipe> recipeList) {
                       Log.d(TAG, "onChanged: recipes" + recipeList);
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
        loadFromDB();
    }

    public void loadFromDB(){
        recipesViewModel = ViewModelProviders.of(RecipesActivity.this).get(RecipesViewModel.class);
        recipesViewModel.getRecipesListFromDB().observe(RecipesActivity.this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeList) {
                recipeListFromDB = recipeList;
                Log.d(TAG, "onCreate: " + recipeListFromDB);
                loadRecipesListFragment();

            }
        });
    }
}


