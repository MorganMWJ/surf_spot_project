package com.example.morgan.surf_spot_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.morgan.surf_spot_app.R
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment() {

    /* Input Fields */
    private lateinit var latInput: EditText
    private lateinit var longInput: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* Handle on inputs */
        this.latInput = view.findViewById(R.id.edit_lat)
        this.longInput = view.findViewById(R.id.edit_long)

        /* Reload instance state upon screen re-orientation */
        if(savedInstanceState != null) {
            this.latInput.setText(savedInstanceState.getString("lat_input"))
            this.longInput.setText(savedInstanceState.getString("long_input"))
        }

        /* Set functionality of load location button */
        var loadLocationButton: Button = view.findViewById(R.id.load_location_button)
        loadLocationButton.setOnClickListener {
            (activity as MainActivity?)?.loadLocationFromGPS(this.latInput, this.longInput)
        }

        /* Set functionality of search button */
        var searchButton: Button = view.findViewById(R.id.run_search_button)
        searchButton.setOnClickListener{
            if(latInput.text.toString().isNotEmpty() && longInput.text.toString().isNotEmpty()) {
                /* Pass latitude & longitude to MainActivity */
                val lat: Double = latInput.text.toString().toDouble()
                (activity as MainActivity?)?.setLatitude(lat)
                val lng: Double = longInput.text.toString().toDouble()
                (activity as MainActivity?)?.setLongitude(lng)
                /* Run the search to get a list places */
                (activity as MainActivity?)?.runSearch()
            }
            else{
                Snackbar.make(view.findViewById(R.id.search_frag),
                        R.string.empty_field_exception,
                        Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lat_input", this.latInput.text.toString())
        outState.putString("long_input", this.longInput.text.toString())
    }
}