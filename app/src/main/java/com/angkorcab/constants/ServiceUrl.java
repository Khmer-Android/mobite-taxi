package com.angkorcab.constants;




public class ServiceUrl {


   final static boolean isOnline = false;

    public static final String IP_ADDRESS  = (isOnline==false)?  "http://192.168.56.1/taxibooking/public/" :  "http://angkorcab.com/public/" ;
    // usersfinal
    public static  String USER_LOGIN = IP_ADDRESS + "android/user/login";

    public static final String USER_UPDATE_AVAILABLE = IP_ADDRESS  + "android/user/update/available";

    public static final String USER_REGISTER = IP_ADDRESS + "android/user/register";

    public static final String USER_MARKER_LISTS = IP_ADDRESS + "android/user/maker/list";

    public static final String USER_GET_PROFILE  = IP_ADDRESS + "android/user/profile/get";
    public static final String USER_UPDATE_PROFILE  = IP_ADDRESS + "android/user/profile/update";

    public static final String TAXI_GET             = IP_ADDRESS + "android/taxi/get";
    public static final String TAXI_UPDATE          = IP_ADDRESS + "android/taxi/update";

   public static final String RIDE_GET              = IP_ADDRESS + "android/ride/get";
   public static final String RIDE_UPDATE           = IP_ADDRESS + "android/ride/update";


    // Passenger

    public static final String PASSENGER_UPDATE_DRIVER_MARKER = IP_ADDRESS + "android/passenger/update_marker";

    public static final String PASSENGER_CALCULAT_DRIVER_DISTANT= IP_ADDRESS + "android/passenger/calculator/distant";

    public static final String PASSENGER_REQUEST_BOOKING_DRIVER  = IP_ADDRESS + "android/passenger/request/booking/driver";

    public static final String PASSENGER_CANCEL_BOOKING_DRIVER_REQUEST=IP_ADDRESS+ "android/passenger/cancel/request/booking/driver";

    // DriverStatus
    public  static final String DRIVER_GET_STATUS  =  IP_ADDRESS + "android/driver/status/get";

    // Driver_loc_update_task.java
    public static final String  DRIVER_UPDATE_STORE_LOCATION_WITH_NEARBY = IP_ADDRESS + "android/driver/store/nearest_driver";

    public static final String  DRIVER_CANCEL_BOOKING_PASSENGER=IP_ADDRESS+ "android/driver/cancel/request/booking/passenger";

    public static final String DRIVER_CONFIRM_BOOKING_PASSENGER= IP_ADDRESS + "android/driver/confirm/request/booking/passenger";

    public static final String DRIVER_MISSED_CONFIRM_PASSENGER=  IP_ADDRESS + "android/driver/missed/confirm/request/booking/passenger";

    public static final String  DRIVER_UPDATE_PASSENGER_MARKER = "";

   // GCM

   public static final String UPDATE_APP_ID = IP_ADDRESS + "android/user/createOrUpdate/app/id";


}
