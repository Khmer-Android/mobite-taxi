package com.angkorcab.taxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.angkorcab.services.RegisterTask;

public class RegisterActivity extends AppCompatActivity {
    Context context;
    private  static final String TAG = "RegisterActivity";
    private Handler mHandler = new Handler();
    private RadioButton passenger;
    private RadioButton driver;
    private Spinner spinner;
    private Button register;
    private Button login;
    private EditText full_name , email, password,password_retype;
    private String taxiType,userType;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        try {
            spinner = (Spinner) findViewById(R.id.register_taxi_type);
            String[] items = getResources().getStringArray(R.array.register_user_register_taxi_type);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(adapter);


            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.register_user_type_radio_button);

            passenger = (RadioButton) findViewById(R.id.register_button_passenger);
            driver    = (RadioButton) findViewById(R.id.register_button_driver);
            register = (Button) findViewById(R.id.register_button_singup);
            full_name = (EditText) findViewById(R.id.register_fullname);
            email = (EditText) findViewById(R.id.register_email);
            password = (EditText) findViewById(R.id.register_password);
            password_retype = (EditText) findViewById(R.id.register_password_repeat);
            linearLayout = (LinearLayout) findViewById(R.id.register_taxi_container);
            login = (Button) findViewById(R.id.register_button_login);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);

                }
            });


            spinner.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if(driver.isChecked()) {
                        linearLayout.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);

                    }
                    if(passenger.isChecked()) {
                        linearLayout.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                    }
                }
            });



            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(driver.isChecked()) {
                       taxiType = spinner.getSelectedItem().toString();
                       userType = "Driver";
                    } else {
                        userType="Ride";
                        taxiType= "0";
                    }

                    int taxi_id = 1;
                    if(taxiType.equalsIgnoreCase("Taxi")) {
                        taxi_id = 1;
                    }
                    if(taxiType.equalsIgnoreCase("Car")) {
                        taxi_id = 2;
                    }
                    if(taxiType.equalsIgnoreCase("TukTuk")) {
                        taxi_id = 3;
                    }
                    if(taxiType.equalsIgnoreCase("Motorbike")){
                        taxi_id = 4;
                    }


                    try{

                        RegisterTask registerTask = new RegisterTask(context,full_name.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString(),
                                userType,
                                String.valueOf(taxi_id));


                        String result = registerTask.execute().get();

                        Log.i(TAG,"result is : "+result);


                    }catch (Exception e) {

                    }


                 //   Toast.makeText(getApplicationContext(),taxiType , Toast.LENGTH_LONG).show();

                }
            });







        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
