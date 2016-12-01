package com.similan.portal.view.leadportal;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.LeadType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadActivityDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadMessageDto;
import com.similan.framework.dto.lead.LeadNoteDto;
import com.similan.framework.dto.lead.LeadTransferStatusDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadDetailView")
@Slf4j
public class LeadDetailView extends BaseView {
	
	private static final long serialVersionUID = 1L;
	
	private LeadDto lead;
	
	private LeadNoteDto currentLeadNote;
	
	private LeadMessageDto currentLeadMessage;
	
	private LeadActivityDto currentLeadActivity;
	
    private MapModel simpleModel;
	
    private Double mapCenterLat = 37.702387;
    
    private Double mapCenterLong = 238.406370;
	
    private String createdByName;
    
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	@Autowired
	private MemberInfoDto memberInfo;
	
	@PostConstruct
    public void init() {
		
		/*String leadId = this.getContextParam("lid");
		String leadTypeStr = this.getContextParam("ldtype");
		log.info("Getting lead with id " + leadId + " of type " + leadTypeStr);
		
		this.currentLeadNote = new LeadNoteDto();
		this.currentLeadMessage = new LeadMessageDto();
		this.currentLeadActivity = new LeadActivityDto();
		
		if(leadId != null){
			
			long id = Long.valueOf(leadId);
			LeadType leadType = LeadType.valueOf(leadTypeStr);
			
			try {
				
				 Get the lead based on id and type 
				this.lead = this.getLeadService().getLead(id, leadType);
				
				 check whether the view flag and if it is null or false 
				 * set it to true and also save it to DB 
				log.info("Lead view status " + this.lead.getViewed());
				this.lead.setViewed(Boolean.TRUE);
				
				Date lastViewedDate = this.lead.getDateLastViewed();
				this.lead.setDateLastViewed(new Date());
				this.getLeadService().saveLead(this.lead, orgInfo);
				this.lead.setDateLastViewed(lastViewedDate);

				
				 get the comment list 
				List<LeadNoteDto> leadCommentList = this.leadService.getLeadComments(this.lead);
				sortCommentList(leadCommentList);
				this.lead.setNoteList(leadCommentList);
				
				 get lead activity 
				List<LeadActivityDto> activityList = this.leadService.getLeadActivity(this.lead);
				this.lead.setActivityList(activityList);
				
				 get all the messages 
				List<LeadMessageDto> leadMessageList = this.leadService.getLeadSentMessage(this.lead);
				this.lead.setMessageList(leadMessageList);
				
				List<LeadTransferStatusDto> leadTransferStatusList = this.leadService.getLeadTransferStatus(this.lead);
				this.lead.setLeadTransferStatusList(leadTransferStatusList);
				
				 map coordinates 
				if(lead.getLocation() != null){
					this.generateMapModel();
				}
				
				if(lead.getCreatedById() != null) {
					MemberInfoDto cratedByMemberInfo = this.memberService.getMemberById(lead.getCreatedById());
					createdByName = cratedByMemberInfo.getFirstName() + " " + cratedByMemberInfo.getLastName();
				}
			}
			catch(Exception exp){
				
			}
			
		}
		else {
			 Throw an exception 
		}
		
		// Need to set the selected lead in OrganizationLeadView for lead transfers
		FacesContext facesContext = FacesContext.getCurrentInstance();
		OrganizationLeadView bean = (OrganizationLeadView) facesContext.getApplication().evaluateExpressionGet(facesContext, "#{organizationLeadView}", OrganizationLeadView.class);
		bean.setSelectedLead(lead);*/
	}
	
	private void sortCommentList(List<LeadNoteDto> leadCommentList) {
		if (leadCommentList == null)
			return;
		Collections.sort(leadCommentList, new Comparator<LeadNoteDto>() {

			public int compare(LeadNoteDto lead1, LeadNoteDto lead2) {
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

				if (leadId1 < leadId2)
					return 1;
				else if (leadId2 < leadId1)
					return -1;
				else
					return 0;
			}
		});
	}

	public String deleteLead(){
		
		/*try {
			log.info("Delete lead " + this.lead);
			this.leadService.deleteLead(this.lead);
		}
		catch(Exception exp){
			log.info("Cannot delete lead", exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error",
							"Cannot delete lead "));
		}
		
		return "result";*/
	  throw new UnsupportedOperationException();
	}
	
    private void generateMapModel(){
    	
    	simpleModel = new DefaultMapModel();
		
		/* put the address */
		LatLng memberAddrCoord = new LatLng(lead.getLocation().getLatitude(), 
				lead.getLocation().getLongitude()); 
		
		this.mapCenterLat = lead.getLocation().getLatitude();
		this.mapCenterLong = lead.getLocation().getLongitude();
		
		simpleModel.addOverlay(new Marker(memberAddrCoord, 
				                   lead.getName()));
		
	}
	
	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public Double getMapCenterLat() {
		return mapCenterLat;
	}

