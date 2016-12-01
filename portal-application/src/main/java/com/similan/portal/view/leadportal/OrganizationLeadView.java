package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.dto.lead.AffiliateLeadDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadModel;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.databean.CountryBean;

@Scope("view")
@Component("organizationLeadView")
@Slf4j
public class OrganizationLeadView extends LeadManagementView {

  private static final long serialVersionUID = 1L;

  protected LeadType[] includeLeadTypes;
  protected Boolean includeUnreadOnly;

  protected String leadType = "Active";

  protected String leadFilterRefreshOption = "All";

  protected LeadDto newLeadDto = null;

  private List<StateType> selectedStateList = null;

  protected List<AffiliateLeadDto> availableLeads = null;

  @PostConstruct
  public void init() {
    this.initnewLeadDto();
    this.initializeView();
  }

  private void initnewLeadDto() {

    this.newLeadDto = new LeadDto();
    this.newLeadDto.getLocation().setCountry("United States");
    this.newLeadDto.setLeadType(LeadType.AcquiredLead);
    this.newLeadDto.setTimeStamp(new Date());

    CountryBean countryBean = (CountryBean) this.findBackingBean("countryBean");
    this.selectedStateList = countryBean.getStateTypeList("United States");
  }

  public String getLeadFilterRefreshOption() {
    return leadFilterRefreshOption;
  }

  public void setLeadFilterRefreshOption(String leadFilterRefreshOption) {
    this.leadFilterRefreshOption = leadFilterRefreshOption;
  }

  public List<StateType> getSelectedStateList() {
    return selectedStateList;
  }

  public void setSelectedStateList(List<StateType> selectedStateList) {
    this.selectedStateList = selectedStateList;
  }

  public LeadDto getNewLeadDto() {
    return newLeadDto;
  }

  public void setNewLeadDto(LeadDto newLeadDto) {
    this.newLeadDto = newLeadDto;
  }

  public List<AffiliateLeadDto> getAvailableLeads() {
    return availableLeads;
  }

  public void setAvailableLeads(List<AffiliateLeadDto> availableLeads) {
    this.availableLeads = availableLeads;
  }

  private void initializeView() {

    /*
     * log.info("Leads to be fetched for org " + this.getOrgInfo().getId());
     * 
     * try {
     * 
     * populate contact list if(this.getOrgInfo().getContacts() == null){
     * 
     * Set<Contact> contactList =
     * this.orgService.getContacts(this.getOrgInfo()); log.info("Got contacts "
     * + contactList.size()); this.getOrgInfo().setContacts(new
     * ArrayList<Contact>(contactList)); }
     * 
     * this.socialActorContact = this.getOrgInfo(); this.socialActorId =
     * this.getOrgInfo().getId(); updateDataTable();
     * 
     * check whether it is coming from purchase view String showFlag =
     * JsfUtils.getContextParam("showFlag"); if(showFlag != null){
     * FacesContext.getCurrentInstance().addMessage(null, new
     * FacesMessage(FacesMessage.SEVERITY_INFO,"Failure",
     * "You have purchased the lead successfully")); } super.init();
     * 
     * } catch(Exception exp){ log.error("Cannot initialize org leads ", exp); }
     */
  }

  public void purchaseAvailableLead(AffiliateLeadDto leadToPurchase) {
    log.info("Lead to purchase " + leadToPurchase.toString());

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Lead successfully purchased"));
  }

  public Integer getNumNewLeads() {
    /*
     * return this.getLeadService().getCountNewLeads( getOrgInfo().getId());
     */
    throw new UnsupportedOperationException();
  }

  public Integer getNumAvailableLeads() {
    /*
     * return this.getLeadService().getCountAvailableAffiliateLeads(
     * getMember().getId());
     */
    throw new UnsupportedOperationException();
  }

