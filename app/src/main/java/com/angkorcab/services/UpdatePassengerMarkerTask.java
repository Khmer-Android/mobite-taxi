package com.angkorcab.services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.Role;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.DriverModel;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.DialogMessage;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author by phuong phally on 11/7/2016.
 */

public class UpdatePassengerMarkerTask    extends AsyncTask<String, String, String> {


    private Context context;
    private String response;
    private String TAG = getClass().getSimpleName().toString();

    public UpdatePassengerMarkerTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        DialogMessage.showProgressDialog(context,"Do Login",context.getResources() .getString(R.string.please_wait));
    }

    protected String doInBackground(String... params) {

        try {
            Thread.sleep(300);




        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return response;
    }

    protected void onPostExecute(String response) {
        try {
            DialogMessage.dismissProgressDialog();
            String json = response;
            JSONObject obj = new JSONObject(json);

            // result status
            int result = obj.getInt("success");

            // When user and password match
            if (result == ResultStatus.SUCCESS) {

                Log.i(TAG,"success result.");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:


                    break;
                default:
                    break;
            }
        }
    };



}
