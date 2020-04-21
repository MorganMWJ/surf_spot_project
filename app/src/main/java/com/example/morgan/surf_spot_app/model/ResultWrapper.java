package com.example.morgan.surf_spot_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class ResultWrapper {

    @SerializedName("html_attributions")
    private List htmlAttributions;
    private List<Place> results;
    private String status;

    public List getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void sortPlaces(){
        Collections.sort(this.results);
    }



}
