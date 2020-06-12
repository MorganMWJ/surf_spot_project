package com.example.morgan.surf_spot_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.db.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class RecentSearchesFragment extends Fragment implements SearchesRecyclerViewClickListener{

    /* Recycler View instance variables */
    private RecyclerView recyclerView;
    private SearchesRecyclerWithListAdapter searchesRecyclerAdapter;
    private RecyclerView.LayoutManager viewManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recent_searches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        //todo - set list adapter to result of get all searches from DB
    }


    @Override
    public void searchesRecyclerViewListClicked(Search s) {
        //todo - Launch search fragment and set search details in main activity and fill out details in other 2 fragmetns
    }
}
