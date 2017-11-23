package com.example.gregorio.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.gregorio.bakingapp.R;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregorio on 23/11/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

  public static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

  //finding and setting the TextViews for Recipe Name and Recipe Servings
  private TextView recipe;
  private TextView servings;

  private Context mContext;
  /*
    * An on-click handler that we've defined to make it easy for an Activity to interface with
    * our RecyclerView
   */
  private final RecipeAdapterOnClickHandler mClickHandler;

  // A copy of the original mObjects array, initialized from and then used instead as soon as
  private List<RecipeModel> mRecipeData = new ArrayList<>();

  public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler, int numberOfItems) {
    this.mClickHandler = clickHandler;
    int items = numberOfItems;
  }

  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.fragment_main_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    boolean shouldAttachToParentImmediately = false;
    View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

    return new RecipeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    RecipeModel currentRecipe = mRecipeData.get(position);
    String recipeName = currentRecipe.getName();
    int servingsNumber = currentRecipe.getServings();
    String servingsText = String.valueOf(servingsNumber);
    recipe.setText(recipeName);
    servings.setText(servingsText);
  }

  @Override
  public int getItemCount() {
    int recipeCount = mRecipeData.size();
    return recipeCount;
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface RecipeAdapterOnClickHandler {
    void onClick(int recipeIndex);
  }

  public void setRecipeData(ArrayList<RecipeModel> recipesIn, Context context) {
    mRecipeData = recipesIn;
    mContext = context;
    notifyDataSetChanged();
  }

  public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RecipeViewHolder(View itemView) {
      super(itemView);
      recipe = itemView.findViewById(R.id.recipe_name);
      servings = itemView.findViewById(R.id.servings);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      RecipeModel recipe = mRecipeData.get(adapterPosition);
      String recipeName = recipe.getName();
      int recipeServings = recipe.getServings();
      int recipeIndex = recipe.getId();
      mClickHandler.onClick(adapterPosition);

    }
  }
}
