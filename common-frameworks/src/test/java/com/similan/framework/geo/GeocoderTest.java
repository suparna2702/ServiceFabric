package com.similan.framework.geo;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.similan.domain.entity.util.AddressDto;

public class GeocoderTest {
  private Geocoder newGoogleGeocoder() {
    GoogleGeocoder geocoder = new GoogleGeocoder();
    geocoder.initialize();
    return geocoder;
  }
  
	@Ignore
	@Test
	public void data() {
		Geocoder geocoder = newGoogleGeocoder();
		AddressDto[] locations = geocoder.geocode(
				"1603 Amphitheatre Pkwy, Mountain View, CA, US", null, null,
				null);
		Assert.assertTrue(1 <= locations.length);
		AddressDto location = locations[0];
		Assert.assertEquals("1603", location.getNumber());
		Assert.assertEquals("Amphitheatre Parkway", location.getStreet());
    Assert.assertEquals("Mountain View", location.getCity());
    Assert.assertEquals("San Jose", location.getArea());
    Assert.assertEquals("Santa Clara", location.getCounty());
		Assert.assertEquals("California", location.getState());
		Assert.assertEquals("94043", location.getZipCode());
		Assert.assertEquals("United States", location.getCountry());
		Assert.assertEquals(
				"1603 Amphitheatre Parkway, Mountain View, CA 94043, USA",
				location.getFormattedAddress());
		Assert.assertEquals(
				"1603 Amphitheatre Parkway Amphitheatre Pkwy Mountain View San Jose Santa Clara California CA United States US 94043",
				location.getSearchableAddress());
	}

	@Ignore
	@Test
	public void multiple() {
		Geocoder geocoder = newGoogleGeocoder();
		AddressDto[] locations = geocoder.geocode("Springfield", null, null,
				null);
		Assert.assertTrue(locations.length >= 2);
	}

	@Test
	public void region() {
		Geocoder geocoder = newGoogleGeocoder();
		AddressDto[] spainLocations = geocoder.geocode("Cordoba", null, "es",
				null);
		Assert.assertEquals(1, spainLocations.length);
		AddressDto spainLocation = spainLocations[0];
		Assert.assertEquals("Spain", spainLocation.getCountry());

		AddressDto[] argentinaLocations = geocoder.geocode("Cordoba", null,
				"ar", null);
		Assert.assertEquals(1, argentinaLocations.length);
		AddressDto argentinaLocation = argentinaLocations[0];
		Assert.assertEquals("Argentina", argentinaLocation.getCountry());
	}

	@Ignore
	@Test
	public void language() {
		Geocoder geocoder = newGoogleGeocoder();
		AddressDto[] locations = geocoder.geocode("Moscow", null, "ru", "fr");
		Assert.assertEquals(1, locations.length);
		AddressDto location = locations[0];
		Assert.assertEquals("Moscou", location.getCity());
	}

	@Ignore
	@Test
	public void bounds() {
		Geocoder geocoder = newGoogleGeocoder();
		AddressDto[] usLocations = geocoder.geocode("Monterey", new GeoBounds(
				33D, -125D, 39D, -120D), null, null);
		Assert.assertEquals(1, usLocations.length);
		AddressDto usLocation = usLocations[0];
		Assert.assertEquals("United States", usLocation.getCountry());

		AddressDto[] mexicoLocations = geocoder.geocode("Monterrey",
				new GeoBounds(10D, -180D, 30D, 180D), null, null);
		Assert.assertEquals(1, mexicoLocations.length);
		AddressDto mexicoLocation = mexicoLocations[0];
		Assert.assertEquals("Mexico", mexicoLocation.getCountry());
	}
}
