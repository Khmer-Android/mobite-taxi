package com.angkorcab.login;

public class LoginDetails {
    public static String Username;
    public static String Password;
    // public static int loginsuccess=-1;
    public static String usertype = "nouser";
    public static String Address = "Getting your location .....";
    public static String Full_Address = " ";
    public static String conversionjsonstring = "";
    public static String Destination = "";
    public static String S_D_Distance = "0 KM";
    public static String S_D_Time = "0";
    public static String GCM_Reg_id = "";
    public static int User_Cab_Type = 2;
    public static int googleplayflag = 0; // 0 if installed 1 if uninstalled
    public static String DriverName = "NOT FOUND";
    public static String CabNumber = "NOT FOUND";
    public static String DriverNumber = "NOT FOUND";
    public static int CabType = 2;
    public static String Driver_status = "available";
    public static int NearestCabDistance = 0;
    public static String NearesCabReachingTime = "NOT FOUND";
    public static String UserTime = "";
    public static String UserDate = "";
    public static String Coupon = "XX ZZ UU 123";
    public static String UserTimehitformat = "";
    public static String UserDatehitformat = "";
    public static String Driver_email = "";
    public static String PassengerEmail = "";
    public static String Unique_Table_ID = "";
    private static double sourc_lat = 0.0;

    public static double getSourc_lat() {
        return sourc_lat;
    }

    public static void setSourc_lat(double sourc_lat) {
        LoginDetails.sourc_lat = sourc_lat;
    }

    public static double getSourc_longt() {
        return sourc_longt;
    }

    public static void setSourc_longt(double sourc_longt) {
        LoginDetails.sourc_longt = sourc_longt;
    }

    private static double sourc_longt = 0.0;
    public static int number_of_people_reffered = 0;
    public static int refer_count = 0;
    public static String[] REFER_IDS;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}