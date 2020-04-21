package com.example.morgan.surf_spot_app.ui

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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
    private var apiKey: String = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"
    private var placesRecyclerAdapter: PlacesRecyclerWithListAdapter
            = PlacesRecyclerWithListAdapter(this@MainActivity)

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


        /* Ask user for location permission during runtime */
        //checkLocationPermission()
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
     * Get current device latitude and longitude from the GPS/location service.
     * Store the values as instance variables.
     * Update the text fields to display those values.
     */
    private fun loadLocationFromGPS(){
        var temp = 5
//        try {
//
//            /* Get device latitude and longitude */
//            var lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            var location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            this.longitude = location.longitude
//            this.latitude = location.latitude
//
//            /* Display values in text fields */
//            var latTextField: EditText = findViewById(R.id.edit_lat)
//            latTextField.setText(""+this.latitude)
//            var longTextField: EditText = findViewById(R.id.edit_long)
//            longTextField.setText(""+this.longitude)
//
//            /* Notify user of success */
//            Toast.makeText(this@MainActivity, "Loaded location from GPS.", Toast.LENGTH_SHORT).show()
//        }
//        catch(se: SecurityException){
//            /* Notify user of exception */
//            Toast.makeText(this@MainActivity, "Security Exception: Location not enabled", Toast.LENGTH_SHORT).show()
//        }
    }

    /**
     * Parse both EditText inputs as Doubles.
     * Returns true if parsed false otherwise.
     */
    private fun parseValues(): Boolean {

        var isParsed: Boolean = false

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
        //val location: String = this.latitude.toString() + "," + this.longitude.toString()
        val location: String = "50.4164582,-5.100202299999978"

        /* Create map of query parameter key-values */
        val queryParams: Map<String, String> = mapOf(
                "radius" to 1500.toString() ,
                "type" to "cafe", //REPLACE WIRH 'lodging'
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

                if (!response.isSuccessful) {
                    var textViewResult = findViewById<TextView>(R.id.text_view_result)
                    textViewResult.text = "Code: " + response.code()
                    return
                }

                /* Results from response body */
                val result = response.body()

                /* Display results */
                if(result != null) {
                    updateListView(result)
                }

            }

            override fun onFailure(call: Call<ResultWrapper>, t: Throwable) {
                var textViewResult = findViewById<TextView>(R.id.text_view_result)
                textViewResult.text = t.message
            }
        })
        Toast.makeText(this@MainActivity, "You clicked search.", Toast.LENGTH_SHORT).show()

    }

    fun updateListView(result: ResultWrapper){

        /* If we have places in our results */
        if (result.results != null) {

            /* And it is not an empty list  */
            if (!result.results.isEmpty()) {

                /* Update our view to display them */
                this.placesRecyclerAdapter.changeDataSet(result.results)
            }
        }
        else{
            //todo
            //deal with status code error (alert user)
        }
    }

}
