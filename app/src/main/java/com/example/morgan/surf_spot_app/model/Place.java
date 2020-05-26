package com.example.morgan.surf_spot_app.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

public class Place implements Comparable<Place>, Parcelable{

    private String name;
    private Double rating;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    public Place(String name, Double rating, OpeningHours openingHours) {
        this.name = name;
        this.rating = rating;
        this.openingHours = openingHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    /**
     *  Implemented comparable for sorting by rating in descending order.
     *  */
    @Override
    public int compareTo(@NonNull Place other) {
        if(this.rating == null){
            return 1;
        }
        if(other.rating == null){
            return -1;
        }

        if(this.rating < other.rating){
            return 1;
        }
        else if(this.rating > other.rating){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.rating);
        dest.writeParcelable(this.openingHours,0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Place createFromParcel(Parcel in) {
            String nm = in.readString();
            Double rting = in.readDouble();
            OpeningHours oh = in.readParcelable(OpeningHours.class.getClassLoader());
            return new Place(nm, rting, oh);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
