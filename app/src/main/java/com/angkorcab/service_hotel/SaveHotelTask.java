package com.angkorcab.service_hotel;

import android.os.AsyncTask;
import android.util.Log;

import com.angkorcab.services.FetchUrl;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by phuon on 2/10/2017.
 */







public class SaveHotelTask extends AsyncTask<String,String,String> {


    private String TAG = "SaveHotelTask";

    private String response;
    private String url = "http://angkorcab.com/api/hotel/store";


    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG,"first start");
    }




    @Override
    protected String doInBackground(String... params) {


        Log.i(TAG,"Do inbackground....");

        HashMap<String, String> queryString = new HashMap<String, String>();
        queryString.put("name","hotel name");
        queryString.put("price",""+500);
        queryString.put("address","hotel address");
        FetchUrl findLocation = new FetchUrl();
        try {
            response = findLocation.doGet(url,queryString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void onPostExecute(String response) {

        Log.i(TAG,"process finished...");

        Log.i(TAG,response);


    }

}
