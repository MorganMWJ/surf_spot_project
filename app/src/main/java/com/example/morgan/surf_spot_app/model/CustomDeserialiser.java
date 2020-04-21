package com.example.morgan.surf_spot_app.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CustomDeserialiser implements JsonDeserializer<List<Place>> {
    @Override
    public List<Place> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Get the "results" element from the parsed JSON
        JsonElement results = json.getAsJsonObject().get("results");

        // Deserialize it.
        return new Gson().fromJson(results, new TypeToken<List<Place>>(){}.getType());
    }
}
