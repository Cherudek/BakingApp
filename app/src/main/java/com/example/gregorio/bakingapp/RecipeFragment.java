package com.example.gregorio.bakingapp;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter;
import com.example.gregorio.bakingapp.adapters.RecipeAdapter.RecipeAdapterOnClickHandler;
import com.example.gregorio.bakingapp.retrofit.RecipeModel;
import com.example.gregorio.bakingapp.retrofit.RecipesCall;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

  private String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
  private RecipesCall recipesCall;
  private RecyclerView recyclerView;
  private RecipeAdapter recipeAdapter;
  private LinearLayoutManager layoutManager;
  private int numberOFRecipes;
  private Context mContext;

  public RecipeFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    mContext = getActivity().getApplicationContext();

    //inflating the main fragment layout within its container
    View rootView = inflater.inflate(R.layout.fragment_list, container, false);

    //finding the recyclerView and setting the Adapter
    recyclerView = rootView.findViewById(R.id.rv_fragment_list);
    layoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(layoutManager);
    recipeAdapter = new RecipeAdapter( this,numberOFRecipes);
    recyclerView.setAdapter(recipeAdapter);

    //Retrofit OkHttp connection builder
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.client(httpClient.build()).build();

    // Create a very simple REST adapter which points the GitHub API endpoint.
    recipesCall = retrofit.create(RecipesCall.class);

    // Fetch a list of the Github repositories.
    Call<List<RecipeModel>> call = recipesCall.recipesForChef();

    // Execute the call asynchronously. Get a positive or negative callback.
    call.enqueue(new Callback<List<RecipeModel>>() {

      @Override
      public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
        // The network call was a success and we got a response
        // TODO: use the repository list and display it
        if (response !=null){
          List<RecipeModel> repos = response.body();
          numberOFRecipes = repos.size();
          recipeAdapter.setRecipeData((ArrayList<RecipeModel>) repos, mContext);


        }
      }

      @Override
      public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
        // the network call was a failure
        // TODO: handle error
        t.getStackTrace();
        Toast.makeText(getContext(), "Error Connecting", Toast.LENGTH_LONG).show();
      }
    });

    return rootView;

  }

  @Override
  public void onClick(int recipeIndex) {

  }
}
