package com.angkorcab.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.angkorcab.network.OkHttpClientSingleTon;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
public class FetchUrl {
    private URL url;


    private  OkHttpClientSingleTon client =  OkHttpClientSingleTon.getInstance();

    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");



    public String doGet(String url, HashMap<String, String> values) throws IOException {

        HttpUrl.Builder urlBuilder = makeQueryString(url,values);
        String requestUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }


    public String doPost(String url, HashMap<String, String> values) throws IOException {

        HttpUrl.Builder urlBuilder = makeQueryString(url,values);
        String requestUrl = urlBuilder.build().toString();
        String json =  "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "]}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private HttpUrl.Builder makeQueryString(String url,HashMap<String, String> params){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();


        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(),entry.getValue());
        }
        return urlBuilder;
    }





    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String fetchUrl(String urlString, HashMap<String, String> values) {
        String response = "";
        try {
            url = new URL(urlString);
            Log.d("url string", urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    os, "UTF-8"));
            writer.write(getPostDataString(values));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
                throw new Exception(responseCode + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.d("query string", result.toString());
        return result.toString();
    }
}