package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.taxi.R;
import com.angkorcab.constants.ProjectURls;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONObject;

import java.util.HashMap;

public class RideLaterService extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = "ridelatertask";
    private String response = "";
    private ProgressDialog dialog;
    private UserData userData;
    private DriverDetails driverDetails;
    private SharedPreferencesUtility sharedPreferencesUtility = SharedPreferencesUtility.getInstance();

    public RideLaterService(Context context) {
        this.context = context;
        userData = UserData.getinstance(context);
        driverDetails = DriverDetails.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.please_wait));
        dialog.show();
    }

    protected String doInBackground(String... params) {
        String url = ProjectURls.RIDE_LATER_CONFIRM_URL;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("d_email", DriverDetails.getDriver_email());
        keyValue.put("d_cabtype", "" + userData.getCab_type());
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("pick_date", userData.getUserDatehitformat());
        keyValue.put("pick_time", userData.getUserTimehitformat());
        keyValue.put("pick_address", userData.getAddress());
        keyValue.put("dest_address", userData.getDesti_address());
        keyValue.put("distance", userData.getDistance());
        keyValue.put("cab_number", DriverDetails.getCabNumber());
        Log.d(TAG, "" + keyValue);
        try {
            FetchUrl findLocation = new FetchUrl();
            response = findLocation.fetchUrl(url, keyValue);
            Log.d("response", response);
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
            userData.setUnique_Table_ID(obj.getString("unique_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
