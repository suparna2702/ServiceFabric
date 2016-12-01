package com.similan.domain.entity.lead;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialPerson;

@Entity(name="LeadAutoDistributeRule")
public class LeadAutoDistributeRule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
    private String name;
	
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
	private Integer limitPerDay;
	
	@Column
	private Integer distanceRange;
	
	@ManyToOne
	private SocialPerson owner;

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

	public Integer getLimitPerDay() {
		return limitPerDay;
	}

	public void setLimitPerDay(Integer limitPerDay) {
		this.limitPerDay = limitPerDay;
	}
	
	public Integer getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(Integer distanceRange) {
		this.distanceRange = distanceRange;
	}

	public SocialPerson getOwner() {
		return owner;
	}

	public void setOwner(SocialPerson owner) {
		this.owner = owner;
	}
	
	

}
