package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.angkorcab.constants.ResultStatus;
import com.angkorcab.constants.ServiceUrl;
import com.angkorcab.constants.TransportationType;
import com.angkorcab.model.MarkerObject;
import com.angkorcab.model.UserData;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.SharedPreferencesUtility;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ListMarkerTask extends AsyncTask<String, String, String> {
    private Context context;
    private SharedPreferencesUtility preferencesUtility;
    String TAG = getClass().getSimpleName();
    private String response;
    private ProgressDialog dialog;
    private UserData userData;
    private HashMap<Integer,MarkerObject> markers;
    private MarkerCallBack markerCallBack;
    public static boolean isDriver=false;


    public ListMarkerTask(Context context,MarkerCallBack markerCallBack, HashMap<Integer,MarkerObject> markers ) {

        this.context = context;
        this.markerCallBack = markerCallBack;
        this.markers = markers;
        this.userData = UserData.getinstance(context);
        preferencesUtility = SharedPreferencesUtility.getInstance();


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


            String url = ServiceUrl.USER_MARKER_LISTS;
            HashMap<String, String> keyValue = new HashMap<String, String>();

            keyValue.put("transportation", "" + userData.getTransportationName());
            keyValue.put("latitude", "" +       userData.getCurrentLocation().latitude);
            keyValue.put("longitude",  "" +     userData.getCurrentLocation().longitude);
            keyValue.put("email", ""+      SharedPreferencesUtility.loadUserEmail(context));
            keyValue.put("role", ""+       SharedPreferencesUtility.loadRoleName(context));

            Log.i(TAG, TAG.toString()+"URL:"+url);
            Log.i(TAG, TAG.toString()+"Query String: "+keyValue.toString());

            FetchUrl fetchUrl = new FetchUrl();
            response = fetchUrl.doGet(url, keyValue);

            Log.d(TAG, "response: "+ response);


        }catch (IOException e){
            Log.i(TAG,"doInBackground error"+e.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        super.onPostExecute(result);

        /*if(googleMap !=null) {
            googleMap.clear();
        }*/

        try {
            String json = response;
            JSONObject obj = new JSONObject(json);

            // result status
            int success = obj.getInt("success");
            if(ResultStatus.SUCCESS==success){

                JSONArray users = obj.getJSONArray("users");
                Log.i(TAG,"length user: "+users.length());
                Log.i(TAG,"users"+users);

                for (int i=0;i<users.length();i++){

                    JSONObject user = users.getJSONObject(i);
                    Log.i(TAG,"working loop"+user.getDouble("latitude")+user.getDouble("longitude"));
                    LatLng position = new LatLng(user.getDouble("latitude"), user.getDouble("longitude"));


                    if(isDriver){
                        // driver...
                        markers.put(user.getInt("id"),new MarkerObject(position,user.getInt("id"),user.getString("name"),R.mipmap.passenger_call_taxi));
                    } else {
                        switch (userData.getTransportationName()){
                            case TransportationType.TAXI:
                                markers.put(user.getInt("id"),new MarkerObject(position,user.getInt("id"),user.getString("name"),R.drawable.tab_icon_angkorcab_taxi));
                                break;
                            case TransportationType.CAR:
                                markers.put(user.getInt("id"),new MarkerObject(position,user.getInt("id"),user.getString("name"),R.drawable.tab_icon_angkorcab_car));
                                break;
                            case TransportationType.MOTO:
                                markers.put(user.getInt("id"),new MarkerObject(position,user.getInt("id"),user.getString("name"),R.drawable.tab_icon_angkorcab_moto));
                                break;
                            case TransportationType.TUK_TUK:
                                markers.put(user.getInt("id"),new MarkerObject(position,user.getInt("id"),user.getString("name"),R.drawable.tab_icon_angkorcab_tuktuk));
                                break;
                        }
                    }





                }

            }

            myHandler.sendEmptyMessage(0);


        }catch (Exception      e){
            Log.i(TAG,"onPostExecute error "+e.getMessage());
        }



    }




    private  Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i(TAG,"what is working...");
                    markerCallBack.updateMarker();

                    break;
                default:
                    break;
            }
        }
    };




}
