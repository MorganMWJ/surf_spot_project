package com.example.morgan.surf_spot_app.model;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PlacesAPI {

    @GET("place/nearbysearch/json")
    Call<ResultWrapper> getPlaces(@Query(value = "location", encoded = true) String location, @QueryMap Map<String,String> queryParams);

}
