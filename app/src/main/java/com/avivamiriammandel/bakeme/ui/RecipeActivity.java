package com.avivamiriammandel.bakeme.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.RecipeInsertOrDeleteViewModel;
import com.avivamiriammandel.bakeme.aac.RecipeInsertOrDeleteViewModelFactory;
import com.avivamiriammandel.bakeme.aac.RecipeListViewModel;
import com.avivamiriammandel.bakeme.aac.RecipesInsertViewModel;
import com.avivamiriammandel.bakeme.aac.RecipesInsertViewModelFactory;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.dummy.DummyContent;
import com.avivamiriammandel.bakeme.model.Recipe;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;
    private RecipeListViewModel recipeListViewModel;
    private RecipeInsertOrDeleteViewModel recipeInsertOrDeleteViewModel;
    private Context context;
    private List<Recipe> recipeOneList;
    private LifecycleOwner lifecycleOwner;
    private RecipeAdapter adapter;
    private static final String TAG = RecipeActivity.class.getSimpleName();
    public Boolean recipesInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        context = RecipeActivity.this;
        lifecycleOwner = RecipeActivity.this;
        final LiveData<List<Recipe>> recipeList;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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

///this line should be in try catch
        try {
            recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        } catch (NullPointerException e) {
            throw new NullPointerException(e + "null");
        }
         recipeListViewModel.getRecipesListFromApi().observe(lifecycleOwner, new Observer<List<Recipe>>() {
             @Override
             public void onChanged(@Nullable List<Recipe> recipes) {
                 Log.d(TAG, "onChanged: "+ recipes);
                 if (recipes != null) {
                      RecipesInsertViewModelFactory modelFactory =
                                 new RecipesInsertViewModelFactory(getApplication(), recipes);
                     RecipesInsertViewModel recipesInsertViewModel =
                                  ViewModelProviders.of(RecipeActivity.this, modelFactory).get(RecipesInsertViewModel.class);
                     recipesInsertViewModel.InsertListOfRecipes().observe(lifecycleOwner, new Observer<Boolean>() {
                         @Override
                         public void onChanged(@Nullable Boolean recipesInserted) {
                             if (recipesInserted) {
                                 RecipeListViewModel recipeListViewModelDB =
                                         ViewModelProviders.of(RecipeActivity.this).get(RecipeListViewModel.class);
                                 recipeListViewModelDB.getRecipesListFromDB().observe(lifecycleOwner, new Observer<List<Recipe>>() {
                                     @Override
                                     public void onChanged(@Nullable List<Recipe> recipes1) {
                                         adapter = new RecipeAdapter(context, recipes1);
                                         recyclerView.setAdapter(adapter);
                                     }
                                 });
                             }
                         }
                     });

                 }
             }
         });


    }
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                //        if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.id);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);

            }
        };

    }

