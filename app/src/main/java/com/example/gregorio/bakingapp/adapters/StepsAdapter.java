package com.example.gregorio.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.gregorio.bakingapp.R;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter.IngredientsAdapterOnClickHandler;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter.IngredientsViewHolder;
import com.example.gregorio.bakingapp.adapters.StepsAdapter.StepsViewHolder;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.Steps;
import java.util.ArrayList;

/**
 * Created by Gregorio on 03/12/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsViewHolder> {

  public static final String LOG_TAG = StepsAdapter.class.getSimpleName();

  /*
* An on-click handler that we've defined to make it easy for an Activity to interface with
* our RecyclerView
*/
  private final StepsAdapterOnClickHandler mClickHandler;
  //finding and setting the TextViews for Recipe Name and Recipe Servings
  private TextView tvId;
  private TextView tvShortDescription;
  private Context mContext;

  // A copy of the original mObjects array, initialized from and then used instead as soon as
  private ArrayList<Steps> mStepsData;
  private String mId;
  private String mShortDescription;


  public StepsAdapter(StepsAdapterOnClickHandler clickHandler, int numberOfItems) {
    this.mClickHandler = clickHandler;
    int items = numberOfItems;
  }

  @Override
  public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.fragment_step_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    boolean shouldAttachToParentImmediately = false;
    View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
    return new StepsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(StepsViewHolder holder, int position) {

    Steps currentSteps = mStepsData.get(position);
    int id = currentSteps.getId();
    mShortDescription = currentSteps.getShortDescription();
    mId = String.valueOf(id);
    tvId.setText(mId);
    tvShortDescription.setText(mShortDescription);
  }

  @Override
  public int getItemCount() {

    return mStepsData.size();
  }

  public void setStepsData(ArrayList<Steps> stepsIn, Context context) {
    mStepsData = stepsIn;
    mContext = context;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface StepsAdapterOnClickHandler {

    void onClick(int recipeIndex);
  }

  public class StepsViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    public StepsViewHolder(View itemView) {
      super(itemView);
      tvId = itemView.findViewById(R.id.step_id);
      tvShortDescription = itemView.findViewById(R.id.short_description);

      tvId.setText(mId);
      tvShortDescription.setText(mShortDescription);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      Steps steps = mStepsData.get(adapterPosition);
      int id = steps.getId();
      mId = String.valueOf(id);
      mShortDescription = steps.getShortDescription();
      mClickHandler.onClick(adapterPosition);

      Log.d(LOG_TAG, "onClick Steps: " + mId + " " + mShortDescription);
    }
  }


}
