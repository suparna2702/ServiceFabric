package com.similan.framework.geo;

import com.similan.domain.entity.util.AddressDto;


public interface Geocoder {

  AddressDto[] geocode(String address, GeoBounds bounds, String region,
			String language);

  AddressDto[] geocode(double latitude, double longitude, GeoBounds bounds,
			String region, String language);

}