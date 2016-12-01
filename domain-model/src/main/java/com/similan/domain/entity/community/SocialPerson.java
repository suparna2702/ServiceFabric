package com.similan.domain.entity.community;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

@Entity(name = "SocialPerson")
@DiscriminatorValue("SocialPerson")
public class SocialPerson extends SocialActor {
	
	@Column
	private String firstName;

	@Column
	private String lastName;
	
	@Embedded
	private Account memberAccount;

	@Column
	private String businessPhone;

	@Column
	private String mobilePhone;

	@Column
	private String primaryEmail;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String industry;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String role;

	@Column
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String company;

	@Enumerated(EnumType.STRING)
	@Column(name = "memberState")
	private MemberState state;
	
	@Column(length = 2000)
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<SocialPersonExpertise> expertise;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<SocialPersonInterest> interest;

	@Column
	private Date startDate;

	@Column
	private String photoLocation = null;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PhotoViewOption memberPhotoViewOptionType = PhotoViewOption.HidePhoto;
	
	@Enumerated(EnumType.STRING)
	@Column
	private MemberJoinIntent joinMethod = MemberJoinIntent.JoinBySignup;
	
	@Column(length=3000)
	private String aboutMe;
	
	@JoinColumn
	@ManyToOne
	private SocialEmployee employer;
	
	public SocialEmployee getEmployer() {
		return employer;
	}

	public void setEmployer(SocialEmployee employer) {
		this.employer = employer;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public MemberJoinIntent getJoinMethod() {
		return joinMethod;
	}

	public void setJoinMethod(MemberJoinIntent joinMethod) {
		this.joinMethod = joinMethod;
	}

	public PhotoViewOption getMemberPhotoViewOptionType() {
		return memberPhotoViewOptionType;
	}

	public void setMemberPhotoViewOptionType(
			PhotoViewOption memberPhotoViewOptionType) {
		this.memberPhotoViewOptionType = memberPhotoViewOptionType;
	}

	public String getPhotoLocation() {
		return photoLocation;
	}

	public void setPhotoLocation(String photoLocation) {
		this.photoLocation = photoLocation;
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
	
	public void setInterest(List<SocialPersonInterest> interest){
		this.interest = interest;
	}
	
	public List<SocialPersonInterest> getInterest(){
		return this.interest;
	}
	
	public void setExpertise(List<SocialPersonExpertise> expertise) {
		this.expertise = expertise;
	}
	
	public List<SocialPersonExpertise> getExpertise(){
		return this.expertise;
	}
	
	public Account getMemberAccount() {
		return memberAccount;
	}

	public void setMemberAccount(Account memberAccount) {
		this.memberAccount = memberAccount;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public MemberState getState() {
		return state;
	}

	public void setState(MemberState state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
  public String getDisplayName() {
    if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      return firstName + " " + lastName;
    } else if (StringUtils.isNotBlank(firstName) && StringUtils.isBlank(lastName)) {
      return firstName;
    } else if (StringUtils.isBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      return lastName;
    } else {
      return getName();
    }
  }
  
  @Override
  public String getImage() {
    return getPhotoLocation();
  }
}
