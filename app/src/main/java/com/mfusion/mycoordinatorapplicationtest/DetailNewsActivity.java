package com.mfusion.mycoordinatorapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("imgUrl");
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("description");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

        findViewById(R.id.fab).setContentDescription("Go To Home");

        Picasso.with(this).load(imgUrl).into((ImageView) findViewById(R.id.imagePoster));
        ((TextView)findViewById(R.id.title_art)).setText(title);
        findViewById(R.id.title_art).setContentDescription(title);
        ((TextView)findViewById(R.id.desc_art)).setText(desc);
        findViewById(R.id.desc_art).setContentDescription(desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail_news, menu);
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
