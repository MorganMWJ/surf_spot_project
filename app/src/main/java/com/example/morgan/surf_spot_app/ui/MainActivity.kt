package com.example.morgan.surf_spot_app.ui

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.morgan.surf_spot_app.R
import com.example.morgan.surf_spot_app.model.Place
import com.example.morgan.surf_spot_app.model.PlacesAPI
import com.example.morgan.surf_spot_app.model.ResultWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var latitude: Double = 50.4164582
    private var longitude: Double = -5.100202299999978
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var apiKey: String = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"
    private var placesRecyclerAdapter: PlacesRecyclerWithListAdapter
            = PlacesRecyclerWithListAdapter(this@MainActivity)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Set functionality of buttons */
        var loadLocationButton: Button = findViewById(R.id.load_location_button)
        loadLocationButton.setOnClickListener {
            loadLocationFromGPS()
        }

        var searchButton: Button = findViewById(R.id.run_search_button)
        searchButton.setOnClickListener{
            /* If we can parse inputs */
           if(parseValues()){
               /* Run the search to get a list places */
               runSearch()
           }
        }

        /* Initialise recycler view adapter */
        var listPlaces: RecyclerView = findViewById(R.id.place_list)
        listPlaces.adapter = placesRecyclerAdapter
        listPlaces.layoutManager = LinearLayoutManager(this)


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
            Toast.makeText(this@MainActivity,
                    "API key changed: " + this.apiKey
                    , Toast.LENGTH_SHORT).show()
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
        var latTextField: EditText = findViewById(R.id.edit_lat)
        latTextField.setText(""+this.latitude)
        var longTextField: EditText = findViewById(R.id.edit_long)
        longTextField.setText(""+this.longitude)

        /* Notify user of success */
        Toast.makeText(this@MainActivity, "Loaded location from GPS.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Parse both EditText inputs as Doubles.
     * Returns true if parsed false otherwise.
     */
    private fun parseValues(): Boolean {

        var isParsed = false

        /* Get EditText fields */
        val latText = findViewById<EditText>(R.id.edit_lat).text.toString()
        val longText = findViewById<EditText>(R.id.edit_long).text.toString()

        /* Parse their values as doubles if not empty */
        if (!latText.isEmpty() && !longText.isEmpty())
            try {
                this.latitude = java.lang.Double.parseDouble(latText)
                this.longitude = java.lang.Double.parseDouble(longText)
                isParsed = true
            } catch (e1: Exception) {
                Toast.makeText(this@MainActivity, getString(R.string.parse_double_exception), Toast.LENGTH_SHORT).show()
                e1.printStackTrace()
            }
        else
            Toast.makeText(this@MainActivity, getString(R.string.empty_field_exception), Toast.LENGTH_SHORT).show()

        return isParsed
    }

    private fun runSearch() {

        /* Lat/Long as location string */
        val location: String = this.latitude.toString() + "," + this.longitude.toString()
        //val location: String = "50.4164582,-5.100202299999978"

        /* Create map of query parameter key-values */
        val queryParams: Map<String, String> = mapOf(
                "radius" to 1000.toString() ,
                "type" to "lodging", //REPLACE WIRH 'lodging'
                "keyword" to "surf",
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
                    alterResultsTextView("Code: " + response.code(), true)
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
                /* On call failure print our error message */
                alterResultsTextView(t.message, true)
            }
        })
        Toast.makeText(this@MainActivity, "You clicked search.", Toast.LENGTH_SHORT).show()

    }

    /**
     * Handles result returned by places API call.
     * Either by displaying results or alerting user to status.
     */
    fun handleResult(result: ResultWrapper){

        /* If we have places in our results */
        if (result.results != null && !result.results.isEmpty()) {

            /* Update our view to display them */
            this.placesRecyclerAdapter.changeDataSet(result.results)
            alterResultsTextView(null, false)
        }

        /* If no places */
        else if(result.status == "ZERO_RESULTS"){

            /* Tell user there were no results */
            alterResultsTextView("Zero Results", true)
            /* Empty previous list of places */
            this.placesRecyclerAdapter.clearDataSet()
        }

        /* If api key wrong */
        else if(result.status == "REQUEST_DENIED"){
            /* Tell user there was a problem with the request */
            alterResultsTextView(
                    "Problem with api or key.\nStatus: " + result.status, true)
            /* Empty previous list of places */
            this.placesRecyclerAdapter.clearDataSet()
        }
    }

    /**
     * Helper function to edit remove duplicate code.
     * Edits text display should result not return places.
     */
    private fun alterResultsTextView(message: String?, isShown: Boolean){
        var txtView = findViewById<TextView>(R.id.results_text)

        if(message != null)
            txtView.text = message

        if(isShown)
            txtView.visibility=View.VISIBLE
        else
            txtView.visibility = View.GONE
    }

}
