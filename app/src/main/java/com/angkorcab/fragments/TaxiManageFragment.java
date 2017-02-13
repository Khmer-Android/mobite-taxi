package com.angkorcab.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.angkorcab.model.UserData;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.services.TaxiGetTask;
import com.angkorcab.services.TaxiUpdateTask;
import com.angkorcab.services.UserGetProfileTask;
import com.angkorcab.services.UserUpdateProfileTask;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;


public class TaxiManageFragment extends BaseFragment implements View.OnClickListener, TaskCallback
{
    private Context context;
    boolean doubleBackToExitPressedOnce = false;
    private final int MY_PERMISSIONS_REQUEST_STOP = 6;

    private Spinner register_taxi_type;


    private UserData userData;
    private Button btnUpdate;
    private boolean noChanged = true;
    private EditText editName,editNumber,editFarePerHour,editFarePerKm;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_taxi_manage, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("manage taxi.");
        context = getActivity();


        editName          = (EditText)root.findViewById(R.id.name);
        editNumber           = (EditText)root.findViewById(R.id.number);
        editFarePerHour       = (EditText)root.findViewById(R.id.fare_per_hour);
        editFarePerKm= (EditText)root.findViewById(R.id.fare_per_km);
        btnUpdate          = (Button)root.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        userData = UserData.getinstance(context);

/*
        if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase(Role.PASSENGER)){

                register_taxi_type.setVisibility(View.GONE);
        }
        if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase(Role.DRIVER)){
            register_taxi_type.setVisibility(View.VISIBLE);
            String[] items = getResources().getStringArray(R.array.register_user_register_taxi_type);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
            register_taxi_type.setAdapter(adapter);
        }*/

        new TaxiGetTask(TaxiManageFragment.this,context).execute();



        return root;
    }

    @Override
    public void done(String action){

        switch (action){
            case Action.GET_TAXI:


                editName.setText(userData.getTransportation().getName());
                editNumber.setText(userData.getTransportation().getNumber());
                editFarePerHour.setText(userData.getTransportation().getFare_per_hour());
                editFarePerKm.setText(userData.getTransportation().getFare_per_km());
                Log.i(TAG,"name"+userData.getTransportation().getName());
                Log.i(TAG,"GET Taxi working...");

                break;

            case Action.UPDATE_TAXI:

                Toast.makeText(context,"Update taxi success",Toast.LENGTH_SHORT).show();

                break;
        }


    }


    @Override
    protected void backPressed() {
        //replaceFragment(context,new HelpFragment());
        try {
            if (doubleBackToExitPressedOnce) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    System.exit(0);
                    ((MainActivity) getActivity()).finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    super.onDestroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(context, R.string.tap_again, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

          Log.i(TAG,"id"+v.getId()+"");
        switch (v.getId()) {
            case R.id.btnUpdate:
                Log.i(TAG,"click");

               if(validateForm()){
                   new TaxiUpdateTask(TaxiManageFragment.this,context).execute();
               }
                break;
        }
    }


    private boolean validateForm(){

        boolean $return = true;

        UserData.Transportation tran = UserData.Transportation.getInstance();

       String name = editName.getText().toString();
       String number = editNumber.getText().toString();
       String farePerHour = editFarePerHour.getText().toString();
       String farePerKm   = editFarePerKm.getText().toString();


        if(name.equals("") || name.isEmpty()) {
            Toast.makeText(context,"You name can not empty",Toast.LENGTH_SHORT).show();
            $return = false;
        }else {
            $return = true;
           tran.setName(name);
        }

        if(number.equals("") || number.isEmpty()){
            Toast.makeText(context,"You number can not empty",Toast.LENGTH_SHORT).show();
            $return = false;
        } else {
            $return = true;
            tran.setNumber(number);
        }


        if(farePerHour.equals("") || farePerHour.isEmpty()) {
            Toast.makeText(context,"You fare per hour can not empty",Toast.LENGTH_SHORT).show();
            $return = false;
        }else {
            $return = true;
            tran.setFare_per_hour(farePerHour);
        }


        if(farePerKm.equals("") || farePerKm.isEmpty()) {
            Toast.makeText(context,"You fare per km can not empty",Toast.LENGTH_SHORT).show();
            $return = false;
        }else {
            $return = true;
            tran.setFare_per_hour(farePerHour);
        }

           userData.setTransportation(tran);
        return  $return;

    }



    @Override
    public void onPause() {
        super.onPause();
        try {
            System.gc();

        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG,"onPause"+e.getMessage());
        }
    }

    @Override
    public void onStop() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_STOP);
        } else {

        }

        super.onStop();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onStart(){
        Log.i(TAG,"onStart");
        super.onStart();
    }


    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        try {




        } catch (Exception e) {
            Log.e(TAG,"onResume"+e.getMessage().toLowerCase());
        }
    }









    
}