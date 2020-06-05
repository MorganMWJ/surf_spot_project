package com.example.morgan.surf_spot_app.ui;

import android.os.Bundle;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Place;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {

    /* The place object this activity is displaying */
    private Place place;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        /* Extract Place object from intent */
        this.place = getIntent().getParcelableExtra("com.example.surfspotapp.PLACE_TO_VIEW");

        /* Populate with Place object attributes */
        //todo
    }
}
