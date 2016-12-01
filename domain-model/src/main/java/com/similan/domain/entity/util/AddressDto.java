package com.similan.domain.entity.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class AddressDto implements Serializable {

  private static final long serialVersionUID = 7372181063197395203L;

  private Long id;

  private String addrName;

  private String type;

  private Double latitude;

  private Double longitude;

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

  private String locationEmail;
  
  private String locationPhone;
  
  private String locationFax;
  
  private String locationUrl;
  
  public AddressDto() {
	  id = Long.MIN_VALUE;
	  latitude = Double.MIN_VALUE;
	  longitude = Double.MIN_VALUE;
	  this.country = null;
	  this.state = null;
  }

  public AddressDto(String name, String type, Double latitude,
      Double longitude, String number, String street, String city, String area,
      String county, String state, String zipCode, String country,
      String formattedAddress, String searchableAddress) {
    super();
    this.addrName = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
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

  public Long getId() {
	 return id;
  }

  public void setId(Long id) {
	  this.id = id;
  }
  
  public String getAddrName() {
  return addrName;
  }

  public void setAddrName(String addrName) {
  this.addrName = addrName;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public String getNumber() {
    return number;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getArea() {
    return area;
  }

  public String getCounty() {
    return county;
  }

  public String getState() {
    return state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCountry() {
    return country;
  }

  public String getFormattedAddress() {
    return formattedAddress;
  }

  public String getSearchableAddress() {
    return searchableAddress;
  }
  
  public void setLatitude(Double latitude) {
	this.latitude = latitude;
  }  

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	
	public void setSearchableAddress(String searchableAddress) {
		this.searchableAddress = searchableAddress;
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
