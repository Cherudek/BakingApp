package com.example.gregorio.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gregorio.bakingapp.RecipeFragment.OnRecipeClickListener;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {

  public static final String LOG_TAG = MainActivity.class.getSimpleName();

  public static final String INTENT_KEY = "Bundle";
  public static final String PARCEL_KEY = "Parcel";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getSupportActionBar().setTitle("Recipes");

    // Only create new fragments when there is no previously saved state
    if (savedInstanceState == null) {

      RecipeFragment recipeFragment = new RecipeFragment();
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction()
          .add(R.id.recipe_container, recipeFragment)
          .commit();
    }
  }

  @Override
  public void onRecipeSelected(int position, ArrayList<RecipeModel> mRecipes) {

    int size = mRecipes.size();
    //Select the correct recipe model to pass to the Detail Intent
    RecipeModel recipeModels = mRecipes.get(position - 1);

    //Intent to open the detail activity and a parcel bringing recipe data.
    Intent intent = new Intent(this, DetailActivity.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable(PARCEL_KEY, recipeModels);
    intent.putExtra(INTENT_KEY, bundle);
    startActivity(intent);

    //Intent to pass recipe data (ingredient list) to the Widget Layout
    Intent widgetIntent = new Intent(this, IngredientsWidgetProvider.class);
    widgetIntent.putExtra(INTENT_KEY, bundle);
    widgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    int ids[] = AppWidgetManager.getInstance(getApplication())
        .getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidgetProvider.class));
    widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    sendBroadcast(widgetIntent);

  }
}
