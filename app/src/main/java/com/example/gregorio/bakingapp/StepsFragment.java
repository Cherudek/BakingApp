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
import android.view.ViewGroup;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 03/12/2017.
 */

public class StepsFragment extends Fragment {

  private static final String LOG_TAG = StepsFragment.class.getSimpleName();

  ArrayList<Steps> stepsArrayList;
  private StepsAdapter stepsAdapter;
  private RecyclerView rvSteps;
  private LinearLayoutManager layoutManager;
  private int numberOfSteps;
  private Context mContext;

  public StepsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    Bundle bundle = this.getArguments();
    if (bundle != null) {
      //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
      RecipeModel recipeModel = bundle.getParcelable(PARCEL_KEY);
      //Getting the corresponding recipe ingredients Array List
      stepsArrayList = recipeModel.getSteps();
    }

    mContext = getActivity().getApplicationContext();
    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvSteps = rootView.findViewById(R.id.rv_fragment_step_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvSteps.setLayoutManager(layoutManager);
    stepsAdapter = new IngredientsAdapter(this, numberOfSteps);
    rvSteps.setAdapter(stepsAdapter);
    //Passing the Context and the Ingredients Array to our IngredientsAdapter to populate the RecycleView.
    stepsAdapter.setIngredientsData(stepsArrayList, mContext);

    return rootView;
  }
}