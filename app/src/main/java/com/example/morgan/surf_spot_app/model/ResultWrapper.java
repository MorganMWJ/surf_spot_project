package com.example.morgan.surf_spot_app.model;

import java.util.List;

public class ResultWrapper {

    private List<Place> results;
    private String status;


    public ResultWrapper(List<Place> results, String status) {
        this.results = results;
        this.status = status;
    }

    public List<Place> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }


}
