package com.mfusion.mycoordinatorapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent i = getIntent();

        DecimalFormat precision = new DecimalFormat("0.00");

        ((ImageView)findViewById(R.id.weatherImage)).setImageResource(getIconResourceForWeatherCondition(i.getIntExtra("weatherId", 800)));
        ((TextView) findViewById(R.id.weatherText)).setText(i.getStringExtra("weatherDesc"));
        ((ImageView)findViewById(R.id.weatherImage)).setContentDescription(i.getStringExtra("weatherDesc"));
        ((TextView)findViewById(R.id.weatherText)).setContentDescription(i.getStringExtra("weatherDesc"));
        ((TextView)findViewById(R.id.placeText)).setText(i.getStringExtra("place"));
        ((TextView)findViewById(R.id.placeText)).setContentDescription(i.getStringExtra("place"));
        //((TextView)findViewById(R.id.maxTempText)).setText( Double.toString(i.getDoubleExtra("maxTemp",0.00)) + "°C");
        ((TextView)findViewById(R.id.maxTempText)).setText(precision.format(i.getDoubleExtra("maxTemp",0.00)) + "°C");
        ((TextView)findViewById(R.id.maxTempText)).setContentDescription(precision.format(i.getDoubleExtra("maxTemp", 0.00)) + "°C");
        ((TextView)findViewById(R.id.minTempText)).setText(precision.format(i.getDoubleExtra("minTemp", 0.00)) + "°C");
        ((TextView)findViewById(R.id.minTempText)).setContentDescription(precision.format(i.getDoubleExtra("minTemp", 0.00)) + "°C");
        ((TextView) findViewById(R.id.windSpeedText)).setText(precision.format(i.getDoubleExtra("speed", 0.00)) + " mps");
        ((TextView) findViewById(R.id.windSpeedText)).setContentDescription(precision.format(i.getDoubleExtra("speed", 0.00)) + " mps");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_weather, menu);
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
