package com.angkorcab.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angkorcab.Fcm.Message;
import com.angkorcab.constants.N;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.UserData;

import com.angkorcab.services.DriverCancelRequestBookingPassenger;
import com.angkorcab.services.DriverConfirmRequest;
import com.angkorcab.services.DriverMisssedConfirmTask;
import com.angkorcab.services.PassengerRequestBookDriver;
import com.angkorcab.taxi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DriverRecieverPassengerRequesst extends BaseFragment implements View.OnClickListener {


    public  Context context;
    private ImageButton cancelbutton,
            ride_now,
            confirm,
            reject;

    private boolean isConfirmed = false;

    private TextView
            text_source,
            text_destination,
            text_passenger_name,
            text_passenger_phone,
            text_reach_time,
            text_travel_time,
            text_fare_approx;


    private LinearLayout detail_passenger;
    private Button button_confirm,button_cancel;

    private UserData userData;
    private CheckConnectivity checkConnectivity;
    private MediaPlayer mp;
    String passenger_phone;


    long start = 0;

    long end = 0;

    private static int counter=0;

    private final Timer timer=new Timer();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.driver_reciever_passenger_request, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Passenger request");
        try {
            context = getActivity();
            userData = UserData.getinstance(context);
            mp =   MediaPlayer.create(context, R.raw.sound);

            mp = new MediaPlayer();
            mp = MediaPlayer.create(context, R.raw.sound);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setLooping(true);
            start = System.currentTimeMillis();

           detail_passenger = (LinearLayout) root.findViewById(R.id.passenger_detail);
            detail_passenger.setVisibility(View.GONE);
            button_confirm = (Button) root.findViewById(R.id.button_confirm);
            button_cancel  = (Button) root.findViewById(R.id.button_cancel);
         //   button_call    = (ImageView) root.findViewById(R.id.button_call);
            text_destination =(TextView) root.findViewById(R.id.text_destination);
            text_source      =(TextView) root.findViewById(R.id.text_source);
            text_passenger_name = (TextView) root.findViewById(R.id.text_passenger_name);
            text_passenger_phone= (TextView) root.findViewById(R.id.text_passenger_phone);
            text_reach_time     = (TextView) root.findViewById(R.id.text_reach_time);
            text_fare_approx    = (TextView) root.findViewById(R.id.text_fare_approx);
            text_travel_time    = (TextView) root.findViewById(R.id.text_travel_time);



            if(getArguments().getString(Message.EXTRA_MESSAGE)!=null){
                String data = getArguments().getString(Message.EXTRA_MESSAGE);
                if(data!=null){
                    JSONObject obj = new JSONObject(data.toString());
                    String dest_address = obj.getString("dest_address");
                    String pick_time    = obj.getString("pick_time");
                    String pick_address = obj.getString("pick_address");
                    String pick_date    = obj.getString("pick_date");
                    String passenger_name= obj.getString("passenger_name");
                    passenger_phone = obj.getString("passenger_phone");
                    String passenger_email = obj.getString("passenger_email");
                    String  fare_per_unit =  obj.getString("fare_per_unit");
                    String distance       =  obj.getString("distance");
                    String reach_time     =  obj.getString("reach_time");
                    String travel_time    =  obj.getString("travel_time");
                    userData.setUserEmail(passenger_email);
                    text_source.setText(pick_address);
                    text_destination.setText(dest_address);
                    text_passenger_phone.setText("0XX.xxx-xxx");
                    text_passenger_name.setText(passenger_name);
                    text_fare_approx.setText("$ "+fare_per_unit);
                    text_reach_time.setText(reach_time);
                    text_travel_time.setText(travel_time);

                    userData.setAddressSource(pick_address);
                    userData.setAddressDestination(dest_address);
                    userData.setFare_per_unit(fare_per_unit);
                    userData.setDistance(distance);
                    userData.setTravelTime(travel_time);
                    userData.setReachTime(reach_time);

                    mp.start();

                }else {
                    Toast.makeText(context,"null data",Toast.LENGTH_SHORT).show();
                }
            }

            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    Log.i(TAG,"driver rice cancel");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(getResources().getString(
                                            R.string.messagedialogtitle))
                                            .setMessage(getResources().getString(
                                                    R.string.driver_cancel_request_success))
                                            .setCancelable(false)
                                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    new DriverCancelRequestBookingPassenger(context).execute();
                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    Fragment fragment = new DriverFragment();
                                                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                                                    fragmentTransaction.addToBackStack(null);
                                                    fragmentTransaction.commit();

                                                    stopSound();
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

                }
            });

            // confirm
            button_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"confirmed...");

                    end =  System.currentTimeMillis();

                    detail_passenger.setVisibility(View.VISIBLE);
                    text_passenger_phone.setText(passenger_phone);
                    stopTimer();
                    stopSound();
                  if(!isConfirmed) {
                      new DriverConfirmRequest(context).execute();
                  } else {
                      Toast.makeText(context,"You has been confirm already.",Toast.LENGTH_SHORT).show();
                  }
                    isConfirmed = true;
                    button_cancel.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();

        }



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(counter  >= N.TIME_CALL_EXPIRED){
                    if(timer !=null){
                        stopTimer();
                        stopSound();

                        myHandler.sendEmptyMessage(0);
                        myHandler.sendEmptyMessage(1);
                        counter=0;
                        return;
                    }
                }
                counter++;


                Log.i(TAG,"counter"+counter);


            }
            // time, delay
        }, N.ONE_SECOND, N.ONE_SECOND);

        return root;
    }

    private void stopTimer(){
        if(timer !=null)
        timer.cancel();
        timer.purge();
    }
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(
                            R.string.messagedialogtitle))
                            .setMessage(getResources().getString(R.string.driver_missed_pickup))
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    Fragment fragment = new DriverFragment();
                                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                    break;

                case 1:

                   new DriverMisssedConfirmTask(context).execute();

                    break;
                default:
                    break;
            }
        }
    };



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

            switch (flag) {
                case Message.flag.DRIVER_RECEIVE_CANCEL_BOOKING_REQUEST_PASSENGER:


                    String data = intent.getExtras().getString(Message.EXTRA_MESSAGE);
                    if(data!=null) {
                       try{
                           JSONObject obj = new JSONObject(data.toString());

                           userData.setDistance(obj.getString("distance"));
                           userData.setFare_per_unit(obj.getString("fare_per_unit"));
                           userData.setAddressSource(obj.getString("pick_address"));
                           userData.setAddressDestination(obj.getString("dest_address"));


                           Log.i(TAG,"haha after"+userData.getAddressSource());

                       }catch (JSONException e){
                           Log.i(TAG,"json error"+e.getMessage());
                       }

                    }



                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(
                            R.string.messagedialogtitle))
                            .setMessage(getResources().getString(R.string.passenger_cancel_request))
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    Fragment fragment = new DriverFragment();
                                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                     stopSound();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;

            }


        }
    };





    @Override
    protected void backPressed() {

        this.stopSound();
        replaceFragment(context, new DriverFragment());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_passenger_phone:
                    makeCall();
                break;

            case R.id.ride_button_cancel:
                replaceFragment(context, new DriverFragment());
                break;
            case R.id.ride_now_IMGbutton:
                if (checkConnectivity.isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(
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
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            case R.id.confirm_IMGbutton:


                Log.i(TAG,"confirm.");

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

        /*    case R.id.button_call:
                makeCall();
                break;*/

            default:
                break;
        }
    }

    private void makeCall() {
        try {
            String phoneNumber =Uri.parse("tel:" + Uri.encode("093883292")).toString();


            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
            context.startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        try {

            context.registerReceiver(mHandleMessageReceiver, new IntentFilter(Message.DRIVER_RECIEVE_REQUEST));

        } catch (Exception e) {
            Log.e(TAG,"onResume"+e.getMessage().toLowerCase());
        }
    }
    @Override
    public void onStop() {


        try {
            context.unregisterReceiver(mHandleMessageReceiver);
            stopSound();
        } catch (Exception e) {
            Log.e(TAG,"onStop"+e.getMessage());
        }

        super.onStop();
    }


    @Override
    public void onPause(){
        super.onPause();

        this.stopSound();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.stopSound();
    }


    private void stopSound(){
        try {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
        }catch (IllegalStateException e){

                //Do nothing
            Log.i(TAG,"error stop"+e.getMessage());

        }
    }

}
