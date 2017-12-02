package com.example.gregorio.bakingapp.adapters;

import static com.example.gregorio.bakingapp.MainActivity.INGREDIENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.MEASURE_KEY;
import static com.example.gregorio.bakingapp.MainActivity.QUANTITY_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.gregorio.bakingapp.R;
import com.example.gregorio.bakingapp.adapters.IngredientsAdapter.IngredientsViewHolder;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregorio on 01/12/2017.
 */

public class IngredientsAdapter extends
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

  public static final String LOG_TAG = IngredientsAdapter.class.getSimpleName();

  /*
  * An on-click handler that we've defined to make it easy for an Activity to interface with
  * our RecyclerView
 */
  private final IngredientsAdapterOnClickHandler mClickHandler;
  //finding and setting the TextViews for Recipe Name and Recipe Servings
  private TextView tvQuantity;
  private TextView tvMeasure;
  private TextView tvIngredient;
  private Context mContext;
  // A copy of the original mObjects array, initialized from and then used instead as soon as
  private List<Ingredients> mIngredientsData = new ArrayList<>();
  private Bundle ingredientIntent;
  private float mQuantity;
  private String mQuantityString;
  private String mMeasure;
  private String mIngredient;


  public IngredientsAdapter(IngredientsAdapterOnClickHandler clickHandler, int numberOfItems) {
    this.mClickHandler = clickHandler;
    int items = numberOfItems;
  }


  @Override
  public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.fragment_ingredient_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    boolean shouldAttachToParentImmediately = false;
    View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
    return new IngredientsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(IngredientsViewHolder holder, int position) {
    Ingredients currentIngredient = mIngredientsData.get(position);

    float quantity = currentIngredient.getQuantity();
    String measure = currentIngredient.getMeasure();
    String ingredient = currentIngredient.getIngredient();
    String quantityString = String.valueOf(mQuantity);

    tvQuantity.setText(quantityString);
    tvMeasure.setText(measure);
    tvIngredient.setText(ingredient);

  }

  @Override
  public int getItemCount() {
    int ingredientsNumber = mIngredientsData.size();
    return ingredientsNumber;
  }

  public void setIngredientsData(ArrayList<Ingredients> recipesIn, Context context) {
    mIngredientsData = recipesIn;
    mContext = context;
    notifyDataSetChanged();
  }


  /**
   * The interface that receives onClick messages.
   */
  public interface IngredientsAdapterOnClickHandler {

    void onClick(int recipeIndex);
  }

  public class IngredientsViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    public IngredientsViewHolder(View itemView) {
      super(itemView);
      tvQuantity = itemView.findViewById(R.id.quantity);
      tvMeasure = itemView.findViewById(R.id.measure);
      tvIngredient = itemView.findViewById(R.id.ingredient);

      tvQuantity.setText(mQuantityString);
      tvMeasure.setText(mMeasure);
      tvIngredient.setText(mIngredient);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      Ingredients ingredients = mIngredientsData.get(adapterPosition);
      float quantity = ingredients.getQuantity();
      mQuantityString = String.valueOf(quantity);
      mMeasure = ingredients.getMeasure();
      mIngredient = ingredients.getIngredient();
      mClickHandler.onClick(adapterPosition);

      Log.d(LOG_TAG, "Click Ingredient: " + mIngredient + " " + mMeasure + " " + mQuantityString);
    }
  }


}
