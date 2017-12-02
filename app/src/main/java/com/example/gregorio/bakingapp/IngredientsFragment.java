package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INGREDIENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.MEASURE_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;
import static com.example.gregorio.bakingapp.MainActivity.QUANTITY_KEY;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import java.util.ArrayList;

/**
 * Created by Gregorio on 29/11/2017.
 */

public class IngredientsFragment extends Fragment implements
    IngredientsAdapter.IngredientsAdapterOnClickHandler {


  private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();
  ArrayList<Ingredients> ingredientsArrayList;
  private IngredientsAdapter ingredientsAdapter;
  private RecyclerView rvIngredients;
  private LinearLayoutManager layoutManager;
  private int numberOfIngredients;
  private Context mContext;
  private Bundle ingredientIntent;
  private float mQuantity;
  private String mMeasure;
  private String mIngredient;


  public IngredientsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (getArguments() != null) {

      ingredientIntent = getArguments().getBundle(INTENT_KEY);

      ingredientsArrayList = ingredientIntent.getParcelableArrayList(PARCEL_KEY);

      //For Testing Only
      Ingredients a = ingredientsArrayList.get(0);
      String ingredient = a.getIngredient();

      Log.d(LOG_TAG, "Ingredients: " + ingredient + " Measure: " + mMeasure
          + " Quantity: " + mQuantity);

    }

    mContext = getActivity().getApplicationContext();

    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvIngredients = rootView.findViewById(R.id.rv_fragment_detail_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvIngredients.setLayoutManager(layoutManager);
    ingredientsAdapter = new IngredientsAdapter(this, numberOfIngredients);

    ingredientsAdapter.setIngredientsData(ingredientsArrayList, mContext);

    rvIngredients.setAdapter(ingredientsAdapter);

    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onClick(int recipeIndex) {

  }
}
