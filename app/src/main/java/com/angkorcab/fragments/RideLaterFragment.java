package com.angkorcab.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.angkorcab.taxi.R;
import com.angkorcab.constants.IWebConstant;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.helper.CheckDateNTime;
import com.angkorcab.login.LoginDetails;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;

import com.angkorcab.services.RideLaterService;

public class RideLaterFragment extends BaseFragment implements View.OnClickListener {
    private Context context;
    LinearLayout book_button_layout, confirm_button_layout,fareRideLaterLayout;
    TextView date, time, currentloc, destinationloc, cab_type, fare, distance,
            driver_name, driver_number, cab_number,fare_approx;
    ImageButton booknow_button, confirmnow_button, reject_button;
    private UserData userData;
    private DriverDetails driverDetails;
    private CheckConnectivity checkConnectivity;
    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ride_later, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.item_ride_later);
        try {
            context = getActivity();
            userData = UserData.getinstance(context);
            driverDetails = DriverDetails.getinstance(context);
            date = (TextView) root.findViewById(R.id.pick_up_date_RL);
            time = (TextView) root.findViewById(R.id.pickup_time_RL);
            currentloc = (TextView) root.findViewById(R.id.current_loc_RideLater);
            destinationloc = (TextView) root.findViewById(R.id.destination_RideLater);
            cab_type = (TextView) root.findViewById(R.id.cab_type_RideLater);
            fare = (TextView) root.findViewById(R.id.fare_RideLater);
            fare_approx=(TextView)root.findViewById(R.id.fare_approx_RL);
            distance = (TextView) root.findViewById(R.id.distance_RideLater);
            driver_name = (TextView) root.findViewById(R.id.driver_name_RideLater);
            driver_number = (TextView) root.findViewById(R.id.driver_contact_num_RideLater);
            cab_number = (TextView) root.findViewById(R.id.Cab_num_RideLater);
            book_button_layout = (LinearLayout) root.findViewById(R.id.booknow_button_layout);
            fareRideLaterLayout = (LinearLayout) root.findViewById(R.id.fare_distance_layout_RL);
            confirm_button_layout = (LinearLayout) root.findViewById(R.id.confim_button_layout);
            reject_button = (ImageButton) root.findViewById(R.id.reject_img_user_rl);
            booknow_button = (ImageButton) root.findViewById(R.id.booknowbutton_RL);
            confirmnow_button = (ImageButton) root.findViewById(R.id.confirm_img_user_rl);
            checkConnectivity = new CheckConnectivity(context);
            reject_button.setOnClickListener(this);
            booknow_button.setOnClickListener(this);
            confirmnow_button.setOnClickListener(this);
            date.setOnClickListener(this);
            time.setOnClickListener(this);
            driver_number.setOnClickListener(this);
            setData();
            selectDateAndTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    private void selectDateAndTime() {
        final View dialogView = View.inflate(context,
                R.layout.datetimepicker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setView(dialogView);
        alertDialog.show();
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View view) {
                        int hour, minute;
                        DatePicker datePicker = (DatePicker) dialogView
                                .findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView
                                .findViewById(R.id.time_picker);
                        if (CheckDateNTime.check(datePicker, timePicker, context) == 1) {
                            try {
                                datePicker.setMinDate(System
                                        .currentTimeMillis());
                            } catch (Exception e) {
                                Toast.makeText(context,
                                        R.string.time_already_passed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            CheckDateNTime.make_time(timePicker, datePicker);
                            date.setText(LoginDetails.UserDate);
                            time.setText(LoginDetails.UserTime);
                            Log.e("Correct", "TIME SELECTED");
                            alertDialog.dismiss();
                        }
                    }
                });
    }

    private void setData() {
        try {
            currentloc.setText(userData.getAddress());
            if (userData.getCab_type().equalsIgnoreCase("1")) {
                //fare.setText("$75");
                cab_type.setText(R.string.taxi);
            } else if (userData.getCab_type().equalsIgnoreCase("2")) {
               // fare.setText("$100");
                cab_type.setText(R.string.taxi);
            } else {
                //fare.setText("$250");
                cab_type.setText(R.string.taxi);
            }
            destinationloc.setText(userData.getDesti_address());
            distance.setText(userData.getDistance());
            driver_name.setText(DriverDetails.getDriverName());
            driver_number.setText(DriverDetails.getDriverNumber());
            cab_number.setText(DriverDetails.getCabNumber());
            fare.setText(getResources().getString(R.string.currency_sign)+" "+DriverDetails.getFarePerUnit());
            fare_approx.setText(getResources().getString(R.string.currency_sign)+" "+DriverDetails.getFare());
            fareRideLaterLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void backPressed() {
        replaceFragment(context, new RideFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reject_img_user_rl:
                Toast.makeText(context,
                        R.string.rejecting_msg, Toast.LENGTH_LONG).show();

                confirmnow_button.setVisibility(View.INVISIBLE);
                reject_button.setVisibility(View.INVISIBLE);
                break;
            case R.id.booknowbutton_RL:
                if (checkConnectivity.isNetworkAvailable()) {
                    try {
                        new RideLaterService(context).execute();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(
                                R.string.messagedialogtitle))
                                .setMessage(getResources().getString(
                                        R.string.messagedialogmessage))
                                .setCancelable(false)
                                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        book_button_layout.setVisibility(View.GONE);
                                        confirm_button_layout
                                                .setVisibility(View.VISIBLE);
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.confirm_img_user_rl:
                if (checkConnectivity.isNetworkAvailable()) {
                    try {

                        confirmnow_button.setVisibility(View.INVISIBLE);
                        reject_button.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.pick_up_date_RL:
                selectDateAndTime();
                break;
            case R.id.pickup_time_RL:
                selectDateAndTime();
                break;

            case R.id.driver_contact_num_RideLater:
                makeCall();
                break;
        }
    }

    private void makeCall() {
        try {
            phoneNumber = "tel:" + DriverDetails.getDriverNumber();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
            context.startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
