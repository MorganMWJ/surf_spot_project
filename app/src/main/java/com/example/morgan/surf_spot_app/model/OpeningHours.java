package com.example.morgan.surf_spot_app.model;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {

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
}
