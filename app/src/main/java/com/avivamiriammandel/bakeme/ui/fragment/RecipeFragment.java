package com.avivamiriammandel.bakeme.ui.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.RecipeListTypeConverter;
import com.avivamiriammandel.bakeme.aac.RecipeTypeConverter;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.activity.DetailsActivity;
import com.avivamiriammandel.bakeme.ui.activity.RecipesActivity;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesFromApiViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesInsertViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesViewModel;

import java.util.List;

public class RecipeFragment extends Fragment {
    private List<Recipe> recipes;
    public RecipeApiRepository recipeApiRepository;
    public RecipeDBRepository recipeDBRepository;
    private RecipesViewModel recipesViewModel;
    private RecipesFromApiViewModel recipesFromApiViewModel;
    private RecipesInsertViewModel recipesInsertViewModel;
    private Context context;
    private List<Recipe> recipeListFromDB;
    private List<Recipe> recipeListFromApi;
    private LifecycleOwner lifecycleOwner = RecipeFragment.this;
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
    public RecipeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        Bundle bundle = this.getArguments();
        Log.d(TAG, "onCreateView: " + bundle);
        if (bundle != null) {
            recipes = RecipeListTypeConverter.stringToRecipeList(bundle.getString(getString(R.string.recipes_bundle)));
            context = RecipeFragment.this.getContext();
            Log.d(TAG, "onCreateView: bundle = " + recipes);
            final RecyclerView recyclerView = rootView.findViewById(R.id.recycler_fragment);


            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new RecipeAdapter(context, recipes);
            recyclerView.setAdapter(adapter);

            return rootView;
        } else {
            throw new NullPointerException(TAG);
        }
    }
}
