package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INGREDIENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.MEASURE_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;
import static com.example.gregorio.bakingapp.MainActivity.QUANTITY_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

    IngredientsFragment ingredientsFragment = new IngredientsFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    Intent intent = getIntent();
    //ingredientIntent = intent.getB
    Bundle parcelable = intent.getBundleExtra(INTENT_KEY);

    ingredientsFragment.setArguments(parcelable);

    fragmentManager.beginTransaction()
        .add(R.id.ingredients_container, ingredientsFragment)
        .commit();

  }
}
