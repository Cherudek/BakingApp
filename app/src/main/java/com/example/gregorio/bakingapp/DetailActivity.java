package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.gregorio.bakingapp.IngredientsFragment.OnStepsBtnClickListener;
import com.example.gregorio.bakingapp.StepsFragment.OnBtnIngredientsListClickListener;
import com.example.gregorio.bakingapp.StepsFragment.OnStepsClickListener;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class DetailActivity extends AppCompatActivity implements OnStepsClickListener,
    OnStepsBtnClickListener, OnBtnIngredientsListClickListener {

  public static final String LOG_TAG = DetailActivity.class.getSimpleName();
  public static final String VIDEO_KEY_BUNDLE = "Video URL bundle";
  public static final String DESCRIPTION_KEY_BUNDLE = "Description bundle";
  public static final String STEP_PARCEL_KEY = "Step Parcel Key";
  public static final String INGREDIENTS_PARCEL_KEY = "Ingredients Parcel Key";
  public static final String RECIPE_NAME_KEY = "Recipe Name";
  public static final String STEPS_ARRAY_LIST_KEY = "Steps Array List Key";
  public static final String INGREDIENTS_ARRAY_LIST_KEY = "Ingredients Array List Key";
  public static final String VIDEO_POSITION_BUNDLE = "Video Position Key";
  public static final String VIDEO_ID_BUNDLE = "Video ID Key";
  public static final String STEPS_SIZE_BUNDLE = "Steps Size Key";
  public static final String STEP_FRAGMENT_TAG = "Step Fragment Tag";
  public static final String INGREDIENTS_FRAGMENT_TAG = "Ingredients Fragment Tag";
  public static final String VIDEO_FRAGMENT_TAG = "Video Fragment Tag";
  public static final String IS_TABLET_LANDSCAPE_TAG = "Tablet Landscape Tag";
  public static final String IS_TABLET_PORTRAIT_TAG = "Tablet Portrait Tag";
  public static final String PARCELABLE_KEY = "Parcelable Key";

  private static final String RECIPE_MODEL_KEY = "Recipe Model Key";

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
  private VideoStepFragment videoStepFragment;

  private Steps steps;
  private Intent intent;
  private Bundle parcelable;
  private Bundle onDestroyBundle;
  private Integer position;
  private Boolean isTabletLandscape;
  private Boolean isTabletPortrait;
  private RecipeModel recipeModel;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ingredientsLabel = findViewById(R.id.ingredient_label);
    ingredientsFrame = findViewById(R.id.details_container);
    stepsLabel = findViewById(R.id.steps_label);



    if (savedInstanceState != null) {
      mRecipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
      mStepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_LIST_KEY);
      mIngredientsArrayList = savedInstanceState.getParcelableArrayList(INGREDIENTS_ARRAY_LIST_KEY);
//      isTabletLandscape = savedInstanceState.getBoolean(IS_TABLET_LANDSCAPE_TAG);
//      isTabletPortrait = savedInstanceState.getBoolean(IS_TABLET_PORTRAIT_TAG);
      recipeModel = savedInstanceState.getParcelable(RECIPE_MODEL_KEY);
      parcelable = savedInstanceState.getParcelable(PARCELABLE_KEY);

      Log.i(LOG_TAG, "Recipe Name Key Saved:  " + mRecipeName);
      Log.i(LOG_TAG, "OnSavedInstance bundle Steps Array Size: " + mStepsArrayList);
      Log.i(LOG_TAG, "OnSavedInstance bundle Ingredients Array Size: " + mIngredientsArrayList);

      //This will be called in case we need to replace the fragment after a rotation using the saved states
      fragmentSetUp2();
      Log.d(LOG_TAG, "OnCreate: fragmentSetUp2() *** TEST ***");


    } else {

      // This method will launch  set up the Ingredients fragment (Title, RecyclerView and 1 Btn).
      fragmentSetUp();
      Log.d(LOG_TAG, "OnCreate: fragmentSetUp() *** TEST ***");

    }
  }

  // This method will launch the set up of the Ingredients Fragment (recyclerView and the View recipe Steps Button).

  public void fragmentSetUp() {

    //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
    intent = getIntent();
    parcelable = intent.getBundleExtra(INTENT_KEY);
    recipeModel = parcelable.getParcelable(PARCEL_KEY);
    mIngredientsArrayList = recipeModel.getIngredients();
    mStepsArrayList = recipeModel.getSteps();
    mRecipeName = recipeModel.getName();

    //Instantiate the IngredientsFragment
    ingredientsFragment = new IngredientsFragment();
    // stepsFragment = new StepsFragment();
    videoStepFragment = new VideoStepFragment();

    //Set the Bundle to the IngredientsFragment
    ingredientsFragment.setArguments(parcelable);
    videoStepFragment.setArguments(parcelable);

    fragmentManager = getSupportFragmentManager();

    // provide compatibility to all the versions
    getSupportActionBar().setTitle(mRecipeName);

    isTabletLandscape = getResources().getBoolean(R.bool.isTabletLand);
    isTabletPortrait = getResources().getBoolean(R.bool.isTabletPortrait);


    //Check if we are in Tablet Landscape layout
    if (isTabletLandscape) {
      fragmentManager.beginTransaction()
          .add(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
          .add(R.id.details_container_2, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "fragmentSetUp() *** TEST *** isTabletLandscape");

    } else if (isTabletPortrait) {

      fragmentManager.beginTransaction()
          .add(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "fragmentSetUp() *** TEST *** is Tablet Portrait");

    } else {

      fragmentManager.beginTransaction()
          .add(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "fragmentSetUp() *** TEST *** is Mobile");


    }
  }

  // Fragment Set Up using saved data bundle
  public void fragmentSetUp2() {

    //Instantiate the IngredientsFragment
    ingredientsFragment = new IngredientsFragment();
    // stepsFragment = new StepsFragment();
    videoStepFragment = new VideoStepFragment();

    //Set the Bundle to the IngredientsFragment
    ingredientsFragment.setArguments(parcelable);
    videoStepFragment.setArguments(parcelable);

    fragmentManager = getSupportFragmentManager();

    // provide compatibility to all the versions
    getSupportActionBar().setTitle(mRecipeName);

    isTabletLandscape = getResources().getBoolean(R.bool.isTabletLand);
    isTabletPortrait = getResources().getBoolean(R.bool.isTabletPortrait);

    //Check if we are in Tablet Landscape layout
    if (isTabletLandscape) {

      FrameLayout container2 = findViewById(R.id.details_container_2);
      container2.setVisibility(View.VISIBLE);

      fragmentManager.beginTransaction()
          .replace(R.id.details_container, ingredientsFragment)
          .add(R.id.details_container_2, videoStepFragment)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "fragmentSetUp2() *** TEST *** isTabletLandscape");

    } else if (isTabletPortrait) {

      fragmentManager.beginTransaction()
          .replace(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
          .remove(videoStepFragment)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "fragmentSetUp2() *** TEST *** is Tablet Portrait");

    } else {

      Log.d(LOG_TAG, "fragmentSetUp2() *** TEST *** is Mobile");

    }
  }

  // This method will launch the recipe steps fragment that will replace the Ingredients one
  // in the Detail Activity
  public void viewRecipeSteps() {

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

    if (isTabletLandscape) {
      fragmentManager.beginTransaction()
          .replace(R.id.details_container, stepsFragment, STEP_FRAGMENT_TAG)
          .replace(R.id.details_container_2, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();
    } else {
      fragmentManager.beginTransaction()
          .replace(R.id.details_container, stepsFragment, STEP_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();
    }
  }


  // This method will trigger OnStepsClickListener Interface to Set up the VideoSteps Fragment on the
  @Override
  public void onStepSelected(int position) {

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
    videoUrlBundle.putInt(VIDEO_POSITION_BUNDLE, position);

    Log.d(LOG_TAG, "My Video Url is : " + mVideoUrl);
    Log.d(LOG_TAG, "My Long Description is : " + mLongDescription);
    Log.d(LOG_TAG, "My Video ID is : " + mId);
    Log.d(LOG_TAG, "My Video Position is : " + position);


    //Instantiating the Video & Long description  Fragment
    if (videoStepFragment == null) {
      videoStepFragment = new VideoStepFragment();
      videoStepFragment.setArguments(videoUrlBundle);
    }

    //Instantiating the steps  Fragment
    if (stepsFragment == null) {
      stepsFragment = new StepsFragment();
      Bundle bundle = new Bundle();
      RecipeModel recipeModel = parcelable.getParcelable(PARCEL_KEY);
      bundle.putParcelable(STEPS_ARRAY_LIST_KEY, recipeModel);
      stepsFragment.setArguments(bundle);

    }

    if (videoUrlBundle != null) {
      videoStepFragment.setArguments(videoUrlBundle);
    }

    //if fragments are not null pass on the bundle and start the fragment transaction
    videoStepFragment.setArguments(videoUrlBundle);
    stepsFragment.setArguments(parcelable);

    if (isTabletLandscape) {

      // Reload current fragment
      videoStepFragment = null;
      FragmentManager fm = getSupportFragmentManager();

      videoStepFragment = (VideoStepFragment) fm.findFragmentByTag(VIDEO_FRAGMENT_TAG);
      final FragmentTransaction ft = fm.beginTransaction();
      ft.detach(videoStepFragment);
      ft.attach(videoStepFragment);
      ft.commit();

      fm.beginTransaction()
          .replace(R.id.details_container, stepsFragment, STEP_FRAGMENT_TAG)
          .replace(R.id.details_container_2, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "onStepSelected() **** TEST **** is tablet Landscape");

    } else if (isTabletPortrait) {

      if (videoStepFragment == null) {
        videoStepFragment = new VideoStepFragment();
        videoStepFragment.setArguments(videoUrlBundle);
      }

      FragmentManager fm = getSupportFragmentManager();
      videoStepFragment.setArguments(videoUrlBundle);

      if (videoStepFragment == null) {
        videoStepFragment = (VideoStepFragment) fm.findFragmentByTag(VIDEO_FRAGMENT_TAG);
      }

      stepsFragment = (StepsFragment) fm.findFragmentByTag(STEP_FRAGMENT_TAG);
      ingredientsFragment = (IngredientsFragment) fm.findFragmentByTag(INGREDIENTS_FRAGMENT_TAG);

      final FragmentTransaction ft = fm.beginTransaction();
      ft.detach(videoStepFragment);
      ft.detach(stepsFragment);
      ft.detach(ingredientsFragment);
      ft.attach(videoStepFragment);
      ft.commit();

      fm.beginTransaction()
          .replace(R.id.details_container, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "onStepSelected() **** TEST **** is tablet Portrait");

    } else {

      //For Mobile (Portrait and landscape)
      FragmentManager fm = getSupportFragmentManager();
      fm.beginTransaction()
          .replace(R.id.details_container, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "onStepSelected() **** TEST **** is Mobile Portrait or Landscape");

    }
  }

  // This method will trigger OnBtnIngredientsListClickListener Interface to Set up the Ingredient Fragment
  @Override
  public void onBtnIngredientList() {

    if (ingredientsFragment == null) {
      ingredientsFragment = new IngredientsFragment();
    }

    ingredientsFragment.setArguments(parcelable);

    if (isTabletLandscape) {
      fragmentManager.beginTransaction()
          .replace(R.id.details_container, ingredientsFragment, INGREDIENTS_FRAGMENT_TAG)
          .replace(R.id.details_container_2, videoStepFragment, VIDEO_FRAGMENT_TAG)
          .addToBackStack(null)
          .commit();

      Log.d(LOG_TAG, "onBtnIngredientList() **** TEST **** is Tablet Landscape");

    }
  }


  @Override
  protected void onResume() {
    super.onResume();
    getSupportActionBar().setTitle(mRecipeName);

    isTabletLandscape = getResources().getBoolean(R.bool.isTabletLand);
    isTabletPortrait = getResources().getBoolean(R.bool.isTabletPortrait);

    Log.d(LOG_TAG, "TEST: OnResume isTabletLandscape is" + isTabletLandscape);
    Log.d(LOG_TAG, "TEST: OnResume isTabletPortrait is" + isTabletPortrait);

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(STEPS_ARRAY_LIST_KEY, mStepsArrayList);
    outState.putParcelableArrayList(INGREDIENTS_ARRAY_LIST_KEY, mIngredientsArrayList);
    outState.putParcelable(STEP_PARCEL_KEY, steps);
    outState.putString(RECIPE_NAME_KEY, mRecipeName);
    outState.putParcelable(RECIPE_MODEL_KEY, recipeModel);
    outState.putParcelable(PARCELABLE_KEY, parcelable);

    int stepsArraySize = mStepsArrayList.size();
    int ingredientsArraySize = mIngredientsArrayList.size();

    Log.i(LOG_TAG, "Recipe Name Key Saved:  " + mRecipeName);
    Log.i(LOG_TAG, "OnSavedInstance bundle Steps Array Size: " + stepsArraySize);
    Log.i(LOG_TAG, "OnSavedInstance bundle Ingredients Array Size: " + ingredientsArraySize);

  }


}
