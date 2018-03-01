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


  // Define a new interface OnStepsClickListener that triggers a callback in the host activity
  private OnStepsBtnClickListener mCallbackHostActivity;

  private ArrayList<Ingredients> ingredientsArrayList;
  private IngredientsAdapter ingredientsAdapter;
  private RecyclerView rvIngredients;
  private LinearLayoutManager layoutManager;
  private int numberOfIngredients;
  private Context mContext;
  private Button viewSteps;

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

    //Find the Button to Launch the Step by Step Fragment
    viewSteps = rootView.findViewById(R.id.btn_view_steps);
    viewSteps.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mCallbackHostActivity.viewRecipeSteps();

      }
    });

    return rootView;
  }


  //Attaching the onClick Btn Interface to the Detail Activity
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This makes sure that the host activity has implemented the callback interface to launch
    // the Step by Step Fragment
    // If not, it throws an exception
    try {
      mCallbackHostActivity = (OnStepsBtnClickListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnStepsClickListener");
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      ingredientsArrayList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_KEY);
    }

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

  // OnStepsClickListener interface, calls a method in the Detail activity named viewRecipeSteps()
  public interface OnStepsBtnClickListener {

    void viewRecipeSteps();
  }
}
