package com.angkorcab.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.fragments.RideFragment;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONObject;

import java.util.HashMap;

public class PassengerCalculateDistant extends AsyncTask<String, String, String> {
    private Context context;
    private RideFragment rideFragment;
    private LinearLayout ridenowlaterlayout;
    private final String TAG =  getClass().getSimpleName();
    private String response = "";
    UserData userdata;

    public PassengerCalculateDistant(Context context, LinearLayout ridenowlaterlayout, RideFragment rideFragment) {
        this.context = context;
        this.rideFragment = rideFragment;
        this.ridenowlaterlayout = ridenowlaterlayout;
        userdata = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        ridenowlaterlayout.setVisibility(View.INVISIBLE);
    }

    protected String doInBackground(String... params) {
        // old url
        //String url = ProjectURls.UPDATE_USER_LOCATION_URL;

        String url = ServiceUrl.PASSENGER_CALCULAT_DRIVER_DISTANT;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("lat", "" + userdata.getSource().latitude);
        keyValue.put("long", "" + userdata.getSource().longitude);
        keyValue.put("cabtype",  userdata.getTransportationName());
        keyValue.put("distance", userdata.getDistance());
        Log.d(TAG, "query: " + keyValue);
        Log.d(TAG, "url"+url);

        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url, keyValue);
            String json = response;
            JSONObject jsonresult = new JSONObject(json);
            DriverDetails.setCabNumber("NOT FOUND");
            DriverDetails.setDriverName("NOT FOUND");
            DriverDetails.setDriverNumber("NOT FOUND");
            DriverDetails.setNearesCabReachingTime("NOT FOUND");
            DriverDetails.setNearestCabDistance((int) Float.parseFloat(jsonresult.getString("distance")));
            int time = 0;
            time = (jsonresult.getInt("reach_time"));
            int hour = 0;
            hour = time / 60;
            int min = time % 60;
            if (hour == 0) {
                DriverDetails.setNearesCabReachingTime("" + min
                        + " min");
            } else {
                DriverDetails.setNearesCabReachingTime("" + hour
                        + " h " + min + " min");
            }
            DriverDetails.setNearesCabReachingTime("" + (jsonresult.getInt("reach_time")) + " min");
            DriverDetails.setCabNumber(jsonresult
                    .getString("cab_number"));
            DriverDetails.setDriverName(jsonresult
                    .getString("name"));
            DriverDetails.setDriverNumber(jsonresult
                    .getString("number"));
            DriverDetails.setDriver_email(jsonresult.getString("email"));
            DriverDetails.setFare(jsonresult.getString("fare"));
            DriverDetails.setFarePerUnit(jsonresult.getString("fare_per_unit"));
            DriverDetails.setCurrency_type(""+jsonresult.getString("currency_type"));
            Log.d("response", response);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        try {
            String json = response;
            JSONObject obj = new JSONObject(json);
            SharedPreferencesUtility.setDriverEmail(context,""+obj.get("driver_email"));
            Log.i(TAG, "dirver is "+SharedPreferencesUtility.getDriverEmail(context));
            userdata.setDriverEmail(obj.get("driver_email")+"");


           /* if (DriverDetails.getDriverNumber().equals("NOT FOUND")
                    && DriverDetails.getCabNumber().equals("NOT FOUND")
                    || DriverDetails.getDriverNumber().length() <= 1
                    && DriverDetails.getCabNumber().length() <= 1) {
                ((RideFragment) rideFragment).responseFirst();
            } else {
                ((RideFragment) rideFragment).responseSecond();
            }*/

            ((RideFragment) rideFragment).responseSecond();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
