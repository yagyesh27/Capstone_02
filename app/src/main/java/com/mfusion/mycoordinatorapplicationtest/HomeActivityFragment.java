package com.mfusion.mycoordinatorapplicationtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    static String JSONstring;
    String imageUrl1,imageUrl2,imageUrl3,imageUrl4,imageUrl5,imageUrl6;
    String[] imageUrls = {"", "", "", "", "", ""};
    String[] artImageUrls = {"", "", "", "", "", ""};
    Intent intent = null;
    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    public String latitude = "";
    public String longitude = "";
    public String country = "";
    public String countryPrev = "";
    View rootView;

    public interface Callback{

        public void onItemSelected(Bundle extras);
    }

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);



        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG).show();
            }
        });

        intent = new Intent(getActivity(), ArticlesListActivity.class);

        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        mGoogleApiClient.connect();


        return rootView;
    }







    @Override
    public void onConnected(Bundle bundle) {

        Log.d("onConnected", "onConnected");



        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2500);

        try {
            requestPermissions(INITIAL_PERMS,102);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception e){
            Log.e("Main_Weather",e.toString());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("Latitude", (Double.toString(location.getLatitude())));
        Log.d("Longitude", (Double.toString(location.getLongitude())));

        latitude = Double.toString(location.getLatitude());
        longitude = Double.toString(location.getLongitude());




        new FetchWeatherdata().execute("weather");

        if(!country.equals(countryPrev)){
            new Fetchdata().execute("Thumbnail");
            countryPrev = country;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public class Fetchdata extends AsyncTask<String, Void, ArrayList> {


        protected ArrayList doInBackground(String[] params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;



            String dataJsonStr = null;

            try {


                String baseurl = "https://newsapi.org/v1/sources?language=en";


                Uri builturi1 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "general")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("country",country)
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                Uri builturi2 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "business")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                Uri builturi3 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "technology")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                Uri builturi4 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "entertainment")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                Uri builturi5 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "sport")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                Uri builturi6 = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("category", "science-and-nature")
                        .appendQueryParameter("language", "en")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();

                URL url = new URL(builturi1.toString());
                URL url1 = new URL(builturi1.toString());
                URL url2 = new URL(builturi2.toString());
                URL url3 = new URL(builturi3.toString());
                URL url4 = new URL(builturi4.toString());
                URL url5 = new URL(builturi5.toString());
                URL url6 = new URL(builturi6.toString());



                Log.d("Arr_imageurl","strt");
                imageUrls[0] = getImgUrl(url1);
                Log.d("Arr_imageurl","afterFirst");
                Log.d("Arr_imageurl","strt");
                artImageUrls[0] = getImgArtUrl(imageUrls[0]);
                Log.d("Arr_imageurl","afterFirst");

                imageUrls[1] = getImgUrl(url2);
                artImageUrls[1] = getImgArtUrl(imageUrls[1]);

                imageUrls[2] = getImgUrl(url3);
                artImageUrls[2] = getImgArtUrl(imageUrls[2]);

                imageUrls[3] = getImgUrl(url4);
                artImageUrls[3] = getImgArtUrl(imageUrls[3]);

                imageUrls[4] = getImgUrl(url5);
                artImageUrls[4] = getImgArtUrl(imageUrls[4]);

                imageUrls[5] = getImgUrl(url6);
                artImageUrls[5] = getImgArtUrl(imageUrls[5]);















            }catch (IOException e){
                Log.e("MainActivity", "Error ", e);
            }





            ArrayList a = new ArrayList();

            return a;

        }


        public String getImgUrl(URL url){
            String imageUrlStr = "";
            String dataJsonStrImg;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String sourceId = "";

            try{


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                dataJsonStrImg = buffer.toString();
                Log.d("Jsondata1", dataJsonStrImg);

            } catch (IOException e) {
                Log.e("MainActivity", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            ArrayList a = new ArrayList();
            try {
                Log.d("Jsondata", ".JSONObject>");
                JSONObject articleJson = new JSONObject(dataJsonStrImg);
                Log.d("Jsondata", ".JSONObject<");
                JSONArray sources = articleJson.getJSONArray("sources");
                Log.d("Jsondata", ".length()>");
                String[] titles = new String[sources.length()];
                Log.d("Jsondata", ".length()<");

                JSONObject ob;
                for (int i = 0; i < sources.length(); i++) {
                    ob = sources.getJSONObject(i);
                    JSONObject urls = ob.getJSONObject("urlsToLogos");

                    if(i==0){
                        sourceId = ob.getString("id");
                        imageUrlStr = urls.getString("small");
                        Log.d("UrlStr",imageUrlStr);
                    }

                    a.add(0, titles);




                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return sourceId;
        }

        public String getImgArtUrl(String srcId){
            String imageUrlStr = "";
            String dataJsonStrImg;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try{
                String baseurl = "https://newsapi.org/v1/articles?";


                Uri builturi = Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("source",srcId)
                        .appendQueryParameter("sortBy", "top")
                        .appendQueryParameter("apiKey", getString(R.string.api_key))
                        .build();
                URL url = new URL(builturi.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                dataJsonStrImg = buffer.toString();
                Log.d("Jsondata1", dataJsonStrImg);

            } catch (IOException e) {
                Log.e("MainActivity", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            ArrayList a = new ArrayList();
            try {
                Log.d("Jsondata", ".JSONObject>");
                JSONObject articleJson = new JSONObject(dataJsonStrImg);
                Log.d("Jsondata", ".JSONObject<");
                JSONArray articles = articleJson.getJSONArray("articles");
                Log.d("Jsondata", ".length()>");
                String[] titles = new String[articles.length()];
                Log.d("Jsondata", ".length()<");

                JSONObject ob;
                for (int i = 0; i < articles.length(); i++) {
                    ob = articles.getJSONObject(i);


                    if(i==0){

                        imageUrlStr = ob.getString("urlToImage");
                        Log.d("UrlStrArt",imageUrlStr);
                    }

                    a.add(0, titles);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return imageUrlStr;

        }


        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);


            GridImgViewAdap adapter = new GridImgViewAdap(getActivity());
            GridView grid = (GridView) rootView.findViewById(R.id.gridView);
            grid.setAdapter(adapter);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("Clicked", "Clicked");
                    Bundle extras = new Bundle();
                    extras.putString("srcId",imageUrls[i]);
                    ((Callback) getActivity()).onItemSelected(extras);

                }
            });



        }
    }


    public class FetchWeatherdata extends AsyncTask<String , Void , ArrayList> {

        String weatherJsonString = "";
        int weatherId;


        protected ArrayList doInBackground(String[] params) {




            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String dataJsonStr = null;

            if(latitude.length() == 0){
                latitude = getString(R.string.lat);
                longitude = getString(R.string.lon);
            }

            try {

                String baseurl ="http://api.openweathermap.org/data/2.5/weather?";
                String sort_param = params[0];

                Uri builturi= Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter("lat", latitude)
                        .appendQueryParameter("lon", longitude)
                        .appendQueryParameter("appid", getString(R.string.weather_api_key))
                        .build();
                URL url = new URL(builturi.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                dataJsonStr = buffer.toString();
                weatherJsonString = dataJsonStr;
                Log.d("Jsondata", dataJsonStr);
                Log.d("Jsondata", weatherJsonString);
            } catch (IOException e) {
                Log.e("MainActivityWeather", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            ArrayList a = new ArrayList();
            try {
                JSONObject weatherJson = new JSONObject(weatherJsonString);
                JSONArray weather = weatherJson.getJSONArray("weather");
                JSONObject sys = weatherJson.getJSONObject("sys");

                country = (sys.getString("country")).toLowerCase();

                Log.d("country",country);

                JSONObject weatherObj = weather.getJSONObject(0);
                weatherId = weatherObj.getInt("id");

                Log.d("weatherId",Integer.toString(weatherId));




            }catch (JSONException e) {
                e.printStackTrace();
            }


            return a;

        }



        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);

            ((ImageView)rootView.findViewById(R.id.weather_img)).setImageResource(getIconResourceForWeatherCondition(weatherId));






        }

        public  int getIconResourceForWeatherCondition(int weatherId) {
            // Based on weather code data found at:
            // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
            if (weatherId >= 200 && weatherId <= 232) {
                return R.drawable.ic_storm;
            } else if (weatherId >= 300 && weatherId <= 321) {
                return R.drawable.ic_light_rain;
            } else if (weatherId >= 500 && weatherId <= 504) {
                return R.drawable.ic_rain;
            } else if (weatherId == 511) {
                return R.drawable.ic_snow;
            } else if (weatherId >= 520 && weatherId <= 531) {
                return R.drawable.ic_rain;
            } else if (weatherId >= 600 && weatherId <= 622) {
                return R.drawable.ic_snow;
            } else if (weatherId >= 701 && weatherId <= 761) {
                return R.drawable.ic_fog;
            } else if (weatherId == 761 || weatherId == 781) {
                return R.drawable.ic_storm;
            } else if (weatherId == 800) {
                return R.drawable.ic_clear;
            } else if (weatherId == 801) {
                return R.drawable.ic_light_clouds;
            } else if (weatherId >= 802 && weatherId <= 804) {
                return R.drawable.ic_cloudy;
            }
            return -1;
        }


    }




    public class GridImgViewAdap extends BaseAdapter {
        private Context mContext;
        Integer[] mThumbnails;
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int i) {
            return mThumbnails[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public GridImgViewAdap(Context c) {
            mContext = c;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {


                grid = inflater.inflate(R.layout.grid_cell_home,null);
                ImageView imageView = (ImageView)grid.findViewById(R.id.imageViewGrid);
                imageView.setAdjustViewBounds(true);

                Picasso.with(mContext).load(artImageUrls[i]).into(imageView);


            } else {
                grid = (View) convertView;
            }

            return grid;
        }
    }
}
