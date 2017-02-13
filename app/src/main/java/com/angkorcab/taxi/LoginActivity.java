package com.angkorcab.taxi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.angkorcab.constants.IWebConstant;
import com.angkorcab.helper.CheckConnectivity;
import com.angkorcab.helper.MyWebViewClient;
import com.angkorcab.model.UserData;
import com.angkorcab.services.LoginTask;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.utils.GPSTracker;
import com.angkorcab.utils.SharedPreferencesUtility;


public class LoginActivity extends AppCompatActivity implements TaskCallback, View.OnClickListener{

    private Context context;
    private UserData userData;
    private EditText email;
    private EditText password;
    private ProgressBar login_progress_bar;
    private Button login;
    private Button register;
    private TextView forget_password;

    private String TAG = "LoginActivity";

    private CheckConnectivity checkConnectivity;
    private GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        userData= UserData.getinstance(context);
        Log.i(TAG,"clear all data");
        SharedPreferencesUtility.resetSharedPreferences(context);
        try {
            checkConnectivity = new CheckConnectivity(getApplicationContext());
            email = (EditText)                  findViewById(R.id.login_email);
            password = (EditText)               findViewById(R.id.login_password);
            login_progress_bar = (ProgressBar)  findViewById(R.id.login_progressbar);
            login =(Button)                     findViewById(R.id.login_button_login);
            register= (Button)                  findViewById(R.id.login_button_register);
            forget_password = (TextView)        findViewById(R.id.login_text_forget_password);
            login_progress_bar.setVisibility(View.INVISIBLE);
            login.setOnClickListener(this);
            forget_password.setOnClickListener(this);
            register.setOnClickListener(this);



            gpsTracker = new GPSTracker(context);
            gpsTracker.getLocation();

                      Log.i(TAG,"Locatin Page"+gpsTracker.getCurrentLocation());
            userData.setCurrentLocation(gpsTracker.getCurrentLocation());

            if (getIntent().getBooleanExtra("EXIT", false)) {
                Log.i(TAG,"click exit");
                MyWebViewClient.handleIncomingIntent(context,  SplashActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loginProcess() {
        try {
            login_progress_bar.setVisibility(View.INVISIBLE);
            if (checkConnectivity.isNetworkAvailable()) {
                if (email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            IWebConstant.EMAIL_NOT_ENTERED, Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().contains("@") == false
                        || email.getText().toString().contains(".") == false) {
                    Toast.makeText(getApplicationContext(),
                            IWebConstant.EMAIL_NOT_VALID, Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            IWebConstant.ENTER_PASSWORD, Toast.LENGTH_SHORT).show();
                } else {

                    userData.setEmail(email.getText()+"");
                    userData.setPassword(password.getText()+"");
                            new LoginTask(LoginActivity.this).execute();

                }
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.internetdisabledmessage, Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (SharedPreferencesUtility.getLoginflag(context)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            super.onDestroy();
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        email.setText("");
        password.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button_login:
                loginProcess();
                break;
            case R.id.login_text_forget_password:
                startActivity(new Intent(context,
                        ForgotPasswordActivity.class));
                break;
            case R.id.login_button_register:
                startActivity(new Intent(context, RegisterActivity.class));
                break;
        }
    }




    @Override
    public void done(String action){

        Log.i(TAG,"done method in loginActivity");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

       finish();

    }

    protected void onDestroy() {
        super.onDestroy();
        if(checkConnectivity !=null) {
            checkConnectivity = null;
        }
    }


}
