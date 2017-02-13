package com.angkorcab.Fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.angkorcab.constants.Page;
import com.angkorcab.fragments.DriverRecieverPassengerRequesst;
import com.angkorcab.services.UpdatePassengerMarkerTask;
import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.services.Driver_loc_update_task;
import com.angkorcab.utils.WakeLocker;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    // public static String message;

    private String title="unknow title";
    private String flag="unknow flag";
    private String message="unknow message";
    Map<String, String> data;

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            WakeLocker.acquire(getApplicationContext());

            data = remoteMessage.getData();
            flag = data.get("flag");
            title= data.get("title");

            Log.d(TAG, "Notification Title"+data.get("title"));
            Log.d(TAG, "Data" + data);
            if(flag==null)return;
            switch (flag) {

                // update marker in driver home
                case Message.flag.DRIVER_UPDATE_USER_MARKER:

                    Log.e(TAG, " DRIVER_MESSAGE" +data.get("title"));

                    Intent driver_100= new Intent(Message.DRIVER_MESSAGE);
                    driver_100.putExtra(Message.EXTRA_MESSAGE,   data.toString());
                    driver_100.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(driver_100);
                    break;


                // user click button book driver
                case Message.flag.DRIVER_RECEIVE_BOOKING_REQUEST_PASSENGER:
                    Log.e(TAG, " DRIVER_MESSAGE" +data.get("title"));

                    Page.setCurrent(Page.DRIVER_RECEIVE_PASSENGER_REQUEST_PAGE);

                    Intent driver_101= new Intent(Message.DRIVER_MESSAGE);
                    driver_101.putExtra(Message.EXTRA_MESSAGE,   data.get("data").toString());
                    driver_101.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(driver_101);

                    sendNotificationToIntent();

                    break;

                // user cancel booking Driver
                case Message.flag.DRIVER_RECEIVE_CANCEL_BOOKING_REQUEST_PASSENGER:
                    Log.e(TAG, " DRIVER_MESSAGE" +data.get("title"));



                    Intent driver_102= new Intent(Message.DRIVER_RECIEVE_REQUEST);
                    driver_102.putExtra(Message.EXTRA_MESSAGE,   data.get("data").toString());
                    driver_102.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(driver_102);

                    sendNotificationToIntent();

                    break;

                case Message.flag.DRIVER_CANCEL_REQUEST_PASSENGER_BOOKING:

                    Log.i(TAG,"flag working "+flag);
                    data = remoteMessage.getData();
                    Log.i(TAG,"flag data "+data);

                    Intent driver_103= new Intent(Message.RIDE_MESSAGE);
                    driver_103.putExtra(Message.EXTRA_MESSAGE,   data.get("data").toString());
                    driver_103.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(driver_103);

                    break;


                case Message.flag.DRIVER_CONFIRM_REQUEST_PASSENGER:

                    Log.i(TAG,"flag working "+flag);
                    data = remoteMessage.getData();
                    Log.i(TAG,"flag data "+data);
                    Intent driver_104= new Intent(Message.RIDE_MESSAGE);
                    driver_104.putExtra(Message.EXTRA_MESSAGE,   data.get("data").toString());
                    driver_104.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(driver_104);

                    break;





                // update marker in passeger home
                case Message.flag.PASSENGER_UPDATE_DRIVER_MARKER:

                    Log.e(TAG, " PASSENGER_MESSAGE" );
                    Intent passenger_200 = new Intent(Message.PASSENGER_MESSAGE);
                    passenger_200.putExtra(Message.EXTRA_MESSAGE,  data.toString());
                    passenger_200.putExtra(Message.EXTRA_FLAG,  flag);
                    sendBroadcast(passenger_200);
                    break;
            }

            WakeLocker.release();

        } catch (Exception e) {

        }
    }


    private void driverUpdatePassengerMarker(Context context){

        new UpdatePassengerMarkerTask(context).execute();

    }


    private void updateDriverlocation(Context context, String email) {
        Driver_loc_update_task task = new Driver_loc_update_task(context,
                email);
            task.execute();

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notificationicon)
                .setContentTitle("Cabbooking Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendNotificationToIntent() {



        Intent intent = new Intent(this, DriverRecieverPassengerRequesst.class);
        intent.putExtra(Message.EXTRA_MESSAGE,data.get("data").toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notificationicon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
