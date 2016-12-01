package com.similan.process.action.lead;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.lead.TransferLeadRequestRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
//import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class LeadTransferProcessedAction implements ActivityBehaviour {
	private static final long serialVersionUID = -1908011987348434991L;

	private static final int NUMBER_OF_REMINDERS_TO_SEND = 1;
	
	@Autowired
	private TransferLeadRequestRepository transferLeadRequestRepository;
	@Autowired
	private SocialActorRepository socialActorRepository;
	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	public void execute(ActivityExecution execution) throws Exception {
/*		log.info("Starting LeadTransferTimeoutAction");
		TransferLeadRequest transferLeadRequest = getTransferLeadRequest(execution);

		if (checkAllLeadsProcessed(transferLeadRequest)) {
			handleProcessComplete(execution);
		} else {

			 Get the parameters 
			Integer remindersSent = getRemindersSent(transferLeadRequest);

			if (remindersSent < NUMBER_OF_REMINDERS_TO_SEND) {
				handleReminder(execution, transferLeadRequest);
			} else {
				handleExpired(execution);
			}
		}
		log.info("Finished LeadTransferTimeoutAction");
*/	}

	private void handleExpired(ActivityExecution execution) {
		execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
				"expired");
	}

	private void handleReminder(ActivityExecution execution,
			TransferLeadRequest transferLeadRequest) {
/*		SocialActor toSocialActor = getToSocialActorForRequest(transferLeadRequest);
		SocialActor fromSocialActor = getFromSocialActorForRequest(transferLeadRequest);

		execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
				"remind");
		execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
				getEmailToAddress(toSocialActor));

		execution
				.setVariable(
						ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD_REQUEST_ID,
						transferLeadRequest.getId());

		 Put everything for sending the approval request email 

		if (toSocialActor instanceof SocialPerson) {
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_FIRST_NAME,
							StringUtils.defaultIfEmpty(
									((SocialPerson) toSocialActor)
											.getFirstName(), ""));
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_TO_LAST_NAME,
							StringUtils.defaultIfEmpty(
									((SocialPerson) toSocialActor)
											.getLastName(), ""));
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
		if (toSocialActor instanceof SocialPerson) {

			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_FIRST_NAME,
							StringUtils.defaultIfEmpty(
									((SocialPerson) fromSocialActor)
											.getFirstName(), ""));
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_LAST_NAME,
							StringUtils.defaultIfEmpty(
									((SocialPerson) fromSocialActor)
											.getLastName(), ""));
		} else {
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_FIRST_NAME,
							"");
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_LAST_NAME,
							"");
		}
		execution
				.setVariable(
						ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEADS,
						getLeadDtoFromTransferLeads(transferLeadRequest
								.getTransferLeads()) != null ? getLeadDtoFromTransferLeads(transferLeadRequest
								.getTransferLeads()) : new ArrayList<LeadDto>());

		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_BASE_URL,
				StringUtils.defaultIfEmpty(platformCommonSettings
						.getPortalApplcationUrl().getValue(), ""));

		execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
				getEmailToAddress(toSocialActor));
*/	}

/*	private List<LeadDto> getLeadDtoFromTransferLeads(
			List<TransferLead> transferLeads) {
		List<LeadDto> leads = new ArrayList<LeadDto>();

		for (TransferLead transferLead : transferLeads) {
			if (transferLead.getTransferState() == TransferState.Pending)
				leads.add(getLeadDtoFromTransferLead(transferLead));
		}
		return leads;
	}
*/
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
	private void handleProcessComplete(ActivityExecution execution) {
		execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
				"complete");
	}

	private SocialActor getToSocialActorForRequest(
			TransferLeadRequest transferLeadRequest) {
		SocialActor actor = socialActorRepository.findOne(transferLeadRequest
				.getToSocialActorId());
		return actor;
	}

	private SocialActor getFromSocialActorForRequest(
			TransferLeadRequest transferLeadRequest) {
		SocialActor actor = socialActorRepository.findOne(transferLeadRequest
				.getFromSocialActorId());
		return actor;
	}

	private boolean checkAllLeadsProcessed(
			TransferLeadRequest transferLeadRequest) {
		boolean allLeadsAreProcessed = true;
		for (TransferLead lead : transferLeadRequest.getTransferLeads()) {
			if (lead.getTransferState() == TransferState.Pending) {
				allLeadsAreProcessed = false;
				break;
			}
		}
		return allLeadsAreProcessed;
	}

	private Integer getRemindersSent(TransferLeadRequest transferLeadRequest) {
		Integer remindersSent = transferLeadRequest.getRemindersSent();

		if (remindersSent == null)
			remindersSent = 0;
		return remindersSent;
	}

	private Object getEmailToAddress(SocialActor socialActor) {
		String emailAddress = null;
		if (socialActor instanceof SocialOrganization)
			emailAddress = ((SocialOrganization) socialActor).getPrimaryEmail();
		else if (socialActor instanceof SocialPerson)
			emailAddress = ((SocialPerson) socialActor).getPrimaryEmail();

		return emailAddress;
	}

	private TransferLeadRequest getTransferLeadRequest(
			ActivityExecution execution) {
		Long transferLeadRequestId = (Long) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD_REQUEST_ID);

		TransferLeadRequest transferLeadRequest = this.transferLeadRequestRepository
				.findOne(transferLeadRequestId);
		return transferLeadRequest;
	}
}
