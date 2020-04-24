package com.example.morgan.surf_spot_app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.SerializedName;

public class OpeningHours implements Parcelable{

    @SerializedName("open_now")
    private boolean openNow;

    @SerializedName("weekday_text")
    private String[] weekdayText;

    public OpeningHours(boolean openNow, String[] weekdayText) {
        this.openNow = openNow;
        this.weekdayText = weekdayText;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }


    public String[] getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(String[] weekdayText) {
        this.weekdayText = weekdayText;
    }

    public String toHtmlString() {
        StringBuilder sb = new StringBuilder();

        if(this.openNow){
            sb.append("Open Now<br/>");
        }
        else{
            sb.append("Closed<br/>");
        }

        if(this.weekdayText != null) {
            for (String weekday : this.weekdayText) {
                sb.append("&#8226;");
                sb.append(weekday);
                sb.append("<br/>");
            }
        }

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.openNow ? 1 : 0));
        dest.writeStringArray(this.weekdayText);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public OpeningHours createFromParcel(Parcel in) {
            Boolean on = in.readByte() != 0;
            String[] weektxt = in.createStringArray();
            return new OpeningHours(on, weektxt);
        }

        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
