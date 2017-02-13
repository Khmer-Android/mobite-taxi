package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.LoginActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import java.util.HashMap;

/**
 * Created by phuong on 12-Dec-16.
 */

public class DriverMisssedConfirmTask extends AsyncTask<String,String,String> {

    private String response;
    private Context context;
    private String TAG = DriverMisssedConfirmTask.class.getSimpleName();
    private UserData userData;


    public DriverMisssedConfirmTask(Context context){
        context = context;
        userData = UserData.getinstance(context);
    }


    protected void onPreExecute() {
        super.onPreExecute();


    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        String url = ServiceUrl.DRIVER_MISSED_CONFIRM_PASSENGER;
        HashMap<String, String> keyValue = new HashMap<String, String>();

        keyValue.put("user_email", userData.getUserEmail());
        Log.i(TAG, "url : " +url);
        Log.d(TAG, "query : " + keyValue);

        try {

            FetchUrl findLocation = new FetchUrl();
            response = findLocation.doGet(url,keyValue);
            Log.d(TAG, "" +response);


        } catch (Exception e) {

            Log.i(TAG,"Error"+e.getMessage());

            e.printStackTrace();
            StackTraceElement[] ele = e.getStackTrace();
        }
        return response;
    }


    protected void onPostExecute(String response){


        Log.i(TAG,"finished...");

    }


}
