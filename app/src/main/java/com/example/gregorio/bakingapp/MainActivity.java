package com.example.gregorio.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.gregorio.bakingapp.RecipeFragment.OnRecipeClickListener;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {

  public static final String LOG_TAG = MainActivity.class.getSimpleName();

  public static final String QUANTITY_KEY = "Quantity";
  public static final String MEASURE_KEY = "Measure";
  public static final String INGREDIENT_KEY = "Ingredients";
  public static final String INTENT_KEY = "Bundle";
  public static final String PARCEL_KEY = "Parcel";

  // A copy of the original mObjects array, initialized from and then used instead as soon as
  private List<Ingredients> mIngredientsData = new ArrayList<>();
  private List<RecipeModel> mRecipes = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecipeFragment recipeFragment = new RecipeFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .add(R.id.recipe_container, recipeFragment)
        .commit();
  }

  @Override
  public void onRecipeSelected(int position, ArrayList<RecipeModel> mRecipes) {

    int size = mRecipes.size();
    //Select the correct recipe model to pass to the Detail Intent
    RecipeModel recipeModels = mRecipes.get(position - 1);

    Intent intent = new Intent(this, DetailActivity.class);
    Bundle bundle = new Bundle();
    // bundle.putParcelableArrayList(PARCEL_KEY, new ArrayList<>(mRecipes));
    bundle.putParcelable(PARCEL_KEY, recipeModels);

    intent.putExtra(INTENT_KEY, bundle);
    startActivity(intent);

    Toast.makeText(getApplicationContext(), "Recipe Index: " + position + " Size  " + size,
        Toast.LENGTH_LONG).show();
  }
}
