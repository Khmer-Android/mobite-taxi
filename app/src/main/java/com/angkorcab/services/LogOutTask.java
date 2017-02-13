package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.DriverModel;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by phuong on 11/2/2016.
 */

public class LogOutTask  extends AsyncTask<String, String, String> {

    private String TAG = "LogOutTask";
    private TaskCallback taskCallback;
    private Context context;
    private String response = "";
    private ProgressDialog dialog;
    private UserData userData;


    public LogOutTask(Context context,TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
        this.context = context;
        userData = UserData.getinstance(context);

    }
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(R.string.please_wait));


        if(!((MainActivity) context).isFinishing())
        {
            dialog.show();
        }

    }

    protected String doInBackground(String... params){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = ServiceUrl.USER_UPDATE_AVAILABLE;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("latitude",  userData.getUserLongitude());
        keyValue.put("longitude", userData.getUserLongitude());

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

                  myHandler.sendEmptyMessage(0);

              }

          }catch (JSONException ex) {

          }
    }



    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SharedPreferencesUtility.resetSharedPreferences(context);
                    taskCallback.done(TaskCallback.Action.LOGOUT_FINISHED);

                    break;
                default:
                    break;
            }
        }
    };


}
