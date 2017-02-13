package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by phuong on 10/26/2016.
 */
public class UpdateDriverMarkerTask extends AsyncTask<String, String, String> {

    private Context context;
    private ProgressDialog dialog;

    private  String TAG = "UpdateDriverMarkerTask";
    private String url  = ServiceUrl.PASSENGER_UPDATE_DRIVER_MARKER;
    private String response = null;




    public UpdateDriverMarkerTask(Context $context ){

        this.context   = $context;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources()
                .getString(R.string.please_wait));
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            Log.i(TAG, " do in background.");

            HashMap<String, String> keyValue = new HashMap<String, String>();
            keyValue.put("cabtype", "" + SharedPreferencesUtility.loadCabType(context));
            keyValue.put("lat", "" +     SharedPreferencesUtility.getLatitude(context));
            keyValue.put("long",  "" +   SharedPreferencesUtility.getLongitude(context));
            keyValue.put("email", ""+    SharedPreferencesUtility.loadUserEmail(context));
            Log.i(TAG, TAG.toString()+"URL:"+url);
            Log.i(TAG, TAG.toString()+"Query String: "+keyValue.toString());
            FetchUrl fetchUrl = new FetchUrl();
            response = fetchUrl.doGet(url, keyValue);
            Log.d(TAG, TAG.toString()+" doInBackground "+ response);
        }catch (IOException e){
            Log.i(TAG,"error"+e.getMessage());
        }

        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        super.onPostExecute(result);

    }
}
