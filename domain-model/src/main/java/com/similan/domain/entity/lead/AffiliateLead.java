package com.similan.domain.entity.lead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.SocialActor;

@Entity(name="AffiliateLead")
public class AffiliateLead {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "Affiliated_Lead_ID")
	private Long id;
	
	@ManyToOne
	private SocialActor socialActor;
	
	@Column
	private String firstName;
	
	@Column
	private String title;
	
	@Column(length=3000)
	private String description;
	
	@Column
	private String lastName;
	
	@Column
	private String name;
	
	@Column
	private String contactPhone;
	
	@Column
	private String contactEmail;
	
	@Column
	private String company;
	
	@Column
	private String linkedinPublicProfileUrl;
	
	@Column
	private String linkedInProfileId;
	
	@Column
	private String leadPhoto;
	
	@Column
	private Long affiliateId;
	
	@ManyToOne
	private Address location;
	
	@Column(length=10000)
	private String keyword;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean locationVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean phoneVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean nameVerified;
	
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean intentVerified;
	
	@Column
	private Date infoLastVerifiedByBR;
	
	@Column
	private Date timeStamp;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AcquiredLead> acquiredLeads;
	
	@Column
	private String industry;
	
	@Column
	private Float defaultPrice;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SocialActor getSocialActor() {
		return socialActor;
	}

	public void setSocialActor(SocialActor socialActor) {
		this.socialActor = socialActor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLinkedinPublicProfileUrl() {
		return linkedinPublicProfileUrl;
	}

	public void setLinkedinPublicProfileUrl(String linkedinPublicProfileUrl) {
		this.linkedinPublicProfileUrl = linkedinPublicProfileUrl;
	}

	public String getLinkedInProfileId() {
		return linkedInProfileId;
	}

	public void setLinkedInProfileId(String linkedInProfileId) {
		this.linkedInProfileId = linkedInProfileId;
	}

	public String getLeadPhoto() {
		return leadPhoto;
	}

	public void setLeadPhoto(String leadPhoto) {
		this.leadPhoto = leadPhoto;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public Float getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Float defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public Date getInfoLastVerifiedByBR() {
		return infoLastVerifiedByBR;
	}

	public void setInfoLastVerifiedByBR(Date infoLastVerifiedByBR) {
		this.infoLastVerifiedByBR = infoLastVerifiedByBR;
	}

	public List<AcquiredLead> getAcquiredLeads() {
		if(acquiredLeads == null){
			acquiredLeads = new ArrayList<AcquiredLead>();
		}
		
		return acquiredLeads;
	}

	public void setAcquiredLeads(List<AcquiredLead> acquiredLeads) {
		this.acquiredLeads = acquiredLeads;
	}

	public Long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}
	
	

}
