package com.angkorcab.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.angkorcab.taxi.MainActivity;

/**
 * Created by phuong on 11/3/2016.
 */

public class DialogMessage {

    private static ProgressDialog progressDialog = null;

    public static void showProgressDialog(Context context, String title, String message)
    {

        if(progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }



        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        if(!((AppCompatActivity) context).isFinishing())
        {
            progressDialog.show();
        }
    }

    public static void dismissProgressDialog()
    {
        if(progressDialog !=null)  progressDialog.dismiss();
    }



}
