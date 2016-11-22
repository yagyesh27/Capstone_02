package com.mfusion.mycoordinatorapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class DetailActivity extends AppCompatActivity {

    static String JSONstring;
    static String titles[];
    static String urls[];
    static String desc[];
    static String imageUrlStr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        Intent intent = getIntent();
        String srcId = intent.getStringExtra("srcId");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        new Fetchdata().execute(srcId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_detail, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public class Fetchdata extends AsyncTask<String, Void, ArrayList> {


        protected ArrayList doInBackground(String[] params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Log.d("srcId_Detail",params[0]);
            String srcId = params[0];

            // Will contain the raw JSON response as a string.
            String dataJsonStr = null;

            try {

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
                DetailActivity.JSONstring = dataJsonStr;
                Log.d("Jsondata1", dataJsonStr);
                Log.d("Jsondata2", DetailActivity.JSONstring);
            } catch (IOException e) {
                Log.e("DetailActivity", "Error ", e);

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
                String[] urls = new String[articles.length()];
                String[] desc = new String[articles.length()];
                String[] imageUrlStr = new String[articles.length()];
                Log.d("Jsondata", ".length()<");
                /*String[] title = new String[movie.length()];
                String[] release_date = new String[movie.length()];
                String[] vote_avg = new String[movie.length()];
                String[] plot_synopsis = new String[movie.length()];*/

                JSONObject ob;
                for (int i = 0; i < articles.length(); i++) {
                    ob = articles.getJSONObject(i);
                    titles[i] = ob.getString("title");
                    urls[i] = ob.getString("url");
                    desc[i] = ob.getString("description");
                    imageUrlStr[i] = ob.getString("urlToImage");
                    /*title[i] = ob.getString("original_title");
                    release_date[i] = ob.getString("release_date");
                    vote_avg[i] = ob.getString("vote_average");
                    plot_synopsis[i] = ob.getString("overview");*/

                    a.add(0, titles);
                    /*a.add(1, title);
                    a.add(2, release_date);
                    a.add(3, vote_avg);
                    a.add(4, plot_synopsis);*/


                    Log.d("ArticlesTitle", titles[i]);
                    Log.d("ArticlesUrl", urls[i]);
                    Log.d("ArticleDesc", desc[i]);
                    Log.d("ArticlePoster", imageUrlStr[i]);
                }

                DetailActivity.titles = titles;
                DetailActivity.urls = urls;
                DetailActivity.desc = desc;
                DetailActivity.imageUrlStr = imageUrlStr;



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return a;

        }

        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);
            /*titles = (String[]) list.get(0);
            title = (String[]) list.get(1);
            release_date = (String[]) list.get(2);
            vote_avg = (String[]) list.get(3);
            plot_synopsis = (String[]) list.get(4);*/


                DetailListAdap adapter = new DetailListAdap(DetailActivity.this, titles,urls);
                ListView listview = (ListView) findViewById(R.id.listview);
                listview.setAdapter(adapter);


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Clicked", "Clicked");

                        Intent browserIntent = new Intent(DetailActivity.this,DetailNewsActivity.class);
                        browserIntent.putExtra("imgUrl",imageUrlStr[position]);
                        browserIntent.putExtra("title",titles[position]);
                        browserIntent.putExtra("description",desc[position]);
                        startActivity(browserIntent);

                        /*Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("poster_url", posters[position]);
                        i.putExtra("title", title[position]);
                        i.putExtra("release_date", release_date[position]);
                        i.putExtra("vote_avg", vote_avg[position]);
                        i.putExtra("plot_synopsis", plot_synopsis[position]);

                        startActivity(i);*/

                    }
                });




        }
    }

    public class DetailListAdap extends BaseAdapter {
        private Context mContext;
        //Integer[] mThumbnails;
        String titles[];
        String urls[];
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public DetailListAdap(Context c,String titles[], String urls[]) {
            mContext = c;
            this.titles = titles;
            this.urls = urls;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View list;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {

                //grid = new View(mContext);
                list = inflater.inflate(R.layout.detail_list_item,null);
                TextView textview = (TextView)list.findViewById(R.id.article_title_text);
                textview.setText(titles[i]);
                textview.setContentDescription(titles[i]);

                /*ImageView imageView = (ImageView)grid.findViewById(R.id.imageViewGrid);
                imageView.setAdjustViewBounds(true);*/

                //Picasso.with(mContext).load(artImageUrls[i]).into(imageView);


            } else {
                list = (View) convertView;
            }

            return list;
        }
    }
}
