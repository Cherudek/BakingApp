package com.example.gregorio.bakingapp.retrofit;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.gregorio.bakingapp.RecipeFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregorio on 22/11/2017.
 */

public class RecipeModel implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
    @Override
    public RecipeModel createFromParcel(Parcel in) {
      return new RecipeModel(in);
    }

    @Override
    public RecipeModel[] newArray(int size) {
      return new RecipeModel[size];
    }
  };
  private int id;
  private ArrayList<Ingredients> ingredients = null;
  private String name;
  private int servings;
  private ArrayList<Steps> steps = null;


  //Constructor 1
  public RecipeModel() {
  }

  protected RecipeModel(Parcel in) {
    id = in.readInt();
    if (in.readByte() == 0x01) {
      ingredients = new ArrayList<Ingredients>();
      in.readList(ingredients, Ingredients.class.getClassLoader());
    } else {
      ingredients = null;
    }
    name = in.readString();
    servings = in.readInt();
    if (in.readByte() == 0x01) {
      steps = new ArrayList<Steps>();
      in.readList(steps, Steps.class.getClassLoader());
    } else {
      steps = null;
    }
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    if (ingredients == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(ingredients);
    }
    dest.writeString(name);
    dest.writeInt(servings);
    if (steps == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(steps);
    }
  }


}
