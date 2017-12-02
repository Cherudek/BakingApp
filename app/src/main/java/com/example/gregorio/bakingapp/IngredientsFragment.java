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
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import java.util.ArrayList;

/**
 * Created by Gregorio on 29/11/2017.
 */

public class IngredientsFragment extends Fragment implements
    IngredientsAdapter.IngredientsAdapterOnClickHandler {


  private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();
  ArrayList<Ingredients> ingredientsArrayList;
  ArrayList<RecipeModel> recipeModels;
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

    Bundle bundle = this.getArguments();

    if (bundle != null) {

      recipeModels = bundle.getParcelableArrayList(PARCEL_KEY);
      int length = recipeModels.size();

      //For Testing Only
      RecipeModel a = recipeModels.get(1);
      ingredientsArrayList = a.getIngredients();
      Ingredients foods = ingredientsArrayList.get(1);
      int size = ingredientsArrayList.size();
      String food = foods.getIngredient();
      String measure = foods.getMeasure();
      Log.d(LOG_TAG, "Ingredients: " + food + " Length: " + length
          + " Measure: " + measure + " SIZE:  " + size);

    }

    mContext = getActivity().getApplicationContext();

    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvIngredients = rootView.findViewById(R.id.rv_fragment_detail_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvIngredients.setLayoutManager(layoutManager);
    ingredientsAdapter = new IngredientsAdapter(this, numberOfIngredients);
    rvIngredients.setAdapter(ingredientsAdapter);

    ingredientsAdapter.setIngredientsData(ingredientsArrayList, mContext);

    return rootView;
  }

  @Override
  public void onClick(int recipeIndex) {

  }
}
