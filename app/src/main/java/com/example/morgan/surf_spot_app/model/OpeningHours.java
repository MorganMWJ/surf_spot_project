package com.example.morgan.surf_spot_app.model;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    private boolean openNow;

    @SerializedName("weekday_text")
    private String[] weekdayText;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Open Now: ");
        sb.append(this.openNow);

        if(this.weekdayText != null) {
            for (String weekday : this.weekdayText) {
                sb.append("\n");
                sb.append(weekday);
            }
        }

        return sb.toString();
    }
}
