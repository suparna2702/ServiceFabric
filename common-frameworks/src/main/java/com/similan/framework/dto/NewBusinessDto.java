package com.similan.framework.dto;

import java.io.Serializable;

import com.similan.service.api.community.dto.basic.SocialEmployeeType;

public class NewBusinessDto implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String businessName;
  
  private SocialEmployeeType memberOrgAssociationRoleType = SocialEmployeeType.Unspecified;
  
  private String businessPrimaryEmail;
  
  private String businessPhone;
  
  private String street;
  
  private String city;
  
  private String country;
  
  private String state;
  
  private String zipCode;

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public SocialEmployeeType getMemberOrgAssociationRoleType() {
    return memberOrgAssociationRoleType;
  }

  public void setMemberOrgAssociationRoleType(
      SocialEmployeeType memberOrgAssociationRoleType) {
    this.memberOrgAssociationRoleType = memberOrgAssociationRoleType;
  }

  public String getBusinessPrimaryEmail() {
    return businessPrimaryEmail;
  }

  public void setBusinessPrimaryEmail(String businessPrimaryEmail) {
    this.businessPrimaryEmail = businessPrimaryEmail;
  }

  public String getBusinessPhone() {
    return businessPhone;
  }

  public void setBusinessPhone(String businessPhone) {
    this.businessPhone = businessPhone;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public String toString() {
    return "NewBusinessDto [businessName=" + businessName
        + ", memberOrgAssociationRoleType=" + memberOrgAssociationRoleType
        + ", businessPrimaryEmail=" + businessPrimaryEmail + ", businessPhone="
        + businessPhone + ", street=" + street + ", city=" + city
        + ", country=" + country + ", state=" + state + ", zipCode=" + zipCode
        + "]";
  }
  
  

}
