package com.similan.framework.geo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class GeoBounds {
	double minLatitude;
	double minLongitude;
	double maxLatitude;
	double maxLongitude;

	public GeoBounds(double minLatitude, double minLongitude,
			double maxLatitude, double maxLongitude) {
		this.minLatitude = minLatitude;
		this.minLongitude = minLongitude;
		this.maxLatitude = maxLatitude;
		this.maxLongitude = maxLongitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}