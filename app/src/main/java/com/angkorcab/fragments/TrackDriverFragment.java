package com.angkorcab.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angkorcab.taxi.R;
import com.angkorcab.services.GetDriverLocationService;
import com.angkorcab.utils.GPSTracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackDriverFragment extends BaseFragment {
    private Context context;
    private ProgressDialog dialog;
    private static GoogleMap googlemap;
    private SharedPreferences preferences;
    private MarkerOptions marker;
    private GPSTracker gpsTracker;
    String driverId = "";
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_track_driver, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.item_track_driver);
        context = getActivity();
        try {
            bundle = this.getArguments();
            if (bundle != null) {
                driverId = bundle.getString("driver_id");
            }
            if (googlemap == null) {
              /*  googlemap = ((SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapfragment_trackdriver)).getMap();*/
                if (googlemap != null) {
                    new GetDriverLocationService(context, googlemap)
                            .execute(driverId);
                }
            } else {
            /*    googlemap = ((SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapfragment_trackdriver)).getMap();*/
                new GetDriverLocationService(context, googlemap)
                        .execute(driverId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected void backPressed() {
        replaceFragment(context, new RideDetailsFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.mapfragment_trackdriver));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }
}
