package com.avivamiriammandel.bakeme.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.StepListTypeConverter;
import com.avivamiriammandel.bakeme.aac.StepTypeConverter;
import com.avivamiriammandel.bakeme.adaper.StepDetailAdapter;
import com.avivamiriammandel.bakeme.adaper.StepsDetailsAdapter;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.rest.Service;

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
    private List<Step> steps;
    private Step thisStep;
    private String position;
    private int recipePosition;
    private ImageView recipeImage;
    private LifecycleOwner lifecycleOwner = StepsDetailsActivity.this;
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
    StepsDetailsAdapter adapter;
    ViewPager viewPager;
    String stringSteps, stepsString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_details);


        Intent fromIntent = getIntent();
        if (fromIntent.hasExtra(getString(R.string.steps_bundle))) {
          stepsString   = fromIntent.getStringExtra(getString(R.string.steps_bundle));
          steps = StepListTypeConverter.stringToStepList(stepsString);
          position = fromIntent.getStringExtra(getString(R.string.steps_position));
          context = getApplicationContext();
          loadViewPager();

        }


    }


    private void loadViewPager() {
       /* if (getSupportFragmentManager().getFragments().isEmpty()){
            addFragment = true;
        }
       */

        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.steps_position), position);
//        arguments.putParcelable(getString(R.string.steps_bundle), (Parcelable) steps);
        stringSteps = StepListTypeConverter.stepListToString(steps);
        //arguments.putString(getString(R.string.bundle_activity), String.valueOf(StepsDetailsActivity.class));
        // intent.putExtra(getString(R.string.steps_position), position);
        arguments.putString(getString(R.string.steps_bundle), stringSteps);



        viewPager = findViewById(R.id.view_pager);
        adapter = new StepsDetailsAdapter(context, arguments, StepsDetailsActivity.this);
        viewPager.setAdapter(adapter);


        /*if (addFragment) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, fragment)
                    .commit();
            addFragment = false;
        }else  {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        }*/
    }

}


