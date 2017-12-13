package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gregorio.bakingapp.StepsFragment.OnStepsClickListener;
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


  private String mVideoUrl;
  private String mLongDescription;
  private Bundle videoUrlBundle;
  private String mRecipeName;
  private VideoStepFragment videoStepFragment;
  private FragmentManager fragmentManager;
  private ArrayList<Steps> mStepsArrayList;

  private TextView ingredientsLabel;
  private FrameLayout ingredientsFrame;
  private FrameLayout stepsFrame;
  private LinearLayout detailContainer;
  private TextView stepsLabel;
  private StepsFragment stepsFragment;
  private IngredientsFragment ingredientsFragment;

  private Steps steps;
  private Intent intent;
  private Bundle parcelable;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    if (savedInstanceState != null) {
      mRecipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
      steps = savedInstanceState.getParcelable(STEP_PARCEL_KEY);
    }

      //Instantiate the IngredientsFragment
      ingredientsFragment = new IngredientsFragment();
      fragmentManager = getSupportFragmentManager();

      //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
    intent = getIntent();
    parcelable = intent.getBundleExtra(INTENT_KEY);

      RecipeModel recipeModel = parcelable.getParcelable(PARCEL_KEY);
      mStepsArrayList = recipeModel.getSteps();
      mRecipeName = recipeModel.getName();

      // provide compatibility to all the versions
      getSupportActionBar().setTitle(mRecipeName);

      Log.d(LOG_TAG, "My Recipe name is : " + mRecipeName);

      //Set the Bundle to the IngredientsFragment
      ingredientsFragment.setArguments(parcelable);

      fragmentManager.beginTransaction()
          .add(R.id.ingredients_container, ingredientsFragment)
          .commit();

      //Instantiating the Steps Fragment
      stepsFragment = new StepsFragment();

      fragmentManager.beginTransaction()
          .add(R.id.steps_container, stepsFragment)
          .commit();

      //Setting an Intent Bundle to the Steps Fragment
      stepsFragment.setArguments(parcelable);


    ingredientsLabel = findViewById(R.id.ingredient_label);
    ingredientsFrame = findViewById(R.id.ingredients_container);
    stepsFrame = findViewById(R.id.steps_container);
    stepsLabel = findViewById(R.id.steps_label);

  }

  @Override
  public void onRecipeSelected(int position) {

    steps = mStepsArrayList.get(position);

    mVideoUrl = steps.getVideoURL();
    mLongDescription = steps.getDescription();

    videoUrlBundle = new Bundle();
    videoUrlBundle.putString(VIDEO_KEY_BUNDLE, mVideoUrl);
    videoUrlBundle.putString(DESCRIPTION_KEY_BUNDLE, mLongDescription);

    Log.d(LOG_TAG, "My Video Url is : " + mVideoUrl);
    Log.d(LOG_TAG, "My Long Description is : " + mLongDescription);

    //Instantiating the Video & Long description  Fragment
    VideoStepFragment videoStepFragment = new VideoStepFragment();
    if (videoUrlBundle != null) {
      videoStepFragment.setArguments(videoUrlBundle);
    }

    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.ingredients_container, videoStepFragment)
        //.remove(stepsFragment)
        .addToBackStack(null)
        .commit();

    stepsFrame = findViewById(R.id.steps_container);

    //Setting the Ingredients frame  as the only one visible and Hiding the Steps Frame
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0f);
    ingredientsFrame.setLayoutParams(params);
    stepsFrame.setLayoutParams(paramsStepContainer);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
          ViewGroup.LayoutParams.MATCH_PARENT, 0);
      stepsFrame.setLayoutParams(params2);
    }

  }

  @Override
  protected void onResume() {
    super.onResume();

    //Set the Bundle to the IngredientsFragment
    ingredientsFragment.setArguments(parcelable);
    //Setting an Intent Bundle to the Steps Fragment
    stepsFragment.setArguments(parcelable);

    Log.i(LOG_TAG, "OnResume bundle Recipe Name: " + mRecipeName);
    getSupportActionBar().setTitle(mRecipeName);

    //Check Phone Orientation
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

      //Setting the horizontal View of 2 Fragments with weight 1
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          0, LayoutParams.MATCH_PARENT, 1);
      LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
          0, LayoutParams.MATCH_PARENT, 1);
      ingredientsFrame.setLayoutParams(params);
      stepsFrame.setLayoutParams(paramsStepContainer);

    } else {

      //If the Phone is in Portrait Mode
      //Set the 2 fragments with equal weight 1
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LayoutParams.MATCH_PARENT, 0, 1);
      LinearLayout.LayoutParams paramsStepContainer = new LinearLayout.LayoutParams(
          LayoutParams.MATCH_PARENT, 0, 1);
      ingredientsFrame.setLayoutParams(params);
      stepsFrame.setLayoutParams(paramsStepContainer);
    }

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(STEP_PARCEL_KEY, steps);
    outState.putString(RECIPE_NAME_KEY, mRecipeName);

    Log.i(LOG_TAG, "Recipe Key Saved:  " + mRecipeName);
  }
}
