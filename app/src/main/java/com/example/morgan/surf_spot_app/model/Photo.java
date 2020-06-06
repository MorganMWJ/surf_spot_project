package com.example.morgan.surf_spot_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Photo implements Parcelable {

    @SerializedName("photo_reference")
    private String ref;
    private Integer height;
    private Integer width;

    public Photo(String ref, Integer height, Integer width) {
        this.ref = ref;
        this.height = height;
        this.width = width;
    }

    public String getRef() {
        return ref;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    /**
     Create a Map of query parameter Key-Values to be used in API call.
     */
    public Map<String, String> getQueryParameters(String apiKey){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("maxwidth", Integer.toString(400));
        queryParams.put("photoreference", this.ref);
        queryParams.put("key", apiKey);

        return queryParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ref);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            String ref = in.readString();
            Integer h = in.readInt();
            Integer w = in.readInt();
            return new Photo(ref, h, w);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
