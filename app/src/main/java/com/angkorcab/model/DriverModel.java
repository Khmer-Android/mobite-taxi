package com.angkorcab.model;

/**
 * Created by phuong on 11/2/2016.
 */

public class DriverModel {


    private static String taxi_type;

    private static String latitude;

    private static String longitude;

    private static String status;






    public static String getTaxi_type() {
        return taxi_type;
    }

    public static void setTaxi_type(String taxi_type) {
        DriverModel.taxi_type = taxi_type;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        DriverModel.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        DriverModel.longitude = longitude;
    }


    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        DriverModel.status = status;
    }
}
