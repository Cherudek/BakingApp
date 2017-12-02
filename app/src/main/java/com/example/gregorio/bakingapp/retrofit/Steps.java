package com.example.gregorio.bakingapp.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {


  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
    @Override
    public Steps createFromParcel(Parcel in) {
      return new Steps(in);
    }

    @Override
    public Steps[] newArray(int size) {
      return new Steps[size];
    }
  };
  private String description;
  private int id;
  private String shortDescription;
  private String thumbnailURL;
  private String videoURL;

  protected Steps(Parcel in) {
    description = in.readString();
    id = in.readInt();
    shortDescription = in.readString();
    thumbnailURL = in.readString();
    videoURL = in.readString();

  }

  public int getId() {
    return id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(description);
    dest.writeInt(id);
    dest.writeString(shortDescription);
    dest.writeString(thumbnailURL);
    dest.writeString(videoURL);

  }


}
