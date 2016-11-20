package com.mfusion.mycoordinatorapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements HomeActivityFragment.Callback {

    static boolean mTwoPane;
    String ARTICLE_LIST_FRAGMENT_TAG = "art_list_tag";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(findViewById(R.id.article_list_container) != null){

            HomeActivity.mTwoPane = true;


            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.article_list_container, new ArticlesListActivityFragment(), ARTICLE_LIST_FRAGMENT_TAG)
                        .commit();
            }

        }else{

            HomeActivity.mTwoPane = false;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Bundle extras) {

        if(HomeActivity.mTwoPane){

            ArticlesListActivityFragment fragment = new ArticlesListActivityFragment();
            fragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.article_list_container, fragment, ARTICLE_LIST_FRAGMENT_TAG)
                    .commit();


        }else {
            Log.d("HomeAct","Redirect to detail List");
            Intent i = new Intent(this, ArticlesListActivity.class);
            i.putExtras(extras);
            startActivity(i);
        }

    }
}
