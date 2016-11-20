package com.mfusion.mycoordinatorapplicationtest;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class PositionActivityFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    View rootView;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION

    };

    public PositionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_position, container, false);

        Log.d("position", " position");

        //Toast.makeText(getActivity(),"position",Toast.LENGTH_LONG).show();


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

        Log.d("onConnected","onConnected");



        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);

        try {
            requestPermissions(INITIAL_PERMS,101);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        /*try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                *//*TextView mLatitudeText = find
                TextView mLongitudeText*//*
                ((TextView) rootView.findViewById(R.id.textView1)).setText(String.valueOf(mLastLocation.getLatitude()));
               ((TextView) rootView.findViewById(R.id.textView2)).setText(String.valueOf(mLastLocation.getLongitude()));
                Log.d("LatitudeText",String.valueOf(mLastLocation.getLatitude()));
                Log.d("LongitudeText",String.valueOf(mLastLocation.getLongitude()));
            }
        }*/catch (Exception e){

            Log.e("Position",e.toString());

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("Latitude",(Double.toString(location.getLatitude())));
        Log.d("Latitude",(Double.toString(location.getLongitude())));

        ((TextView) rootView.findViewById(R.id.textView1)).setText(Double.toString(location.getLatitude()));
        ((TextView) rootView.findViewById(R.id.textView2)).setText(Double.toString(location.getLongitude()));

        //Toast.makeText(getActivity(),"Latitude = " + (Double.toString(location.getLatitude())) + "Longitude = "+(Double.toString(location.getLongitude())),Toast.LENGTH_LONG).show();

        
    }
}
