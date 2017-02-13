package com.angkorcab.constants;

/**
 * Created by phuong on 28-Nov-16.
 */

public class Page {

    public static String current="";


    public static final String DRIVER_RECEIVE_PASSENGER_REQUEST_PAGE= "driver_receive_passenger_request";

    public static final String DRIVER_PAGE = "driver_page";
    public static final String PASSENGER_PAGE="passenger_page";


    public static String getCurrent() {
        return current;
    }

    public static void setCurrent(String current) {
        Page.current = current;
    }
}
