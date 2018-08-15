/*
package com.avivamiriammandel.bakeme.ui.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.IngredientTypeConverter;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.RecipeTypeConverter;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.glide.GlideApp;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.activity.DetailsActivity;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.List;

public class DetailsFragment extends Fragment {

    private boolean mTwoPane;
    private Context context;
    private List<Recipe> recipes;
    private Recipe thisRecipe;
    private int recipePosition;
    private String recipeString;
    private ImageView recipeImage;
    private LifecycleOwner lifecycleOwner = DetailsFragment.this;
    private RecipeAdapter adapter;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    public static Boolean hasNoDatabase = true;
    private SharedPreferences sharedPreferences;
    private MyApplication myApplication;
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private Service apiService;
    private SharedPreferences.Editor editor;
    private Boolean insertCompleted;
    private BottomNavigationView navigation;
    private AHBottomNavigation bottomNavigation;
    private Boolean navRecipes, navIngredients, navSteps,
            addFragment = false, removeIngredientsFragment = false,
            removeStepsFragment = false;
    Toolbar toolbar;


    public DetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.baking_lists, container, false);
        Bundle bundle = this.getArguments();
        Log.d(TAG, "onCreateView: " + bundle);
        if (bundle != null) {
            thisRecipe = bundle.getParcelable(bundle.getString(getString(R.string.recipe_bundle)));
            context = DetailsFragment.this.getContext();
            navigation = rootView.findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            toolbar = rootView.findViewById(R.id.toolbar);
            recipeImage = rootView.findViewById(R.id.recipe_image);


            toolbar.setTitle(thisRecipe.getName());

            loadImage();


            navRecipes = true;
            navIngredients = false;
            navSteps = false;

        }
        return rootView;
    }


    private void loadImage() {

        try {
            GlideApp.with(context)
                    .load(thisRecipe.getImage())
                    .listener(GlidePalette.with(thisRecipe.getImage())
                            .use(GlidePalette.Profile.MUTED)
                            .intoBackground(toolbar)
                            .crossfade(true)
                    )
                    .placeholder(R.drawable.cake_loading)
                    .error(R.drawable.cake_error)
                    .into(recipeImage);
        } catch (final IllegalArgumentException e) {
            Log.e(TAG, getString(R.string.on_bind_view_holder) + e.getMessage());
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setElevation(10.f);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recipes:
                    unLoadIngredientsFragments();
                    unLoadStepsFragments();
                    navRecipes = true;
                    navIngredients = false;
                    navSteps = false;
                    return true;
                case R.id.navigation_ingredients:
                    loadIngredientsFragment();
                    navRecipes = false;
                    navIngredients = true;
                    navSteps = false;
                    return true;
                case R.id.navigation_steps:
                    loadStepsFragment();
                    navRecipes = false;
                    navIngredients = false;
                    navSteps = true;
                    return true;
                default:
                    return true;
            }
        }
    };


    private void unLoadIngredientsFragments() {
        */
/*if (getSupportFragmentManager().findFragmentByTag(getString(R.string.ingredients_bundle)) != null)
            removeIngredientsFragment = true;

        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        if (removeIngredientsFragment) {
            getSupportFragmentManager().beginTransaction()
                    .remove(ingredientsFragment)
                    .commit();
            removeIngredientsFragment = false;
        }*//*

    }
    private void unLoadStepsFragments() {

        */
/*if (getSupportFragmentManager().findFragmentByTag(getString(R.string.steps_bundle)) != null)
            removeStepsFragment = true;

        StepsFragment stepsFragment = new StepsFragment();

        if (removeStepsFragment) {
            getSupportFragmentManager().beginTransaction()
                    .remove(stepsFragment)
                    .commit();
            removeStepsFragment = false;*//*

        }

       private void loadIngredientsFragment() {
        unLoadStepsFragments();
        */
/*if ((getSupportFragmentManager().findFragmentByTag(getString(R.string.ingredients_bundle)) == null)
                && (getSupportFragmentManager().findFragmentByTag(getString(R.string.steps_bundle)) == null)
                && (getSupportFragmentManager().findFragmentByTag(getString(R.string.recipes_bundle)) == null)) {
            addFragment = true;
        }*//*

        String ingredientString =  IngredientTypeConverter.ingredientListToString(thisRecipe.getIngredients());
        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.ingredients_bundle), ingredientString);
        arguments.putString(getString(R.string.recipe_name_bundle), thisRecipe.getName());
        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(arguments);

        */
/*if (addFragment) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment)
                    .commit();
            addFragment = false;
        }else  {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        }*//*

    }
    private void loadStepsFragment() {
        unLoadIngredientsFragments();
        */
/*if ((getSupportFragmentManager().findFragmentByTag(getString(R.string.ingredients_bundle)) == null)
                && (getSupportFragmentManager().findFragmentByTag(getString(R.string.steps_bundle)) == null)
                && (getSupportFragmentManager().findFragmentByTag(getString(R.string.recipes_bundle)) == null)) {
            addFragment = true;
        }
*//*

        String stepString =  StepTypeConverter.stepListToString(thisRecipe.getSteps());
        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.steps_bundle), stepString);
        arguments.putString(getString(R.string.recipe_name_bundle), thisRecipe.getName());
        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(arguments);
*/
/*

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
*//*


    }
}

*/
