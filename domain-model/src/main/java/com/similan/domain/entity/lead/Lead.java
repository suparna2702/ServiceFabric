package com.similan.domain.entity.lead;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.common.Address;

@Entity(name = "Lead")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LeadDiscriminatorType", 
                     discriminatorType = DiscriminatorType.STRING)
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;

	@Column
	private Long forSocialActorId;

	@Column
	private Long fromSocialActorId;
	
	@Column
	private Date timeStamp;

	@Enumerated(EnumType.STRING)
	@Column
	private LeadType leadType;

	@ManyToOne
	private LeadAssignment leadAssignment;

	@Enumerated(EnumType.STRING)
	@Column
	private LeadStatusType leadStatus;
	
	@Column
	private String industry;

	@Column
	private String sicCode;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean viewed = Boolean.FALSE;
	
	@Column
	private String firstName;
	
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

	@ManyToOne
	private Address location;
	
	@Column(length=10000)
	private String keyword;
	
	@Column
	private String leadPhotoLocation;
	
	@Column
	private String leadSource;
	
	@Column
	private Date dateCreated;

	@Column
	private Long createdById;

	@Column
	private Date dateLastViewed;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLeadPhotoLocation() {
		return leadPhotoLocation;
	}

	public void setLeadPhotoLocation(String leadPhotoLocation) {
		this.leadPhotoLocation = leadPhotoLocation;
	}

	public Boolean getViewed() {
		return viewed;
	}

	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}

	public LeadType getLeadType() {
		return leadType;
	}

	public void setLeadType(LeadType leadType) {
		this.leadType = leadType;
	}

	public Long getForSocialActorId() {
		return forSocialActorId;
	}

	public void setForSocialActorId(Long forSocialActorId) {
		this.forSocialActorId = forSocialActorId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public LeadAssignment getLeadAssignment() {
		return leadAssignment;
	}

	public void setLeadAssignment(LeadAssignment leadAssignment) {
		this.leadAssignment = leadAssignment;
	}

	public LeadStatusType getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(LeadStatusType leadStatus) {
		this.leadStatus = leadStatus;
	}
	
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSicCode() {
		return sicCode;
	}

	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Date getDateLastViewed() {
		return dateLastViewed;
	}

	public void setDateLastViewed(Date dateLastViewed) {
		this.dateLastViewed = dateLastViewed;
	}

	public Long getFromSocialActorId() {
		return fromSocialActorId;
	}

	public void setFromSocialActorId(Long fromSocialActorId) {
		this.fromSocialActorId = fromSocialActorId;
	}
}


