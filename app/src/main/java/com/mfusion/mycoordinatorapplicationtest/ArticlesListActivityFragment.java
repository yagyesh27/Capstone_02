package com.mfusion.mycoordinatorapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class ArticlesListActivityFragment extends Fragment {

    View rootView;
    static String JSONstring;
    static String titles[];
    static String urls[];
    static String desc[];
    static String imageUrlStr[];

    public ArticlesListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_articles_list, container, false);

        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
        Intent intent = getActivity().getIntent();
        String srcId = intent.getStringExtra("srcId");
        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);


        Bundle extras =  getArguments();
        if (extras != null) {

            srcId = extras.getString("srcId");

            if(srcId == null){

                Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_LONG).show();

            }else{
                new Fetchdata().execute(srcId);
            }

        }


        return rootView;
    }

    public class Fetchdata extends AsyncTask<String, Void, ArrayList> {


        protected ArrayList doInBackground(String[] params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Log.d("srcId_Detail", params[0]);
            String srcId = params[0];


            String dataJsonStr = null;

            try {

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
                ArticlesListActivityFragment.JSONstring = dataJsonStr;
                Log.d("Jsondata1thrs", dataJsonStr);
                Log.d("Jsondata2shtr", ArticlesListActivityFragment.JSONstring);
            } catch (IOException e) {
                Log.e("ArtListActFragment", "Error ", e);

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


                JSONObject ob;
                for (int i = 0; i < articles.length(); i++) {
                    ob = articles.getJSONObject(i);
                    titles[i] = ob.getString("title");
                    urls[i] = ob.getString("url");
                    desc[i] = ob.getString("description");
                    imageUrlStr[i] = ob.getString("urlToImage");


                    a.add(0, titles);



                    Log.d("ArticlesTitle", titles[i]);
                    Log.d("ArticlesUrl", urls[i]);
                    Log.d("ArticleDesc", desc[i]);
                    Log.d("ArticlePoster", imageUrlStr[i]);
                }

                ArticlesListActivityFragment.titles = titles;
                ArticlesListActivityFragment.urls = urls;
                ArticlesListActivityFragment.desc = desc;
                ArticlesListActivityFragment.imageUrlStr = imageUrlStr;



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return a;

        }

        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);



            DetailListAdap adapter = new DetailListAdap(getActivity(), titles,urls);
            ListView listview = (ListView) rootView.findViewById(R.id.listview);
            listview.setAdapter(adapter);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Clicked", "Clicked");

                    Intent browserIntent = new Intent(getActivity(),DetailNewsActivity.class);
                    browserIntent.putExtra("imgUrl",imageUrlStr[position]);
                    browserIntent.putExtra("title",titles[position]);
                    browserIntent.putExtra("description",desc[position]);
                    startActivity(browserIntent);



                }
            });




        }
    }

    public class DetailListAdap extends BaseAdapter {
        private Context mContext;

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


                list = inflater.inflate(R.layout.detail_list_item,null);
                TextView textview = (TextView)list.findViewById(R.id.article_title_text);
                textview.setText(titles[i]);
                textview.setContentDescription(titles[i]);




            } else {
                list = (View) convertView;
            }

            return list;
        }
    }
}
