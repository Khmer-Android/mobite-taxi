package com.angkorcab.network;

/**
 * Created by phuong on 10/18/2016.
 */
import com.squareup.okhttp.OkHttpClient;

public class OkHttpClientSingleTon extends OkHttpClient  {


    protected static OkHttpClientSingleTon instance = null;

    public static OkHttpClientSingleTon getInstance(){
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (OkHttpClientSingleTon.class) {
                if (instance == null) {
                    instance = new OkHttpClientSingleTon();
                }
            }
        }
        return instance;

    }


}
