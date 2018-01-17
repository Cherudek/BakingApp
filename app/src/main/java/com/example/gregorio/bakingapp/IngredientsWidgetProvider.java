package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.MainActivity.INTENT_KEY;
import static com.example.gregorio.bakingapp.MainActivity.PARCEL_KEY;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import com.example.gregorio.bakingapp.retrofit.Ingredients;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

  private int numberOfIngredients;
  private String ingredientsList;
  private String LOG_TAG = IngredientsWidgetProvider.class.getSimpleName();


  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId, String ingredientsList) {

    CharSequence widgetText = context.getString(R.string.ingredients);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.ingredients_widget_provider);

    //set the Title of the label title view
    views.setTextViewText(R.id.appwidget_title, widgetText);

    // TO DO fetch the ingredients list from the ingredients fragment.

    //set the text
    views.setTextViewText(R.id.appwidget_ingredients_list, ingredientsList);

    //Set Up an Intent to launch the Detail Activity
    Intent intent = new Intent(context, DetailActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    views.setOnClickPendingIntent(R.id.appwidget_ingredients_list, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    Log.i(LOG_TAG, "onUpdate: Number of Ingredients: " + numberOfIngredients);
    ingredientsList = String.valueOf(numberOfIngredients);

    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId, ingredientsList);
    }
  }


  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    Bundle parcelable = intent.getParcelableExtra(INTENT_KEY);
    int ids[] = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    String action = intent.getAction();

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

    if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE && parcelable != null) {

      parcelable = intent.getParcelableExtra(INTENT_KEY);
      RecipeModel recipeModel = parcelable.getParcelable(PARCEL_KEY);
      ArrayList<Ingredients> mIngredientsArrayList = recipeModel.getIngredients();
      numberOfIngredients = mIngredientsArrayList.size();
      ingredientsList = String.valueOf(numberOfIngredients);

      updateAppWidget(context, appWidgetManager, ids[0], ingredientsList);
      Log.i(LOG_TAG, "onReceive: Number of Ingredients: " + ingredientsList);
      Log.i(LOG_TAG, "onReceive: Widgets Ids: " + ids);


    }


  }
}

