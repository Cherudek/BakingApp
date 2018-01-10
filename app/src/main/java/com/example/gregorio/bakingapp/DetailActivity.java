package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gregorio.bakingapp.StepsFragment.OnStepsClickListener;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class DetailActivity extends AppCompatActivity implements OnStepsClickListener {

  public static final String LOG_TAG = DetailActivity.class.getSimpleName();
  public static final String VIDEO_KEY_BUNDLE = "Video URL bundle";
  public static final String DESCRIPTION_KEY_BUNDLE = "Description bundle";
  public static final String STEP_PARCEL_KEY = "Step Parcel Key";
  public static final String RECIPE_NAME_KEY = "Recipe Name";
  public static final String STEPS_ARRAY_LIST_KEY = "Steps Array List Key";
  public static final String INGREDIENTS_ARRAY_LIST_KEY = "Ingredients Array List Key";
  public static final String VIDEO_ID_BUNDLE = "Video ID Key";
  public static final String STEPS_SIZE_BUNDLE = "Steps Size Key";
  public static final String STEP_FRAGMENT_TAG = "Step Fragment Tag";
  public static final String INGREDIENTS_FRAGMENT_TAG = "Ingredients Fragment Tag";
  public static final String VIDEO_FRAGMENT_TAG = "Video Fragment Tag";




  private String mVideoUrl;
  private String mLongDescription;
  private int mId;
  private Bundle videoUrlBundle;
  private String mRecipeName;
  private FragmentManager fragmentManager;
  private ArrayList<Steps> mStepsArrayList;
  private ArrayList<Ingredients> mIngredientsArrayList;


  private TextView ingredientsLabel;
  private FrameLayout ingredientsFrame;
  private FrameLayout stepsFrame;
  private TextView stepsLabel;
  private StepsFragment stepsFragment;
  private IngredientsFragment ingredientsFragment;

  private Steps steps;
  private Intent intent;
  private Bundle parcelable;
  private Bundle onDestroyBundle;
  private Button viewSteps;

  private VideoStepFragment videoStepFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ingredientsLabel = findViewById(R.id.ingredient_label);
    ingredientsFrame = findViewById(R.id.details_container);
    stepsLabel = findViewById(R.id.steps_label);

    viewSteps = findViewById(R.id.btn_view_steps);
    viewSteps.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        viewRecipeSteps();
      }
    });


    if (savedInstanceState != null) {
      mRecipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
      mStepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_LIST_KEY);
      mIngredientsArrayList = savedInstanceState.getParcelableArrayList(INGREDIENTS_ARRAY_LIST_KEY);

      Log.i(LOG_TAG, "Recipe Name Key Saved:  " + mRecipeName);
      Log.i(LOG_TAG, "OnSavedInstance bundle Steps Array Size: " + mStepsArrayList);
      Log.i(LOG_TAG, "OnSavedInstance bundle Ingredients Array Size: " + mIngredientsArrayList);
    } else {

      // This method will launch  set up the Ingredients recyclerView and the View recipe Steps Button.
      fragmentSetUp();
    }
  }

  // This method will launch the recipe steps fragment that will replace the Ingredients one
  // in the Detail Activity
  private void viewRecipeSteps() {

    //Instantiating the Steps Fragment
    stepsFragment = new StepsFragment();
    //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
    intent = getIntent();
    parcelable = intent.getBundleExtra(INTENT_KEY);
    RecipeModel recipeModel = parcelable.getParcelable(PARCEL_KEY);
    mStepsArrayList = recipeModel.getSteps();

    //Setting an Intent Bundle to the Steps Fragment
    stepsFragment.setArguments(parcelable);
    fragmentManager = getSupportFragmentManager();
    // provide compatibility to all the versions
    getSupportActionBar().setTitle(mRecipeName);

    fragmentManager.beginTransaction()
        .replace(R.id.details_container, stepsFragment, STEP_FRAGMENT_TAG)
        .addToBackStack(null)
        .commit();

    viewSteps.setVisibility(View.INVISIBLE);

  }

  // This method will launch the set up of the Ingredients recyclerView and the View recipe Steps Button.

  public void fragmentSetUp() {

    //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
    intent = getIntent();
    parcelable = intent.getBundleExtra(INTENT_KEY);

    RecipeModel recipeModel = parcelable.getParcelable(PARCEL_KEY);
    mIngredientsArrayList = recipeModel.getIngredients();
    mStepsArrayList = recipeModel.getSteps();
    mRecipeName = recipeModel.getName();

    //Instantiate the IngredientsFragment
    ingredientsFragment = new IngredientsFragment();

    //Set the Bundle to the IngredientsFragment
    ingredientsFragment.setArguments(parcelable);

    fragmentManager = getSupportFragmentManager();

    // provide compatibility to all the versions
    getSupportActionBar().setTitle(mRecipeName);

    fragmentManager.beginTransaction()
        .add(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
        .commit();

    viewSteps.setVisibility(View.VISIBLE);

  }


  @Override
  public void onRecipeSelected(int position) {

    steps = mStepsArrayList.get(position);
    mVideoUrl = steps.getVideoURL();
    mLongDescription = steps.getDescription();
    mId = steps.getId();
    int stepsSize = mStepsArrayList.size();

    videoUrlBundle = new Bundle();
    videoUrlBundle.putString(VIDEO_KEY_BUNDLE, mVideoUrl);
    videoUrlBundle.putString(DESCRIPTION_KEY_BUNDLE, mLongDescription);
    videoUrlBundle.putInt(VIDEO_ID_BUNDLE, mId);
    videoUrlBundle.putInt(STEPS_SIZE_BUNDLE, stepsSize);
    videoUrlBundle.putParcelableArrayList(STEPS_ARRAY_LIST_KEY, mStepsArrayList);

    Log.d(LOG_TAG, "My Video Url is : " + mVideoUrl);
    Log.d(LOG_TAG, "My Long Description is : " + mLongDescription);
    Log.d(LOG_TAG, "My Video ID is : " + mId);


    //Instantiating the Video & Long description  Fragment
    videoStepFragment = new VideoStepFragment();
    if (videoUrlBundle != null) {
      videoStepFragment.setArguments(videoUrlBundle);
    }

    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.details_container, videoStepFragment)
        .addToBackStack(null)
        .commit();

    viewSteps.setVisibility(View.INVISIBLE);

    //videoFragmentView();
  }


  public void resumeTwoFragmentView() {

    //Check Phone Orientation
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

      //Setting the horizontal View of 2 Fragments with weight 1
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          0, LayoutParams.MATCH_PARENT, 1);
      ingredientsFrame.setLayoutParams(params);

      LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
          0, LayoutParams.MATCH_PARENT, 1);
      stepsFrame.setLayoutParams(paramsStepContainer);

    } else {

      //If the Phone is in Portrait Mode
      //Set the 2 fragments with equal weight 1
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LayoutParams.MATCH_PARENT, 0, 1);
      ingredientsFrame.setLayoutParams(params);

      LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
          LayoutParams.MATCH_PARENT, 0, 1);
      stepsFrame.setLayoutParams(paramsStepContainer);
    }
  }

  public void videoFragmentView() {

    ingredientsFrame = findViewById(R.id.details_container);

    //Setting the Ingredients frame  as the only one visible and Hiding the Steps Frame
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0f);

    ingredientsFrame.setLayoutParams(params);
