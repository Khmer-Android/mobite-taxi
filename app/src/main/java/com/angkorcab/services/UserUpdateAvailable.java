package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONObject;

import java.util.HashMap;

public class UserUpdateAvailable extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = "UserUpdateAvailable";
    private String response = "";
    private ProgressDialog dialog;

    private UserData userData;


    public UserUpdateAvailable(Context context) {
        this.context = context;
        userData = UserData.getinstance(context);

    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.updating_status));
        dialog.show();
    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.USER_UPDATE_AVAILABLE;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("latitude", userData.getUserLatitude());
        keyValue.put("longitude",userData.getUserLongitude());

        keyValue.put("available", userData.getAvailable());

        Log.d(TAG, " " + keyValue);
        Log.i(TAG, " "+url);


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
            dialog.dismiss();
            JSONObject obj = new JSONObject(response);
            int result = obj.getInt("success");
            if (result == 1) {
                Toast.makeText(
                        context,
                        context.getString(R.string.status_updated) +  " "+userData.getAvailable(), Toast.LENGTH_SHORT)
                        .show();
            }
            if (result == 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
