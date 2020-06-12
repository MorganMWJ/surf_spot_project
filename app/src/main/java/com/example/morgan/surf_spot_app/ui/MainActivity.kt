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
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.morgan.surf_spot_app.R
import com.example.morgan.surf_spot_app.model.Place
import com.example.morgan.surf_spot_app.model.PlacesAPI
import com.example.morgan.surf_spot_app.model.ResultWrapper
import com.example.morgan.surf_spot_app.model.db.AppDatabase
import com.example.morgan.surf_spot_app.model.db.Search
import com.example.morgan.surf_spot_app.model.db.SearchDao
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    /* Latitude & longitude of device */
    private var deviceLatitude: Double = 50.4164582
    private var deviceLongitude: Double = -5.100202299999978

    /* Search settings */
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var radius: Int = 1000
    private var placeType: String = "Lodging"
    private var useSurfKeyword: Boolean = true

    /* Default Google Places API key */
    private val apiKey = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"

    /* User set Google Places API key */
    private var currentApiKey = "AIzaSyCgG0fI-uAhdByF0L63kB-9hjuWTjMpqwM"

    /* Location Manager & Listener */
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    /* Access to DB */
    private lateinit var searchDao: SearchDao

    private lateinit var pagerAdapter: SectionsPagerAdapter
    private lateinit var pager: ViewPager


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


        /* Set up location manager & listener */
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                Log.i("LOCATION", location.toString())
                setDeviceLatitude(location.latitude)
                setDeviceLongitude(location.longitude)
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


        /* attach the adapter to the view pager - for swipe functionality */
        this.pagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        this.pager = findViewById(R.id.pager)

        this.pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                when (i) {
                    0, 1, 2 -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

        this.pager.adapter = pagerAdapter

        /* Associate the TabLayout it with the view pager */
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }

    /**
     * Setter for deviceLatitude instance variable.
     */
    fun setDeviceLatitude(dl: Double){
        this.deviceLatitude = dl
    }

    /**
     * Setter for deviceLongitude instance variable.
     */
    fun setDeviceLongitude(dl: Double){
        this.deviceLongitude = dl
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
     * Setter for radius instance variable.
     */
    fun setRadius(radius: Int){
        this.radius = radius
    }

    /**
     * Setter for placeType instance variable.
     */
    fun setPlaceType(placeType: String){
        this.placeType = placeType
    }

    /**
     * Setter for surfKeyword instance variable.
     */
    fun setSurfKeyword(surfKeyword: Boolean){
        this.useSurfKeyword = surfKeyword
    }

    /**
     * Set the currently used API key to the default.
     */
    fun resetApiKey(){
        this.currentApiKey = this.apiKey
    }

    /**
     * Setter for currentApiKey instance variable.
     */
    fun setCurrentApiKey(currentApiKey: String){
        this.currentApiKey = currentApiKey
    }

    /**
     * Getter for currentApiKey instance variable.
     */
    fun getCurrentApiKey(): String{
        return this.currentApiKey
    }

//    /**
//     * Create an action bar.
//     */
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_activity_action_bar_buttons, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    /**
//     * Handle action bar button clicks.
//     */
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        if(item != null) {
//            if (item.itemId == R.id.key_change_icon) {
//                if(this.displayingChangeApiKeySection) {
//                    findViewById<GridLayout>(R.id.api_key_section_layout).visibility = View.GONE
//                    displayingChangeApiKeySection = false
//                }
//                else{
//                    findViewById<GridLayout>(R.id.api_key_section_layout).visibility = View.VISIBLE
//                    displayingChangeApiKeySection = true
//                }
//            }
//            if (item.itemId == R.id.settings_icon){
//                if(this.displayingSettingsSection) {
//                    findViewById<GridLayout>(R.id.settings_section_layout).visibility = View.GONE
//                    displayingSettingsSection = false
//                }
//                else{
//                    findViewById<GridLayout>(R.id.settings_section_layout).visibility = View.VISIBLE
//                    displayingSettingsSection = true
//                }
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    /**
     * Set search lat/long to device location.
     * Update the text fields to display device latitude & longitude.
     */
    fun loadLocationFromGPS(latInput: EditText, longInput: EditText){

        /* Set lat/long to device location */
        setLongitude(this.deviceLongitude)
        setLatitude(this.deviceLatitude)

        /* Display values in text fields */
        latInput.setText(""+this.latitude)
        longInput.setText(""+this.longitude)

        /* Notify user of success */
        Toast.makeText(this@MainActivity, "Loaded location from GPS.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Builds the a search object and calls the Places API
     */
    fun runSearch() {

        /* Create Search object from inputs */
        val newSearch = Search(latitude, longitude, radius, placeType, useSurfKeyword)
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
                    var sb: Snackbar = Snackbar.make(findViewById(android.R.id.content),
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
                //var currentFragment: Fragment? = pagerAdapter.getItem(pager.currentItem)

                var sb: Snackbar = Snackbar.make(findViewById(android.R.id.content),
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
            var sb: Snackbar = Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.zero_results_text), Snackbar.LENGTH_INDEFINITE)
            sb.show()
        }

        /* If other issue {ZERO_RESULTS,OVER_QUERY_LIMIT,REQUEST_DENIED..etc.}  */
        else{
            pager.adapter
            /* Tell user there was a problem with the request */
            var sb: Snackbar = Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.request_issue_text) + result.status,
                    Snackbar.LENGTH_INDEFINITE)
            //sb.view.setBackgroundColor(0x9E1A1A)
            sb.show()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putString("lat_input", this.latInput.text.toString())
//        outState.putString("long_input", this.longInput.text.toString())
//        outState.putInt("radius_input", this.radiusInput.progress)
//        outState.putInt("type_input_spinner_position", this.placeTypeInput.selectedItemPosition)
//        outState.putBoolean("use_surf_input", this.surfKeywordInput.isChecked)
//        outState.putString("key_input", this.keyInput.text.toString())
    }

    //a class that knows how to manage fragments
    //adapter needed to associate fragments with the view pager
    private class SectionsPagerAdapter  //NEED THIS CONSTRUCTOR WILL LEARN WHY LATER
    (fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        //where we create and return fragment objects depending on which page is being displayed
        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return RecentSearchesFragment()
                1 -> return SearchFragment()
                2 -> return SettingsFragment()
            }
            return null
        }

        //method adds labels to each of our tabs
        //allows each swipe position to have a page title
        override fun getPageTitle(pos: Int): CharSequence? {
            when (pos) {
                0 -> return "History"
                1 -> return "Search"
                2 -> return "Settings"
            }
            return null
        }

        //number of pages the view pager supports -  one for each tab
        override fun getCount(): Int {
            return 3
        }
    }

}
