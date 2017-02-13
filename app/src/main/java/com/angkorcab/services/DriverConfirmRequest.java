package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.constants.ProjectURls;
import com.angkorcab.model.UserData;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONObject;

import java.util.HashMap;

public class DriverConfirmRequest extends AsyncTask<String, String, String> {
    private Context context, context1;
    private static final String TAG = DriverConfirmRequest.class.getSimpleName();
    private String response = "";
    private ProgressDialog dialog;
    private UserData userdata;

    public DriverConfirmRequest(Context context) {
        this.context = context;

        userdata = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.please_wait));
        dialog.show();
    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.DRIVER_CONFIRM_BOOKING_PASSENGER;

        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("driver_email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("user_email",  userdata.getUserEmail());
        keyValue.put("pickup_location",userdata.getAddressSource());
        keyValue.put("dropoff_location",userdata.getAddressDestination());
        keyValue.put("reach_time", userdata.getReachTime());
        keyValue.put("travel_time", userdata.getTravelTime());
        keyValue.put("fare_per_unit",userdata.getFare_per_unit());
        keyValue.put("distance",userdata.getDistance());
        Log.i(TAG,"url"+url);
        Log.i(TAG, "queryString" + keyValue);
        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url, keyValue);
            Log.i(TAG,"response"+ response);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        try {
            dialog.dismiss();
            JSONObject obj = new JSONObject(response);
            String success = obj.getString("success");

            Log.i(TAG,"success:"+success);


          /*  String status = obj.getString("status");
            if (success.equals("0")) {
                if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase("driver")) {
                    Toast.makeText(context, "Cannot Confirm Booking!! Passenger has Rejected This Booking", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cannot Confirm Booking!! DriverStatus has Rejected This Booking", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context1, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            } else {
                if (status.equalsIgnoreCase("confirm")) {
                    Toast.makeText(context, "Your Ride is Confirmed", Toast.LENGTH_SHORT).show();
                    if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase("passenger")) {
                       *//* RideDetailsFragment fragment = new RideDetailsFragment();
                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, fragment).commit();*//*
                    }
                } else {
                    Toast.makeText(context, "Your Ride is Rejected", Toast.LENGTH_SHORT).show();
                    if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase("passenger")) {
                        context.startActivity(new Intent(context1, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
