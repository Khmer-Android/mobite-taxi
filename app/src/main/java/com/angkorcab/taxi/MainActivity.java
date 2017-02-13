package com.angkorcab.taxi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angkorcab.constants.Page;
import com.angkorcab.constants.Role;
import com.angkorcab.constants.UserStatusList;
import com.angkorcab.fragments.CabMoneyFragment;
import com.angkorcab.fragments.DriverFragment;
import com.angkorcab.fragments.DriverRecieverPassengerRequesst;
import com.angkorcab.fragments.HelpFragment;
import com.angkorcab.fragments.HomeFragment;
import com.angkorcab.fragments.ProfileFragment;
import com.angkorcab.fragments.ProfileManageFragment;
import com.angkorcab.fragments.RateCardFragment;
import com.angkorcab.fragments.RideDetailsFragment;

import com.angkorcab.fragments.TaxiManageFragment;
import com.angkorcab.model.UserData;
import com.angkorcab.services.LogOutTask;
import com.angkorcab.services.TaskCallback;
import com.angkorcab.utils.SharedPreferencesUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.provider.ContactsContract;
public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener , TaskCallback{
    private Context context;
    private NavigationView navigationView;
    private TextView user_name, user_email;
    private ImageView profilePic;
    private String base64String;
    private UserData userData;
    private  Fragment fragment;
    private static final String  TAG ="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
               try {
            context = MainActivity.this;
            userData = UserData.getinstance(context);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
             user_name = (TextView) headerView.findViewById(R.id.user_name);
            user_email = (TextView) headerView.findViewById(R.id.user_emailid);
            profilePic = (ImageView) headerView.findViewById(R.id.profile_picture);
            base64String = SharedPreferencesUtility.loadProfilePic(context);
            if (base64String.isEmpty()) {
                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.user));
            } else {
                Log.i(TAG,"Profile: shoud be get from database.");
/*
               byte[] encodeByte = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                profilePic.setImageBitmap(bitmap);*/
                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.user));
            }





            user_name.setText(SharedPreferencesUtility.loadUsername(context));
            user_email.setText(SharedPreferencesUtility.loadUserEmail(context));
            //Checking play service is available or not
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            //if play service is not available
            if (ConnectionResult.SUCCESS != resultCode) {
                //If play service is supported but not installed
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    //Displaying message that play service is not installed
                    Toast.makeText(context, R.string.playstore_not_install, Toast.LENGTH_LONG).show();
                    GooglePlayServicesUtil.showErrorNotification(resultCode, context);
                    //If play service is not supported
                    //Displaying an error message
                } else {
                    Toast.makeText(context, R.string.playstore_not_support, Toast.LENGTH_LONG).show();
                }
                //If play service is available
            } else {
                //Starting intent to register device
                 //*Intent itent = new Intent(this, GCMRegistrationIntentService.class);
                //startService(itent);
            }
            // onNewIntent(getIntent());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

           Log.i(TAG,"user type is "+SharedPreferencesUtility.loadUserType(context));

           if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase(Role.PASSENGER)){

               FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragment = new HomeFragment();
               fragmentTransaction.replace(R.id.containerView, fragment, null);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();
               MenuItem item = navigationView.getMenu().getItem(3);
               item.setVisible(false);

           }
           if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase(Role.DRIVER)){

               FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragment = new DriverFragment();
               fragmentTransaction.replace(R.id.containerView, fragment, null);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();


           }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }





    @Override
    public void onBackPressed() {

        Log.i(TAG,"press back main class");
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.home:
                if (SharedPreferencesUtility.loadUserType(context).equalsIgnoreCase(Role.PASSENGER)) {
                    fragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                    fragmentTransaction.commit();
                } else {
                    fragment = new DriverFragment();
                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.profile:
                fragment = new ProfileManageFragment();
                fragmentTransaction.replace(R.id.containerView, fragment, null);
                fragmentTransaction.commit();
                break;
            case R.id.manage_taxi:
                fragment = new TaxiManageFragment();
                fragmentTransaction.replace(R.id.containerView, fragment, null);
                fragmentTransaction.commit();

                break;

            case R.id.rides:
                fragment = new RideDetailsFragment();
                fragmentTransaction.replace(R.id.containerView, fragment, null);
                fragmentTransaction.commit();
                break;

            case R.id.help:
                fragment = new HelpFragment();
                fragmentTransaction.replace(R.id.containerView, fragment, null);
                fragmentTransaction.commit();
                break;
            case R.id.sign_out:
                signOutMethod();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void signOutMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to signout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {


                            userData.setAvailable(UserStatusList.OFFLINE);
                            new LogOutTask(context,MainActivity.this).execute();

                        } catch (Exception e) {
                           Log.i(TAG,"finished"+e.getMessage());
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void done(String action) {
        SharedPreferencesUtility.resetSharedPreferences(context);
        Log.i(TAG,"done method in loginActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"haha super working...");
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



}