	public void setMapCenterLat(Double mapCenterLat) {
		this.mapCenterLat = mapCenterLat;
	}

	public Double getMapCenterLong() {
		return mapCenterLong;
	}

	public void setMapCenterLong(Double mapCenterLong) {
		this.mapCenterLong = mapCenterLong;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}

	public LeadActivityDto getCurrentLeadActivity() {
		return currentLeadActivity;
	}

	public void setCurrentLeadActivity(LeadActivityDto currentLeadActivity) {
		this.currentLeadActivity = currentLeadActivity;
	}

	public LeadNoteDto getCurrentLeadNote() {
		return currentLeadNote;
	}

	public void setCurrentLeadNote(LeadNoteDto currentLeadNote) {
		this.currentLeadNote = currentLeadNote;
	}

	public LeadDto getLead() {
		return lead;
	}

	public void setLead(LeadDto lead) {
		this.lead = lead;
	}
	
	public LeadMessageDto getCurrentLeadMessage() {
		return currentLeadMessage;
	}

	public void setCurrentLeadMessage(LeadMessageDto currentLeadMessage) {
		this.currentLeadMessage = currentLeadMessage;
	}

	public void updateLeadNote(LeadNoteDto leadNoteDto) {
		/*log.info("Updating note  " + leadNoteDto.getLeadNote().getSubject());
		this.leadService.updateLeadNote(leadNoteDto);*/
		
	}
	public void addLeadNote(){
		
		/*log.info("Current lead note " + currentLeadNote.getLeadNote().getSubject());
		
		lead.getNoteList().add(0,currentLeadNote);
		currentLeadNote.getLeadNote().setTimeStamp(new Date());
		currentLeadNote.setCommenterPhoto(this.memberInfo.getPhotoLocation());
		currentLeadNote.setCommenterFirstName(this.memberInfo.getFirstName());
		currentLeadNote.setCommenterLastName(this.memberInfo.getLastName());
		currentLeadNote.setCommenterId(this.memberInfo.getId());
		this.leadService.addLeadNote(this.currentLeadNote, this.lead, this.memberInfo);
		
		currentLeadNote = new LeadNoteDto();*/
	}
	
	public void deleteLeadNote(String uuIdNote){
		/*log.info("Deleteing lead note " + uuIdNote);
		LeadNoteDto noteTobeDeleted = null;
		
		 get the lead note 
		for(LeadNoteDto noteDto : this.lead.getNoteList()){
			if(noteDto.getUuId().equalsIgnoreCase(uuIdNote) == true){
				noteTobeDeleted = noteDto;
				break;
			}
		}
		
		 if found delete 
		if(noteTobeDeleted != null){
			this.lead.getNoteList().remove(noteTobeDeleted);			
			if(noteTobeDeleted.getId() != Long.MIN_VALUE){
				this.leadService.deleteLeadNote(noteTobeDeleted);
			}
		}*/
	}
	
	public void sendMessageToLead(){
		
		/*log.info("Current lead message " + currentLeadMessage.getLeadNote().getSubject());
		
		currentLeadMessage.getLeadNote().setTimeStamp(new Date());
		currentLeadMessage.setCommenterFirstName(this.memberInfo.getFirstName());
		currentLeadMessage.setCommenterLastName(this.memberInfo.getLastName());
		currentLeadMessage.setCommenterId(this.memberInfo.getId());
		currentLeadMessage.setCommenterPhoto(this.memberInfo.getPhotoLocation());
		lead.getMessageList().add(currentLeadMessage);
		
		try {
			this.getLeadService().sendLeadMessage(this.currentLeadMessage, this.lead, this.memberInfo);
		}
		catch(Exception exp){
           log.error("Error ", exp);
           FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error",
							"Cannot send message to lead "));
           
		}
		this.currentLeadMessage = new LeadMessageDto();*/
	}
	
	public void addLeadActivity(){
		
		/*log.info("Current lead activity " + currentLeadActivity.getSubjectActivity());
		lead.getActivityList().add(currentLeadActivity);
		this.getLeadService().addLeadActivity(this.currentLeadActivity, this.lead);
		
		currentLeadActivity = new LeadActivityDto();*/
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	
	public void editLead(){
		
		/*log.info("Edit lead " + lead.toString());
		
		try {
			
			this.getLeadService().saveLead(lead, this.getOrgInfo());

			// Refresh the lead from the backend so that addresses are loaded
			this.lead = this.getLeadService().getLead(lead.getId(), lead.getLeadType());
			
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"Successfully edited the lead", 
	                   "Successfully edited the lead")); 
		}
		catch(Exception exp){
           log.error("Cannot edit lead ", exp);
           FacesContext.getCurrentInstance().addMessage(null, 
                   new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                   "Failure to edid a lead " + exp.getMessage()));  
		}*/
	}	
}
