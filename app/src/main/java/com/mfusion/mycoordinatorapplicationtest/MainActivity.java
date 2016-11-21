package com.mfusion.mycoordinatorapplicationtest;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
/*import android.support.v4.app.LoaderManager;*/
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImage;
import com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener/*, LoaderManager.LoaderCallbacks<ArrayList<ArticleSourceImage>>*/,LoaderManager.LoaderCallbacks<Cursor> {

    static String JSONstring;
    String imageUrl1,imageUrl2,imageUrl3,imageUrl4,imageUrl5,imageUrl6;
    String[] imageUrls = {"", "", "", "", "", ""};
    String[] artImageUrls = {"", "", "", "", "", ""};
    String[] artTitles = {"", "", "", "", "", ""};
    Intent intent = null;
    GridImgViewAdap adapter;
    private static final int URL_LOADER = 0;
    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    public String latitude = "";
    public String longitude = "";
    public String country = "";
    public String countryPrev = "";
    public String place = "";
    public String weatherDesc = "";
    public double maxTemp ;
    public double minTemp ;
    public double speed ;
    int weatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String URL = "content://com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";

                Uri students = Uri.parse(URL);
                Cursor c = getContentResolver().query(students, null, null, null, "name");

                if (c.moveToFirst()) {
                    do{
                        Toast.makeText(MainActivity.this,
                                c.getString(c.getColumnIndex( ArticleSourceImage.COL_SOURCE)) +
                                        ", " + c.getString(c.getColumnIndex( ArticleSourceImage.COL_ART_IMG_URL)),
                                Toast.LENGTH_SHORT).show();
                    } while (c.moveToNext());
                }

                /*Log.d("DB return", c.getString(c.getColumnIndex( ArticleSourceImage.COL_SOURCE)) +
                        ", " + c.getString(c.getColumnIndex( ArticleSourceImage.COL_ART_IMG_URL)));*/

                Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.weather_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),WeatherActivity.class);
                i.putExtra("weatherId",weatherId);
                i.putExtra("country",country);
                i.putExtra("place",place);
                i.putExtra("maxTemp",maxTemp);
                i.putExtra("minTemp",minTemp);
                i.putExtra("speed",speed);
                i.putExtra("weatherDesc",weatherDesc);

                startActivity(i);



            }
        });

        intent = new Intent(getApplicationContext(), DetailActivity.class);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Tool bar http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=weatherkey

        /*findViewById(R.id.thumbnail1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("inMain","next is intent call");

                startActivity(intent);

                *//*i.putExtra("poster_url", posters[position]);
                i.putExtra("title", title[position]);
                i.putExtra("release_date", release_date[position]);
                i.putExtra("vote_avg", vote_avg[position]);
                i.putExtra("plot_synopsis", plot_synopsis[position]);

                startActivity(i);*//*
            }
        });*/

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        mGoogleApiClient.connect();

        getLoaderManager().initLoader(URL_LOADER, null, this);

        //new FetchWeatherdata().execute("weather");
        //new Fetchdata().execute("Thumbnail");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_location) {
            startActivity(new Intent(getApplicationContext(), PositionActivity.class));
            return true;
        }
        if (id == R.id.action_places) {
            startActivity(new Intent(getApplicationContext(), PlacesActivity.class));
            return true;
        }
        if (id == R.id.action_home) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d("onConnected","onConnected");



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


        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();



       // Toast.makeText(MainActivity.this, "Latitude = " + (Double.toString(location.getLatitude())) + "Longitude = " + (Double.toString(location.getLongitude())), Toast.LENGTH_LONG).show();
      if(activeNetworkInfo.isConnected()) {

          Log.d("Location","Location dvdvdb" + activeNetworkInfo.isConnected());

          new FetchWeatherdata().execute("weather");
        /*new Fetchdata().execute("Thumbnail");*/
          if (!country.equals(countryPrev)) {



              new Fetchdata().execute("Thumbnail");
              countryPrev = country;
          }

      }else{
          Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
      }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.parse("content://com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider")
                , new String[]{"source", "artImgUrl", "artTitle" }, null, null, null);
        /*return null;*/
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {

        ArrayList list =  new ArrayList();






        if (cursor.moveToFirst()) {
            do{
                Log.d("Cursor", "Corsor main act");
                list.add(new ArticleSourceImage(cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_SOURCE)),cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL)),cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_ART_TITLE))));
                Log.d("Loader",
                        cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_SOURCE)) +
                                ", " + cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL))+
                                ", " + cursor.getString(cursor.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL)));
            } while (cursor.moveToNext());
        }





        adapter.setArticleSourceImages(list);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.setArticleSourceImages(new ArrayList<ArticleSourceImage>());
    }

    /*@Override
    public Loader<ArrayList<ArticleSourceImage>> onCreateLoader(int id, Bundle args) {
        return new MyContentLoader(MainActivity.this);
    }*/



    /*@Override
    public void onLoadFinished(Loader<ArrayList<ArticleSourceImage>> loader, ArrayList<ArticleSourceImage> data) {

        adapter.setArticleSourceImages(data);

    }*/




    public class Fetchdata extends AsyncTask<String, Void, ArrayList> {


        protected ArrayList doInBackground(String[] params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            // Will contain the raw JSON response as a string.
            String dataJsonStr = null;

            try {

                //String baseurl = "https://newsapi.org/v1/articles?";
                String baseurl = "https://newsapi.org/v1/sources?language=en";

                //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey={API_KEY}
                //String sort_param = params[0];

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

                /*Log.d("Arr_imageurl","strt");
                imageUrls[0] = getImgUrl(url1);
                Log.d("Arr_imageurl","afterFirst");
                imageUrls[1] = getImgUrl(url2);
                imageUrls[2] = getImgUrl(url3);
                imageUrls[3] = getImgUrl(url4);
                imageUrls[4] = getImgUrl(url5);
                imageUrls[5] = getImgUrl(url6);

                Log.d("Arr_imageurl","strt");
                artImageUrls[0] = getImgArtUrl(imageUrls[0]);
                Log.d("Arr_imageurl","afterFirst");
                artImageUrls[1] = getImgArtUrl(imageUrls[1]);
                artImageUrls[2] = getImgArtUrl(imageUrls[2]);
                artImageUrls[3] = getImgArtUrl(imageUrls[3]);
                artImageUrls[4] = getImgArtUrl(imageUrls[4]);
                artImageUrls[5] = getImgArtUrl(imageUrls[5]);*/

                Log.d("Arr_imageurl","strt");
                imageUrls[0] = getImgUrl(url1);
                Log.d("Arr_imageurl","afterFirst");
                Log.d("Arr_imageurl","strt");
                artImageUrls[0] = getImgArtUrl(imageUrls[0]);
                artTitles[0] = getArtTitle(imageUrls[0]);
                Log.d("Arr_imageurl","afterFirst");

                imageUrls[1] = getImgUrl(url2);
                artImageUrls[1] = getImgArtUrl(imageUrls[1]);
                artTitles[1] = getArtTitle(imageUrls[1]);

                imageUrls[2] = getImgUrl(url3);
                artImageUrls[2] = getImgArtUrl(imageUrls[2]);
                artTitles[2] = getArtTitle(imageUrls[2]);

                imageUrls[3] = getImgUrl(url4);
                artImageUrls[3] = getImgArtUrl(imageUrls[3]);
                artTitles[3] = getArtTitle(imageUrls[3]);

                imageUrls[4] = getImgUrl(url5);
                artImageUrls[4] = getImgArtUrl(imageUrls[4]);
                artTitles[4] = getArtTitle(imageUrls[4]);

                imageUrls[5] = getImgUrl(url6);
                artImageUrls[5] = getImgArtUrl(imageUrls[5]);
                artTitles[5] = getArtTitle(imageUrls[5]);

                for(int i=0; i<6 ; i++){

                    ContentValues values = new ContentValues();
                    values.put(ArticleSourceImage.COL_SOURCE,imageUrls[i]
                           );

                    values.put(ArticleSourceImage.COL_ART_IMG_URL,artImageUrls[i]
                           );

                    values.put(ArticleSourceImage.COL_ART_TITLE,artTitles[i]
                    );

                    Uri uri = getContentResolver().insert(
                            ArticleSourceImageProvider.URI_ARTICLESOURCES, values);

                    Log.d("URI_ARTICLESOURCES",
                            uri.toString());

                }


/*
                ContentValues values = new ContentValues();
                values.put(ArticleSourceImage.COL_ID,
                        ((EditText)findViewById(R.id.editText2)).getText().toString());

                values.put(ArticleSourceImage.GRADE,
                        ((EditText)findViewById(R.id.editText3)).getText().toString());

                Uri uri = getContentResolver().insert(
                        ArticleSourceImage.CONTENT_URI, values);

                Toast.makeText(getBaseContext(),
                        uri.toString(), Toast.LENGTH_LONG).show();*////////////////////////////////////////////////////bc










                /*urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();*/

            }catch (IOException e){
                Log.e("MainActivity", "Error ", e);
            }



                /*InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                dataJsonStr = buffer.toString();
                MainActivity.JSONstring = dataJsonStr;
                Log.d("Jsondata1", dataJsonStr);
                Log.d("Jsondata2", MainActivity.JSONstring);
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
                JSONObject articleJson = new JSONObject(JSONstring);
                Log.d("Jsondata", ".JSONObject<");
                JSONArray articles = articleJson.getJSONArray("articles");
                Log.d("Jsondata", ".length()>");
                String[] titles = new String[articles.length()];
                Log.d("Jsondata", ".length()<");
                *//*String[] title = new String[movie.length()];
                String[] release_date = new String[movie.length()];
                String[] vote_avg = new String[movie.length()];
                String[] plot_synopsis = new String[movie.length()];*//*

                JSONObject ob;
                for (int i = 0; i < articles.length(); i++) {
                    ob = articles.getJSONObject(i);
                    titles[i] = ob.getString("title");
                    if(i==0){
                        imageUrl = ob.getString("urlToImage");
                    }
                    *//*title[i] = ob.getString("original_title");
                    release_date[i] = ob.getString("release_date");
                    vote_avg[i] = ob.getString("vote_average");
                    plot_synopsis[i] = ob.getString("overview");*//*

                    a.add(0, titles);
                    *//*a.add(1, title);
                    a.add(2, release_date);
                    a.add(3, vote_avg);
                    a.add(4, plot_synopsis);*//*


                    Log.d("Articles", titles[i]);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }*/

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
           // Log.d("Jsondata2", MainActivity.JSONstring);
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



                //Log.d("Articles", titles[i]);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return imageUrlStr;
            return sourceId;
    }

        public String getImgArtUrl(String srcId){
            String imageUrlStr = "";
            String dataJsonStrImg;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //String sourceId = "";

            try{
                String baseurl = "https://newsapi.org/v1/articles?";//https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey={API_KEY}
                //String sort_param = params[0];

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
                // Log.d("Jsondata2", MainActivity.JSONstring);
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
                    //JSONObject urls = ob.getJSONObject("urlsToLogos");

                    if(i==0){

                        imageUrlStr = ob.getString("urlToImage");
                        Log.d("UrlStrArt",imageUrlStr);
                    }

                    a.add(0, titles);



                    //Log.d("Articles", titles[i]);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return imageUrlStr;

        }

        public String getArtTitle(String srcId){
            String ArtTitle = "";
            String dataJsonStrImg;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //String sourceId = "";

            try{
                String baseurl = "https://newsapi.org/v1/articles?";//https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey={API_KEY}
                //String sort_param = params[0];

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
                // Log.d("Jsondata2", MainActivity.JSONstring);
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
                    //JSONObject urls = ob.getJSONObject("urlsToLogos");

                    if(i==0){

                        ArtTitle = ob.getString("title");
                        Log.d("UrlStrArt",ArtTitle);
                    }

                    a.add(0, titles);



                    //Log.d("Articles", titles[i]);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ArtTitle;

        }


        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);
           /* titles = (String[]) list.get(0);
            title = (String[]) list.get(1);
            release_date = (String[]) list.get(2);
            vote_avg = (String[]) list.get(3);
            plot_synopsis = (String[]) list.get(4);*/


            String URL = "content://com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";

            Uri students = Uri.parse(URL);
            Cursor c = getContentResolver().query(students, null, null, null, "name");



            if (c.moveToFirst()) {
                do{
                    /*Toast.makeText(MainActivity.this,
                            c.getString(c.getColumnIndex( ArticleSourceImage.COL_SOURCE)) +
                                    ", " + c.getString(c.getColumnIndex( ArticleSourceImage.COL_ART_IMG_URL)),
                            Toast.LENGTH_SHORT).show();*/

                    Log.d("DB return", c.getString(c.getColumnIndex(ArticleSourceImage.COL_SOURCE)) +
                            ", " + c.getString(c.getColumnIndex(ArticleSourceImage.COL_ART_IMG_URL)));

                } while (c.moveToNext());
            }



            adapter = new GridImgViewAdap(MainActivity.this, new ArrayList<ArticleSourceImage>());
            GridView grid = (GridView) findViewById(R.id.gridView);
            grid.setAdapter(adapter);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("Clicked", "Clicked");
                 /*   String URL = "content://com.mfusion.mycoordinatorapplicationtest.data.ArticleSourceImageProvider";

                    Uri students = Uri.parse(URL);
                    Cursor c = getContentResolver().query(students, null, null, null, "name");

                    if (c.moveToFirst()) {
                        do{
                            Toast.makeText(MainActivity.this,
                                    c.getString(c.getColumnIndex( ArticleSourceImage.COL_SOURCE)) +
                                            ", " + c.getString(c.getColumnIndex( ArticleSourceImage.COL_ART_IMG_URL)),
                                    Toast.LENGTH_SHORT).show();
                        } while (c.moveToNext());
                    }

                    Log.d("DB return", c.getString(c.getColumnIndex( ArticleSourceImage.COL_SOURCE)) +
                            ", " + c.getString(c.getColumnIndex( ArticleSourceImage.COL_ART_IMG_URL)));*/
                    intent.putExtra("srcId", imageUrls[i]);
                    startActivity(intent);
                }
            });


            //getLoaderManager().initLoader(URL_LOADER, null, this);



            /*Picasso.with(getApplicationContext()).load(imageUrl1).into((ImageView)findViewById(R.id.thumbnail1));
            Picasso.with(getApplicationContext()).load(imageUrl2).into((ImageView)findViewById(R.id.thumbnail2));
            Picasso.with(getApplicationContext()).load(imageUrl3).into((ImageView)findViewById(R.id.thumbnail3));
            Picasso.with(getApplicationContext()).load(imageUrl4).into((ImageView)findViewById(R.id.thumbnail4));
            Picasso.with(getApplicationContext()).load(imageUrl5).into((ImageView)findViewById(R.id.thumbnail5));
            Picasso.with(getApplicationContext()).load(imageUrl6).into((ImageView)findViewById(R.id.thumbnail6));*/

            /*if (isAdded()){
                ListCustomAdapter adapter = new ListCustomAdapter(getActivity(), posters);
                ListView list = (ListView) rootView.findViewById(R.id.listview);
                list.setAdapter(adapter);


                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Clicked", "Clicked");


                        Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("poster_url", posters[position]);
                        i.putExtra("title", title[position]);
                        i.putExtra("release_date", release_date[position]);
                        i.putExtra("vote_avg", vote_avg[position]);
                        i.putExtra("plot_synopsis", plot_synopsis[position]);

                        startActivity(i);

                    }
                });


            }*/

        }
    }


    public class FetchWeatherdata extends AsyncTask<String , Void , ArrayList> {

        String weatherJsonString = "";

        String main;


        protected ArrayList doInBackground(String[] params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String dataJsonStr = null;

            if (latitude.length() == 0) {
                latitude = getString(R.string.lat);
                longitude = getString(R.string.lon);
            }


            try {

                String baseurl = "http://api.openweathermap.org/data/2.5/weather?";
                String sort_param = params[0];

                Uri builturi = Uri.parse(baseurl).buildUpon()
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
                JSONObject oMain = weatherJson.getJSONObject("main");

                maxTemp = oMain.getDouble("temp_max") - 274 ;
                minTemp = oMain.getDouble("temp_min") - 274 ;

                place = weatherJson.getString("name");

                JSONObject wind = weatherJson.getJSONObject("wind");

                speed = wind.getDouble("speed");

                country = (sys.getString("country")).toLowerCase();

                Log.d("country",country);

                JSONObject weatherObj = weather.getJSONObject(0);
                weatherId = weatherObj.getInt("id");
                main = weatherObj.getString("main");
                weatherDesc = main;
                Log.d("weatherId",Integer.toString(weatherId));

      /*          String[] posters = new String[movie.length()];
                String[] title = new String[movie.length()];
                String[] release_date = new String[movie.length()];
                String[] vote_avg = new String[movie.length()];
                String[] plot_synopsis = new String[movie.length()];
                String[] movie_id = new String[movie.length()];
                String[] backdrop_path = new String[movie.length()];

                JSONObject ob;
                for(int i=0;i<movie.length();i++){
                    ob = movie.getJSONObject(i);
                    posters[i] = ob.getString("poster_path") ;
                    title[i] = ob.getString("original_title") ;
                    release_date[i] = ob.getString("release_date") ;
                    vote_avg[i] = ob.getString("vote_average") ;
                    plot_synopsis[i] = ob.getString("overview") ;
                    movie_id[i] = ob.getString("id");
                    backdrop_path[i] = ob.getString("backdrop_path");

                    a.add(0,posters);
                    a.add(1,title);
                    a.add(2,release_date);
                    a.add(3,vote_avg);
                    a.add(4,plot_synopsis);
                    a.add(5,movie_id);
                    a.add(6,backdrop_path);


                    Log.d("posters",posters[i]);
                }
*/


            }catch (JSONException e) {
                e.printStackTrace();
            }


            return a;

        }



        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);

            ((ImageView)findViewById(R.id.weather_img)).setImageResource(getIconResourceForWeatherCondition(weatherId));
            findViewById(R.id.weather_img).setContentDescription(main);

