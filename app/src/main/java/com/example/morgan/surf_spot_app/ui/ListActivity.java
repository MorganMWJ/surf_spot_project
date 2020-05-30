package com.example.morgan.surf_spot_app.ui;

import android.os.Bundle;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Place;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends AppCompatActivity {

    /* Recycler View */
    private RecyclerView recyclerView;
    private PlacesRecyclerWithListAdapter placesRecyclerAdapter;
    private RecyclerView.LayoutManager viewManager;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /* Reload instance state upon screen re-orientation */
        if(savedInstanceState != null){
            ArrayList<Place> savedPlaces = savedInstanceState.getParcelableArrayList("CURRENT_PLACES");
            placesRecyclerAdapter = new PlacesRecyclerWithListAdapter(this, savedPlaces);
        }
        else{
            placesRecyclerAdapter = new PlacesRecyclerWithListAdapter(this);
        }

        /* Initialise recycler view & adapter */
        viewManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.place_list);
        recyclerView.setLayoutManager(viewManager);
        recyclerView.setAdapter(placesRecyclerAdapter);

        ArrayList<Place> surfPlaces = getIntent().getParcelableArrayListExtra("com.example.surfspotapp.LIST");
        if (surfPlaces != null) {

            /* Tell the user how many results were returned */
            String alertText = surfPlaces.size() + " " + getString(R.string.request_sucess_text);
            Snackbar sb = Snackbar.make(findViewById(R.id.place_list), alertText, Snackbar.LENGTH_INDEFINITE);
            sb.show();

            /* Update our view to display them */
            this.placesRecyclerAdapter.changeDataSet(surfPlaces);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("CURRENT_PLACES",
                placesRecyclerAdapter.getPlacesArrayList());
    }

}
