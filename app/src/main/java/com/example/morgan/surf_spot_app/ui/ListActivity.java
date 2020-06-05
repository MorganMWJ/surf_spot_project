package com.example.morgan.surf_spot_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Place;
import com.example.morgan.surf_spot_app.model.db.Search;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends AppCompatActivity implements RecyclerViewClickListener{

    /* Recycler View instance variables */
    private RecyclerView recyclerView;
    private PlacesRecyclerWithListAdapter placesRecyclerAdapter;
    private RecyclerView.LayoutManager viewManager;

    /* Search object that this activity is displaying results for */
    private Search currentSearch;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /* Reload instance state upon screen re-orientation */
        if(savedInstanceState != null){
            ArrayList<Place> savedPlaces = savedInstanceState.getParcelableArrayList("CURRENT_PLACES");
            placesRecyclerAdapter = new PlacesRecyclerWithListAdapter(this, savedPlaces, this);
        }
        else{
            placesRecyclerAdapter = new PlacesRecyclerWithListAdapter(this, this);
        }

        /* Initialise recycler view & adapter */
        viewManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.place_list);
        recyclerView.setLayoutManager(viewManager);
        recyclerView.setAdapter(placesRecyclerAdapter);

        /* Extract Places array from Intent and use to initialise Recycler Adapter */
        ArrayList<Place> surfPlaces = getIntent().getParcelableArrayListExtra("com.example.surfspotapp.LIST");
        if (surfPlaces != null) {

            /* Tell the user how many results were returned */
            String alertText = surfPlaces.size() + " " + getString(R.string.request_sucess_text);
            Snackbar sb = Snackbar.make(findViewById(R.id.place_list), alertText, Snackbar.LENGTH_INDEFINITE);
            sb.show();

            /* Update our view to display them */
            this.placesRecyclerAdapter.changeDataSet(surfPlaces);
        }

        /* Extract Search object from intent */
        this.currentSearch = getIntent().getParcelableExtra("com.example.surfspotapp.SEARCH_OBJECT");

    }

    /**
     * Functionality when an item of the RecyclerView is clicked.
     * Send Place object to be displayed in ViewActivity.
     * @param p place clicked on.
     */
    @Override
    public void recyclerViewListClicked(Place place){
        /* Launch intent for ViewActivity passing Place to view */
        Intent placeViewIntent = new Intent(this, ViewActivity.class);

        /* Add parcelable Place object as intent extra to view activity */
        placeViewIntent.putExtra("com.example.surfspotapp.PLACE_TO_VIEW", place);

        /* Start ViewActivity using Intent */
        startActivity(placeViewIntent);
    }

    /**
     * Create an action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.list_activity_action_bar_buttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar button clicks.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item != null) {
            if (item.getItemId() == R.id.map_icon) {

                /* Launch intent for MapActivity passing List of Places */
                Intent listResultsIntent = new Intent(this, MapActivity.class);

                /* Add parcelable array list as intent extra to send to list activity  */
                listResultsIntent.putParcelableArrayListExtra("com.example.surfspotapp.MAP",
                        placesRecyclerAdapter.getPlacesArrayList());

                /* Add parcelable Search object as intent extra to map activity */
                listResultsIntent.putExtra("com.example.surfspotapp.SEARCH_OBJECT", this.currentSearch);

                /* Start MapActivity using Intent */
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("CURRENT_PLACES",
                placesRecyclerAdapter.getPlacesArrayList());
    }

}
