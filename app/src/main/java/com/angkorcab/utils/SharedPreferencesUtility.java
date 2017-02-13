package com.angkorcab.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.angkorcab.constants.DataBaseKey;
import com.angkorcab.constants.Value;
import com.google.android.gms.maps.model.LatLng;

public class SharedPreferencesUtility
{
	 static SharedPreferences mySharedPreferences;
	
	static SharedPreferencesUtility sharedPreferencesUtility;

	public SharedPreferencesUtility() {
	}

	public static SharedPreferencesUtility getInstance() {
		if (sharedPreferencesUtility == null) {
			sharedPreferencesUtility = new SharedPreferencesUtility();
		}
		return sharedPreferencesUtility;
	}


	public static void setDriverEmail(Context context, String email) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.DRIVER_EMAIL, email);
		sharedpreferenceeditor.commit();
	}

	public static String getDriverEmail(Context context) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.DRIVER_EMAIL, "0");
	}


	public static void saveUsername(Context context, String email) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_NAME, email);
		sharedpreferenceeditor.commit();
	}

	public static String loadUsername(Context context) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER_NAME, "0");
	}





	public static void setDriverStatus(Context context, String status) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.DRIVER_STATUS, status);
		sharedpreferenceeditor.commit();
	}
	public static String getDriverStatus(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.DRIVER_STATUS, "0");
	}



	public static void setStartLocationLat(Context context, String lat) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.START_LOCATION_LAT, lat);
		sharedpreferenceeditor.commit();
	}
	public static String getStartLocationLat(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.START_LOCATION_LAT, "0");
	}

	public static void setStartLocationLong(Context context, String longs) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.START_LOCATION_LONG, longs);
		sharedpreferenceeditor.commit();
	}
	public static String getStartLocationLong(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.START_LOCATION_LONG, "0");
	}
	public static void setDestinationLocationLat(Context context, String lat) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.DESTINATION_LOCATION_LAT, lat);
		sharedpreferenceeditor.commit();
	}
	public static String getDestinationLocationLat(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.DESTINATION_LOCATION_LAT, "0");
	}

	public static void setDestinationLocationLong(Context context, String longs) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.DESTINATION_LOCATION_LONG, longs);
		sharedpreferenceeditor.commit();
	}
	public static String getDestinationLocationLong(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.DESTINATION_LOCATION_LONG, "0");
	}

	public static void setUserId(Context context,String user_id) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_ID, user_id);
		sharedpreferenceeditor.commit();
	}

	public static String getUserId(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER_ID, "0");
	}







	/*
	* @@
	* @@ Save user object json to share preference
	*
	* @param Context context
	* @param String user json object
	* */

	public static void setUserObject( Context context ,String user ) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER, user);
		sharedpreferenceeditor.commit();
	}



	public static String getUserObject( Context context ){
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER, "null");
	}




	public static void setRoleObject( Context context, String role) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.ROLE, role);
		sharedpreferenceeditor.commit();
	}


	public static String getRoleObject( Context context ){
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.ROLE, "null");
	}


	public static void  setTransportationObject( Context context , String transportation) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.TRANSPORTATION, transportation);
		sharedpreferenceeditor.commit();
	}

	public static String getTransportationObject( Context context ) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.TRANSPORTATION, "null");
	}


	public static void saveLatitude(Context context , String latitude) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.LATITUDE, latitude);
		sharedpreferenceeditor.commit();
	}

	public static String getLatitude(Context context) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return  mySharedPreferences.getString(DataBaseKey.LATITUDE,"0");
	}



	public static void saveLongitude(Context context , String longitude) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.LONGITUDE, longitude);

		sharedpreferenceeditor.commit();
	}

	public static String getLongitude(Context context) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return  mySharedPreferences.getString(DataBaseKey.LONGITUDE,"0") ;
	}






	public static void savePassengerLocation(Context context,Location location) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.LATITUDE, String.valueOf(location.getLatitude()));
		sharedpreferenceeditor.putString(DataBaseKey.LONGITUDE,String.valueOf(location.getLongitude()));
		sharedpreferenceeditor.commit();
	}

	public static Location getPassengerLocation(Context context){
		Location location;
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		double latitude = mySharedPreferences.getLong(DataBaseKey.LATITUDE,0);
		double longitude = mySharedPreferences.getLong(DataBaseKey.LONGITUDE,0);
		location = new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return location;
	}




	public static void saveMyLocation(Context context, LatLng location) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString("latitude", String.valueOf(location.latitude));
		sharedpreferenceeditor.putString("longitude",String.valueOf(location.longitude));
		sharedpreferenceeditor.commit();
	}

	public static LatLng getMylocation(Context context){
		LatLng location;
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String latitude = mySharedPreferences.getString("latitude","0");
		String longitude = mySharedPreferences.getString("longitude","0");
		location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

		return location;
	}

	public static void saveUserEmail(Context context, String Value) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_EMAIL, Value);
		sharedpreferenceeditor.commit();
	}

	public static String loadUserEmail(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER_EMAIL, "0");
	}



	public static void saveProfilePic(Context context,String Value) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString("profile_pic", Value);
		sharedpreferenceeditor.commit();
	}

	public static String loadProfilePic(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString("profile_pic", "");
	}




	public static void saveRegistrationId(Context context,String Value) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString("registration_id", Value);
		sharedpreferenceeditor.commit();
	}

	public static String loadRegistrationId(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString("registration_id", "0");
	}





	public static void saveUserType(Context context,String Value) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_TYPE, Value);
		sharedpreferenceeditor.commit();
	}

	public static String loadUserType(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER_TYPE, "0");
	}




	public static void saveUserPassword(Context context, String Value)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_PASSWORD, Value);
	    sharedpreferenceeditor.commit();
	}
	
	
	

	public static String loadUserPassword(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.USER_PASSWORD, "0");
		
		
	}
	
	public static void resetSharedPreferences(Context context)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.USER_EMAIL,       Value.nullAble);
		sharedpreferenceeditor.putString(DataBaseKey.USER_PASSWORD,    Value.nullAble);
		sharedpreferenceeditor.putString(DataBaseKey.ROLE,             Value.nullAble);
		sharedpreferenceeditor.putBoolean(DataBaseKey.USER_LOGIN_FLAG, Value.falseAble);
	    sharedpreferenceeditor.commit();
	}


	public static void saveRoleName(Context context,String role)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putString(DataBaseKey.ROLE, role);
		sharedpreferenceeditor.commit();
	}
	public static String loadRoleName(Context context)
	{
		// default taxi : 9
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getString(DataBaseKey.ROLE,null);
	}





	public static void saveCabType(Context context,int Value)
	{
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor sharedpreferenceeditor=mySharedPreferences.edit();
		sharedpreferenceeditor.putInt("cab_type", Value);
	    sharedpreferenceeditor.commit();
	}
	public static int loadCabType(Context context)
	{
		// default taxi : 9
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		if(mySharedPreferences != null) {
			Log.i("mySharedPreferences","Load cab type."+mySharedPreferences.getInt("cab_type", 1));
		}

		return mySharedPreferences.getInt("cab_type", 1);
	}
	public static void setLoginflag(Context context, boolean loginflag) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putBoolean(DataBaseKey.USER_LOGIN_FLAG, loginflag);
		editor.commit();
	}
	public static boolean getLoginflag(Context context) {
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mySharedPreferences.getBoolean(DataBaseKey.USER_LOGIN_FLAG, Value.falseAble);
	}
	
	
}