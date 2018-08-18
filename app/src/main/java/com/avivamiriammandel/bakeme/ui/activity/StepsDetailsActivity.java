package com.avivamiriammandel.bakeme.ui.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.RecipeTypeConverter;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.adaper.RecipeAdapter;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.fragment.DetailsFragment;
import com.avivamiriammandel.bakeme.ui.fragment.StepsDetailsFragment;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepsDetailsActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Context context;
    private List<Recipe> recipeList;
    private Step thisStep;
    private int recipePosition;
    private ImageView recipeImage;
    private LifecycleOwner lifecycleOwner = StepsDetailsActivity.this;
    private RecipeAdapter adapter;
    private static final String TAG = StepsDetailsActivity.class.getSimpleName();
    public static Boolean hasNoDatabase = true;
    private SharedPreferences sharedPreferences;
    private MyApplication myApplication;
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private Service apiService;
    private SharedPreferences.Editor editor;
    private Boolean insertCompleted;
    private BottomNavigationView navigation;
    private int ingredientFragmentId, stepFragmentId;
    private Boolean navRecipes, navIngredients, navSteps,
            addFragment = false, removeIngredientsFragment = false,
            removeStepsFragment = false;
    Toolbar toolbar;
    private final int[] colors = {R.color.bottomtab_0, R.color.bottomtab_1, R.color.bottomtab_2};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);


        Intent fromIntent = getIntent();
        if (fromIntent.hasExtra(getString(R.string.steps_bundle))) {
          thisStep   = fromIntent.getParcelableExtra(getString(R.string.steps_bundle));
          loadStepsDetailsFragment();

        }


    }


    private void loadStepsDetailsFragment() {
        if (getSupportFragmentManager().getFragments().isEmpty()){
            addFragment = true;
        }
        String stepString = StepTypeConverter.stepToString(thisStep);
        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.steps_bundle), stepString);
        StepsDetailsFragment fragment = new StepsDetailsFragment();
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

}


