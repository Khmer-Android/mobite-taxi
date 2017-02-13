package com.angkorcab.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.fragments.RideFragment;
import com.angkorcab.model.UserData;

import java.util.HashMap;

/**
 * Created by phuong on 05-Dec-16.
 */

public class DriverCancelRequestBookingPassenger   extends AsyncTask<String, String, String> {

    private Context context;
    private final String TAG =  getClass().getSimpleName();
    private String response = "";
    private UserData userData;


    public DriverCancelRequestBookingPassenger(Context context) {
        this.context = context;
        this.userData= UserData.getinstance(context);

    }

    @Override
    public void onPreExecute(){
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        // old url
        //String url = ProjectURls.UPDATE_USER_LOCATION_URL;
        String url = ServiceUrl.DRIVER_CANCEL_BOOKING_PASSENGER;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", userData.getUserEmail());

        Log.d(TAG, "query: " + keyValue);
        Log.d(TAG, "url"+url);

        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url, keyValue);

        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }

    @Override
    public void onPostExecute(String response){
        try {
            String json = response;

            Log.i(TAG,"respone from service");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
