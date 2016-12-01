package com.similan.framework.dto.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.common.YesNoEnum;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.util.AddressDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Slf4j
public class MemberInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private Integer newCommentRating;

  private Long id;

  private String userName;

  private String firstName;

  private String lastName;

  private String password;

  private String newPassword;

  private String confirmPassword;

  private String email;

  private String newEmail;

  private String confirmEmail;

  private String industry;

  private String company;

  private String title;

  private String businessPhone;

  private String mobilePhone;

  private String search;

  private String description;

  private String aboutMe;

  private String currentPassword;

  private List<MemberInterestInfo> interest;

  private List<MemberInterestInfo> deletedInterestInfo = new ArrayList<MemberInterestInfo>();

  private MemberInterestInfo selectedMemberInterest;

  private List<MemberExpertiseInfo> expertise;

  private List<MemberExpertiseInfo> deletedExpertiseInfo = new ArrayList<MemberExpertiseInfo>();

  private MemberExpertiseInfo selectedMemberExpertise;

  private String multiInviteEmails;

  private String relationshipType;

  private boolean logged = false;

  private boolean embeddedMode = false;

  private PhotoViewOption memberPhotoViewOptionType = PhotoViewOption.HidePhoto;

  private String photoLocation = null;

  private OrganizationBasicInfoDto employer = null;

  private OrganizationBasicInfoDto selectedOrgTag = null;

  private SocialEmployeeType memberOrgAssociationRole = SocialEmployeeType.Admin;

  private String memberFeedback;

  private String imagePath;
  private YesNoEnum publicSearchVisibility;

  private YesNoEnum communitySearchVisibility;

  private String contactCategory = com.similan.service.api.connection.dto.basic.ContactType.Other
      .toString();

  private AddressDto addressDto = new AddressDto();

  private MemberState status;

  /**
   * @param firstName
   * @param lastName
   * @param email
   * @param company
   * @param title
   * @param country
   * @param city
   */
  public MemberInfoDto(String firstName, String lastName, String email,
      String company, String title, String country, String city) {
    super();
    this.reset();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.company = company;
    this.title = title;
    this.addressDto.setCountry(country);
    this.addressDto.setCity(city);
    embeddedMode = false;
  }

  public MemberInfoDto() {
    this.addressDto.setLatitude(Double.MIN_VALUE);
    this.addressDto.setLongitude(Double.MIN_VALUE);
    this.memberPhotoViewOptionType = PhotoViewOption.ShowPhoto;
    this.publicSearchVisibility = YesNoEnum.Yes;
    this.communitySearchVisibility = YesNoEnum.Yes;
  }

  public void reset() {

    embeddedMode = false;
    this.logged = false;
    this.firstName = null;
    this.lastName = null;
    this.password = null;
    this.currentPassword = null;
    this.newPassword = null;
    this.confirmPassword = null;
    this.title = null;
    this.email = null;
    this.mobilePhone = null;
    this.businessPhone = null;
    this.company = null;
    this.password = null;
    this.userName = null;
    this.search = null;
    this.description = null;
    this.multiInviteEmails = null;
    this.publicSearchVisibility = YesNoEnum.Yes;
    this.communitySearchVisibility = YesNoEnum.Yes;

    this.addressDto = new AddressDto();

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AddressDto getAddressDto() {
    return addressDto;
  }

  public void setAddressDto(AddressDto addressDto) {
    this.addressDto = addressDto;
  }

  public void setBusinessLogo(String logo) {

  }

  public String getBusinessLogo() {

    String logoLocation = null;
    if (this.employer != null) {
      logoLocation = this.employer.getLogoLocation();
    }

    return PhotoViewOption.ShowPhoto.effectivePhoto("/images/businessLogo.jpg",
        logoLocation);
  }

  public boolean getEmbeddedMode() {
    return embeddedMode;
  }

  public void setEmbeddedMode(boolean embeddedMode) {
    this.embeddedMode = embeddedMode;
  }

  public String getLoggedInName() {
    String loggedInName = "Member";

    if (!StringUtils.isBlank(this.firstName)) {
      loggedInName = StringUtils.trimToEmpty(this.firstName);
      log.info("Login name " + loggedInName);
    } else if (!StringUtils.isBlank(this.lastName)) {
      loggedInName = StringUtils.trimToEmpty(this.lastName);
    }
    // Use only the first name if there are multiple names.
    if (loggedInName.indexOf(' ') > -1) {
      loggedInName = loggedInName.substring(0, loggedInName.indexOf(' '))
          .trim();
    }
    return loggedInName;
  }

  public YesNoEnum getPublicSearchVisibility() {
    return publicSearchVisibility;
  }

  public void setPublicSearchVisibility(YesNoEnum publicSearchVisibility) {
    this.publicSearchVisibility = publicSearchVisibility;
  }

  public YesNoEnum getCommunitySearchVisibility() {
    return communitySearchVisibility;
  }

  public void setCommunitySearchVisibility(YesNoEnum communitySearchVisibility) {
    this.communitySearchVisibility = communitySearchVisibility;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public void setLoggedInName(String name) {
    /* do nothing */
  }

  public Double getLatidute() {
    return this.addressDto.getLatitude();
  }

  public void setLatidute(Double latidute) {
    this.addressDto.setLatitude(latidute);
  }

  public Double getLongitude() {
    return this.addressDto.getLongitude();
  }

  public void setLongitude(Double longitude) {
    this.addressDto.setLongitude(longitude);
  }

  public OrganizationBasicInfoDto getSelectedOrgTag() {
    return selectedOrgTag;
  }

  public void setSelectedOrgTag(OrganizationBasicInfoDto selectedOrgTag) {
    this.selectedOrgTag = selectedOrgTag;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMemberFeedback() {
    return memberFeedback;
  }

  public void setMemberFeedback(String memberFeedback) {
    this.memberFeedback = memberFeedback;
  }

  public List<MemberInterestInfo> getDeletedInterestInfo() {
    return deletedInterestInfo;
  }

  public void setDeletedInterestInfo(
      List<MemberInterestInfo> deletedInterestInfo) {
    this.deletedInterestInfo = deletedInterestInfo;
  }

  public List<MemberExpertiseInfo> getDeletedExpertiseInfo() {
    return deletedExpertiseInfo;
  }

  public void setDeletedExpertiseInfo(
      List<MemberExpertiseInfo> deletedExpertiseInfo) {
    this.deletedExpertiseInfo = deletedExpertiseInfo;
  }

  public OrganizationBasicInfoDto getEmployer() {
    return employer;
  }

  public void setEmployer(OrganizationBasicInfoDto employer) {
    this.employer = employer;
  }

  public String getPhotoLocation() {

    return this.memberPhotoViewOptionType.effectivePhoto(
        "/images/memberDefaultPhoto.png", this.photoLocation);
  }

  public void setPhotoLocation(String photoLocation) {
    this.photoLocation = photoLocation;
  }

  public PhotoViewOption getMemberPhotoViewOptionType() {
    return memberPhotoViewOptionType;
  }

  public void setMemberPhotoViewOptionType(
      PhotoViewOption memberPhotoViewOptionType) {
    this.memberPhotoViewOptionType = memberPhotoViewOptionType;
  }

  public String getPhotoViewOption() {
    return memberPhotoViewOptionType.toString();
  }

  public void setPhotoViewOption(String photoViewOption) {
    this.memberPhotoViewOptionType = PhotoViewOption.valueOf(photoViewOption);
  }

  public String getNewEmail() {
    return newEmail;
  }

  public void setNewEmail(String newEmail) {
    this.newEmail = newEmail;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public MemberInterestInfo getSelectedMemberInterest() {
    return selectedMemberInterest;
  }

  public void setSelectedMemberInterest(
      MemberInterestInfo selectedMemberInterest) {
    this.selectedMemberInterest = selectedMemberInterest;
  }

  public MemberExpertiseInfo getSelectedMemberExpertise() {
    return selectedMemberExpertise;
  }

  public void setSelectedMemberExpertise(
      MemberExpertiseInfo selectedMemberExpertise) {
    this.selectedMemberExpertise = selectedMemberExpertise;
  }

  public Integer getNewCommentRating() {
    return newCommentRating;
  }

  public void setNewCommentRating(Integer newCommentRating) {
    this.newCommentRating = newCommentRating;
  }

  public List<MemberInterestInfo> getInterest() {

    if (interest == null) {
      interest = new ArrayList<MemberInterestInfo>();
    }

    return interest;
  }

  public void setInterest(List<MemberInterestInfo> interest) {
    this.interest = interest;
  }

  public List<MemberExpertiseInfo> getExpertise() {

    if (expertise == null) {
      expertise = new ArrayList<MemberExpertiseInfo>();
    }

    return expertise;
  }

  public void setExpertise(List<MemberExpertiseInfo> expertise) {
    this.expertise = expertise;
  }

  public String getRelationshipType() {
    return relationshipType;
  }

  public void setRelationshipType(String relationshipType) {
    this.relationshipType = relationshipType;
  }

  public String getZipCode() {
    return this.addressDto.getZipCode();
  }

  public void setZipCode(String zipCode) {
    this.addressDto.setZipCode(zipCode);
  }

  public String getMultiInviteEmails() {
    return multiInviteEmails;
  }

  public void setMultiInviteEmails(String multiInviteEmails) {
    this.multiInviteEmails = multiInviteEmails;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getLogged() {
    return logged;
  }

  public boolean isLogged() {
    return logged;
  }

  public void setLogged(boolean logged) {
    this.logged = logged;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCountry() {
    return this.addressDto.getCountry();
  }

  public void setCountry(String country) {
    this.addressDto.setCountry(country);
  }

  public String getStreet() {
    return this.addressDto.getStreet();
  }

  public void setStreet(String street) {
    this.addressDto.setStreet(street);
  }

  public String getCity() {
    return this.addressDto.getCity();
  }

  public void setCity(String city) {
    this.addressDto.setCity(city);
  }

  public String getState() {
    return addressDto.getState();
  }

  public void setState(String state) {
    this.addressDto.setState(state);
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getBusinessPhone() {
    return businessPhone;
  }

  public void setBusinessPhone(String businessPhone) {
    this.businessPhone = businessPhone;
  }

  public String getConfirmEmail() {
    return confirmEmail;
  }

  public void setConfirmEmail(String confirmEmail) {
    this.confirmEmail = confirmEmail;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCompany() {
    if (this.getEmployer() != null) {
      return this.getEmployer().getName();
    }

    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SocialEmployeeType getMemberOrgAssociationRole() {
    return memberOrgAssociationRole;
  }

  public void setMemberOrgAssociationRole(
      SocialEmployeeType memberOrgAssociationRole) {
    this.memberOrgAssociationRole = memberOrgAssociationRole;
  }

  public String getMemberOrgAssociationRoleType() {
    return memberOrgAssociationRole.toString();
  }

  public void setMemberOrgAssociationRoleType(String memberOrgAssociationRole) {
    this.memberOrgAssociationRole = SocialEmployeeType
        .valueOf(memberOrgAssociationRole);
  }

  public Integer getPriority() {
    return getNewCommentRating();
  }

  public void setPriority(int priority) {
    setNewCommentRating(priority);
  }

  public String getLabel() {
    return getFirstName() + " " + getLastName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.similan.framework.dto.Contact#getImagePath()
   */
  public String getImagePath() {
    return this.getPhotoLocation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.similan.framework.dto.Contact#getFullName()
   */
  public String getFullName() {
    return getLastName() + ", " + getFirstName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.similan.framework.dto.Contact#getAddress()
   */
  public String getAddress() {
    return this.addressDto.toString();
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public void setIsBusiness(boolean value) {

  }

  public void setIsMember(boolean value) {

  }

  public boolean getIsBusiness() {
    return false;
  }

  public boolean getIsMember() {
    return true;
  }

  public void setContactCategory(String contactCat) {
    this.contactCategory = contactCat;
  }

  public String getContactCategory() {
    return this.contactCategory;
  }

  public Long getCompanyId() {
    if (this.employer != null) {
      return this.employer.getId();
    }

    return null;
  }

  public MemberState getStatus() {
    return status;
  }

  public void setStatus(MemberState status) {
    this.status = status;
  }

}
