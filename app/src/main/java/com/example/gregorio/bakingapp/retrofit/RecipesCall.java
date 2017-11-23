package com.example.gregorio.bakingapp.retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gregorio on 22/11/2017.
 */

public interface RecipesCall {

  @GET("baking.json")
  Call<List<RecipeModel>> recipesForChef();

}
