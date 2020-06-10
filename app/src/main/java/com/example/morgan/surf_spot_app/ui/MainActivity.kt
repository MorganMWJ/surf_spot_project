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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.morgan.surf_spot_app.R
import com.example.morgan.surf_spot_app.model.Place
import com.example.morgan.surf_spot_app.model.PlacesAPI
import com.example.morgan.surf_spot_app.model.ResultWrapper
import com.example.morgan.surf_spot_app.model.db.AppDatabase
import com.example.morgan.surf_spot_app.model.db.Search
import com.example.morgan.surf_spot_app.model.db.SearchDao
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    /* Latitude & longitude of device */
    private var latitude: Double = 50.4164582
    private var longitude: Double = -5.100202299999978

    /* Input Fields */
    private lateinit var latInput: EditText
    private lateinit var longInput: EditText
    private lateinit var keyInput: EditText

    /* If extra settings input fields are currently shown */
    private var displayingSettingsSection: Boolean = true

    /* If displaying change API key section */
    private var displayingChangeApiKeySection: Boolean = true

    /* Location Manager & Listener */
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    /* Default Google Places API key */
    private val apiKey: String = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"

    /* User set Google Places API key */
    private var currentApiKey: String = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"

    /* Inputs for extra options section */
    private lateinit var radiusInput: SeekBar
    private lateinit var surfKeywordInput: CheckBox
    private lateinit var placeTypeInput: Spinner

    /* Access to DB */
    private lateinit var searchDao: SearchDao




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

        /* Handle on inputs */
        this.latInput = findViewById(R.id.edit_lat)
        this.longInput = findViewById(R.id.edit_long)
        this.keyInput = findViewById(R.id.edit_key)
        this.surfKeywordInput = findViewById(R.id.keyword_checkbox)
        this.placeTypeInput = findViewById(R.id.type_dropdown)
        this.radiusInput = findViewById(R.id.radius_bar)

        /* By default 'surf' keyword is used */
        this.surfKeywordInput.isChecked = true

        /* Default search radius is 10000m / 10km */
        this.radiusInput.progress = 10

        /* Reload instance state upon screen re-orientation */
        if(savedInstanceState != null) {
            this.latInput.setText(savedInstanceState.getString("lat_input"))
            this.longInput.setText(savedInstanceState.getString("long_input"))
            this.radiusInput.progress = savedInstanceState.getInt("radius_input")
            this.placeTypeInput.setSelection(savedInstanceState.getInt("type_input_spinner_position"))
            this.surfKeywordInput.isChecked = savedInstanceState.getBoolean("use_surf_input")
            this.keyInput.setText(savedInstanceState.getString("key_input"))
        }

        /* Functionality of radius SeekBar */
        this.radiusInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                /* Do Nothing */
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                /* Do Nothing */
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                /* Alert user to successful change of radius */
                Toast.makeText(this@MainActivity,
                        "Radius set: " + seek.progress + "km",
                        Toast.LENGTH_SHORT).show()
            }
        })

        /* Set functionality of load location button */
        var loadLocationButton: Button = findViewById(R.id.load_location_button)
        loadLocationButton.setOnClickListener {
            loadLocationFromGPS()
        }

        /* Set functionality of search button */
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

        /* Set functionality of reset default API key button */
        var resetKeyButton: Button = findViewById(R.id.reset_default_api_key_button)
        resetKeyButton.setOnClickListener{
            currentApiKey = apiKey
            var currentKeyTextView: TextView = findViewById(R.id.current_key)
            currentKeyTextView.text = currentApiKey
            keyInput.text.clear()
        }

        /* Set functionality of set API key button */
        var setKeyButton: Button = findViewById(R.id.set_api_key_button)
        setKeyButton.setOnClickListener{
            /* Get text from new key input */
            currentApiKey = keyInput.text.toString()
            var currentKeyTextView: TextView = findViewById(R.id.current_key)
            currentKeyTextView.text = currentApiKey
            keyInput.text.clear()
        }

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
                /* Do Nothing */
            }

            override fun onProviderEnabled(provider: String) {
                /* Do Nothing */
            }

            override fun onProviderDisabled(provider: String) {
                /* Do Nothing */
            }

        }

        /* Check & request location permissions */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10f, locationListener)
        }

        /* Use DB instance to get a handle on SearchDao */
        this.searchDao = AppDatabase.getDatabase(this).searchDao()
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
        menuInflater.inflate(R.menu.main_activity_action_bar_buttons, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Handle action bar button clicks.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item != null) {
            if (item.itemId == R.id.key_change_icon) {
                if(this.displayingChangeApiKeySection) {
                    findViewById<GridLayout>(R.id.api_key_section_layout).visibility = View.GONE
                    displayingChangeApiKeySection = false
                }
                else{
                    findViewById<GridLayout>(R.id.api_key_section_layout).visibility = View.VISIBLE
                    displayingChangeApiKeySection = true
                }
            }
            if (item.itemId == R.id.settings_icon){
                if(this.displayingSettingsSection) {
                    findViewById<GridLayout>(R.id.settings_section_layout).visibility = View.GONE
                    displayingSettingsSection = false
                }
                else{
                    findViewById<GridLayout>(R.id.settings_section_layout).visibility = View.VISIBLE
                    displayingSettingsSection = true
                }
            }
        }

        return super.onOptionsItemSelected(item)
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
     * Builds the a search object and calls the Places API
     */
    private fun runSearch() {

        /* Latitude from input */
        val lat: Double = latInput.text.toString().toDouble()
        /* Longitude from input */
        val lng: Double = longInput.text.toString().toDouble()
        /* Search Radius for SeekBar */
        var radius = radiusInput.progress
        /* Type of place from dropdown */
        var placeType = placeTypeInput.selectedItem.toString()
        /* If to use surf as search keyword */
        var useSurfKeyword = surfKeywordInput.isChecked

        /* Create Search object from inputs */
        val newSearch = Search(lat, lng, radius, placeType, useSurfKeyword)
        newSearch.apiKey = this.currentApiKey

        /* Save search in the database */
        searchDao.insertSearch(newSearch)

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
        val jsonPlaceHolderApi = retrofit.create(PlacesAPI::class.java)
        val call = jsonPlaceHolderApi.getPlaces(newSearch.locationString, newSearch.queryParameters)



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
                    handleResult(result, newSearch)
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
    fun handleResult(result: ResultWrapper, search: Search){

        /* If we have places in our results */
        if (result.results != null && result.results.isNotEmpty()) {

            /* Convert result to (parcelable) ArrayList  */
            val placesResult = ArrayList<Place>(result.results)

            /* Launch intent to send result to ListActivity */
            val listResultsIntent = Intent(this, ListActivity::class.java).apply {
                /* Add parcelable array list as intent extra to send to list activity  */
                putParcelableArrayListExtra("com.example.surfspotapp.LIST", placesResult)
                /* Add parcelable Search object as intent extra to send to list activity */
                putExtra("com.example.surfspotapp.SEARCH_OBJECT", search);
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
        }

        /* If other issue {ZERO_RESULTS,OVER_QUERY_LIMIT,REQUEST_DENIED..etc.}  */
        else{

            /* Tell user there was a problem with the request */
            var sb: Snackbar = Snackbar.make(findViewById(R.id.root_layout),
                    getString(R.string.request_issue_text) + result.status,
                    Snackbar.LENGTH_INDEFINITE)
            //sb.view.setBackgroundColor(0x9E1A1A)
            sb.show()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lat_input", this.latInput.text.toString())
        outState.putString("long_input", this.longInput.text.toString())
        outState.putInt("radius_input", this.radiusInput.progress)
        outState.putInt("type_input_spinner_position", this.placeTypeInput.selectedItemPosition)
        outState.putBoolean("use_surf_input", this.surfKeywordInput.isChecked)
        outState.putString("key_input", this.keyInput.text.toString())
    }

}
