package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gregorio.bakingapp.adapters.StepsAdapter;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 03/12/2017.
 */

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

  private static final String LOG_TAG = StepsFragment.class.getSimpleName();
  private static final String STEPS_ARRAY_KEY = "Steps Array List Key";


  // Define a new interface OnStepsClickListener that triggers a callback in the host activity
  OnStepsClickListener mCallbackHostActivity;

  ArrayList<Steps> stepsArrayList;
  private StepsAdapter stepsAdapter;
  private RecyclerView rvSteps;
  private LinearLayoutManager layoutManager;
  private int numberOfSteps;
  private Context mContext;
  private Steps mStepsData;
  private String mVideoURL;

  public StepsFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (savedInstanceState != null) {
      stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_KEY);

    } else {
      Bundle bundle = this.getArguments();
      if (bundle != null) {
        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        RecipeModel recipeModel = bundle.getParcelable(PARCEL_KEY);
        //Getting the corresponding recipe ingredients Array List
        stepsArrayList = recipeModel.getSteps();
      }

    }

    mContext = getActivity().getApplicationContext();
    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
    //finding the recyclerView and setting the Adapter
    rvSteps = rootView.findViewById(R.id.rv_fragment_step_list);
    layoutManager = new LinearLayoutManager(getContext());
    rvSteps.setLayoutManager(layoutManager);
    stepsAdapter = new StepsAdapter(this, numberOfSteps);
    rvSteps.setAdapter(stepsAdapter);
    //Passing the Context and the Steps Array to our StepsAdapter to populate the RecycleView.
    stepsAdapter.setStepsData(stepsArrayList, mContext);

    return rootView;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This makes sure that the host activity has implemented the callback interface
    // If not, it throws an exception
    try {
      mCallbackHostActivity = (OnStepsClickListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnStepsClickListener");
    }
  }

  @Override
  public void onClick(int recipeIndex) {
    mCallbackHostActivity.onRecipeSelected(recipeIndex);
    mStepsData = stepsArrayList.get(recipeIndex);
    mVideoURL = mStepsData.getVideoURL();

    Log.d(LOG_TAG, "Recipe Index is: " + recipeIndex + " " + "Video Url is: " + mVideoURL);

  }

  @Override
  public void onResume() {
    super.onResume();


  }

  //Saving the current State of the Recipe Steps Fragment
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(STEPS_ARRAY_KEY, stepsArrayList);

  }

  // OnStepsClickListener interface, calls a method in the host activity named onRecipeSelected
  public interface OnStepsClickListener {

    void onRecipeSelected(int position);
  }
}
