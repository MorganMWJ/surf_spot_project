package com.example.morgan.surf_spot_app.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.Photo;
import com.example.morgan.surf_spot_app.model.Place;
import com.example.morgan.surf_spot_app.model.PlacesAPI;
import com.example.morgan.surf_spot_app.model.db.Search;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewActivity extends AppCompatActivity {

    /* The place object this activity is displaying */
    private Place place;

    /* Search object that this activity is displaying results for */
    private Search currentSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        /* Extract Place object from intent */
        this.place = getIntent().getParcelableExtra("com.example.surfspotapp.PLACE_TO_VIEW");

        /* Extract Search object from intent */
        this.currentSearch = getIntent().getParcelableExtra("com.example.surfspotapp.SEARCH");

        /* Get Image for Place */
        requestImages();

        /* Populate with Place object attributes */
        TextView name = findViewById(R.id.place_name);
        name.setText(this.place.getName());

        if (this.place.getRating() != null) {
            TextView rating = findViewById(R.id.place_rating);
            rating.setText(this.place.getRating().toString());
        }

        if(this.place.getOpeningHours() != null) {
            TextView openHours = findViewById(R.id.place_open_hours);
            openHours.setText(this.place.getOpeningHours().toHtmlString());
        }

        if(this.place.getPriceLevel() != null) {
            TextView priceLevel = findViewById(R.id.place_price_level);
            priceLevel.setText(this.place.getPriceLevel().toString());
        }

        if(this.place.getWebsite() != null) {
            TextView website = findViewById(R.id.place_website);
            website.setText(this.place.getWebsite());
        }

        if(this.place.getAddress() != null) {
            TextView address = findViewById(R.id.place_address);
            address.setText(this.place.getAddress());
        }

        if(this.place.getPhoneNumber() != null) {
            TextView phone = findViewById(R.id.place_phone_number);
            phone.setText(this.place.getPhoneNumber());
        }
        //todo
    }

    private void requestImages(){

        /*  Get a logger */
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        /* Set log level */
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        /* Add logger as interceptor to HTTP client */
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        /* Build retrofit with logging enabled client */
        Retrofit retrofit = (new Retrofit.Builder())
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        /* Get photo details to query in form of query parameters */
        Photo photoToGet = this.place.getPhotos()[0];

        /* Get retrofit client interface class and call object to getPlaceImage GET query */
        PlacesAPI jsonPlaceHolderApi = retrofit.create(PlacesAPI.class);
        Call call = jsonPlaceHolderApi.getPlaceImage(photoToGet.getQueryParameters(this.currentSearch.getApiKey()));

        /* Deal with HTTP response */
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                try ( ResponseBody responseBody = (ResponseBody) response.body()) {
                    /* Read the jpeg data(bytes) from the stream */
                    InputStream is = responseBody.byteStream();

                    /* Create Bitmap from byte stream */
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    /* Set BitMap to be content of ImageView */
                    setPlaceImageView(bitmap);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                /* On call failure show error message */
                Snackbar sb = Snackbar.make(findViewById(R.id.root_layout_view_activity),
                        t.getMessage(), Snackbar.LENGTH_INDEFINITE);
                sb.show();
            }
        });
    }

    /**
     * Sets BitMap returned from API GET request to be content of ImageView.
     * @param bitmap
     */
    private void setPlaceImageView(Bitmap bitmap){
        ImageView picture = findViewById(R.id.place_image);
        picture.setImageBitmap(bitmap);
    }
}
