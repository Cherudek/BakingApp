package com.example.gregorio.bakingapp.retrofit;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

  public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>() {
    public Ingredients createFromParcel(Parcel in) {
      return new Ingredients(in);
    }

    public Ingredients[] newArray(int size) {
      return new Ingredients[size];
    }
  };
  private String ingredient;
  private String measure;
  private float quantity;

  public Ingredients() {
  }

  protected Ingredients(Parcel in) {
    this.ingredient = ((String) in.readValue((String.class.getClassLoader())));
    this.measure = ((String) in.readValue((String.class.getClassLoader())));
    this.quantity = ((float) in.readValue((float.class.getClassLoader())));
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(ingredient);
    dest.writeValue(measure);
    dest.writeValue(quantity);
  }

  public int describeContents() {
    return 0;
  }

  public float getQuantity() {
    return quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public String getIngredient() {
    return ingredient;
  }


}
