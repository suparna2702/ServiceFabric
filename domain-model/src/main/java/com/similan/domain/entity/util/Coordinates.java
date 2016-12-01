package com.similan.domain.entity.util;

public class Coordinates {

	private final double latitude;

	private final double longitude;

	public Coordinates(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
