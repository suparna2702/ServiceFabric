package com.similan.framework.dto;

import java.io.Serializable;

public class OrganizationAddressDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer locationIndex = Integer.MIN_VALUE;
	
	private String name;
	
	private long id = Long.MIN_VALUE;
	
	private String country;
	
	private String city;
	
	private String county;
	
	private String streetNumber;
	
	private String street;
	
	private String zipCode;
	
	private String contactNumber;
	
	private String state;
	
	private double latitude;
	
	private double longitude;
	
	private String locationType = "Headquarter";
	
	private boolean changed;
	
	  private String locationEmail;
	  
	  private String locationPhone;
	  
	  private String locationFax;
	  
	  private String locationUrl;
	  
	  private boolean copyContactFromPrimary;
	
	public OrganizationAddressDto(){
		
		this.id = Long.MIN_VALUE;
		this.latitude = Double.MIN_VALUE;
		this.longitude = Double.MIN_VALUE;
		this.locationIndex = Integer.MIN_VALUE;
		this.state = "Select State";
		this.country = "Select Country";
		this.changed = false;
		this.name = "";
		this.zipCode = "";
		this.streetNumber = "";
		this.street = "";
		this.locationEmail="";
		this.locationFax="";
		this.locationPhone="";
		this.locationUrl="";
		this.copyContactFromPrimary=true;
	}
	
	
  public OrganizationAddressDto(long id, String country, String city,
      String county, String streetNumber, String street, String zipCode,
      String state, double latitude, double longitude) {
    super();
    this.locationIndex = Integer.MIN_VALUE;
    this.name = "";
    this.id = id;
    this.country = country;
    this.city = city;
    this.county = county;
    this.streetNumber = streetNumber;
    this.street = street;
    this.zipCode = zipCode;
    this.state = state;
    this.latitude = latitude;
    this.longitude = longitude;
    this.changed = false;
	this.copyContactFromPrimary=true;
 }

  public void updateAddress(OrganizationAddressDto addr){
		
		this.state = addr.getState();
		this.country = addr.getCountry();
		this.changed = true;
		this.name = addr.getName();
		this.zipCode = addr.getZipCode();
		this.streetNumber = addr.getStreetNumber();
		this.street = addr.getStreet();
		this.locationEmail=addr.getLocationEmail();
		this.locationFax=addr.getLocationFax();
		this.locationPhone=addr.getLocationPhone();
		this.locationUrl=addr.getLocationUrl();
	}
  
    public String getLocationType() {
	    return locationType;
    }

    public void setLocationType(String locationType) {
	    this.locationType = locationType;
    }
	
	public boolean getChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public Integer getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation(){
		return this.toString();
	}
	
	public void setLocation(String location){
		/* do nothing */
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String toString(){
		StringBuilder orgAddressBuilder = new StringBuilder();
		
		if(this.street != null){
			orgAddressBuilder.append(this.street).append(", ");
		}
		
		if(this.city != null){
			orgAddressBuilder.append(this.city).append(", ");
		}
		
		if(this.state != null){
			orgAddressBuilder.append(this.state).append(", ");
		}
		
		if(this.country != null){
			orgAddressBuilder.append(this.country).append(", ");
		}
		
		if(this.zipCode != null){
			orgAddressBuilder.append(this.zipCode).append(" ");
		}
		
		return orgAddressBuilder.toString();
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


	public boolean isCopyContactFromPrimary() {
		return copyContactFromPrimary;
	}


	public void setCopyContactFromPrimary(boolean copyContactFromPrimary) {
		this.copyContactFromPrimary = copyContactFromPrimary;
	}

}
