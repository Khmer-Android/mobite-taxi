package com.angkorcab.Fcm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.PUT;

/**
 * Created by phuong on 16-Nov-16.
 */

public class Message {

    /*
    *
    Flag class contain information about flag number of message
    #Flag number of broadcast Receiver
    * */
    public static class flag {

        // driver message flag

         public static final String DRIVER_UPDATE_USER_MARKER                      = "100";
         public static final String DRIVER_RECEIVE_BOOKING_REQUEST_PASSENGER       = "101";
        // receive passenger cancel booking 
        public static final String DRIVER_RECEIVE_CANCEL_BOOKING_REQUEST_PASSENGER= "102";
        // cancel booking of passenger
         public static final String DRIVER_CANCEL_REQUEST_PASSENGER_BOOKING        = "103";
        public static final String DRIVER_CONFIRM_REQUEST_PASSENGER =   "104";

         // Passenger Message flag
         public static final String PASSENGER_UPDATE_DRIVER_MARKER  =   "200";








    }



    public static final String DRIVER_RECIEVE_REQUEST="com.angkorcab.taxi.DRIVER_MESSAGE_REQUEST";

    public static final String DRIVER_MESSAGE     = "com.angkorcab.taxi.DRIVER_MESSAGE";
    public static final String PASSENGER_MESSAGE  = "com.angkorcab.taxi.PASSENGER_MESSAGE";
    public static final String RIDE_MESSAGE       = "com.angkorcab.taxi.RIDE_MESSAGE";






    public static final String EXTRA_MESSAGE = "data";
    public static final String EXTRA_FLAG    = "flag";







}
