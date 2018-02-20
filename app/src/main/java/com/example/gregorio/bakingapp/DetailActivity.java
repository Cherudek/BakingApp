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
import com.example.gregorio.bakingapp.IngredientsFragment.OnStepsBtnClickListener;
import com.example.gregorio.bakingapp.StepsFragment.OnStepsClickListener;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class DetailActivity extends AppCompatActivity implements OnStepsClickListener,
    OnStepsBtnClickListener {

  public static final String LOG_TAG = DetailActivity.class.getSimpleName();
  public static final String VIDEO_KEY_BUNDLE = "Video URL bundle";
  public static final String DESCRIPTION_KEY_BUNDLE = "Description bundle";
  public static final String STEP_PARCEL_KEY = "Step Parcel Key";
  public static final String RECIPE_NAME_KEY = "Recipe Name";
  public static final String STEPS_ARRAY_LIST_KEY = "Steps Array List Key";
  public static final String INGREDIENTS_ARRAY_LIST_KEY = "Ingredients Array List Key";
  public static final String VIDEO_POSITION_BUNDLE = "Video Position Key";
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
  private Integer position;

  private VideoStepFragment videoStepFragment;

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

      Log.i(LOG_TAG, "Recipe Name Key Saved:  " + mRecipeName);
      Log.i(LOG_TAG, "OnSavedInstance bundle Steps Array Size: " + mStepsArrayList);
      Log.i(LOG_TAG, "OnSavedInstance bundle Ingredients Array Size: " + mIngredientsArrayList);
    } else {

      // This method will launch  set up the Ingredients fragment (Title, RecyclerView and 1 Btn).
      fragmentSetUp();
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

    fragmentManager.beginTransaction()
        .replace(R.id.details_container, stepsFragment, STEP_FRAGMENT_TAG)
        .addToBackStack(null)
        .commit();

  }

  // This method will launch the set up of the Ingredients Fragment (recyclerView and the View recipe Steps Button).

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
  }

  // This method will trigger OnStepsClickListener Interface to Set up the VideoSteps Fragment on the

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
    videoUrlBundle.putInt(VIDEO_POSITION_BUNDLE, position);

    Log.d(LOG_TAG, "My Video Url is : " + mVideoUrl);
    Log.d(LOG_TAG, "My Long Description is : " + mLongDescription);
    Log.d(LOG_TAG, "My Video ID is : " + mId);
    Log.d(LOG_TAG, "My Video Position is : " + position);


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
