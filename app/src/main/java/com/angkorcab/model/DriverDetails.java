package com.angkorcab.model;

import android.content.Context;

/**
 * Created by ract on 15-Jun-16.
 */
public class DriverDetails {

    private static DriverDetails driverDetails;
    private static String DriverName = "NOT FOUND";
    private static String CabNumber = "NOT FOUND";
    private static String DriverNumber = "NOT FOUND";
    private static String NearesCabReachingTime = "NOT FOUND";
    private static int NearestCabDistance = 0;
    private static String Driver_email = "";
    private static String DriverStatus = "available";
    private static String DriverCabType = "1";
    private static String DriverLat = "";
    private static String DriverLong = "";

    private static String currency_type;

    public static DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public static void setDriverDetails(DriverDetails driverDetails) {
        DriverDetails.driverDetails = driverDetails;
    }

    public static String getCurrency_type() {
        return currency_type;
    }

    public static void setCurrency_type(String currency_type) {
        DriverDetails.currency_type = currency_type;
    }

    public static String getFarePerUnit() {
        return farePerUnit;
    }

    public static void setFarePerUnit(String farePerUnit) {
        DriverDetails.farePerUnit = farePerUnit;
    }

    private static String farePerUnit = "";

    public static String getNoCabFound() {
        return NoCabFound;
    }

    public static void setNoCabFound(String noCabFound) {
        NoCabFound = noCabFound;
    }

    private static String NoCabFound = "";

    public static String getProfilePic() {
        return profilePic;
    }

    public static void setProfilePic(String profilePic) {
        DriverDetails.profilePic = profilePic;
    }

    private static String profilePic = "";

    public static String getFare() {
        return Fare;
    }

    public static void setFare(String fare) {
        Fare = fare;
    }

    private static String Fare = "0";

    public static String getTravelTime() {
        return TravelTime;
    }

    public static void setTravelTime(String travelTime) {
        TravelTime = travelTime;
    }

    private static String TravelTime = "0";

    public static String getDriverLat() {
        return DriverLat;
    }

    public static void setDriverLat(String driverLat) {
        DriverLat = driverLat;
    }

    public static String getDriverLong() {
        return DriverLong;
    }

    public static void setDriverLong(String driverLong) {
        DriverLong = driverLong;
    }

    public static String getDriverCabType() {
        return DriverCabType;
    }

    public static void setDriverCabType(String driverCabType) {
        DriverCabType = driverCabType;
    }

    public static String getDriverStatus() {
        return DriverStatus;
    }

    public static void setDriverStatus(String driverStatus) {
        DriverStatus = driverStatus;
    }

    public static DriverDetails getinstance(Context context) {
        if (driverDetails == null)
            driverDetails = new DriverDetails();
        return driverDetails;
    }

    public static String getCabNumber() {
        return CabNumber;
    }

    public static void setCabNumber(String cabNumber) {
        CabNumber = cabNumber;
    }

    public static String getDriverName() {
        return DriverName;
    }

    public static void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public static String getDriverNumber() {
        return DriverNumber;
    }

    public static void setDriverNumber(String driverNumber) {
        DriverNumber = driverNumber;
    }

    public static String getNearesCabReachingTime() {
        return NearesCabReachingTime;
    }

    public static void setNearesCabReachingTime(String nearesCabReachingTime) {
        NearesCabReachingTime = nearesCabReachingTime;
    }

    public static int getNearestCabDistance() {
        return NearestCabDistance;
    }

    public static void setNearestCabDistance(int nearestCabDistance) {
        NearestCabDistance = nearestCabDistance;
    }

    public static String getDriver_email() {
        return Driver_email;
    }

    public static void setDriver_email(String driver_email) {
        Driver_email = driver_email;
    }
}
