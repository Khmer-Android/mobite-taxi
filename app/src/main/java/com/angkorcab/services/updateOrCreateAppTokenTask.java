package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;

import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import java.util.HashMap;

public class updateOrCreateAppTokenTask extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = getClass().getSimpleName();
    private String response = "";
    private ProgressDialog dialog;

    public updateOrCreateAppTokenTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.please_wait));
        dialog.show();
    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.UPDATE_APP_ID;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email",      SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("app_id",     SharedPreferencesUtility.loadRegistrationId(context));
        Log.i(TAG,""+url);
        Log.d(TAG, "" + keyValue);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
