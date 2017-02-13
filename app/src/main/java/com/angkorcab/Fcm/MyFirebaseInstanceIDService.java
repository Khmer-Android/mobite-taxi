package com.angkorcab.Fcm;

import android.content.Context;
import android.util.Log;

import com.angkorcab.utils.SharedPreferencesUtility;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    Context context;

    /**
     *
     *
     @https://firebase.google.com/docs/cloud-messaging/android/client

     The registration token may change when:

     The app deletes Instance ID
     The app is restored on a new device
     The user uninstalls/reinstall the app
     The user clears app data.


     * **/
    @Override
    public void onTokenRefresh() {

        context = getApplicationContext();
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // save token to data preferience .
        SharedPreferencesUtility.saveRegistrationId(context, refreshedToken);
        sendRegistrationToServer(refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
    }
}
