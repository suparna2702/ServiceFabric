package com.similan.domain.entity.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.opengis.geometry.coordinate.Position;

import com.similan.domain.util.GeometryUtils;

@Entity(name = "Address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String addrName;

  private Double longitude;

  private Double latitude;

  private String number;

  private String street;

  private String city;

  private String area;

  private String county;

  private String state;

  private String zipCode;

  private String country;

  private String formattedAddress;

  private String searchableAddress;
  
  private String locationType;
  
  private String locationEmail;
  
  private String locationPhone;
  
  private String locationFax;
  
  private String locationUrl;

  protected Address() {
  }

  public Address(String name, String type, Position position, String number, String street,
      String city, String area, String county, String state, String zipCode,
      String country, String formattedAddress, String searchableAddress) {
    super();
    this.addrName = name;
    this.locationType = type;
    double[] coordinates = GeometryUtils.toCoordinates(position);
    this.longitude = coordinates[0];
    this.latitude = coordinates[1];
    this.number = number;
    this.street = street;
    this.city = city;
    this.area = area;
    this.county = county;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
    this.formattedAddress = formattedAddress;
    this.searchableAddress = searchableAddress;
  }
  
  public String getLocationType() {
	  return locationType;
  }

  public void setLocationType(String locationType) {
	  this.locationType = locationType;
  }

  public String getAddrName() {
	 return addrName;
  }

  public void setAddrName(String addrName) {
	  this.addrName = addrName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Position getPosition() {
    if (longitude == null || latitude == null) {
      return null;
    }
    return GeometryUtils.toPositon(longitude, latitude);
  }

  public void setPosition(Position position) {
    if (position == null) {
      longitude = null;
      latitude = null;
    }
    double[] coordinates = GeometryUtils.toCoordinates(position);
    longitude = coordinates[0];
    latitude = coordinates[1];
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getFormattedAddress() {
    return formattedAddress;
  }

  public void setFormattedAddress(String formattedAddress) {
    this.formattedAddress = formattedAddress;
  }

  public String getSearchableAddress() {
    return searchableAddress;
  }

  public void setSearchableAddress(String searchableAddress) {
    this.searchableAddress = searchableAddress;
  }
  
  public Double getLongitude() {
	  return longitude;
  }

  public void setLongitude(Double longitude) {
	  this.longitude = longitude;
  }

  public Double getLatitude() {
	  return latitude;
  }

  public void setLatitude(Double latitude) {
	this.latitude = latitude;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

public String getLocationEmail() {
	return locationEmail;
}

public void setLocationEmail(String locationEmail) {
	this.locationEmail = locationEmail;
}

public String getLocationPhone() {
	return locationPhone;
}

public void setLocationPhone(String locationPhone) {
	this.locationPhone = locationPhone;
}

public String getLocationFax() {
	return locationFax;
}

public void setLocationFax(String locationFax) {
	this.locationFax = locationFax;
}

public String getLocationUrl() {
	return locationUrl;
}

public void setLocationUrl(String locationUrl) {
	this.locationUrl = locationUrl;
}
}
