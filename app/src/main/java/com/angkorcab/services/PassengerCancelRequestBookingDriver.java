package com.angkorcab.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.fragments.RideFragment;
import com.angkorcab.model.UserData;

import java.util.HashMap;

/**
 * Override this method to perform a computation on a background thread. The
 * specified parameters are the parameters passed to {@link #execute}
 * by the caller of this task.
 * <p>
 * This method can call {@link #publishProgress} to publish updates
 * on the UI thread.
 *
 * @param #params  The parameters of the task.
 * @return A result, defined by the subclass of this task.
 * @see #onPreExecute()
 * @see #onPostExecute
 * @see #publishProgress
 */

public class PassengerCancelRequestBookingDriver  extends AsyncTask<String, String, String> {

    private Context context;


    private final String TAG =  getClass().getSimpleName();
    private String response = "";
    private UserData userData;


    public PassengerCancelRequestBookingDriver(Context context) {
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
        String url = ServiceUrl.PASSENGER_CANCEL_BOOKING_DRIVER_REQUEST;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", userData.getDriverEmail());

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
