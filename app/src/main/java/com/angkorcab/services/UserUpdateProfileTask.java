package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
public class UserUpdateProfileTask extends AsyncTask<String,String,String> {

    private Context context;
    private UserData userData;
    private ProgressDialog dialog;
    private static final String TAG = UserUpdateProfileTask.class.getCanonicalName();
    private String response;
    private TaskCallback taskCallback;

    public UserUpdateProfileTask(Context context,TaskCallback taskCallback){
        this.context = context;
        this.taskCallback = taskCallback;
        userData = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(R.string.please_wait));
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = ServiceUrl.USER_UPDATE_PROFILE;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("email", SharedPreferencesUtility.loadUserEmail(context));
        keyValue.put("name",userData.getName().toString());
        if(!userData.getPassword().isEmpty() || !userData.getPassword().equals("")){
            keyValue.put("password",userData.getPassword());
        }



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
                    taskCallback.done(TaskCallback.Action.SAVE_USER_PROFILE);

                    break;
                default:
                    break;
            }
        }
    };






}
