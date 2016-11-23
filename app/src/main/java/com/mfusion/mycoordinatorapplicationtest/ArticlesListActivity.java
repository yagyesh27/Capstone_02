package com.mfusion.mycoordinatorapplicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ArticlesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("ArtListAct", "Oncreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);

        Log.d("ArtListAct", "After setContentView");

        if(savedInstanceState == null){

            Log.d("ArtListAct", "In savedInstanceState");

            ArticlesListActivityFragment fragment =new ArticlesListActivityFragment();
            fragment.setArguments(getIntent().getExtras());


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.article_list_container,fragment)
                    .commit();
        }

        Log.d("ArtListAct", "After savedInstanceState");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_articles_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