  public void addNewLead() {

    /*
     * log.info("Add new lead " + newLeadDto.toString());
     * 
     * try {
     * 
     * if (StringUtils.isEmpty(newLeadDto.getLeadSource())) { String leadSource
     * = "Entered by " + this.getMember().getFullName() + " for business " +
     * this.getOrgInfo().getBusinessName();
     * this.newLeadDto.setLeadSource(leadSource); }
     * newLeadDto.setCreatedById(this.getMember().getId());
     * 
     * this.getLeadService().saveLead(newLeadDto, this.getOrgInfo());
     * this.getLeads().add(0, newLeadDto);
     * 
     * this.initnewLeadDto();
     * 
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added the lead",
     * "Successfully added the lead")); } catch (Exception exp) {
     * log.error("Cannot add a new lead ", exp);
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
     * "Failure to add a lead " + exp.getMessage())); }
     */
    throw new UnsupportedOperationException();
  }

  public void loadStatesForSelectedCountry() {
    CountryBean countryBean = (CountryBean) this.findBackingBean("countryBean");
    this.selectedStateList = countryBean.getStateTypeList(this.newLeadDto
        .getLocation().getCountry());
  }

  public void approveLeadTransfer(Long id) {
    /*
     * if (validateTransferLead(id)) { getLeadService().approveLeadTransfer(id,
     * this.getMember().getId());
     * 
     * refreshLeadDataTable();
     * 
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
     * "You have successfully approved the lead"));
     * 
     * }
     */
    throw new UnsupportedOperationException();
  }

  private void refreshLeadDataTable() {
    /*
     * try { // refresh lead service this.leads =
     * this.getLeadService().getMemberLeads(getOrgInfo().getId(),
     * includeLeadTypes, includeUnreadOnly); sortLeadsByIdDescending();
     * this.leadModel = new LeadModel(this.leads); this.selectedLeads = new
     * LeadDto[0]; } catch (Exception exp) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "Error refreshing DataTable")); log.error("Error refreshing DataTable",
     * exp); }
     */
    throw new UnsupportedOperationException();
  }

  public void rejectLeadTransfer(Long id) {
    /*
     * if (validateTransferLead(id)) { getLeadService().rejectLeadTransfer(id,
     * this.getMember().getId());
     * 
     * refreshLeadDataTable(); FacesContext.getCurrentInstance().addMessage(
     * null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
     * "You have successfully rejected the lead"));
     * 
     * }
     */
    throw new UnsupportedOperationException();
  }

  public void processApproveRejectTransferLeadUrl() {
    String transferLeadId = this.getContextParam("id");
    String approvalType = this.getContextParam("decision");
    if (approvalType != null) {
      if ("approve".equals(approvalType))
        approveLeadTransfer(Long.valueOf(transferLeadId));
      else if ("reject".equals(approvalType))
        rejectLeadTransfer(Long.valueOf(transferLeadId));

    }
  }

  private boolean validateTransferLead(Long id) {
    /*
     * boolean result = true;
     * 
     * try {
     * 
     * LeadDto lead = leadService.getLead(id, LeadType.TransferLead);
     * 
     * log.info("Checking " + lead.getForSocialActorId() + " with " +
     * this.getOrgInfo().getId());
     * 
     * if (!lead.getForSocialActorId().equals(this.getOrgInfo().getId())) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "Lead could not be found.")); result = false; } else if
     * (lead.getTransferState() == null) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "Lead could not be found.")); result = false; } else if
     * (lead.getTransferState() == TransferState.Expired) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "The selected lead is no longer available.")); result = false; } else if
     * (lead.getTransferState() == TransferState.Accepted) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "The selected lead has already been accepted.")); result = false; } else
     * if (lead.getTransferState() == TransferState.Rejected) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
     * "The selected lead has already been rejected.")); result = false; }
     * 
     * } catch (Exception exp) { log.error("Lead transfer validation error ",
     * exp); }
     * 
     * return result;
     */
    throw new UnsupportedOperationException();
  }

  public void updateDataTable() {
    if (leadFilterRefreshOption.equals("All")) {
      includeLeadTypes = LeadType.values();
      this.includeUnreadOnly = false;
    } else if (leadFilterRefreshOption.equals("New")) {
      includeLeadTypes = LeadType.values();
      this.includeUnreadOnly = true;
    } else if (leadFilterRefreshOption.equals("Transferred")) {
      includeLeadTypes = new LeadType[] { LeadType.TransferLead };
      this.includeUnreadOnly = false;
    }

    refreshLeadDataTable();
  }

}