/*
            posters = (String[]) list.get(0);
            title = (String[]) list.get(1);
            release_date = (String[]) list.get(2);
            vote_avg = (String[]) list.get(3);
            plot_synopsis = (String[]) list.get(4);
            movie_id = (String[]) list.get(5);
            backdrop_path = (String[]) list.get(6);


            if (isAdded()){
                GridCustomAdapter adapter = new GridCustomAdapter(getActivity(), posters);
                GridView grid = (GridView) rootView.findViewById(R.id.gridView);
                grid.setAdapter(adapter);


                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Clicked", "Clicked");


                        //Intent i = new Intent(getActivity(), DetailActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("poster_url", posters[position]);
                        extras.putString("title", title[position]);
                        extras.putString("release_date", release_date[position]);
                        extras.putString("vote_avg", vote_avg[position]);
                        extras.putString("plot_synopsis", plot_synopsis[position]);
                        extras.putString("movie_id", movie_id[position]);
                        extras.putString("backdrop_path", backdrop_path[position]);
                        ((Callback)getActivity()).onItemSelected(extras);
                    */
/*i.putExtras(extras);
                    startActivity(i);*//*


                    }
                });


            }
*/




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
        private LayoutInflater inflater;

        private ArrayList<ArticleSourceImage> artSource = new ArrayList<ArticleSourceImage>();
        Integer[] mThumbnails;

        public GridImgViewAdap(Context context, ArrayList<ArticleSourceImage> artSource) {
            this.artSource = artSource;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public ArticleSourceImage getItem(int i) {
            return artSource.get(i);
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
            ArticleSourceImage source = getItem(i);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {

                //grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_cell_home,null);
                ImageView imageView = (ImageView)grid.findViewById(R.id.imageViewGrid);
                imageView.setAdjustViewBounds(true);

                Picasso.with(mContext).load(source.artImgUrl).into(imageView);


            } else {
                grid = (View) convertView;
            }

            return grid;
        }

        /*public void setArticleSourceImages(Cursor cursor) {
            artSource.addAll(data);
            notifyDataSetChanged();
        }*/

        public void setArticleSourceImages(ArrayList<ArticleSourceImage> data) {
            artSource.addAll(data);
            notifyDataSetChanged();
        }

    }

}
