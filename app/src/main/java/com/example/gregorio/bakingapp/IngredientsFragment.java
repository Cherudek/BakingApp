package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
  private static final String SAVED_INSTANCE_KEY = "Saved Instance Key";


  ArrayList<Ingredients> ingredientsArrayList;
  private IngredientsAdapter ingredientsAdapter;
  private RecyclerView rvIngredients;
  private LinearLayoutManager layoutManager;
  private int numberOfIngredients;
  private Context mContext;

  //Constructor
  public IngredientsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (savedInstanceState != null) {
      ingredientsArrayList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_KEY);

    } else {
      Bundle bundle = this.getArguments();
      if (bundle != null) {
        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        RecipeModel recipeModel = bundle.getParcelable(PARCEL_KEY);
        //Getting the corresponding recipe ingredients Array List
        ingredientsArrayList = recipeModel.getIngredients();
      }
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
    //Passing the Context and the Ingredients Array to our IngredientsAdapter to populate the RecycleView.
    ingredientsAdapter.setIngredientsData(ingredientsArrayList, mContext);

    return rootView;
  }


  @Override
  public void onClick(int recipeIndex) {
  }

  /**
   * Save the current state of this fragment
   */
  @Override
  public void onSaveInstanceState(Bundle currentState) {
    currentState.putParcelableArrayList(SAVED_INSTANCE_KEY, ingredientsArrayList);
  }
}
