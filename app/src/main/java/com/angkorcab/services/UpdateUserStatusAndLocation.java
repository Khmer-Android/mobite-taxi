package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.R;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdateUserStatusAndLocation extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = getClass().getSimpleName();
    private String response = "";
    private ProgressDialog dialog;
    private UserData userData;




    public UpdateUserStatusAndLocation(Context context ) {
        this.context = context;
        userData = UserData.getinstance(context);


    }

    protected void onPreExecute() {
        super.onPreExecute();
        if(dialog==null) {
            dialog = ProgressDialog.show(context, null,
                    context.getString(R.string.get_d_status_dialog), true, false);
        }

    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.USER_UPDATE_AVAILABLE;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("latitude",userData.getCurrentLocation().latitude+"");
        keyValue.put("longitude",userData.getCurrentLocation().longitude+"");
        keyValue.put("available",userData.getAvailable());

        Log.d(TAG, ""+keyValue);
        Log.d(TAG, url);
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
            if(dialog!=null) {
                dialog.dismiss();
            }
            String json = response;
            JSONObject obj = new JSONObject(json);




            int success = obj.getInt("success");
         /*   JSONObject driver = obj.getJSONObject("driver");

            String status   = driver.getString("driver_status");

            Log.i(TAG,"status: "+status);
            SharedPreferencesUtility.setDriverStatus(context, status+"");

            Log.i(TAG,"driver"+driver);

            JSONObject user  = driver.getJSONObject("user");

            Log.i(TAG, "Ride"+user);

            JSONArray  transportations = user.getJSONArray("transportations");

            Log.i(TAG, "transportations"+transportations);

            JSONObject transportation  = transportations.getJSONObject(0);

            Log.i(TAG,"transportation"+transportation);
*/

            if(success == ResultStatus.SUCCESS) {




            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
