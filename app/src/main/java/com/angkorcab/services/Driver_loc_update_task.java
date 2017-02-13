package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.utils.SharedPreferencesUtility;

import java.util.HashMap;

public class Driver_loc_update_task extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = "Driver_loc_update_task";
    private String response = "";
    private ProgressDialog dialog;
    private String email;




    public Driver_loc_update_task(Context context, String email) {
        this.context = context;
        this.email = email;
    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.DRIVER_UPDATE_STORE_LOCATION_WITH_NEARBY;


        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("driver_email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("d_lat", "" +   SharedPreferencesUtility.getLatitude(context));
        keyValue.put("d_long", "" +  SharedPreferencesUtility.getLongitude(context));
        keyValue.put("user_email", email);
        keyValue.put("driver_status", DriverDetails.getDriverStatus());

        keyValue.put("d_cabtype", String.valueOf(SharedPreferencesUtility.loadCabType(context)));

        Log.d(TAG, "query" + keyValue);
        Log.i(TAG, "url" + url);
        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url, keyValue);
            Log.d("response", response);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
