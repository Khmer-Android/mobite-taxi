package com.angkorcab.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class UserData {

	public static class  Transportation{

		private static Transportation  instance;

		public static synchronized Transportation getInstance(){
			if(instance==null){
				instance = new Transportation();
			}
			return instance;
		}

		private String name;
		private String number;
		private String fare_per_hour;
		private String 	fare_per_km;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getFare_per_hour() {
			return fare_per_hour;
		}

		public void setFare_per_hour(String fare_per_hour) {
			this.fare_per_hour = fare_per_hour;
		}

		public String getFare_per_km() {
			return fare_per_km;
		}

		public void setFare_per_km(String fare_per_km) {
			this.fare_per_km = fare_per_km;
		}
	}


	private Transportation transportation;

	public Transportation getTransportation() {
		return transportation;
	}

	public void setTransportation(Transportation transportation) {
		this.transportation = transportation;
	}

	private String available;
	private String email = "";
	private String password;
	private String userLatitude;
	private String userLongitude;
	private String transportationName;
	private String addressSource;
	private String addressDestination;

	private String driverEmail;
	private String userEmail;
	private String fare_per_unit;
	private String reachTime;

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getReachTime() {
		return reachTime;
	}

	public void setReachTime(String reachTime) {
		this.reachTime = reachTime;
	}

	private String travelTime;








	public String getFare_per_unit() {
		return fare_per_unit;
	}

	public void setFare_per_unit(String fare_per_unit) {
		this.fare_per_unit = fare_per_unit;
	}

	private LatLng source,destination,currentLocation;


	public String getTransportationName() {
		return transportationName;
	}

	public void setTransportationName(String transportationName) {
		this.transportationName = transportationName;
	}

	public String getAddressSource() {
		return addressSource==null?"":addressSource;
	}

	public void setAddressSource(String addressSource) {
		this.addressSource = addressSource;
	}

	public String getAddressDestination() {
		return addressDestination;
	}

	public void setAddressDestination(String addressDestination) {
		this.addressDestination = addressDestination;
	}

	public LatLng getSource() {
		return source;
	}

	public void setSource(LatLng source) {
		this.source = source;
	}

	public LatLng getDestination() {
		return destination;
	}

	public void setDestination(LatLng destination) {
		this.destination = destination;
	}


	public LatLng getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(LatLng currentLocation) {
		this.currentLocation = currentLocation;
	}

	public void setAvailable(String available){
		this.available = available;
	}
	public String getAvailable(){
		return this.available;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserLatitude() {
		return userLatitude;
	}

	public void setUserLatitude(String userLatitude) {
		this.userLatitude = userLatitude;
	}

	public String getUserLongitude() {
		return userLongitude;
	}

	public void setUserLongitude(String userLongitude) {
		this.userLongitude = userLongitude;
	}

	public static UserData getUserData() {
		return userData;
	}

	public static void setUserData(UserData userData) {
		UserData.userData = userData;
	}

	public String getDriverCabNo() {
		return driverCabNo;
	}

	public void setDriverCabNo(String driverCabNo) {
		this.driverCabNo = driverCabNo;
	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	private String distance = "";
	private String time = "";
	private static UserData userData;
	private String office_address = "";
	private double office_lat, office_long;
	private String name = "";

	private String rate_type = "hour";
	private String retunDate = "";
	private String returnTime = "";
	private String rideType = "";
	private String futureRideDate = "";
	private String futureRideTime = "";
	private String rideDateIns = "";
	private String driverId = "";
	private float driverRate=0.0f;
	private String rideId = "";
	private String rideTime = "";
	private String driverLat = "";
	private String driverLong = "";
	private String driverCabNo = "";
	private String rideTimeIns = "";
	private String estimatedFare = "";
	private double dest_lat = 0.0;
	private String desti_address = "";
	private double dest_longt = 0.0;
	private double sourc_lat = 0.0;
	private boolean retunFlag = false;
	private boolean confirmFlag = false;
	private boolean laterRideFlag = false;
	private boolean hourlyInstant = false;
	private boolean hourlyFuture = false;
	private String return_driver_id = "";
	private double sourc_longt = 0.0;
	private String source_address = "";
	private String card_number = "";
	private String card_name = "";
	private String water = "";
	private String airCondition = "";
	private String music = "";
	private String luggage = "";
	private String cardId = "";
	private String voucherId = "";

	private String Unique_Table_ID="";



	private String passengername = "";

	private String passengeremail = "";


	private String passengerimage = "";


	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	private String distance_up_to_source = "";
	private String time_up_to_source = "";
	private String multiCharger = "";
	private HashMap<String, LatLng> favoritePlaces = new HashMap<String, LatLng>();
	private HashMap<String, ArrayList<LatLng>> cabLocation = new HashMap<String, ArrayList<LatLng>>();


	public String getUnique_Table_ID() {
		return Unique_Table_ID;
	}

	public void setUnique_Table_ID(String unique_Table_ID) {
		Unique_Table_ID = unique_Table_ID;
	}

	public String getUserTimehitformat() {
		return UserTimehitformat;
	}

	public void setUserTimehitformat(String userTimehitformat) {
		UserTimehitformat = userTimehitformat;
	}

	public String getUserDatehitformat() {
		return UserDatehitformat;
	}

	public void setUserDatehitformat(String userDatehitformat) {
		UserDatehitformat = userDatehitformat;
	}

	private String UserTimehitformat = "";
    private String UserDatehitformat = "";

	public static UserData getinstance(Context context) {
		if (userData == null)
			userData = new UserData();
		return userData;
	}


	/**
	 * @return the favoritePlaces
	 */
	public HashMap<String, LatLng> getFavoritePlaces() {
		return favoritePlaces;
	}

	/**
	 * @
	 *            the favoritePlaces to set
	 */
	public void setFavoritePlaces(HashMap<String, LatLng> favoritePlace) {
		favoritePlaces.putAll(favoritePlace);
	}

	public void removeFavoritePlaces(String favoritePlace) {
		favoritePlaces.remove(favoritePlace);
	}

	/**
	 * @return the favoritePlaces
	 */

	private String cab_type = "" + 2;

	/**
	 * @return the cab_type
	 */
	public String getCab_type() {
		return cab_type;
	}

	/**
	 * @param cab_type
	 *            the cab_type to set
	 */
	public void setCab_type(String cab_type) {
		this.cab_type = cab_type;
	}



	public String getPassengername() {
		return passengername;
	}

	public void setPassengername(String passengername) {
		this.passengername = passengername;
	}

	public String getPassengeremail() {
		return passengeremail;
	}

	public void setPassengeremail(String passengeremail) {
		this.passengeremail = passengeremail;
	}

	public String getPassengerimage() {
		return passengerimage;
	}

	public void setPassengerimage(String passengerimage) {
		this.passengerimage = passengerimage;
	}

	/**
	 * @return the source_address
	 */
	public String getSource_address() {
		return source_address;
	}
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @param source_address
	 *            the source_address to set
	 */
	public void setSource_address(String source_address) {
		this.source_address = source_address;
	}

	/**
	 * @return the dest_lat
	 */
	public double getDest_lat() {
		return dest_lat;
	}

	/**
	 * @param dest_lat
	 *            the dest_lat to set
	 */
	public void setDest_lat(double dest_lat) {
		this.dest_lat = dest_lat;
	}

	/**
	 * @return the dest_longt
	 */
	public double getDest_longt() {
		return dest_longt;
	}

	/**
	 * @param dest_longt
	 *            the dest_longt to set
	 */
	public void setDest_longt(double dest_longt) {
		this.dest_longt = dest_longt;
	}

	/**
	 * @return the sourc_longt
	 */
	public double getSourc_longt() {
		return sourc_longt;
	}

	/**
	 * @param sourc_longt
	 *            the sourc_longt to set
	 */
	public void setSourc_longt(double sourc_longt) {
		this.sourc_longt = sourc_longt;
	}

	/**
	 * @return the sourc_lat
	 */
	public double getSourc_lat() {
		return sourc_lat;
	}

	/**
	 * @param sourc_lat
	 *            the sourc_lat to set
	 */
	public void setSourc_lat(double sourc_lat) {
		this.sourc_lat = sourc_lat;
	}

	private UserData() {
	};

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the e_mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the e_mail to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return dest_lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.dest_lat = lat;
	}

	/**
	 * @return the longt
	 */
	public double getLongt() {
		return dest_longt;
	}

	/**
	 * @param longt
	 *            the longt to set
	 */
	public void setLongt(double longt) {
		this.dest_lat = longt;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return source_address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.source_address = address;
	}

	/**
	 * @return the desti_address
	 */
	public String getDesti_address() {
		return desti_address;
	}

	/**
	 * @param desti_address
	 *            the desti_address to set
	 */
	public void setDesti_address(String desti_address) {
		this.desti_address = desti_address;
	}

	/**
	 * @return the office_address
	 */
	public String getOffice_address() {
		return office_address;
	}

	/**
	 * @param office_address
	 *            the office_address to set
	 */
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}

	/**
	 * @return the office_lat
	 */
	public double getOffice_lat() {
		return office_lat;
	}

	/**
	 * @param office_lat
	 *            the office_lat to set
	 */
	public void setOffice_lat(double office_lat) {
		this.office_lat = office_lat;
	}

	/**
	 * @return the office_long
	 */
	public double getOffice_long() {
		return office_long;
	}

	/**
	 * @param office_long
	 *            the office_long to set
	 */
	public void setOffice_long(double office_long) {
		this.office_long = office_long;
	}

	/**
	 * @return the cabLocation
	 */
	public HashMap<String, ArrayList<LatLng>> getCabLocation() {
		return cabLocation;
	}

	/**
	 * @param cabLocation
	 *            the cabLocation to set
	 */
	public void setCabLocation(HashMap<String, ArrayList<LatLng>> cabLocation) {
		this.cabLocation = cabLocation;
	}



	/**
	 * @return the retunDate
	 */
	public String getRetunDate() {
		return retunDate;
	}

	/**
	 * @param retunDate
	 *            the retunDate to set
	 */
	public void setRetunDate(String retunDate) {
		this.retunDate = retunDate;
	}

	/**
	 * @return the returnTime
	 */
	public String getReturnTime() {
		return returnTime;
	}

	/**
	 * @param returnTime
	 *            the returnTime to set
	 */
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	/**
	 * @return the futureRideDate
	 */
	public String getFutureRideDate() {
		return futureRideDate;
	}

	/**
	 * @param futureRideDate
	 *            the futureRideDate to set
	 */
	public void setFutureRideDate(String futureRideDate) {
		this.futureRideDate = futureRideDate;
	}

	/**
	 * @return the futureRideTime
	 */
	public String getFutureRideTime() {
		return futureRideTime;
	}

	/**
	 * @param futureRideTime
	 *            the futureRideTime to set
	 */
	public void setFutureRideTime(String futureRideTime) {
		this.futureRideTime = futureRideTime;
	}

	/**
	 * @return the retunFlag
	 */
	public boolean isRetunFlag() {
		return retunFlag;
	}

	/**
	 * @param retunFlag
	 *            the retunFlag to set
	 */
	public void setRetunFlag(boolean retunFlag) {
		this.retunFlag = retunFlag;
	}

	/**
	 * @return the laterRideFlag
	 */
	public boolean isLaterRideFlag() {
		return laterRideFlag;
	}

	/**
	 * @param laterRideFlag
	 *            the laterRideFlag to set
	 */
	public void setLaterRideFlag(boolean laterRideFlag) {
		this.laterRideFlag = laterRideFlag;
	}

	/**
	 * @return the return_driver_id
	 */
	public String getReturn_driver_id() {
		return return_driver_id;
	}

	/**
	 * @param return_driver_id
	 *            the return_driver_id to set
	 */
	public void setReturn_driver_id(String return_driver_id) {
		this.return_driver_id = return_driver_id;
	}

	/**
	 * @return the rate_type
	 */
	public String getRate_type() {
		return rate_type;
	}

	/**
	 * @param rate_type
	 *            the rate_type to set
	 */
	public void setRate_type(String rate_type) {
		this.rate_type = rate_type;
	}

	/**
	 * @return the rideType
	 */
	public String getRideType() {
		return rideType;
	}

	/**
	 * @param rideType
	 *            the rideType to set
	 */
	public void setRideType(String rideType) {
		this.rideType = rideType;
	}

	/**
	 * @return the rideDateIns
	 */
	public String getRideDateIns() {
		return rideDateIns;
	}

	/**
	 * @param rideDateIns
	 *            the rideDateIns to set
	 */
	public void setRideDateIns(String rideDateIns) {
		this.rideDateIns = rideDateIns;
	}

	/**
	 * @return the rideTimeIns
	 */
	public String getRideTimeIns() {
		return rideTimeIns;
	}

	/**
	 * @param rideTimeIns
	 *            the rideTimeIns to set
	 */
	public void setRideTimeIns(String rideTimeIns) {
		this.rideTimeIns = rideTimeIns;
	}

	/**
	 * @return the driverId
	 */
	public String getDriverId() {
		return driverId;
	}

	/**
	 * @param driverId
	 *            the driverId to set
	 */
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	/**
	 * @return the rideId
	 */
	public String getRideId() {
		return rideId;
	}

	/**
	 * @param rideId
	 *            the rideId to set
	 */
	public void setRideId(String rideId) {
		this.rideId = rideId;
	}

	/**
	 * @return the driverLat
	 */
	public String getDriverLat() {
		return driverLat;
	}

	/**
	 * @param driverLat
	 *            the driverLat to set
	 */
	public void setDriverLat(String driverLat) {
		this.driverLat = driverLat;
	}

	/**
	 * @return the driverLong
	 */
	public String getDriverLong() {
		return driverLong;
	}

	/**
	 * @param driverLong
	 *            the driverLong to set
	 */
	public void setDriverLong(String driverLong) {
		this.driverLong = driverLong;
	}


	/**
	 * @return the water
	 */
	public String getWater() {
		return water;
	}

	/**
	 * @param water
	 *            the water to set
	 */
	public void setWater(String water) {
		this.water = water;
	}

	/**
	 * @return the airCondition
	 */
	public String getAirCondition() {
		return airCondition;
	}

	/**
	 * @param airCondition
	 *            the airCondition to set
	 */
	public void setAirCondition(String airCondition) {
		this.airCondition = airCondition;
	}

	/**
	 * @return the music
	 */
	public String getMusic() {
		return music;
	}

	/**
	 * @param music
	 *            the music to set
	 */
	public void setMusic(String music) {
		this.music = music;
	}

	/**
	 * @return the multiCharger
	 */
	public String getMultiCharger() {
		return multiCharger;
	}

	/**
	 * @param multiCharger
	 *            the multiCharger to set
	 */
	public void setMultiCharger(String multiCharger) {
		this.multiCharger = multiCharger;
	}

	/**
	 * @return the rideTime
	 */
	public String getRideTime() {
		return rideTime;
	}

	/**
	 * @param rideTime
	 *            the rideTime to set
	 */
	public void setRideTime(String rideTime) {
		this.rideTime = rideTime;
	}

	/**
	 * @return the luggage
	 */
	public String getLuggage() {
		return luggage;
	}

	/**
	 * @param luggage
	 *            the luggage to set
	 */
	public void setLuggage(String luggage) {
		this.luggage = luggage;
	}

	/**
	 * @return the distance_up_to_source
	 */
	public String getDistance_up_to_source() {
		return distance_up_to_source;
	}

	/**
	 * @param distance_up_to_source
	 *            the distance_up_to_source to set
	 */
	public void setDistance_up_to_source(String distance_up_to_source) {
		this.distance_up_to_source = distance_up_to_source;
	}

	/**
	 * @return the time_up_to_source
	 */
	public String getTime_up_to_source() {
		return time_up_to_source;
	}

	/**
	 * @param time_up_to_source
	 *            the time_up_to_source to set
	 */
	public void setTime_up_to_source(String time_up_to_source) {
		this.time_up_to_source = time_up_to_source;
	}

	/**
	 * @return the estimatedFare
	 */
	public String getEstimatedFare() {
		return estimatedFare;
	}

	/**
	 * @param estimatedFare
	 *            the estimatedFare to set
	 */
	public void setEstimatedFare(String estimatedFare) {
		this.estimatedFare = estimatedFare;
	}

	/**
	 * @return the card_number
	 */
	public String getCard_number() {
		return card_number;
	}

	/**
	 * @param card_number
	 *            the card_number to set
	 */
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	/**
	 * @return the card_name
	 */
	public String getCard_name() {
		return card_name;
	}

	/**
	 * @param card_name
	 *            the card_name to set
	 */
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	/**
	 * @return the driverRate
	 */
	public float getDriverRate() {
		return driverRate;
	}

	/**
	 * @param driverRate the driverRate to set
	 */
	public void setDriverRate(float driverRate) {
		this.driverRate = driverRate;
	}

	/**
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * @return the confirmFlag
	 */
	public boolean isConfirmFlag() {
		return confirmFlag;
	}

	/**
	 * @param confirmFlag the confirmFlag to set
	 */
	public void setConfirmFlag(boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	/**
	 * @return the hourlyInstant
	 */
	public boolean isHourlyInstant() {
		return hourlyInstant;
	}

	/**
	 * @param hourlyInstant the hourlyInstant to set
	 */
	public void setHourlyInstant(boolean hourlyInstant) {
		this.hourlyInstant = hourlyInstant;
	}

	/**
	 * @return the hourlyFuture
	 */
	public boolean isHourlyFuture() {
		return hourlyFuture;
	}

	/**
	 * @param hourlyFuture the hourlyFuture to set
	 */
	public void setHourlyFuture(boolean hourlyFuture) {
		this.hourlyFuture = hourlyFuture;
	}

}
