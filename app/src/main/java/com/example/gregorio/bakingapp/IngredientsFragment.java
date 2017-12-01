package com.example.gregorio.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter;

/**
 * Created by Gregorio on 29/11/2017.
 */

public class IngredientsFragment extends Fragment implements
    IngredientsAdapter.IngredientsAdapterOnClickHandler {


  private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();
  private IngredientsAdapter ingredientsAdapter;
  private RecyclerView rvIngredients;
  private LinearLayoutManager layoutManager;
  private int numberOfIngredients;
  private Context mContext;

  public IngredientsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    mContext = getActivity().getApplicationContext();

    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvIngredients = rootView.findViewById(R.id.rv_fragment_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvIngredients.setLayoutManager(layoutManager);
    ingredientsAdapter = new IngredientsAdapter(this, numberOfIngredients);
    rvIngredients.setAdapter(ingredientsAdapter);

    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onClick(int recipeIndex) {

  }
}
