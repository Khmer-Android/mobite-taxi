package com.angkorcab.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.angkorcab.Fcm.Message;
import com.angkorcab.constants.UserStatusList;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.MarkerObject;
import com.angkorcab.model.MultiDrawable;

import com.angkorcab.model.UserData;
import com.angkorcab.services.ListMarkerTask;
import com.angkorcab.services.LogOutTask;
import com.angkorcab.services.MarkerCallBack;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.services.UpdateUserStatusAndLocation;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.GPSTracker;
import com.angkorcab.utils.GoogleUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DriverFragment  extends BaseFragment implements
        MarkerCallBack,
        TaskCallback,
        View.OnClickListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLoadedCallback,
        ClusterManager.OnClusterClickListener<MarkerObject>,
        ClusterManager.OnClusterInfoWindowClickListener<MarkerObject>,
        ClusterManager.OnClusterItemClickListener<MarkerObject>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MarkerObject>
{

    private Context context;
    private GoogleMap googleMap;
    private ImageButton  mapRefreshButton ; //  Remove button cap type  , yellowCab, limozine, personalCab;
    private CheckConnectivity checkConnectivity;
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog dialog;
    private static boolean isGPSEnabled, isNetworkEnabled;
    private GPSTracker gpsTracker;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 5;
    private final int MY_PERMISSIONS_REQUEST_STOP = 6;
    private static LocationManager locationManager;
    private static final    String  TAG = "DriverFragment";
    private Switch aSwitch;
    private GoogleUtil googleUtil;
    private UserData userData;
    private  Location location;

    private ClusterManager<MarkerObject> mClusterManager;
    private HashMap<Integer,MarkerObject> taxiMarkers = new HashMap<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_driver, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.cabbooking_driver);


        context = getActivity();
        googleUtil = new GoogleUtil(context);
        userData = UserData.getinstance(context);
        checkConnectivity = new CheckConnectivity(context);

        mapRefreshButton = (ImageButton) root.findViewById(R.id.refresh_map);
        aSwitch = (Switch) root.findViewById(R.id.driver_status);

        mapRefreshButton.setVisibility(View.GONE);

        gpsTracker = new GPSTracker(context);
        location = gpsTracker.getLocation();
        userData.setCurrentLocation(gpsTracker.getCurrentLocation());
        mapRefreshButton.setOnClickListener(this);
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }



         if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userData.setAvailable(UserStatusList.AVAILABLE);
                }else{
                    userData.setAvailable(UserStatusList.OFFLINE);
                }
                new  UpdateUserStatusAndLocation(context).execute();
            }
        });
        if(aSwitch.isChecked()){
            userData.setAvailable(UserStatusList.AVAILABLE);
        }
        else {
            userData.setAvailable(UserStatusList.OFFLINE);
        }
        return root;
    }

    @Override
    public void done(String action) {
        Log.i(TAG,"done method working...");
        switch (action){
            case  Action.LOGOUT_FINISHED:

            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
                ((MainActivity) getActivity()).finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                super.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;
        }

    }


    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PassengerRenderer extends DefaultClusterRenderer<MarkerObject> {
        private final IconGenerator mIconGenerator = new IconGenerator(context);
        private final IconGenerator mClusterIconGenerator = new IconGenerator(context);
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PassengerRenderer() {

            super(context, googleMap, mClusterManager);

            View multiProfile =getActivity().getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(context);




            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
            mIconGenerator.setTextAppearance(R.style.iconGenText);
            mIconGenerator.setBackground(null);
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerObject passenger, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(passenger.getProfilePhoto());
            mImageView.setBackground(null);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(passenger.getName());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerObject> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MarkerObject p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.getProfilePhoto());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);


            final Drawable clusterIcon = getResources().getDrawable(R.drawable.no_background);

            clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);

            mClusterIconGenerator.setBackground(clusterIcon);

            //modify padding for one or two digit numbers
            if (cluster.getSize() < 10) {
                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
            }
            else {
                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
            }


            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));


            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }



    @Override
    public void onClusterInfoWindowClick(Cluster<MarkerObject> cluster) {
        // Does nothing, but you could go to a list of the users.
        Log.i(TAG,"clustering click...");


    }

    @Override
    public boolean onClusterItemClick(MarkerObject item) {
        // Does nothing, but you could go into the user's profile page, for example.

        Log.i(TAG,"item click item");

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MarkerObject item) {
        // Does nothing, but you could go into the user's profile page, for example.

        Log.i(TAG,"item click item");
    }


    @Override
    public boolean onClusterClick(Cluster<MarkerObject> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().getName();
        Toast.makeText(context, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }



        /*
         @mHandleMessageReceiver
        * **/
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.i(TAG, "Broadcast received: driverFragment " + action);
            if(action.equals(Message.DRIVER_MESSAGE)){
                String data = intent.getExtras().getString(Message.EXTRA_MESSAGE);
                String flag    = intent.getExtras().getString(Message.EXTRA_FLAG);
                Log.i(TAG,"flag is "+flag);
                switch (flag) {
                    case Message.flag.DRIVER_UPDATE_USER_MARKER:

                        selectListMarker();

                        break;

                    case Message.flag.DRIVER_RECEIVE_BOOKING_REQUEST_PASSENGER:

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Fragment fragment = new DriverRecieverPassengerRequesst();
                        Bundle args = new Bundle();
                        args.putString("data",data);
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.containerView, fragment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        break;



                }


            }
        }




    };

    @Override
    protected void backPressed() {
        try {
            if (doubleBackToExitPressedOnce) {
                new LogOutTask(context,DriverFragment.this).execute();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(context, R.string.tap_again, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            new LogOutTask(context,DriverFragment.this).execute();
        }catch (Exception e) {
            return;
        }



        super.onDestroy();
    }


    @Override
    public void onStart(){
        Log.i(TAG,"onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        try {
            this.setUpMap();

            this.updateAvailable();

            context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Message.DRIVER_MESSAGE));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.refresh_map:
                if (googleMap != null) {
                    try {
                        Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
                        //googleMap.clear();
                        initialiseMap();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialiseMap();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            System.gc();
            locationManager.removeUpdates(this);
            context.unregisterReceiver(mHandleMessageReceiver);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_STOP);
        } else {
            locationManager.removeUpdates(this);
        }
        try {
            context.unregisterReceiver(mHandleMessageReceiver);

        } catch (Exception e) {
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getChildFragmentManager();

        Fragment fragment = (fm.findFragmentById(R.id.driver_map_fragment));
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(fragment);
        ft.commitAllowingStateLoss();


    }

    private void setUpMap(){

        try {
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getResources().getString(R.string.please_wait));
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            // dialog.show();
            if (googleMap == null) {
                //((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.driver_map_fragment)).getMapAsync(this);

                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.driver_map_fragment);
                mapFragment.getMapAsync(this);


            }
        }catch (Exception e){
            Log.e(TAG,"setup map"+e.getMessage().toLowerCase());
        }



    }
    private GoogleMap getMap(){
        return this.googleMap;
    }


    private void selectListMarker(){
        if(mClusterManager!=null){

            mClusterManager.clearItems();
        }


           ListMarkerTask.isDriver = true;
        new ListMarkerTask(context,DriverFragment.this,taxiMarkers).execute();
    }


    private void initialiseMap(){
        if (checkConnectivity.isNetworkAvailable()) {
            try {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);



                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if(latLng==null)return;
                            Log.i(TAG,"click."+latLng);

                            updateMarker();
                        }
                    });
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 10f));
                    getMap().setOnCameraIdleListener(mClusterManager);
                    getMap().setOnMarkerClickListener(mClusterManager);
                    getMap().setOnInfoWindowClickListener(mClusterManager);
                    mClusterManager.setOnClusterClickListener(this);
                    mClusterManager.setOnClusterInfoWindowClickListener(this);
                    mClusterManager.setOnClusterItemClickListener(this);
                    mClusterManager.setOnClusterItemInfoWindowClickListener(this);

                    selectListMarker();
                }
            } catch (Exception w) {
                w.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.internetdisabledmessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        Log.i(TAG,"onMapReady");
        if(this.googleMap == null) {
            this.googleMap = map;
        }
        mClusterManager = new ClusterManager<MarkerObject>(context, googleMap);
        mClusterManager.setRenderer(new DriverFragment.PassengerRenderer());


        this.initialiseMap();

        // select list marker
        this.selectListMarker();

        // close disable waiting...
        if (dialog != null) {
            dialog.dismiss();
        }


    }


    // update available user and location
    private void updateAvailable(){
        userData.setAvailable(UserStatusList.AVAILABLE);
        new UpdateUserStatusAndLocation(context).execute();
    }


    // update user marker
    @Override
    public void  updateMarker(){
        if(mClusterManager==null)return;


        Log.i(TAG,"size : "+taxiMarkers.size());
        mClusterManager.clearItems();
        for (Object value : taxiMarkers.values()) {

            Log.i(TAG,"Loop"+((MarkerObject)value).getName());
            mClusterManager.addItem((MarkerObject)value);
        }
        mClusterManager.cluster();
    }



}
