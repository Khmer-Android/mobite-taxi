package com.angkorcab.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.angkorcab.Fcm.Message;
import com.angkorcab.constants.TransportationType;
import com.angkorcab.services.PassengerCancelRequestBookingDriver;
import com.angkorcab.taxi.R;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;

import com.angkorcab.services.PassengerRequestBookDriver;
import com.angkorcab.services.PassengerCalculateDistant;

public class RideFragment extends BaseFragment implements View.OnClickListener {



    private ImageButton cancelbutton, ride_now, confirm, reject;
    private TextView source, cab_type, destination, distance, dis_time, cab_time, travelTime,
            driver_name, contact, cab_num, fare, progress_text_view,fareApprox;
    private LinearLayout getting_cabdetailsLL, got_cabDetailsLL, ridenowlaterlayout,fareDistanceRideLayout;
    private ProgressBar progress;
    private Context context;
    private CheckConnectivity checkConnectivity;
    public static boolean flag = false;
    private UserData userData;
    private String phoneNumber;
    private ImageView transportationType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ride, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ride");
        try {
            context = getActivity();
            userData = UserData.getinstance(context);


            Log.i(TAG,"ride is : "+userData.getTransportationName());

            getting_cabdetailsLL = (LinearLayout) root.findViewById(R.id.gettingdetailsLL);
            got_cabDetailsLL = (LinearLayout) root.findViewById(R.id.cab_details_layout);
            fareDistanceRideLayout = (LinearLayout) root.findViewById(R.id.distance_fare_layout_Ride);
            cab_time = (TextView) root.findViewById(R.id.cab_reach_time_Ride);
            driver_name = (TextView) root.findViewById(R.id.driver_name_Ride);
            contact = (TextView) root.findViewById(R.id.driver_contact_num_Ride);
            cab_num = (TextView) root.findViewById(R.id.Cab_num_Ride);
            progress_text_view = (TextView) root.findViewById(R.id.cab_details_progress_textview);
            progress = (ProgressBar) root.findViewById(R.id.cab_details_progress);
            ride_now = (ImageButton) root.findViewById(R.id.ride_now_IMGbutton);
            confirm = (ImageButton) root.findViewById(R.id.confirm_IMGbutton);
            reject = (ImageButton) root.findViewById(R.id.reject_IB_Ride);
            ridenowlaterlayout = (LinearLayout) root.findViewById(R.id.ridenowlaterbuttonlayout);
            cancelbutton = (ImageButton) root.findViewById(R.id.ride_button_cancel);
            source = (TextView) root.findViewById(R.id.current_loc_Ride);
            cab_type = (TextView) root.findViewById(R.id.cab_type_Ride);
            destination = (TextView) root.findViewById(R.id.destination_Ride);
            distance = (TextView) root.findViewById(R.id.distance_Ride);
            fare = (TextView) root.findViewById(R.id.fare_Ride);
            fareApprox=(TextView)root.findViewById(R.id.fare_approx);
            travelTime = (TextView) root.findViewById(R.id.travelTime);
            checkConnectivity = new CheckConnectivity(context);
            cancelbutton.setOnClickListener(this);
            ride_now.setOnClickListener(this);
            confirm.setOnClickListener(this);
            reject.setOnClickListener(this);
            contact.setOnClickListener(this);

            transportationType = (ImageView) root.findViewById(R.id.transportation_type);

            new PassengerCalculateDistant(context, ridenowlaterlayout, RideFragment.this).execute();
            setData();
        } catch (Exception e) {
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
            Bundle extras = intent.getExtras();
            String flag    = intent.getExtras().getString(Message.EXTRA_FLAG);
            Log.i(TAG,"flag is "+flag);
            if(action.equalsIgnoreCase(Message.RIDE_MESSAGE)){
                switch (flag) {
                    case Message.flag.DRIVER_CANCEL_REQUEST_PASSENGER_BOOKING:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(
                                R.string.messagedialogtitle))
                                .setMessage(getResources().getString(R.string.driver_cancel_request_success))
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        Fragment fragment = new HomeFragment();
                                        fragmentTransaction.replace(R.id.containerView, fragment, null);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();

                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;


                    case Message.flag.DRIVER_CONFIRM_REQUEST_PASSENGER:
                        Log.i(TAG, "Passenger Recieve message to confirm");


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle(getResources().getString(
                                R.string.messagedialogtitle))
                                .setMessage(getResources().getString(R.string.driver_confirm_request_success))
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // hide button cancel
                                        cancelbutton.setVisibility(View.GONE);

                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();




                        break;

                }
            }

        }
    };





    public void setData() {
        try {

            if (userData.getTransportationName().equalsIgnoreCase(TransportationType.TAXI)) {

                cab_type.setText(R.string.taxi);
                transportationType.setImageResource(R.drawable.tab_icon_angkorcab_taxi);
            } else if (userData.getTransportationName().equalsIgnoreCase(TransportationType.CAR)) {

                cab_type.setText(R.string.car);
                transportationType.setImageResource(R.drawable.tab_icon_angkorcab_car);
            } else if (userData.getTransportationName().equalsIgnoreCase(TransportationType.TUK_TUK)) {

                cab_type.setText(R.string.tuktuk);
                transportationType.setImageResource(R.drawable.tab_icon_angkorcab_tuktuk);
            }   else if (userData.getTransportationName().equalsIgnoreCase(TransportationType.MOTO)) {

                cab_type.setText(R.string.motobike);
                transportationType.setImageResource(R.drawable.tab_icon_angkorcab_moto);
            }else {
                //fare.setText("$250");
                cab_type.setText(R.string.taxi);
            }
            source.setText(userData.getAddressSource());
            destination.setText(userData.getAddressDestination());
            distance.setText(userData.getDistance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void backPressed() {
        replaceFragment(context, new SelectDestinationFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ride_button_cancel:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Log.i(TAG,"rice cancel");
                                new PassengerCancelRequestBookingDriver(context).execute();

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(getResources().getString(
                                        R.string.messagedialogtitle))
                                        .setMessage(getResources().getString(
                                                R.string.passenger_cancel_request_success))
                                        .setCancelable(false)
                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                Fragment fragment = new HomeFragment();
                                                fragmentTransaction.replace(R.id.containerView, fragment, null);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();


                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Log.i(TAG,"no message");
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();




                break;
            case R.id.ride_now_IMGbutton:
                if (checkConnectivity.isNetworkAvailable()) {
                    AlertDialog.Builder builders = new AlertDialog.Builder(context);
                    builders.setTitle(getResources().getString(
                            R.string.messagedialogtitle))
                            .setMessage(getResources().getString(
                                    R.string.messagedialogmessage))
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    new PassengerRequestBookDriver(context).execute();
                                    ride_now.setVisibility(View.GONE);


                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builders.create();
                    alert.show();
                }
                break;
            case R.id.confirm_IMGbutton:
                try {
                    if (checkConnectivity.isNetworkAvailable()) {
                        Toast.makeText(context, R.string.confirming_msg,
                                Toast.LENGTH_LONG).show();

                        confirm.setVisibility(View.INVISIBLE);
                        reject.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.reject_IB_Ride:
                try {
                    if (checkConnectivity.isNetworkAvailable()) {
                        Toast.makeText(context, R.string.rejecting_msg,
                                Toast.LENGTH_LONG).show();

                        confirm.setVisibility(View.INVISIBLE);
                        reject.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.driver_contact_num_Ride:
                makeCall();
                break;
            default:
                break;
        }
    }

    private void makeCall() {
        try {
            phoneNumber = "tel:" + DriverDetails.getDriverNumber();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
            context.startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void responseFirst() {
        try {
            progress_text_view.setText(R.string.no_cabs);
            progress.setVisibility(View.INVISIBLE);
            RideFragment.flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void responseSecond() {
        try {


            Log.i(TAG,"currency is"+DriverDetails.getCurrency_type().toLowerCase());
            getting_cabdetailsLL.setVisibility(View.GONE);
            got_cabDetailsLL.setVisibility(View.VISIBLE);
            ridenowlaterlayout.setVisibility(View.VISIBLE);
            fareDistanceRideLayout.setVisibility(View.VISIBLE);
            cab_time.setText("" + DriverDetails.getNearesCabReachingTime());
            driver_name.setText("" + DriverDetails.getDriverName());
            contact.setText("" + DriverDetails.getDriverNumber());
            cab_num.setText("" + DriverDetails.getCabNumber());
            fare.setText(DriverDetails.getCurrency_type()+" "+DriverDetails.getFarePerUnit());
            travelTime.setText(DriverDetails.getTravelTime());
            fareApprox.setText(DriverDetails.getCurrency_type()+" "+DriverDetails.getFare());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        try {
            context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Message.RIDE_MESSAGE));

        } catch (Exception e) {
            Log.e(TAG,"onResume"+e.getMessage().toLowerCase());
        }
    }
    @Override
    public void onStop() {


        try {
            context.unregisterReceiver(mHandleMessageReceiver);

        } catch (Exception e) {
            Log.e(TAG,"onStop"+e.getMessage());
        }

        super.onStop();
    }


    @Override
    public void onPause(){
        super.onPause();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }


}
