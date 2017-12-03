package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class DetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    //Instantiate the IngredientsFragment
    IngredientsFragment ingredientsFragment = new IngredientsFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    //Receiving the Intent form the MainActivity to pass data to Ingredient & Steps Fragment
    Intent intent = getIntent();
    Bundle parcelable = intent.getBundleExtra(INTENT_KEY);

    //Set the Bundle to the IngredientsFragment
    ingredientsFragment.setArguments(parcelable);

    fragmentManager.beginTransaction()
        .add(R.id.ingredients_container, ingredientsFragment)
        .commit();

    //Instantiating the Steps Fragment
    StepsFragment stepsFragment = new StepsFragment();
    fragmentManager.beginTransaction()
        .add(R.id.steps_container, stepsFragment)
        .commit();

    //Setting an Intent Bundle to the Steps Fragment
    stepsFragment.setArguments(parcelable);

  }
}
