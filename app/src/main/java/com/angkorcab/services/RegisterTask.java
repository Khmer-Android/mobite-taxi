package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.Role;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.taxi.LoginActivity;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.taxi.RegisterActivity;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by phuong on 10/20/2016.
 */
public class RegisterTask extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = "RegisterTask";
    private String response = "";
    private ProgressDialog dialog;
    private String full_name, password,email,taxi_type,user_type;

    private SharedPreferencesUtility sharedPreferencesUtility = SharedPreferencesUtility.getInstance();

    public RegisterTask(Context context,
                        String full_name,
                        String email,
                        String password,
                        String user_type,
                        String taxi_type
    ) {
        this.context = context;
        this.full_name = full_name;
        this.email = email;
        this.taxi_type = taxi_type;
        this.user_type = user_type;
        this.password = password;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        if(dialog==null) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getResources()
                    .getString(R.string.please_wait));
            dialog.show();
        }
    }

    protected String doInBackground(String... params) {
        String url = ServiceUrl.USER_REGISTER;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("app_id",  SharedPreferencesUtility.loadRegistrationId(context));
        keyValue.put("full_name",full_name);
        keyValue.put("email", email);
        keyValue.put("password", password);
        keyValue.put("user_type",user_type);
        keyValue.put("taxi_type",taxi_type);
        Log.d(TAG, "query: " + keyValue);
        Log.i(TAG, "url:" +url);
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

    protected void onPostExecute(String response) {
        try {
            if(dialog!=null) {
                dialog.dismiss();
            }

            String json = response;
            try{
                JSONObject obj = new JSONObject(json);
                // result status
                int result = obj.getInt("success");

                if(result==ResultStatus.SUCCESS) {
                    Toast.makeText(context,
                            R.string.successfully_registered,
                            Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                if(result==ResultStatus.FAILED) {
                    String message = obj.getString("message").toString();

                    Toast.makeText(context,"Something went wrong, "+message,Toast.LENGTH_LONG).show();
                }
            }catch (JSONException message) {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
