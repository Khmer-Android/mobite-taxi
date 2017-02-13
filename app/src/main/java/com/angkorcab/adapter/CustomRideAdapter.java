package com.angkorcab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.angkorcab.taxi.R;
import com.angkorcab.model.Ride;

import java.util.ArrayList;

public class CustomRideAdapter extends ArrayAdapter<Ride> {
    public CustomRideAdapter(Context context, ArrayList<Ride> rides) {
        super(context, 0, rides);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ride user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ride, parent, false);
        }
        // Lookup view for data population
        TextView pickup_location = (TextView) convertView.findViewById(R.id.pickup_location);
        TextView dropoff_location = (TextView) convertView.findViewById(R.id.dropoff_location);
         TextView date = (TextView) convertView.findViewById(R.id.date);

        // Populate the data into the template view using the data object
        pickup_location.setText(user.getPickup_location());
        dropoff_location.setText(user.getDropoff_location());
        date.setText(user.getDate());

        // Return the completed view to render on screen
        return convertView;
    }
}
