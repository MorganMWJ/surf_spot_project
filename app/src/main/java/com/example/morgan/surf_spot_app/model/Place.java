package com.example.morgan.surf_spot_app.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import androidx.annotation.NonNull;

public class Place implements Comparable<Place>, Parcelable{

    private static final double UNKNOWN_RATING = 0.0;

    private String name;
    private Double rating;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    private Geometry geometry;
    @SerializedName("price_level")
    private Integer priceLevel;
    private String website;
    @SerializedName("formatted_address")
    private String address;
    @SerializedName("formatted_phone_number")
    private String phoneNumber;
    private Photo[] photos;

    public Place(String name, Double rating, OpeningHours openingHours,
                 Geometry geometry, Integer priceLevel, String website,
                 String address, String phoneNumber, Photo[] photos) {
        this.name = name;
        this.rating = rating;
        this.openingHours = openingHours;
        this.geometry = geometry;
        this.priceLevel = priceLevel;
        this.website = website;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photos = photos;
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Photo[] getPhotos() {
        return photos;
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
        if(this.rating==null) {
            dest.writeDouble(UNKNOWN_RATING);
        }
        else{
            dest.writeDouble(this.rating);
        }
        dest.writeParcelable(this.openingHours,0);
        dest.writeParcelable(this.geometry, 0);
        if(this.priceLevel==null){
            /* Nulls can be written as String to Parcelable */
            dest.writeString(null);
        }
        else {
            dest.writeInt(this.priceLevel);
        }
        dest.writeString(this.website);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeParcelableArray(this.photos,0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Place createFromParcel(Parcel in) {
            String nm = in.readString();
            Double rting = in.readDouble();
            OpeningHours oh = in.readParcelable(OpeningHours.class.getClassLoader());
            Geometry geo = in.readParcelable(Geometry.class.getClassLoader());
            Integer pl = in.readInt();
            if(pl == -1){
                pl = null;
            }
            String web = in.readString();
            String addr = in.readString();
            String pn = in.readString();

            /* Convert from Parcelable[] result to Photo[] */
            Parcelable[] parcelArray = in.readParcelableArray(Photo.class.getClassLoader());
            Photo[] photos = null;
            if (parcelArray != null) {
                photos = Arrays.copyOf(parcelArray, parcelArray.length, Photo[].class);
            }

            return new Place(nm, rting, oh, geo, pl, web, addr, pn, photos);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
