package com.example.morgan.surf_spot_app.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.morgan.surf_spot_app.R
import com.example.morgan.surf_spot_app.model.Place
import com.example.morgan.surf_spot_app.model.PlacesAPI
import com.example.morgan.surf_spot_app.model.ResultWrapper
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/* Key for list intent content */
const val LIST_INTENT_KEY = "com.example.surfspotapp.LIST"

class MainActivity : AppCompatActivity() {

    /* Latitude & longitude of device */
    private var latitude: Double = 50.4164582
    private var longitude: Double = -5.100202299999978

    /* Input Fields */
    private lateinit var latInput: EditText
    private lateinit var longInput: EditText

    /* If input fields are currently shown */
    private var displayingSearchSection: Boolean = true

    /* Location Manager & Listener */
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    /* Google Places API key */
    private var apiKey: String = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"

//    /* Recycler View */
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var placesRecyclerAdapter: PlacesRecyclerWithListAdapter
//    private lateinit var viewManager: RecyclerView.LayoutManager



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        /* Reload instance state upon screen re-orientation */
//        if(savedInstanceState != null){
//            val savedPlaces: ArrayList<Place> = savedInstanceState.getParcelableArrayList("CURRENT_PLACES")
//            placesRecyclerAdapter = PlacesRecyclerWithListAdapter(this, savedPlaces)
//        }
//        else{
//            placesRecyclerAdapter = PlacesRecyclerWithListAdapter(this)
//        }

        /* Handle on inputs */
        this.latInput = findViewById(R.id.edit_lat)
        this.longInput = findViewById(R.id.edit_long)

        /* Set functionality of buttons */
        var loadLocationButton: Button = findViewById(R.id.load_location_button)
        loadLocationButton.setOnClickListener {
            loadLocationFromGPS()
        }

        var searchButton: Button = findViewById(R.id.run_search_button)
        searchButton.setOnClickListener{
            if(latInput.text.toString().isNotEmpty() && longInput.text.toString().isNotEmpty()) {
                /* Run the search to get a list places */
                runSearch()
            }
            else{
                Snackbar.make(findViewById(R.id.root_layout),
                        R.string.empty_field_exception,
                        Snackbar.LENGTH_SHORT).show()
            }
        }

//        /* Initialise recycler view & adapter */
//        viewManager = LinearLayoutManager(this)
//        recyclerView = findViewById<RecyclerView>(R.id.place_list).apply {
//            // use a linear layout manager
//            layoutManager = viewManager
//            // specify an viewAdapter
//            adapter = placesRecyclerAdapter
//        }

