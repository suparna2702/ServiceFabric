package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.util.AddressDto;

public class AffiliateLeadDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private String firstName;
	
	private String name;
	 
	private String contactPhone;
	
	private String contactEmail;
	
	private String company;
	
    private Long affiliateId;
	
    private String keyword;
    
    private Date timeStamp;
    
    private Boolean locationVerified;
    
    private Boolean phoneVerified;
    
    private Boolean nameVerified;
    
    private Boolean intentVerified;
    
    private String industry;
    
    private String title;
    
    private String description;
    
    private float purchasePrice = 1.00f;
    
    private AddressDto location;
    
    public AffiliateLeadDto(){
    	this.id = Long.MIN_VALUE;
    	this.location = new AddressDto();
    }
    

	public AddressDto getLocation() {
		return location;
	}

	public void setLocation(AddressDto location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getLocationVerified() {
		return locationVerified;
	}

	public void setLocationVerified(Boolean locationVerified) {
		this.locationVerified = locationVerified;
	}

	public Boolean getPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(Boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public Boolean getNameVerified() {
		return nameVerified;
	}

	public void setNameVerified(Boolean nameVerified) {
		this.nameVerified = nameVerified;
	}

	public Boolean getIntentVerified() {
		return intentVerified;
	}

	public void setIntentVerified(Boolean intentVerified) {
		this.intentVerified = intentVerified;
	}

	@Override
	public String toString() {
		return "AffiliateLeadDto [id=" + id + ", firstName=" + firstName
				+ ", name=" + name + ", contactPhone=" + contactPhone
				+ ", contactEmail=" + contactEmail + ", company=" + company
				+ ", affiliateId=" + affiliateId + ", keyword=" + keyword
				+ ", timeStamp=" + timeStamp + ", locationVerified="
				+ locationVerified + ", phoneVerified=" + phoneVerified
				+ ", nameVerified=" + nameVerified + ", intentVerified="
				+ intentVerified + ", industry=" + industry
				+ ", purchasePrice=" + purchasePrice + "]";
	}
	
	
	
	
}
