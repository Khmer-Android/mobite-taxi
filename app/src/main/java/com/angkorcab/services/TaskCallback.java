package com.angkorcab.services;

/**
 * Created by phuong on 11/3/2016.
 */

public interface TaskCallback{


    public class Action{
        public static final String SAVE_USER_PROFILE = "save_user_profile";
        public static final String GET_USER_PROFILE  = "get_user_profile";

        public static final String GET_TAXI          = "get_taxi_";
        public static final String UPDATE_TAXI       = "update_taxi";

        public static final String GET_RIDE          = "get_ride";
        public static final String LOGOUT_FINISHED   = "logout_finished";



    }

    void done(String action);
}