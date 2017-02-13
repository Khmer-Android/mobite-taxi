package com.angkorcab.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.Toast;

import com.angkorcab.Fcm.Message;
import com.angkorcab.constants.DataBaseKey;
import com.angkorcab.constants.TransportationType;
import com.angkorcab.constants.UserStatusList;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.MultiDrawable;
import com.angkorcab.model.MarkerObject;
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
import com.angkorcab.utils.SharedPreferencesUtility;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class HomeFragment extends BaseFragment implements
        TaskCallback,
        MarkerCallBack,
        OnMapReadyCallback,
        View.OnClickListener,
        LocationListener,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLoadedCallback,
        ClusterManager.OnClusterClickListener<MarkerObject>,
        ClusterManager.OnClusterInfoWindowClickListener<MarkerObject>,
        ClusterManager.OnClusterItemClickListener<MarkerObject>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MarkerObject>
{
    private Context context;
    private GoogleMap googleMap =null;
    private ImageButton buttonNext, mapRefreshButton ; //  Remove button cap type  , yellowCab, limozine, personalCab;
    private CheckConnectivity checkConnectivity;
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog dialog;
    private static boolean isGPSEnabled, isNetworkEnabled;

    private  UserData userData;
    private GPSTracker gpsTracker;

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 5;
    private final int MY_PERMISSIONS_REQUEST_STOP = 6;
    private static LocationManager locationManager;
    private SharedPreferencesUtility preferencesUtility;
    private GoogleUtil googleUtil;
    private ClusterManager<MarkerObject> mClusterManager;
    private Random mRandom = new Random(1984);
    private MarkerObject sourceMarker=null;
    private HashMap<Integer,MarkerObject> taxiMarkers = new HashMap<>();
    private  Location location;
    private String addressSource;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        context = getActivity();

        try {
            gpsTracker = new GPSTracker(context);
            userData = UserData.getinstance(context);
            location = gpsTracker.getLocation();
            userData.setCurrentLocation(gpsTracker.getCurrentLocation());
            googleUtil = new GoogleUtil(context);
            preferencesUtility = SharedPreferencesUtility.getInstance();
            checkConnectivity = new CheckConnectivity(context);
            buttonNext = (ImageButton) root.findViewById(R.id.button_next);
            buttonNext.setVisibility(View.GONE);
            mapRefreshButton = (ImageButton) root.findViewById(R.id.refresh_map);
            mapRefreshButton.setOnClickListener(this);
            buttonNext.setOnClickListener(this);
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            else
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            BottomBar bottomBar = (BottomBar) root.findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    Log.i(TAG,">>>>>"+tabId);
                    switch (tabId){
                        case R.id.tab_button_taxi:      userData.setTransportationName(TransportationType.TAXI);
                            break;

                        case R.id.tab_button_car:       userData.setTransportationName(TransportationType.CAR);
                            break;

                        case R.id.tab_button_tuktuk:    userData.setTransportationName(TransportationType.TUK_TUK);
                            break;

                        case R.id.tab_button_moto:      userData.setTransportationName(TransportationType.MOTO);
                            break;
                        default:
                            Log.i(TAG,"Default tab case....");
                            break;
                    }
                    // refresh marker .........
                    selectListMarker();
                }
            });
        } catch (SecurityException e) {

            e.printStackTrace();

        }

        return root;
    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "Broadcast received: on HomeFramment " + action);
            if(action.equals(Message.PASSENGER_MESSAGE)){
                Bundle extras = intent.getExtras();
                String flag    = intent.getExtras().getString(Message.EXTRA_FLAG);
                Log.i(TAG,"flag is "+flag);

                switch (flag) {
                    case Message.flag.DRIVER_UPDATE_USER_MARKER:

                        selectListMarker();

                        break;

                    case Message.flag.DRIVER_CANCEL_REQUEST_PASSENGER_BOOKING:

                        Log.i(TAG,"cancel passenger actions.");

                        break;


                }
            }

        }
    };

    @Override
    public void done(String action) {
        Log.i(TAG,"done method...");
       switch (action) {
           case Action.LOGOUT_FINISHED:

               try {
                    Log.i(TAG,"log out finished");
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
    private class TaxiRenderer extends DefaultClusterRenderer<MarkerObject> {
        private final IconGenerator mIconGenerator = new IconGenerator(context);
        private final IconGenerator mClusterIconGenerator = new IconGenerator(context);
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public TaxiRenderer() {

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
            mIconGenerator.setBackground(null);
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerObject taxi, MarkerOptions markerOptions) {
            markerOptions.draggable(true);
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(taxi.getProfilePhoto());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(taxi.getName());
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

            mClusterImageView.setImageDrawable(multiDrawable);
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
    }

    @Override
    public boolean onClusterItemClick(MarkerObject item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Toast.makeText(context, " (including "+item.getId()  +" : " + item.getName() + ")", Toast.LENGTH_SHORT).show();
        return false;

    }

    @Override
    public void onClusterItemInfoWindowClick(MarkerObject item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Toast.makeText(context, " (including "+item.getId()  +" : " + item.getName() + ")", Toast.LENGTH_SHORT).show();
    }







    private void initialiseMap(boolean isMylocation) {
        if (checkConnectivity.isNetworkAvailable()) {
            try {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                    // if click my location button
                    if(isMylocation){
                        drawSourceMarker(gpsTracker.getCurrentLocation());
                    }

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                                if(latLng==null)return;
                                Log.i(TAG,"click."+latLng);
                                addressSource = googleUtil.getAddress(""+latLng.latitude,""+latLng.longitude);
                                // add address and latlng to userData
                                userData.setAddressSource(addressSource);

                            // draw source marker
                              drawSourceMarker(latLng);



                            // update marker
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
    public boolean onClusterClick(Cluster<MarkerObject> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().getName();
        int id = cluster.getItems().iterator().next().getId();
        Toast.makeText(context, cluster.getSize() + " (including "+id  +" : " + firstName + ")", Toast.LENGTH_SHORT).show();

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




    private void selectListMarker() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/


        if(mClusterManager!=null){

            mClusterManager.clearItems();
        }

        this.clearMarker();

        new ListMarkerTask(context,HomeFragment.this,taxiMarkers).execute();


    }




    private LatLng position() {
        return new LatLng(51.6723432, 51.38494009999999);
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialiseMap(false);
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
    protected void backPressed() {
      Log.i(TAG,"press back 2");
        try {
            if (doubleBackToExitPressedOnce) {
                userData.setAvailable(UserStatusList.OFFLINE);
                new LogOutTask(context,HomeFragment.this).execute();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(context, R.string.tap_again, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.button_next:
                 addressSource = userData.getAddressSource();
                if (DriverDetails.getNoCabFound().equalsIgnoreCase("NoCabFound")) {

                    Toast.makeText(context, "No Cab Available Right Now", Toast.LENGTH_SHORT).show();

                }
                else{
                    if (!addressSource.equalsIgnoreCase("Can't get Address!") && !addressSource.equalsIgnoreCase("")) {

                        replaceFragment(context, new SelectDestinationFragment());
                    } else {
                        Toast.makeText(context, "First Select Source Address", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
                break;
            case R.id.refresh_map:
                if (googleMap != null) {
                    try {
                        Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
                        //googleMap.clear();
                        initialiseMap(true);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onMapLoaded() {
        Log.i(TAG," haha map loaded..");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            System.gc();
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG,"onPause"+e.getMessage());
        }
    }

    @Override
    public void onStop() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_STOP);
        } else {
            locationManager.removeUpdates(this);
        }
        try {
            context.unregisterReceiver(mHandleMessageReceiver);

        } catch (Exception e) {
            Log.e(TAG,"onStop"+e.getMessage());
        }

        super.onStop();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.home_map_fragment));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
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
            context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Message.PASSENGER_MESSAGE));

        } catch (Exception e) {
            Log.e(TAG,"onResume"+e.getMessage().toLowerCase());
        }
    }




    private void setUpMap(){

        try {
            if(dialog==null) {
                dialog = new ProgressDialog(context);
                dialog.setMessage(context.getResources().getString(R.string.please_wait));
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.show();
            }

            // Initailizse google maps
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.home_map_fragment);
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            Log.e(TAG,"setup map"+e.getMessage().toLowerCase());
        }


    }

    private GoogleMap getMap(){
        return this.googleMap;
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        try {

            Log.i(TAG,"onMapReady");
            if(this.googleMap == null) {
                this.googleMap = map;
            }
            mClusterManager = new ClusterManager<MarkerObject>(context, googleMap);
            mClusterManager.setRenderer(new TaxiRenderer());


            this.initialiseMap(false);

            // select list marker
            this.selectListMarker();

            // close disable waiting...
            if (dialog != null) {
                dialog.dismiss();
            }


        }catch (Exception e){
            Log.e(TAG,"onMapReady"+e.getMessage().toLowerCase());
        }

    }

    @Override
    public void updateMarker(){
        if(mClusterManager==null){
            return;
        }


        Log.i(TAG,"size : "+taxiMarkers.size());
        mClusterManager.clearItems();
        for (Object value : taxiMarkers.values()) {

            Log.i(TAG,"Loop"+((MarkerObject)value).getName());
            mClusterManager.addItem((MarkerObject)value);
        }
        mClusterManager.cluster();

        if(taxiMarkers.get(DataBaseKey.KEY_SOURCE_LOCATION)!=null && taxiMarkers.size()>0){
            buttonNext.setVisibility(View.VISIBLE);
        }else {
            buttonNext.setVisibility(View.GONE);
        }

        // set button next
        if(taxiMarkers !=null && taxiMarkers.size()>0){
            DriverDetails.setNoCabFound("CabFound");
        }

    }
    
    
    /**
     * Update available status user after user login : 
     * get location latlng user
     * 
     * 
     * */
    private void updateAvailable(){
        userData.setAvailable(UserStatusList.AVAILABLE);
        new UpdateUserStatusAndLocation(context).execute();
    }


    // make location markerz
    private void drawSourceMarker( LatLng latLng ){
        if( sourceMarker != null ){
            taxiMarkers.remove(DataBaseKey.KEY_SOURCE_LOCATION);
        }

        sourceMarker =  new MarkerObject(latLng,DataBaseKey.KEY_SOURCE_LOCATION,addressSource,R.mipmap.passenger);
        taxiMarkers.put(DataBaseKey.KEY_SOURCE_LOCATION,sourceMarker);
        // more map to that location latlng.

        userData.setSource(latLng);
       Log.i(TAG,"set source address already");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, 11f));



    }

    private void clearMarker(){
        if(taxiMarkers.size()>0) {
            Iterator it = taxiMarkers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if ((int)pair.getKey() != DataBaseKey.KEY_SOURCE_LOCATION) {
                    //taxiMarkers.remove(pair.getKey());
                    it.remove();
                }

            }
        }
    }

    
}