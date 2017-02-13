package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.taxi.R;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;
import com.angkorcab.utils.SharedPreferencesUtility;
import org.json.JSONObject;
import java.util.HashMap;

public class PassengerRequestBookDriver extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = getClass().getSimpleName();
    private String response = "";
    private ProgressDialog dialog;
    private UserData userData;

    public PassengerRequestBookDriver(Context context) {
        this.context = context;
        userData = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.please_wait));
        dialog.show();
    }

    protected String doInBackground(String... params) {
        // order url
        //String url = ProjectURls.RIDE_NOW_CONFIRM_URL;
        String url = ServiceUrl.PASSENGER_REQUEST_BOOKING_DRIVER;
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email",           SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("driver_email",    userData.getDriverEmail());
        keyValue.put("pick_address", userData.getAddressSource());
        keyValue.put("pick_date", "" + today.year + "-" + (today.month + 1) + "-" + today.monthDay);
        keyValue.put("pick_time", "" + today.hour + ":" + today.minute);
        keyValue.put("dest_address", userData.getAddressDestination());
        keyValue.put("distance", userData.getDistance());
        keyValue.put("cab_number", DriverDetails.getCabNumber());
        keyValue.put("fare_per_unit",DriverDetails.getFarePerUnit());
        keyValue.put("reach_time", DriverDetails.getNearesCabReachingTime());
        keyValue.put("travel_time", DriverDetails.getTravelTime());
        Log.d(TAG, "query:" + keyValue);
        Log.d(TAG, "url"+url);

        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url, keyValue);
            Log.d(TAG,"response"+ response);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String response) {
        try {
            dialog.dismiss();
            String json = response;
            JSONObject obj = new JSONObject(json);
            Log.i(TAG,"result "+obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
