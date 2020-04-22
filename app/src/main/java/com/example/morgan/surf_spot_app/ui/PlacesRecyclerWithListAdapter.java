package com.example.morgan.surf_spot_app.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Place;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesRecyclerWithListAdapter extends
        RecyclerView.Adapter<PlacesRecyclerWithListAdapter.ViewHolder> {

    private final Context context;
    private List<Place> places;

    PlacesRecyclerWithListAdapter(Context context, List<Place> places){
        this.context = context;
        this.places = places;

        /* Sort places by rating */
        Collections.sort(this.places);
    }

    PlacesRecyclerWithListAdapter(Context context){
        this.context = context;
        ArrayList<Place> tp = new ArrayList<>();
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 6.4, null));
        tp.add(new Place("Joshs PLAce", 2.4, null));
        tp.add(new Place("Morgans PLAce", 1.4, null));
        tp.add(new Place("Joshs PLAce", 5.4, null));
        this.places = tp;
        //this.places = new ArrayList<>();
        /* Sort places by rating */
        Collections.sort(this.places);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView ratingView;
        TextView hoursView;
        Place place;

        ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.place_name_text_view);
            ratingView = itemView.findViewById(R.id.place_rating_text_view);
            hoursView = itemView.findViewById(R.id.place_open_hours_text_view);
        }

        void bindDataSet(Place p){
            nameView.setText(p.getName());
            ratingView.setText(p.getRating().toString());
            hoursView.setText("TEMP HOLDER");
            //hoursView.setText(p.getOpeningHours().toString());
            place = p;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /* create a view holder with its view as the relative layout for a recycled item */
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_place_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        /* delegate this to viewholder inner class */
        if(places!=null){
            viewHolder.bindDataSet(places.get(i));
        }
    }

    @Override
    public int getItemCount(){
        if (places != null) {
            return places.size();
        } else {
            return 0;
        }
    }

    void changeDataSet(List<Place> places){
        this.places = places;
        /* Sort by rating */
        Collections.sort(this.places);
        notifyDataSetChanged();
    }

    public void clearDataSet(){
        if(places != null) {
            this.places.clear();
        }
    }
}
