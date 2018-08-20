package com.avivamiriammandel.bakeme.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avivamiriammandel.bakeme.MyApplication;
import com.avivamiriammandel.bakeme.R;
import com.avivamiriammandel.bakeme.aac.AppExecutors;
import com.avivamiriammandel.bakeme.aac.RecipeApiRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDBRepository;
import com.avivamiriammandel.bakeme.aac.RecipeDao;
import com.avivamiriammandel.bakeme.aac.StepListTypeConverter;
import com.avivamiriammandel.bakeme.adaper.StepAdapter;
import com.avivamiriammandel.bakeme.model.Recipe;
import com.avivamiriammandel.bakeme.model.Step;
import com.avivamiriammandel.bakeme.rest.Service;
import com.avivamiriammandel.bakeme.ui.activity.RecipesActivity;
import com.avivamiriammandel.bakeme.ui.activity.StepsDetailsActivity;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesFromApiViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesInsertViewModel;
import com.avivamiriammandel.bakeme.ui.viewmodel.RecipesViewModel;

import java.util.List;

@SuppressLint("ParcelCreator")
public class StepsFragment extends Fragment implements Parcelable {
    private List<Recipe> recipes;
    public RecipeApiRepository recipeApiRepository;
    public RecipeDBRepository recipeDBRepository;
    private RecipesViewModel recipesViewModel;
    private RecipesFromApiViewModel recipesFromApiViewModel;
    private RecipesInsertViewModel recipesInsertViewModel;
    private Context context;
    private List<Step> steps;
    private String stringSteps;
    private List<Recipe> recipeListFromApi;
    private LifecycleOwner lifecycleOwner = StepsFragment.this;
    private StepAdapter adapter;
    private static final String TAG = RecipesActivity.class.getSimpleName();
    public static Boolean hasNoDatabase = true;
    private SharedPreferences sharedPreferences;
    private MyApplication myApplication;
    private RecipeDao recipeDao;
    private AppExecutors appExecutors;
    private Service apiService;
    private SharedPreferences.Editor editor;
    private Boolean insertCompleted;
    private Activity callingActivity;
    public StepsFragment() {
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
        if (bundle != null) {
        steps = StepListTypeConverter.stringToStepList(bundle.getString(getString(R.string.steps_bundle)));
        context = StepsFragment.this.getContext();
        final RecyclerView recyclerView = rootView.findViewById(R.id.recycler_fragment);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StepAdapter(context, steps);

        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(false);


            adapter.setOnItemClickListener(new StepAdapter.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(context, StepsDetailsActivity.class);
                    stringSteps = StepListTypeConverter.stepListToString(steps);
                    callingActivity = getActivity();
                    //intent.putExtra(getString(R.string.bundle_activity), callingActivity);
                    intent.putExtra(getString(R.string.steps_position), position);
                    intent.putExtra(getString(R.string.steps_bundle), stringSteps);
                    context.startActivity(intent);
                }
            });
          return rootView;
        } else {
            throw new NullPointerException(TAG);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.recipes);
        dest.writeTypedList(this.steps);
        dest.writeString(this.stringSteps);
        dest.writeTypedList(this.recipeListFromApi);
        dest.writeValue(this.insertCompleted);
        dest.writeValue(this.callingActivity);
    }
}
