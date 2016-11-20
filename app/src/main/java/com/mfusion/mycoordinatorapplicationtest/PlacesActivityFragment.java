package com.mfusion.mycoordinatorapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlacesActivityFragment extends Fragment {

    View rootview;
    int PLACE_PICKER_REQUEST = 1;

    public PlacesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_places, container, false);

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Log.d("reqCode0",Integer.toString(PLACE_PICKER_REQUEST));
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        }catch (Exception e){
            Log.e("Place",e.toString());
        }
        return rootview;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("reqCode1",Integer.toString(requestCode));
        Log.d("placeResult","log11111111111");
        if (requestCode == PLACE_PICKER_REQUEST) {
            Log.d("placeResult","log22222222222");
            if (resultCode == Activity.RESULT_OK) {
                Log.d("placeResult","log33333333333");
                Place place = PlacePicker.getPlace(getActivity(),data);
                String toastMsg = String.format("Place: %s", place.getName());
                ((TextView)rootview.findViewById(R.id.textView11)).setText(place.getName());
                ((TextView)rootview.findViewById(R.id.textView22)).setText(place.getAddress());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}
