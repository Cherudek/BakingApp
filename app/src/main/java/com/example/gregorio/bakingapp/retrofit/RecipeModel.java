package com.example.gregorio.bakingapp.retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class RecipeModel {

  private int id;
  private ArrayList<Ingredients> ingredients = null;
  private String name;
  private int servings;
  private ArrayList<Steps> steps = null;

  public RecipeModel() {
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Ingredients> getIngredients() {
    return ingredients;
  }

  public ArrayList<Steps> getSteps() {
    return steps;
  }

  public int getServings() {
    return servings;
  }

}
