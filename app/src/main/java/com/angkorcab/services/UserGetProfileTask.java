package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by phuon on 22-Dec-16.
 */
public class UserGetProfileTask  extends AsyncTask<String,String,String> {


    protected Context context;
    private ProgressDialog dialog;
    private String TAG = UserGetProfileTask.class.getSimpleName();
    private String response;
    private UserData userData;
    private TaskCallback taskCallback;


    public UserGetProfileTask(TaskCallback taskCallback,Context context){
        this.context = context;
        this.taskCallback = taskCallback;
        this.userData = UserData.getinstance(context);
    }


    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(R.string.please_wait));
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = ServiceUrl.USER_GET_PROFILE;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));

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
            if(result== ResultStatus.SUCCESS){
                Log.i(TAG,"result success");
                JSONObject user = obj.getJSONObject("user");
                userData.setName(user.getString("name"));
                userData.setUserEmail(user.getString("email"));
                myHandler.sendEmptyMessage(0);

            }
            Log.i(TAG,"value: "+obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i(TAG,"logger");
                    taskCallback.done(TaskCallback.Action.GET_USER_PROFILE);

                    break;
                default:
                    break;
            }
        }
    };


}