        /* Set up location manager & listener */
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                Log.i("LOCATION", location.toString())
                //Toast.makeText(getApplicationContext(),location.latitude.toString()+" , "+location.longitude.toString(),Toast.LENGTH_SHORT).show();
                setLatitude(location.latitude)
                setLongitude(location.longitude)

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }

        }

        /* Check & request location permissions */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10f, locationListener)
        }
    }

    /**
     * Setter for latitude instance variable.
     */
    fun setLatitude(latitude: Double){
        this.latitude = latitude
    }

    /**
     * Setter for longitude instance variable.
     */
    fun setLongitude(longitude: Double){
        this.longitude = longitude
    }

    /**
     * Create an action bar.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_buttons, menu);
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Handle action bar button clicks.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item != null) {
            if (item.itemId == R.id.key_change_button) {
                /* Prompt user for key input */
                buildChangeApiKeyDialog()
            }
            if (item.itemId == R.id.search_icon){
                if(this.displayingSearchSection) {
                    findViewById<GridLayout>(R.id.search_section_layout).visibility = View.GONE
                    displayingSearchSection = false
                }
                else{
                    findViewById<GridLayout>(R.id.search_section_layout).visibility = View.VISIBLE
                    displayingSearchSection = true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Build and display input dialog for user to change api key.
     */
    private fun buildChangeApiKeyDialog(){

        /* Build alert dialog with title and layout */
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.change_key_dialog_title)
        builder.setView(R.layout.change_api_key_dialog_view)

        /* Display current api key */
        val view = layoutInflater.inflate(R.layout.change_api_key_dialog_view, null)
        val keyTextView= view.findViewById<TextView>(R.id.api_key_text_view)
        if (keyTextView != null) {
            keyTextView.text = this.apiKey
        }
        builder.setView(view)

        /* Positive button changes api key to user input */
        builder.setPositiveButton(R.string.ok){ dialog, _ ->
            val inputField = (dialog as AlertDialog).findViewById<EditText>(R.id.api_key_input)
            val input = inputField!!.text.toString()

            /* Set new api key */
            if(input.isNotEmpty()) {
                this.apiKey = input
            }

            /* Alert user to key change */
            Snackbar.make(findViewById(R.id.root_layout),
                    getString(R.string.key_change_text) + this.apiKey,
                    Snackbar.LENGTH_INDEFINITE).show()
        }

        /* Negative button closes dialog */
        builder.setNegativeButton("Cancel") {dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    /**
     * Update the text fields to display device latitude and
     * longitude taken from the GPS/location service.
     */
    private fun loadLocationFromGPS(){
        /* Display values in text fields */
        this.latInput.setText(""+this.latitude)
        this.longInput.setText(""+this.longitude)

        /* Notify user of success */
        Toast.makeText(this@MainActivity, "Loaded location from GPS.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Builds the set of query parameters and calls the Places API
     */
    private fun runSearch() {

        /* Lat/Long as location string */
        val location: String = latInput.text.toString() + "," + longInput.text.toString()

        /* Create map of query parameter key-values */
        val queryParams: Map<String, String> = mapOf(
                "radius" to 1000.toString() ,
                "type" to "lodging", //REPLACE WIRH 'lodging'
//                "keyword" to "surf",
                "key" to this.apiKey)

        /*  Get a logger */
        val logging = HttpLoggingInterceptor()

        /* Set log level */
        logging.level = HttpLoggingInterceptor.Level.BODY

        /* Add logger as interceptor to HTTP client */
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        /* Build retrofit with logging enabled client */
        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

        /* Get retrofit client interface class and call object to getPlaces GET query */
        val jsonPlaceHolderApi = retrofit.create<PlacesAPI>(PlacesAPI::class.java)
        val call = jsonPlaceHolderApi.getPlaces(location, queryParams)



        call.enqueue(object : Callback<ResultWrapper> {
            override fun onResponse(call: Call<ResultWrapper>, response: Response<ResultWrapper>) {

                /* If not successful print out status code and return */
                if (!response.isSuccessful) {
                    var sb: Snackbar = Snackbar.make(findViewById(R.id.root_layout),
                            "Code: " + response.code(), Snackbar.LENGTH_INDEFINITE)
                    sb.show()
                    return
                }

                /* Results from response body */
                val result = response.body()

                /* Handle result */
                if(result != null) {
                    handleResult(result)
                }
            }

            override fun onFailure(call: Call<ResultWrapper>, t: Throwable) {
                /* On call failure show error message */
                var sb: Snackbar = Snackbar.make(findViewById(R.id.root_layout),
                        t.message.toString(), Snackbar.LENGTH_INDEFINITE)
                sb.show()
            }
        })

        /* Notify user of search */
        Toast.makeText(this@MainActivity, "Searching Places.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Handles result returned by places API call.
     * Either by displaying results or alerting user to status.
     */
    fun handleResult(result: ResultWrapper){

        /* If we have places in our results */
        if (result.results != null && result.results.isNotEmpty()) {

            /* Convert result to (parcelable) ArrayList  */
            val placesResult = ArrayList<Place>(result.results)

            /* Launch intent to send result to ListActivity */
            val listResultsIntent = Intent(this, ListActivity::class.java).apply {
                /* Add parcelable array list as intent extra to send to list activity  */
                putParcelableArrayListExtra("com.example.surfspotapp.LIST", placesResult)
            }

            /* Start ListActivity using Intent */
            startActivity(listResultsIntent)
        }

        /* If no places */
        else if(result.status == "ZERO_RESULTS"){

            /* Tell user there were no results */
            var sb: Snackbar = Snackbar.make(findViewById(R.id.root_layout),
                    getString(R.string.zero_results_text), Snackbar.LENGTH_INDEFINITE)
            sb.show()

//            /* Empty previous list of places */
//            this.placesRecyclerAdapter.clearDataSet()
        }

        /* If other issue {ZERO_RESULTS,OVER_QUERY_LIMIT,REQUEST_DENIED..etc.}  */
        else{

            /* Tell user there was a problem with the request */
            var sb: Snackbar = Snackbar.make(findViewById(R.id.root_layout),
                    getString(R.string.request_issue_text) + result.status,
                    Snackbar.LENGTH_INDEFINITE)
            //sb.view.setBackgroundColor(0x9E1A1A)
            sb.show()

//            /* Empty previous list of places */
//            this.placesRecyclerAdapter.clearDataSet()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList("CURRENT_PLACES",
//                placesRecyclerAdapter.placesArrayList)
    }

}
