package com.angkorcab.Fcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class CommonUtilities {


/*


    *//**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     *//*

    public static void displayMessage(Context context, String message) {
        Log.e("DISPLAY MESSAGE", "   " + message);
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);

        context.sendBroadcast(intent);
    }*/
}
