package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

  private String mVideoUrl;
  private String mLongDescription;
  private Bundle videoUrlBundle;
  private String mRecipeName;
  private VideoStepFragment videoStepFragment;
  private FragmentManager fragmentManager;
  private ArrayList<Steps> mStepsArrayList;

  private View ingredientsLabel;
  private View ingredientsFrame;
  private View stepsLabel;
  private StepsFragment stepsFragment;
  private IngredientsFragment ingredientsFragment;



  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    if (savedInstanceState == null) {

      //Instantiate the IngredientsFragment
      ingredientsFragment = new IngredientsFragment();
      fragmentManager = getSupportFragmentManager();

      //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
      Intent intent = getIntent();
      Bundle parcelable = intent.getBundleExtra(INTENT_KEY);

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

    }

    ingredientsLabel = findViewById(R.id.ingredient_label);
    ingredientsFrame = findViewById(R.id.ingredients_container);
    stepsLabel = findViewById(R.id.steps_label);

  }

  @Override
  public void onRecipeSelected(int position) {

    Steps steps = mStepsArrayList.get(position);

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

    fragmentManager.beginTransaction()
        .replace(R.id.steps_container, videoStepFragment)
        .commit();

    fragmentManager.beginTransaction()
        .remove(ingredientsFragment)
        .commit();

    ingredientsLabel.setVisibility(View.GONE);
    ingredientsFrame.setVisibility(View.GONE);
    stepsLabel.setVisibility(View.GONE);

  }


}
