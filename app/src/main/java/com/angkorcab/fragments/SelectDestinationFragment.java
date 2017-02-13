package com.angkorcab.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.angkorcab.constants.DataBaseKey;
import com.angkorcab.google.PlaceAutocompleteAdapter;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.MarkerObject;
import com.angkorcab.taxi.R;
import com.angkorcab.model.UserData;
import com.angkorcab.services.DrawrootTask;
import com.angkorcab.utils.GPSTracker;
import com.angkorcab.utils.SharedPreferencesUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.net.Uri;

public class SelectDestinationFragment extends BaseFragment implements
        OnMapReadyCallback,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener
{


    private Context context;
    private ImageButton bookNowButton;
/*    private TextView set_source_SD, destinationText_SD;*/
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 111;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    private UserData userData;
    private GoogleMap googleMap;
    private LatLng mapCenter;

    private LatLng source,destination;
    private String addressSource,addressDestination;

    private ProgressDialog dialog;

    private CheckConnectivity checkConnectivity;
    private Location location;
    private GPSTracker gpsTracker;



    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapterSource;

    private PlaceAutocompleteAdapter mAdapterDestination;


    private AutoCompleteTextView mAutocompleteDestination;

    private AutoCompleteTextView mAutocompleteSource;




    private static final LatLngBounds BOUNDS_GREATER_SOURCE = new LatLngBounds(
            new LatLng(11.544871666666666, 104.89216666666668), new LatLng(11.544871666666666, 104.89216666666668));
    private static final LatLngBounds BOUNDS_GREATER_DESTINATION = new LatLngBounds(
            new LatLng(11.544871666666666, 104.89216666666668), new LatLng(11.544871666666666, 104.89216666666668));



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView");

        View root = inflater.inflate(R.layout.fragment_select_destination, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.item_select_destination);
        try {
            context = getActivity();

            gpsTracker = new GPSTracker(context);
            location   = gpsTracker.getLocation();

            checkConnectivity = new CheckConnectivity(context);
            userData = UserData.getinstance(context);
          //  destinationText_SD = (TextView) root.findViewById(R.id.destination_SD);
            bookNowButton = (ImageButton) root.findViewById(R.id.button_book_now);
         //   set_source_SD = (TextView) root.findViewById(R.id.source_textview_SD);
            ///set_source_SD.setText(userData.getAddress());

            this.initaizeUserData();








            // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
            // functionality, which automatically sets up the API client to handle Activity lifecycle
            // events. If your activity does not extend FragmentActivity, make sure to call connect()
            // and disconnect() explicitly.
            mGoogleApiClient = new GoogleApiClient.Builder((Activity) context)

                    .enableAutoManage(getActivity(), 0 /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .build();



            // Retrieve the AutoCompleteTextView that will display Place suggestions.
            mAutocompleteDestination = (AutoCompleteTextView) root.findViewById(R.id.autocomplete_places_destination);

            mAutocompleteSource      = (AutoCompleteTextView) root.findViewById(R.id.autocomplete_places_source);


            // Register a listener that receives callbacks when a suggestion has been selected
            mAutocompleteDestination.setOnItemClickListener(mAutocompleteDestinationClickListener);
            mAutocompleteSource.setOnItemClickListener(mAutocompleteSourceClickListener);

            // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
            // the entire world.
            mAdapterDestination = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_GREATER_DESTINATION, null);
            mAdapterSource      = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_GREATER_SOURCE,null);
            mAutocompleteDestination.setAdapter(mAdapterDestination);
            mAutocompleteSource.setAdapter(mAdapterSource);

            // Set up the 'clear text' button that clears the text in the autocomplete view
         //   Button clearButton = (Button) root.findViewById(R.id.button_clear);

            // set text to autocomplete source
            mAutocompleteSource.setText(""+addressSource);





        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        return root;
    }


    private void initaizeUserData(){

        source = userData.getSource();
        addressSource = userData.getAddressSource();

    }


    private AdapterView.OnItemClickListener mAutocompleteDestinationClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapterDestination.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdateDestinationPlaceDetailsCallback);

            Toast.makeText(context, "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdateDestinationPlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            destination = place.getLatLng();

            addressDestination = place.getName().toString();

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();


            Log.i(TAG, "Place details received: " + place.getName());

            Log.i(TAG, "Place details received: " + place.getLatLng());

            placeMarker(place.getLatLng(),"2");

            drawRootFromSourcToDestination(source, destination);

            places.release();


        }
    };


    private AdapterView.OnItemClickListener mAutocompleteSourceClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapterSource.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdateSourcePlaceDetailsCallback);

            Toast.makeText(context, "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdateSourcePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);



            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();


            Log.i(TAG, "Place details received: " + place.getName());

            source = place.getLatLng();

            placeMarker(source,"1");
            drawRootFromSourcToDestination(source, destination);

            places.release();
        }
    };




    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(context,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    private void setClickListner() {
        try {
            bookNowButton.setOnClickListener(this);
            ///destinationText_SD.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiseMap() {
        if (checkConnectivity.isNetworkAvailable()) {
            this.setUpMap();
            try {


                if (googleMap != null) {

                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            try {
                                if(latLng==null)return;

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 12));

                                addressDestination = getAddress("" + latLng.latitude, "" + latLng.longitude);
                                mAutocompleteDestination.setText(""+addressDestination);
                                //  destinationText_SD.setText(address);
                                userData.setAddressDestination(addressDestination);
                                userData.setDestination(latLng);


                                drawRootFromSourcToDestination(source, latLng);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });



                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation.getPosition(), 10f));



                    if (location != null) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        placeMarker(source,"1");
                       /* googleMap.addMarker(new MarkerOptions()
                                .position(source )
                                .title(userData.getAddress())
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.mipmap.passenger)));*/

                    }
                    setClickListner();


                }
            } catch (Exception w) {
                w.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.internetdisabledmessage, Toast.LENGTH_LONG).show();
        }

    }

    private void setUpMap(){
        if(dialog==null) dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(R.string.please_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        if (googleMap == null) {
            //((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.driver_map_fragment)).getMapAsync(this);

            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapfragment_SD);
            mapFragment.getMapAsync(this);


        }
    }

    public void drawRootFromSourcToDestination(LatLng source, LatLng destination) {
        try {

            if(source==null || destination==null)return;

            googleMap.clear();

            Log.i(TAG, "Draw root working...");
            addressSource = getAddress(""+source.latitude,""+source.longitude);
            addressDestination = getAddress(""+destination.latitude,""+destination.longitude);

            userData.setAddressSource(addressSource);
            userData.setAddressDestination(addressDestination);
            userData.setSource(source);
            userData.setDestination(destination);


            placeMarker(source, "1");
            placeMarker(destination, "2");
            DrawrootTask drawrootTask = new DrawrootTask(context, source, destination, googleMap,bookNowButton);
            drawrootTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress(String lat, String lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            " ");
                }
                ret = strReturnedAddress.toString();
            } else {
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }

    public void placeMarker(LatLng latlong, String str) {
        try {
            if (str.equalsIgnoreCase("1"))
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latlong.latitude, latlong.longitude))
                        .snippet(String.valueOf(""))
                        .draggable(true)
                        .title(userData.getAddress())
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.passenger)));
            else
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latlong.latitude, latlong.longitude))
                        .snippet(String.valueOf(""))
                        .draggable(true)
                        .title(userData.getDesti_address())
                        .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.destination)));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 12));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void backPressed() {
        replaceFragment(context, new HomeFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           // case R.id.destination_SD:
               // callPlaceAutocompleteActivityIntent();
             //   break;
            case R.id.button_book_now:
                bookNow();
                break;
        }
    }
    private void bookNow() {
        try {
            if (!userData.getAddressSource().equalsIgnoreCase("") && !userData.getAddressDestination().equalsIgnoreCase("")) {
                String string = userData.getDistance();
                String distance = string.replaceAll("[km m]", "").replaceAll("m", "");
                if (Double.parseDouble(distance) < 1) {
                    Toast.makeText(context, R.string.short_distance, Toast.LENGTH_SHORT).show();
                } else {
                    replaceFragment(context, new RideFragment());
                }
            } else {
                Toast.makeText(context,
                        R.string.select_destination_error,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException | Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

   /* private void callPlaceAutocompleteActivityIntent() {

        Log.i(TAG,"call first working..");

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build((Activity) context);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, e.getConnectionStatusCode(),
                    0 *//* requestCode *//*).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);

        }



    }
*/

    @Override
    public void onStart(){
        Log.i(TAG,"onStart");
        super.onStart();
        this.setUpMap();
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG,"onResume");
        this.initaizeUserData();
        this.setUpMap();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.mapfragment_SD));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }



    private GoogleMap getMap(){
        return this.googleMap;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        Log.i(TAG, "onMapReady");
        if (this.googleMap == null) {
            this.googleMap = map;
        }

        dialog.dismiss();


        this.initialiseMap();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onPause() {


        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }





}
