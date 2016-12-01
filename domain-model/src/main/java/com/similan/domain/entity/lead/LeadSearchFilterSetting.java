package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialPerson;

@Entity(name="LeadSearchFilterSetting")
public class LeadSearchFilterSetting {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
    private String name;
	
	@Column
	private String frequency;
	
	@Column
	private Date activeSince;
	
	@Column
    private String radius;
	
	@Column
	private String industry;
	
	@Column
	private String distanceUnitOption;
	
	@Column
    private String street;
	
	@Column
	private String country;
	
	@Column
	private String state;
	
	@Column
	private String city;
	
	@Column
	private String zipCode;
	
	@Column(length=10000)
	private String configListItems;

	@Column
	private Double longitude;

	@Column
	private Double latitude;

	@ManyToOne
	private SocialPerson owner;
	
	@Column
	private Long lastResultCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Date getActiveSince() {
		return activeSince;
	}

	public void setActiveSince(Date activeSince) {
		this.activeSince = activeSince;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getDistanceUnitOption() {
		return distanceUnitOption;
	}

	public void setDistanceUnitOption(String distanceUnitOption) {
		this.distanceUnitOption = distanceUnitOption;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getConfigListItems() {
		return configListItems;
	}

	public void setConfigListItems(String configListItems) {
		this.configListItems = configListItems;
	}

	public SocialPerson getOwner() {
		return owner;
	}

	public void setOwner(SocialPerson owner) {
		this.owner = owner;
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

	public Long getLastResultCount() {
		return lastResultCount;
	}

	public void setLastResultCount(Long lastResultCount) {
		this.lastResultCount = lastResultCount;
	}
	
}
