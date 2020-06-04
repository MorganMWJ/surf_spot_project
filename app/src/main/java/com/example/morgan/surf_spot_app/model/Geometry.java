package com.example.morgan.surf_spot_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometry implements Parcelable {

    private Location location;

    public Geometry(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Geometry createFromParcel(Parcel in) {
            Location loc = in.readParcelable(Location.class.getClassLoader());
            return new Geometry(loc);
        }

        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
