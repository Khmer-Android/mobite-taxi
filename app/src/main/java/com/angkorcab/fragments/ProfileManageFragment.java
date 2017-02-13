package com.angkorcab.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.angkorcab.Fcm.Message;
import com.angkorcab.constants.DataBaseKey;
import com.angkorcab.constants.Role;
import com.angkorcab.constants.TransportationType;
import com.angkorcab.constants.UserStatusList;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.MarkerObject;
import com.angkorcab.model.MultiDrawable;
import com.angkorcab.model.UserData;
import com.angkorcab.services.ListMarkerTask;
import com.angkorcab.services.MarkerCallBack;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.services.UpdateUserStatusAndLocation;
import com.angkorcab.services.UserGetProfileTask;
import com.angkorcab.services.UserUpdateProfileTask;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.utils.GPSTracker;
import com.angkorcab.utils.GoogleUtil;
import com.angkorcab.utils.SharedPreferencesUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;                 
import java.util.Map;
import java.util.Random;


public class ProfileManageFragment extends BaseFragment implements View.OnClickListener, TaskCallback
{
    private Context context;
    boolean doubleBackToExitPressedOnce = false;
    private final int MY_PERMISSIONS_REQUEST_STOP = 6;

    private Spinner register_taxi_type;

    private EditText editName,editEmail,editPassword,editPasswordRepeapt;
    private UserData userData;
    private Button btnUpdate;
    private boolean noChanged = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile_manage, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        context = getActivity();

        register_taxi_type = (Spinner) root.findViewById(R.id.register_taxi_type);
        editEmail          = (EditText)root.findViewById(R.id.email);
        editName           = (EditText)root.findViewById(R.id.name);
        editPassword       = (EditText)root.findViewById(R.id.user_password);
        editPasswordRepeapt= (EditText)root.findViewById(R.id.user_password_repeat);
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

        new UserGetProfileTask(ProfileManageFragment.this,context).execute();



        return root;
    }

    @Override
    public void done(String action){

        switch (action){
            case Action.GET_USER_PROFILE:
                userData = UserData.getinstance(context);
                editEmail.setText(userData.getUserEmail());
                editEmail.setFocusable(false);
                editName.setText(userData.getName());
                userData.setPassword("");
                Log.i(TAG,"done");

                break;

            case Action.SAVE_USER_PROFILE:

               Toast.makeText(context,"You profile updated.",Toast.LENGTH_SHORT).show();

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
                   new UserUpdateProfileTask(context,ProfileManageFragment.this).execute();
               }
                break;
        }
    }


    private boolean validateForm(){

        boolean $return = true;


        String name = editName.getText().toString();
        String pass = editPassword.getText().toString();
        String pass_repeat = editPasswordRepeapt.getText().toString();







        if(name.equals("") || name.isEmpty()) {
            Toast.makeText(context,"You name can not empty",Toast.LENGTH_SHORT).show();
            $return = false;
        }else {
            $return = true;
            userData.setName(name);
        }
        if(!pass.isEmpty()){

            if(!pass.equals(pass_repeat)){
                Toast.makeText(context,"password not match!",Toast.LENGTH_SHORT).show();
                $return = false;

            } else if(pass.length()<=5 || pass.length()>10) {
                Toast.makeText(context,"You password short.Password should be 6 to 10",Toast.LENGTH_SHORT).show();
                $return = false;
            } else {
                $return = true;
                userData.setPassword(pass);
            }

        }

        Log.i(TAG,"name"+name +"pass"+pass+"repeat pass"+pass_repeat);

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