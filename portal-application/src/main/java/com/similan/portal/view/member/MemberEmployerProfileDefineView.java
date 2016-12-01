package com.similan.portal.view.member;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.dto.NewBusinessDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Scope("view")
@Component("memberEmployerProfileDefineView")
@Slf4j
public class MemberEmployerProfileDefineView extends BaseView {
  private static final long serialVersionUID = 1L;


  @Autowired(required = true)
  private MemberInfoDto member = null;

  private boolean showOrgCreateButton = false;

  private NewBusinessDto business = new NewBusinessDto();

  private List<StateType> selectedStateList = null;

  @PostConstruct
  public void init() {
    log.info("Defining employer for " + member.getFullName());

    if (member.getAddressDto() != null
        && StringUtils.isBlank(member.getAddressDto().getCountry()) != true) {
      this.selectedStateList = this.getStatesForCountry(member.getAddressDto()
          .getCountry());
    } else {
      this.selectedStateList = this
          .getStatesForCountry(BaseView.DEFAULT_COUNTRY);
    }

  }

  public List<StateType> getSelectedStateList() {
    return selectedStateList;
  }

  public void setSelectedStateList(List<StateType> selectedStateList) {
    this.selectedStateList = selectedStateList;
  }

  public NewBusinessDto getBusiness() {
    return business;
  }

  public void setBusiness(NewBusinessDto business) {
    this.business = business;
  }

  public boolean getShowOrgCreateButton() {
    return showOrgCreateButton;
  }

  public void setShowOrgCreateButton(boolean showOrgCreateButton) {
    this.showOrgCreateButton = showOrgCreateButton;
  }

  public void getStatesForSelectedCountry() {
    if (StringUtils.isBlank(this.business.getCountry()) != true) {
      this.selectedStateList = this.getStatesForCountry(this.business
          .getCountry());
    }
  }

  /**
   * 1. Check existing org 2. If the org is in community then just associate 3.
   * Otherwise create organization flow will be triggered 4. If the member has
   * an existing employer than we have to delete the relationship and create new
   * one 5. If member does not have an existing employer we create a new
   * relationship
   * 
   */
  public void evaluateBusinessAssociation() {

    log.info("Evaluating or business " + this.business.getBusinessName()
        + " with email " + this.business.getBusinessPrimaryEmail());

    OrganizationBasicInfoDto matchingBusiness = this.getOrgService()
        .getBusinessByNameAndEmail(this.business.getBusinessName(),
            this.business.getBusinessPrimaryEmail());

    if (matchingBusiness == null) {
      matchingBusiness = this.getOrgService().getBusinessByName(
          this.business.getBusinessName());
    }

    if (matchingBusiness != null) {

      log.info("Matching business found for"
          + this.business.getBusinessName() + " with name "
          + matchingBusiness.getName());

      /**
       * 1. If it is the same employer then no need to re-associate 2. If it is
       * different then we need to associate
       */

      if ((member.getEmployer() != null)
          && (member.getEmployer().getId() == matchingBusiness.getId())) {

        /* then check visibility */
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
                "You are already an employee of the business "
                    + this.business.getBusinessName()
                    + " You cannot create another association"));
      } else {

        /* or tag exists */
        this.setShowOrgCreateButton(false);

        this.associateBusinessWithEmployee(matchingBusiness);

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
                "We have sent a request to the Admin of the Business "
                    + this.business.getBusinessName()
                    + " We will be notified upon approval"));
      }

    } else {
      this.setShowOrgCreateButton(true);
      business.setMemberOrgAssociationRoleType(SocialEmployeeType.Admin);

      String msg = "The Business "
          + this.business.getBusinessName()
          + " is currently "
          + "not part of BusinessReach community. In order to create the Business as "
          + "part of BusinessReach community "
          + "additional information is needed. Also if your are not authorized to "
          + "represent your Business contact the right person in your "
          + "organization who is authorized";

      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", msg));
    }
  }

  /**
   * Triggers the actual work flow
   */
  public void createBusinessForEmploee() {
    log.info("Creating Business " + this.business.getBusinessName());

    try {
      this.setShowOrgCreateButton(false);

      /* invoke the work flow */
      if (StringUtils.isBlank(this.business.getBusinessName()) != true) {
        log.info("Creating org with name " + this.business.getBusinessName());

        this.memberService.initiateOrganizationCreate(this.member,
            this.business);

        String msg = "The Business has " + this.business.getBusinessName()
            + " been created successfully and now part of the "
            + "BusinessReach community";
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", msg));
      } else {
        String msg = "Every business requires a valid name. It cannot be empty";
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
      }

    } catch (Exception exp) {
      log.info(
          "Error creating Business with name "
              + this.business.getBusinessName(), exp);

      business = new NewBusinessDto();

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error creating Business with name "
                  + this.business.getBusinessName()));
    }
  }

   public void associateBusinessWithEmployee(
      OrganizationBasicInfoDto mathcingBusiness) {

    if (mathcingBusiness != null) {

      log.info("Associating member to org " + member.getFirstName()
          + "  with " + this.business.getBusinessName() + " in the role "
          + this.member.getMemberOrgAssociationRoleType());

      this.setShowOrgCreateButton(false);
      this.memberService.associateOrganization(this.member, mathcingBusiness);

      String msg = "A request for has been sent to the business "
          + mathcingBusiness.getName() + " for approval of your "
          + "request to be associated. We will confirm you upon the approval";

      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", msg));

    }
  }

}
