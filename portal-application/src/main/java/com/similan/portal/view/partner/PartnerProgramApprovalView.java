package com.similan.portal.view.partner;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("partnerProgramApprovalView")
@Slf4j
public class PartnerProgramApprovalView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private PartnershipDto partnership;
	private String processInstanceId = StringUtils.EMPTY;
	private Long memberStateId = Long.MIN_VALUE;
	
	@PostConstruct
	public void init() {
		
		String partnershipIdParam = this.getContextParam("psid");
		
		log.info("PartnerProgramApprovalView initialization for partnership " 
		                    + partnershipIdParam);
		if(StringUtils.isNotEmpty(partnershipIdParam)){
			try {
				Long partnershipId = Long.valueOf(partnershipIdParam);
				memberStateId = Long.valueOf(this.getContextParam("msid"));
				processInstanceId = this.getContextParam("pid");
				
				log.info("Parameters extracted partnership id " + partnershipId 
						+ " member state id " + memberStateId + " process instance id "
						+ processInstanceId);
				partnership = this.orgService.getPartnershipDtoById(partnershipId);
				
			}
			catch(Exception exp){
				log.error("Partnership cannot be fetched " + partnership.getId(), exp);
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Partnership Approval Error", 
	                    "Error fetching partnership information"));  
			}
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Partnership Approval Error", 
                    "Error fetching partnership information")); 
		}
		
	}
	
	public String approvePartnership(boolean status){
		log.info("Partnership approved " + partnership.getId() + " status " + status);
		
		try { 
			this.orgService.partnershipApprovalNotification(this.memberStateId, 
					this.processInstanceId, status, partnership.getComment());
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Partnership Approval Status", 
                    "An email will be sent to the partner of the decision")); 
			
			if(status)
				return "partnerApproved";
			else
				return "partnerRejected";
		}
		catch(Exception exp){
			log.info("Cannot complete approval process ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Partnership Approval Error", 
                    "Cannot complete approval process " + exp.getMessage())); 
		}
		return null;
	}

	public PartnershipDto getPartnership() {
		return partnership;
	}

	public void setPartnership(PartnershipDto partnership) {
		this.partnership = partnership;
	}
	
	

}
