package com.angkorcab.model;

import java.util.ArrayList;

public class Ride {
	public String name;
	public String hometown;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String date;

	private String pickup_location;
	private String dropoff_location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getPickup_location() {
		return pickup_location;
	}

	public void setPickup_location(String pickup_location) {
		this.pickup_location = pickup_location;
	}

	public String getDropoff_location() {
		return dropoff_location;
	}

	public void setDropoff_location(String dropoff_location) {
		this.dropoff_location = dropoff_location;
	}

	public Ride(String pickup_location, String dropoff_location,String date) {
		this.pickup_location = pickup_location;
		this.dropoff_location = dropoff_location;
		this.date = date;
	}

	public static ArrayList<Ride> getUsers() {
		ArrayList<Ride> users = new ArrayList<Ride>();

		return users;
	}



}
