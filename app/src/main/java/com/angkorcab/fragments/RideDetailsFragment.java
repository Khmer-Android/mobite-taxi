package com.angkorcab.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.angkorcab.adapter.CustomRideAdapter;
import com.angkorcab.model.Ride;
import com.angkorcab.services.RideGetTask;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;

import java.util.ArrayList;

public class RideDetailsFragment extends BaseFragment implements TaskCallback {
    private static final String TAG = RideDetailsFragment.class.getSimpleName();
    private Context context;
    private ArrayList<Ride> rides;
    private   View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          root = inflater.inflate(R.layout.fragment_ridedetails, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.item_two));
        context = getActivity();
        rides = new ArrayList<>();
        try {
            new RideGetTask(RideDetailsFragment.this,context,rides).execute();

            Log.i(TAG,"working man");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }
   @Override
   public  void done(String action) {
       switch (action) {
           case Action.GET_RIDE:

               CustomRideAdapter adapter = new CustomRideAdapter(context, rides);
               // Attach the adapter to a ListView
               ListView listView = (ListView) root.findViewById(R.id.lvUsers);
               listView.setAdapter(adapter);

               break;
       }
   }



    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void backPressed() {
        try {
            context.startActivity(new Intent(context, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
