package com.example.morgan.surf_spot_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Place;
import com.example.morgan.surf_spot_app.model.db.Search;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private ArrayList<Place> surfPlaces;
    private Search currentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* Get SupportMapFragment */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        /*  request notification when map is ready to be used. */
        mapFragment.getMapAsync(this);

        /* Extract Places array from Intent */
        this.surfPlaces = getIntent().getParcelableArrayListExtra("com.example.surfspotapp.MAP");

        /* Extract Search object from intent */
        this.currentSearch = getIntent().getParcelableExtra("com.example.surfspotapp.SEARCH_OBJECT");

    }

    /**
     * Manipulates the map when it's available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /* Add a marker for each place */
        for(Place place : surfPlaces) {
            LatLng loc = new LatLng(place.getGeometry().getLocation().getLatitude(),
                    place.getGeometry().getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(place.getName()));
        }

        /* Set appropriate zoom level */
        googleMap.setMinZoomPreference(15);
        googleMap.setMaxZoomPreference(20);

        /* Move the map's camera to the same location */
        Double searchLat = this.currentSearch.getLatitude();
        Double searchLng = this.currentSearch.getLongitude();
        LatLng center = new LatLng(searchLat, searchLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(center));
    }

    /**
     * Create an action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.map_activity_action_bar_buttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar button clicks.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            if (item.getItemId() == R.id.list_icon) {

                /* Launch intent for MapActivity passing List of Places */
                Intent listResultsIntent = new Intent(this, ListActivity.class);

                /* Add parcelable array list as intent extra to send to list activity  */
                listResultsIntent.putParcelableArrayListExtra("com.example.surfspotapp.LIST",
                        surfPlaces);

                /* Add parcelable Search object as intent extra to list activity */
                listResultsIntent.putExtra("com.example.surfspotapp.SEARCH_OBJECT", this.currentSearch);

                /* Start ListActivity using Intent */
                startActivity(listResultsIntent);
            }
            if(item.getItemId() == R.id.search_icon){
                /* Launch main/search activity */
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        }

        return super.onOptionsItemSelected(item);
    }
}

