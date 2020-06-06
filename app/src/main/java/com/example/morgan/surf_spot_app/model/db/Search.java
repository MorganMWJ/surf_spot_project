package com.example.morgan.surf_spot_app.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "search")
@TypeConverters({DateTimeConverter.class})
public class Search implements Parcelable {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NotNull
    public double latitude;

    @NotNull
    public double longitude;

    @NotNull
    public int radius;

    @NotNull
    public String type;

    @NotNull
    @ColumnInfo(name = "use_surf_keyword")
    public Boolean useSurfKeyword;

    @NotNull
    @ColumnInfo(name = "search_time")
    public Date searchTime;

    /* Do not persist a user's API key */
    @Ignore
    public String apiKey;

    public Search(double latitude, double longitude, int radius, String type, Boolean useSurfKeyword) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.type = type;
        this.useSurfKeyword = useSurfKeyword;
        this.searchTime = new Date();
        this.apiKey = null; //Cannot set @Ignore fields in Entity constructor
    }

    void setApiKey(String apiKey){
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    /**
     * Get a location string containing Lat/lng to be used in API call.
     */
    public String getLocationString(){
        return latitude + "," + longitude;
    }

    /**
     *  Getter for latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for longitude.
     */
    public double getLongitude(){
        return longitude;
    }

    /**
        Create a Map of query parameter Key-Values to be used in API call.
     */
    public Map<String, String> getQueryParameters(){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("radius", Integer.toString(radius));
        queryParams.put("type", type);
        if(useSurfKeyword) {
            queryParams.put("keyword", "surf");
        }
        queryParams.put("key", apiKey);

        return queryParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.radius);
        dest.writeString(this.type);
        dest.writeByte((byte) (this.useSurfKeyword ? 1 : 0));
        dest.writeString(this.apiKey);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Search createFromParcel(Parcel in) {
            Double lat = in.readDouble();
            Double lng = in.readDouble();
            Integer radius = in.readInt();
            String type = in.readString();
            Boolean useSurf = in.readByte() != 0;
            String key = in.readString();
            Search search = new Search(lat, lng, radius, type, useSurf);
            search.setApiKey(key);
            return search;
        }

        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}
