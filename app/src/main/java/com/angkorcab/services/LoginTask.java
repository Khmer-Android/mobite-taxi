package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.Role;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.constants.Value;
import com.angkorcab.model.DriverModel;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.LoginActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginTask extends AsyncTask<String, String, String> {
    private Context context;
    private String TAG = "Logintask";
    private String response = "";
    private ProgressDialog dialog;

    private TaskCallback taskCallback;
    private UserData userData;


    public LoginTask(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
        this.context = (Context)taskCallback;
        userData = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);

        dialog.setMessage(context.getResources() .getString(R.string.please_wait));
        dialog.setCancelable(false);
        if(!((LoginActivity) context).isFinishing())
        {
            dialog.show();
        }

    }

    protected String doInBackground(String... params) {

        try {
            Thread.sleep(300);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = ServiceUrl.USER_LOGIN;
        HashMap<String, String> keyValue = new HashMap<String, String>();
        keyValue.put("app_id", SharedPreferencesUtility.loadRegistrationId(context));

        keyValue.put("latitude", ""+ userData.getCurrentLocation().latitude);
        keyValue.put("longitude",""+ userData.getCurrentLocation().longitude);
        keyValue.put("email", userData.getEmail());
        keyValue.put("password", userData.getPassword());
        Log.i(TAG, "" +url);
        Log.d(TAG, "" + keyValue);

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
            JSONObject obj = new JSONObject(json);

            // result status
            int result = obj.getInt("success");
            int approved = obj.getInt("approved");

            // When user and password match
            if (result == ResultStatus.SUCCESS) {

                JSONObject userObject = obj.getJSONObject("user");
                JSONArray rolesArray = userObject.getJSONArray("roles");
                JSONObject role                 = rolesArray.getJSONObject(0);
                String role_name = role.getString("name");
                SharedPreferencesUtility.setUserId(context,userObject.getString("id"));
                SharedPreferencesUtility.saveUserEmail(context, userData.getEmail());
                SharedPreferencesUtility.saveUserPassword(context, userData.getPassword());
                SharedPreferencesUtility.setLoginflag(context, Value.trueAble);
                SharedPreferencesUtility.saveUserType(context, role_name);
                SharedPreferencesUtility.saveProfilePic(context, "");
                SharedPreferencesUtility.saveRoleName(context,role_name);


                if (role_name.equalsIgnoreCase(Role.PASSENGER) || role_name.equalsIgnoreCase(Role.DRIVER) && approved == ResultStatus.APPROVED )  {
                    JSONArray transportations = userObject.getJSONArray("transportations");
                    SharedPreferencesUtility.saveUsername(context,userObject.getString("name"));
                    if(transportations==null) {
                        JSONObject transportation                 = transportations.getJSONObject(0);
                        String  taxi_type =  String.valueOf(transportation.getInt("id"));
                        DriverModel.setTaxi_type(taxi_type);
                        SharedPreferencesUtility.saveCabType(context,transportation.getInt("id"));
                    }



                    myHandler.sendEmptyMessage(0);



                } else  {

                        if(approved !=ResultStatus.APPROVED){

                            Toast.makeText(context, "Your account not yet approved to access the application."+role_name,
                                    Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(context, "Your authinication can not be here."+role_name,
                                    Toast.LENGTH_SHORT).show();
                        }


                }
            }
            if (result==ResultStatus.FAILED) {
                    Toast.makeText(context, "Wrong user name or password", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtility.saveUserEmail(context,    Value.nullAble);
                SharedPreferencesUtility.saveUserPassword(context, Value.nullAble);
                SharedPreferencesUtility.setLoginflag(context, Value.falseAble);



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

                    taskCallback.done("");

                    break;
                default:
                    break;
            }
        }
    };


}
