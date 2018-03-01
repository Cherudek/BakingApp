package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_FRAGMENT_TAG;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.gregorio.bakingapp.adapters.StepsAdapter;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 03/12/2017.
 */

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

  private static final String LOG_TAG = StepsFragment.class.getSimpleName();
  private static final String STEPS_ARRAY_KEY = "Steps Array List Key";
  private static final String INGREDIENTS_ARRAY_KEY = "Ingredients Array List Key";
  private static final String RECIPE_MODEL_KEY = "Recipe Model Key";




  // Define a new interface OnStepsClickListener that triggers a callback in the host activity
  OnStepsClickListener mCallbackHostActivity;
  OnBtnIngredientsListClickListener mCallbackHostActivity2;

  private ArrayList<Steps> stepsArrayList;
  private ArrayList<Ingredients> ingredientsArrayList;

  private StepsAdapter stepsAdapter;
  private RecyclerView rvSteps;
  private LinearLayoutManager layoutManager;
  private int numberOfSteps;
  private Context mContext;
  private Steps mStepsData;
  private String mVideoURL;
  private VideoStepFragment videoStepFragment;
  private Boolean isTabletLandscape;
  private Boolean isTabletPortrait;
  private Boolean isPortrait;
  private Button viewIngredients;
  private RecipeModel recipeModel;
  private Bundle bundle;

  public StepsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (savedInstanceState != null) {
      stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_KEY);
      recipeModel = savedInstanceState.getParcelable(RECIPE_MODEL_KEY);

    } else {
      bundle = this.getArguments();
      if (bundle != null) {
        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        recipeModel = bundle.getParcelable(PARCEL_KEY);
        //Getting the corresponding recipe ingredients Array List
        stepsArrayList = recipeModel.getSteps();
        ingredientsArrayList = recipeModel.getIngredients();

      }

    }

    isTabletLandscape = getResources().getBoolean(R.bool.isTabletLand);
    isTabletPortrait = getResources().getBoolean(R.bool.isTabletPortrait);
    isPortrait = getResources().getBoolean(R.bool.use_vertical_layout);

    mContext = getActivity().getApplicationContext();
    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvSteps = rootView.findViewById(R.id.rv_fragment_step_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvSteps.setLayoutManager(layoutManager);
    stepsAdapter = new StepsAdapter(this, numberOfSteps);
    rvSteps.setAdapter(stepsAdapter);
    //Passing the Context and the Steps Array to our StepsAdapter to populate the RecycleView.
    stepsAdapter.setStepsData(stepsArrayList, mContext);

    if (isTabletLandscape) {
      viewIngredients = rootView.findViewById(R.id.btn_view_ingredients);

      // Only on Tablet landscape mode set an onClick listener to navigate ack to the
      // Ingredients fragment
      viewIngredients.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mCallbackHostActivity2.onBtnIngredientList();
        }
      });
    }

    return rootView;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This makes sure that the host activity has implemented the callback interface
    // If not, it throws an exception
    try {
      mCallbackHostActivity = (OnStepsClickListener) context;
      mCallbackHostActivity2 = (OnBtnIngredientsListClickListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnStepsClickListener");
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_KEY);
      recipeModel = savedInstanceState.getParcelable(RECIPE_MODEL_KEY);

    }

  }

  @Override
  public void onClick(int recipeIndex) {
    mCallbackHostActivity.onStepSelected(recipeIndex);

    mStepsData = stepsArrayList.get(recipeIndex);
    mVideoURL = mStepsData.getVideoURL();

  }

  @Override
  public void onResume() {
    super.onResume();


  }

  //Saving the current State of the Recipe Steps Fragment
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(STEPS_ARRAY_KEY, stepsArrayList);
    outState.putParcelable(RECIPE_MODEL_KEY, recipeModel);

  }

  public interface OnBtnIngredientsListClickListener {

    void onBtnIngredientList();
  }

  // OnStepsClickListener interface, calls a method in the host activity named onStepSelected
  public interface OnStepsClickListener {

    void onStepSelected(int position);
  }
}
