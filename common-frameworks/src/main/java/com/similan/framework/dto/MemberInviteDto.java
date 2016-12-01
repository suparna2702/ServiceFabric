package com.similan.framework.dto;

import java.io.Serializable;

import com.similan.domain.entity.community.OrganizationType;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.service.api.connection.dto.basic.ContactType;

public class MemberInviteDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String photoLocation;

  private String firstName;

  private String lastName;

  private String company;

  private String businessName;

  private String businessEmail;

  private String confirmBusinessEmail;

  private String email;

  private String confirmEmail;

  private String memberType;

  private ContactType memberInvite = ContactType.Other;

  private OrganizationType businessType = OrganizationType.UNSPECIFIED;

  private OrganizationBasicInfoDto orgTag = new OrganizationBasicInfoDto();

  private MemberInfoDto invitee;

  private PartnerProgramDefinitionDto partnerProgram;

  private InviteValidationResult validationResult;

  public String getPhotoLocation() {
    return photoLocation;
  }

  public void setPhotoLocation(String photoLocation) {
    this.photoLocation = photoLocation;
  }

  public InviteValidationResult getValidationResult() {
    return validationResult;
  }

  public void setValidationResult(InviteValidationResult validationResult) {
    this.validationResult = validationResult;
  }

  public String getConfirmBusinessEmail() {
    return confirmBusinessEmail;
  }

  public void setConfirmBusinessEmail(String confirmBusinessEmail) {
    this.confirmBusinessEmail = confirmBusinessEmail;
  }

  public OrganizationBasicInfoDto getOrgTag() {
    return orgTag;
  }

  public void setOrgTag(OrganizationBasicInfoDto orgTag) {
    this.orgTag = orgTag;
  }

  public void setBusinessType(OrganizationType businessType) {
    this.businessType = businessType;
  }

  public OrganizationType getBusinessType() {
    return businessType;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public String getBusinessEmail() {
    return businessEmail;
  }

  public void setBusinessEmail(String businessEmail) {
    this.businessEmail = businessEmail;
  }

  public ContactType getMemberInvite() {
    return memberInvite;
  }

  public void setMemberInvite(ContactType memberInvite) {
    this.memberInvite = memberInvite;
  }

  public String getMemberInviteType() {
    return memberInvite.toString();
  }

  public void setMemberInviteType(String memberInviteType) {
    this.memberInvite = ContactType.valueOf(memberInviteType);
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

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getConfirmEmail() {
    return confirmEmail;
  }

  public void setConfirmEmail(String confirmEmail) {
    this.confirmEmail = confirmEmail;
  }

  public String getMemberType() {
    return memberType;
  }

  public void setMemberType(String memberType) {
    this.memberType = memberType;
  }

  public MemberInfoDto getInvitee() {
    return invitee;
  }

  public MemberInviteDto setInvitee(MemberInfoDto invitee) {
    this.invitee = invitee;
    return this;
  }

  @Override
  public String toString() {
    return "MemberInvite [firstName=" + firstName + ", lastName=" + lastName
        + ", company=" + company + ", businessName=" + businessName
        + ", businessEmail=" + businessEmail + ", confirmBusinessEmail="
        + confirmBusinessEmail + ", email=" + email + ", confirmEmail="
        + confirmEmail + ", memberType=" + memberType + ", memberInvite="
        + memberInvite + ", businessType=" + businessType + ", orgTag="
        + orgTag + ", invitee=" + invitee + ", partnerProgram="
        + partnerProgram + "]";
  }

  public void reset() {

    this.firstName = "";
    this.lastName = "";
    this.company = "";
    this.businessName = "";
    this.businessEmail = "";
    this.email = "";
    this.confirmEmail = "";
    this.memberType = "";

  }

  public PartnerProgramDefinitionDto getPartnerProgram() {
    return partnerProgram;
  }

  public void setPartnerProgram(PartnerProgramDefinitionDto partnerProgram) {
    this.partnerProgram = partnerProgram;
  }

}
