package com.similan.framework.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.entity.community.OrganizationStatus;
import com.similan.domain.entity.community.SocialMemberVisibilityType;

public class OrganizationBasicInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String IMAGES_ORG_DEFAULT_PHOTO = "/images/businessLogo.jpg";
  private long id;

  private String name;

  private String logoLocation = "/images/businessLogo.jpg";

  private SocialMemberVisibilityType memberVisibilityType;

  private String industry;

  private String description;

  private String email;

  private String phone;

  private Date creationDate;

  private OrganizationStatus status;

  private String accountType = "BUSINESS_STANDARD";

  public OrganizationBasicInfoDto() {
    logoLocation = "/images/businessLogo.jpg";
    id = Long.MIN_VALUE;
    memberVisibilityType = SocialMemberVisibilityType.VisiblePublic;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SocialMemberVisibilityType getMemberVisibilityType() {
    return memberVisibilityType;
  }

  public void setMemberVisibilityType(
      SocialMemberVisibilityType memberVisibilityType) {
    this.memberVisibilityType = memberVisibilityType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogoLocation() {
    if (this.logoLocation != null && StringUtils.isNotBlank(logoLocation)) {
      return logoLocation;
    }

    return IMAGES_ORG_DEFAULT_PHOTO;
  }

  public void setLogoLocation(String logoLocation) {
    this.logoLocation = logoLocation;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public OrganizationStatus getStatus() {
    return status;
  }

  public void setStatus(OrganizationStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "OrganizationBasicInfoDto [id=" + id + ", name=" + name
        + ", logoLocation=" + logoLocation + ", memberVisibilityType="
        + memberVisibilityType + ", industry=" + industry + ", description="
        + description + ", email=" + email + ", phone=" + phone
        + ", creationDate=" + creationDate + ", status=" + status
        + ", accountType=" + accountType + "]";
  }
  
  

}
