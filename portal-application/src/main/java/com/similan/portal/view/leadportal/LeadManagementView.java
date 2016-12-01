package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.LeadType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadAssignmentDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadModel;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.api.connection.dto.basic.ContactType;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.exception.LeadDeleteException;

@Scope("view")
@Component("leadView")
@Slf4j
public class LeadManagementView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	
	private PieChartModel leadPieChartModel;
	
	protected CartesianChartModel leadCartesianModel;
	
	protected Long socialActorId;
	
	protected SelectItem[] leadTypes;
	
	protected LeadModel leadModel = null;
	
	protected LeadDto[] selectedLeads;
	
	protected List<LeadDto> leads = null;
	
	protected Map<String, List<LeadDto>> leadFilterMap = null;
	
	protected List<LeadAssignmentDto> leadAssignedList = null;
	
	@Autowired
	private MemberInfoDto member;

	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	protected void init() {
		
		/*log.info("Post init in LeadManagementView " + this.orgInfo.getId());
		
		try {
			this.leads = this.getLeadService().getAllMemberLeads(this.orgInfo.getId());
			this.leadAssignedList = this.getLeadService()
					                    .getAllLeadAssignmentsForSocialActor(this.orgInfo.getId());
			
		}
		catch(Exception exp){
           log.error("Cannot initialize lead management view ", exp);			
		}
		
		Map<LeadType, Long> leadCountStat = null;
		
		 get all contacts 
		for(Contact leadContact : this.socialActorContact.getContacts()){
			
			if(StringUtils.isEmpty(leadContact.getContactCategory()) != true){
				if(leadContact.getContactCategory().equalsIgnoreCase(ContactType.ChannelPartner.toString()) == true ){
					this.leadContacts.add(leadContact);
				}
			}
		}
		
		 populate lead model 
		sortLeadsByIdDescending();
		this.leadModel = new LeadModel(this.leads);
		leadCountStat = this.getLeadService().getLeadCountStatSocialActor(this.orgInfo.getId());
		
		 populate count stats 
		if(leadCountStat != null){
			this.populateLeadPieChartModel(leadCountStat);
		}
		
		 Lead type 
		LeadType[] leadTypeEnums = LeadType.values();
		leadTypes = new SelectItem[leadTypeEnums.length];
		
		for(int i=0; i<leadTypeEnums.length; i++){
			
			LeadType leadType = leadTypeEnums[i];
			SelectItem leadSelectItem = new SelectItem();
			leadSelectItem.setValue(leadType);
			leadSelectItem.setLabel(leadType.toString());
			leadTypes[i] = leadSelectItem;
		}*/
	}


	protected void reInitLeadModel(){
		/* populate lead model from memory */
		this.leadModel = new LeadModel(this.leads);
	}
	
	private void populateLeadPieChartModel(Map<LeadType, Long> leadCountStat){
		
		if(leadPieChartModel == null){
			
			leadPieChartModel = new PieChartModel();
			leadPieChartModel.set("Click Through Leads", leadCountStat.get(LeadType.ClickThroughLead));
			leadPieChartModel.set("Contact Leads", leadCountStat.get(LeadType.ContactLead));
			leadPieChartModel.set("Search Leads", leadCountStat.get(LeadType.SearchLead));
		}
			
	}
	
	public void deleteLeads(){
		/*try {
			
			if(this.selectedLeads == null){
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Error",
								"Please select leads before delete "));
				return;
			}
			
			this.getLeadService().deleteLeads(this.selectedLeads);
			leads.removeAll(Arrays.asList(this.selectedLeads));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Success",
							"Successfully deleted leads "));
		}
		catch(LeadDeleteException exp){
			log.error("Error deleting leads ", exp);
				FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error",
							"The selected lead(s) could not be deleted because one or more of them are a lead transferred to another member or organization, or a lead that was aquired by a transfer to you or your organization"));
		}
		catch(Exception exp){
			log.error("Error deleting leads ", exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error",
							"Error deleting leads"));
		}*/
	}
	
	

	public List<LeadDto> getLeads() {
		return leads;
	}

	public void setLeads(List<LeadDto> leads) {
		this.leads = leads;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}
	
	public List<LeadAssignmentDto> getLeadAssignedList() {
		return leadAssignedList;
	}

	public void setLeadAssignedList(List<LeadAssignmentDto> leadAssignedList) {
		this.leadAssignedList = leadAssignedList;
	}

	
	public Long getSocialActorId() {
		return socialActorId;
	}

	public void setSocialActorId(Long socialActorId) {
		this.socialActorId = socialActorId;
	}

	public LeadModel getLeadModel() {
		return leadModel;
	}

	public void setLeadModel(LeadModel leadModel) {
		this.leadModel = leadModel;
	}

	
	public CartesianChartModel getLeadCartesianModel() {
		return leadCartesianModel;
	}

	public void setLeadCartesianModel(CartesianChartModel leadCartesianModel) {
		this.leadCartesianModel = leadCartesianModel;
	}

	public PieChartModel getLeadPieChartModel() {
		return leadPieChartModel;
	}

	public void setLeadPieChartModel(PieChartModel leadPieChartModel) {
		this.leadPieChartModel = leadPieChartModel;
	}
	
	public LeadDto[] getSelectedLeads() {
		return selectedLeads;
	}

	public void fetchLeads(String leadFor){
		
		/*log.info("fetchLeads in LeadManagementView " + leadFor);
		Map<LeadType, Long> leadCountStat = null;
		
		try {
			if(leadFor.equals("organization") == true){
				Long orgId = orgInfo.getId();
				log.info("Leads to be fetched for org " + orgId);
				
				this.leads = this.getLeadService().getAllOrganizationLeads(orgId);
				leadCountStat = this.getLeadService().getLeadCountStatSocialActor(orgId);
			}
			else {
				Long memberId = member.getId();
				log.info("Leads to be fetched for member " + memberId);
				this.leads = this.getLeadService().getAllMemberLeads(memberId);
				leadCountStat = this.getLeadService().getLeadCountStatSocialActor(memberId);
			}
			 populate count stats 
			if(leadCountStat != null){
				this.populateLeadPieChartModel(leadCountStat);
			}
		}
		catch(Exception exp){
          log.info("Cannot fetch leads ", exp);			
		}*/
	}

	public void setSelectedLeads(LeadDto[] selectedLeads) {
		log.info("Selected leads " + selectedLeads);
		this.selectedLeads = selectedLeads;
	}
	
	public void setSelectedLead(LeadDto lead) {
		if (lead != null) {
			this.selectedLeads = new LeadDto[1];
			selectedLeads[0] = lead;
		} else {
			this.selectedLeads = new LeadDto[0];
		}
	}

	public void assignLead(){
		/*log.info("Assigning lead to social actors " + this.selectedContacts
				                      + " leads to be assigned " + this.selectedLeads);
		this.getLeadService().assignLeads(this.selectedLeads, this.selectedContacts ,this.member);*/
	}
	
	public SelectItem[] getLeadTypes() {
		return leadTypes;
	}

	public void setLeadTypes(SelectItem[] leadTypes) {
		this.leadTypes = leadTypes;
	}
	
	public void performCrmSync() {

		/*try {

			this.leadService.performSync(member);
			List<LeadDto> updatedLeads = this.leadService.getAllMemberLeads(this.orgInfo.getId());
			
			leads.clear();
			leads.addAll(updatedLeads);
			sortLeadsByIdDescending();

		} catch (CoreServiceException exp) {
			if (exp.getErrorCode().equals(
					CoreServiceErrorCode.CRM_SYNC_ALREADY_IN_PROCESS))
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"CRM Synchronization",
										"Synchronization operation is in process. Force synchronization cannot be performed until the process is complete."));
		} catch (Exception exp) {
			log.error("Error updating member ", exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CRM Synchronization",
							"Failure to perform synchronization. An email message detailing the issue has been sent."));
		}*/
	}
	
	protected void sortLeadsByIdDescending() {
		if(leads != null)
		Collections.sort(leads, new Comparator<LeadDto>() {

			public int compare(LeadDto lead1, LeadDto lead2) {
					if (lead1 == lead2)
						return 0;
					long leadId1;
					long leadId2;
					if (lead1 == null)
						leadId1 = Long.MAX_VALUE;
					else if (lead1.getId() == null)
						leadId1 = Long.MAX_VALUE;
					else
						leadId1 = lead1.getId();

					if (lead2 == null)
						leadId2 = Long.MAX_VALUE;
					if (lead2.getId() == null)
						leadId2 = Long.MAX_VALUE;
					else
						leadId2 = lead2.getId();
					    	
					if(leadId1 < leadId2)
						return 1;
					else if (leadId2 < leadId1)
						return -1;
					else return 0;
			}});
	}
}