//    stepsFrame.setLayoutParams(paramsStepContainer);
//    stepsFrame.setVisibility(View.INVISIBLE);

    //Landscape Layout
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
          ViewGroup.LayoutParams.MATCH_PARENT, 0);

      //stepsFrame.setLayoutParams(params2);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    getSupportActionBar().setTitle(mRecipeName);

    if (ingredientsFrame != null && ingredientsFrame.isInLayout()) {
      viewSteps.setVisibility(View.VISIBLE);
    }


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();

    //resumeTwoFragmentView();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(STEPS_ARRAY_LIST_KEY, mStepsArrayList);
    outState.putParcelableArrayList(INGREDIENTS_ARRAY_LIST_KEY, mIngredientsArrayList);
    outState.putParcelable(STEP_PARCEL_KEY, steps);
    outState.putString(RECIPE_NAME_KEY, mRecipeName);
    int stepsArraySize = mStepsArrayList.size();
    int ingredientsArraySize = mIngredientsArrayList.size();

    Log.i(LOG_TAG, "Recipe Name Key Saved:  " + mRecipeName);
    Log.i(LOG_TAG, "OnSavedInstance bundle Steps Array Size: " + stepsArraySize);
    Log.i(LOG_TAG, "OnSavedInstance bundle Ingredients Array Size: " + ingredientsArraySize);

  }
}
