package com.similan.process.action.lead;

import java.util.ArrayList;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.lead.TransferLeadRepository;
//import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
public class LeadTransferChangeStateAction implements ActivityBehaviour {

	private static final long serialVersionUID = 5593804257303443002L;


	@Autowired
	private TransferLeadRepository transferLeadRepository;
	@Autowired
	private SocialActorRepository socialActorRepository;
	private TransferState transferState;

	public void execute(ActivityExecution execution) throws Exception {
/*		log.info("Starting LeadTransferChangeStateAction");
		TransferLead transferLead = getTransferLead(execution);

		SocialActor toSocialActor = getProcessingActor(execution);
		SocialPerson fromSocialActor = getInitiatoingActor(transferLead);

		log.info("Updating lead to approved in database");

		 //Update transfer to status accepted
		 updateTransferLead(transferLead);

		log.info("Updating finished");

		setExecutionParameters(execution, toSocialActor, fromSocialActor,
				transferLead);

		log.info("Completed LeadTransferChangeStateAction");
*/	}

	private void setExecutionParameters(ActivityExecution execution,
			SocialActor toSocialActor, SocialPerson fromSocialActor,
			TransferLead transferLead) {
		// Set parameters for following steps (send email)

		/* Put everything for sending the approval request email */

/*		if (toSocialActor instanceof SocialPerson) {
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_FIRST_NAME,
							StringUtils.defaultIfEmpty(((SocialPerson) toSocialActor).getFirstName(),""));
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_LAST_NAME,
							StringUtils.defaultIfEmpty(((SocialPerson) toSocialActor).getLastName(),""));
		} else {
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_FIRST_NAME,
							"");
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_LAST_NAME,
							"");
		}

		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_FROM_FIRST_NAME,
				StringUtils.defaultIfEmpty(fromSocialActor.getFirstName(),""));
		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_FROM_LAST_NAME,
				StringUtils.defaultIfEmpty(fromSocialActor.getLastName(),""));

		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD,
				transferLead != null ? getLeadDtoFromTransferLead(transferLead) : new ArrayList<LeadDto>());

		execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
				StringUtils.defaultIfEmpty(fromSocialActor.getPrimaryEmail(),""));
*/	}

/*	private LeadDto getLeadDtoFromTransferLead(TransferLead transferLead) {

		LeadDto lead = new LeadDto();
		lead.setLeadType(LeadType.TransferLead);
		lead.setId(transferLead.getId());
		lead.setForSocialActorId(transferLead.getForSocialActorId());
		lead.setTimeStamp(transferLead.getTimeStamp());
		lead.setFirstName(transferLead.getFirstName());
		lead.setLastName(transferLead.getLastName());
		lead.setName(transferLead.getName());
		lead.setCompany(transferLead.getCompany());
		lead.setKeyword(transferLead.getKeyword());
		lead.setIndustry(transferLead.getIndustry());
	
		lead.setContactEmail(transferLead.getContactEmail());
		lead.setContactPhone(transferLead.getContactPhone());
		
		if(transferLead.getLocation() != null){
			AddressDto location = new AddressDto();
			location.setStreet(transferLead.getLocation().getStreet());
			location.setCity(transferLead.getLocation().getCity());
			location.setState(transferLead.getLocation().getState());
			location.setCountry(transferLead.getLocation().getCountry());
			location.setZipCode(transferLead.getLocation().getZipCode());
		}
		

		if (transferLead.getLeadStatus() == null) {
			lead.setLeadStatus(LeadStatusType.Unassigned);
		} else {
			lead.setLeadStatus(transferLead.getLeadStatus());
		}

		lead.setViewed(transferLead.getViewed());
		return lead;
	}
*/
	private TransferLead getTransferLead(ActivityExecution execution) {
		Long transferLeadId = getTransferLeadId(execution);

		TransferLead transferLead = transferLeadRepository
				.findOne(transferLeadId);
		return transferLead;
	}

	private SocialPerson getInitiatoingActor(TransferLead transferLead) {
		SocialActor actor = this.socialActorRepository.findOne(transferLead
				.getTransferLeadRequest().getFromSocialActorId());

		return (SocialPerson) actor;
	}

	private SocialActor getProcessingActor(ActivityExecution execution) {
		Long memberId = getMemberId(execution);

		SocialActor socialActor = socialActorRepository.findOne(memberId);
		return socialActor;
	}

	private void updateTransferLead(TransferLead transferLead) {
	 // Change transfer state
	
	 transferLead.setTransferState(this.transferState);
	 transferLead.setTimeStamp(new Date());
	 transferLeadRepository.save(transferLead);
	 }

	private Long getTransferLeadId(ActivityExecution execution) {
		Long transferLeadId = (Long) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_ID);
		return transferLeadId;
	}

	private Long getMemberId(ActivityExecution execution) {
		Long memberId = (Long) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_ID);
		return memberId;
	}

	public TransferState getTransferState() {
		return transferState;
	}

	public void setTransferState(TransferState transferState) {
		this.transferState = transferState;
	}
}
