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
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.lead.TransferLeadRequest;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.lead.AcquiredLeadRepository;
import com.similan.domain.repository.lead.ClickThroughLeadRepository;
import com.similan.domain.repository.lead.ContactLeadRepository;
import com.similan.domain.repository.lead.SearchLeadRepository;
import com.similan.domain.repository.lead.TransferLeadRepository;
import com.similan.domain.repository.lead.TransferLeadRequestRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
//import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class LeadTransferInitiateAction implements ActivityBehaviour {
	private static final long serialVersionUID = -1908011987348434991L;

	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	@Autowired
	private ClickThroughLeadRepository clickThroughLeadRepository;
	@Autowired
	private ContactLeadRepository contactLeadRepository;
	@Autowired
	private SearchLeadRepository searchLeadRepository;
	@Autowired
	private AcquiredLeadRepository acquiredLeadRepository;
	@Autowired
	private TransferLeadRepository transferLeadRepository;
	@Autowired
	private TransferLeadRequestRepository transferLeadRequestRepository;
	@Autowired
	private SocialActorRepository socialActorRepository;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Starting LeadTransferInitiateAction ID:"
				+ execution.getId());
/*		SocialActor fromSocialActor = getFromSocialActor(execution);
		SocialActor toSocialActor = getToSocialActor(execution);

		//List<LeadDto> leads = getLeadsToTransfer(execution);

		log.info("Transferring Lead from " + fromSocialActor.getId()
				+ " to " + toSocialActor.getId());

		TransferLeadRequest transferLeadRequest = createTransferLeadsRequest(
				leads, fromSocialActor, toSocialActor);

		setExecutionParameters(execution, fromSocialActor, toSocialActor,
				transferLeadRequest);

		log.info("Finished LeadTransferInitiateAction");
*/	}

	private void setExecutionParameters(ActivityExecution execution,
			SocialActor fromSocialActor, SocialActor toSocialActor,
			TransferLeadRequest transferLeadRequest) {

/*		execution
				.setVariable(
						ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEAD_REQUEST_ID,
						transferLeadRequest.getId());

		 Put everything for sending the approval request email 

		if (toSocialActor instanceof SocialPerson) {
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

		if (fromSocialActor instanceof SocialPerson) {

			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_FIRST_NAME,
							StringUtils.defaultIfEmpty(((SocialPerson) fromSocialActor).getFirstName(),""));
			execution
					.setVariable(
							ProcessContextParameterConstants.TRANSFER_LEAD_FROM_LAST_NAME,
							StringUtils.defaultIfEmpty(((SocialPerson) fromSocialActor).getLastName(),""));
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
		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_TRANSFER_LEADS,
				transferLeadRequest.getTransferLeads() != null ? getLeadDtoFromTransferLeads(transferLeadRequest
						.getTransferLeads()) : new ArrayList<LeadDto>());

		execution.setVariable(
				ProcessContextParameterConstants.TRANSFER_LEAD_BASE_URL,
				StringUtils.defaultIfEmpty(platformCommonSettings.getPortalApplcationUrl().getValue(),""));

		execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
				getEmailToAddress(toSocialActor));
*/	}

	/*
	private List<LeadDto> getLeadDtoFromTransferLeads(
			List<TransferLead> transferLeads) {
		List<LeadDto> leads = new ArrayList<LeadDto>();

		for (TransferLead transferLead : transferLeads) {
			leads.add(getLeadDtoFromTransferLead(transferLead));
		}
		return leads;
	}

	private LeadDto getLeadDtoFromTransferLead(TransferLead transferLead) {

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
	}*/

	private SocialActor getToSocialActor(ActivityExecution execution) {
		/*Contact contact = (Contact) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_CONTACT);

		SocialActor to = getSocialActorFromContact(contact);

		if (to instanceof SocialPerson) {
			SocialPerson person = (SocialPerson) to;
			SocialEmployee employeer = person.getEmployer();
			to = employeer.getContactFrom();
		}*/
		return null;
	}

	private SocialOrganization getOrganizationFromSocialActor(SocialActor actor) {

		/*SocialOrganization organization = null;
		if (actor instanceof SocialPerson) {
			SocialPerson person = (SocialPerson) actor;
			SocialEmployee employeer = person.getEmployer();
			organization = (SocialOrganization)employeer.getContactFrom();
		} else if (actor instanceof SocialOrganization) {
			organization = (SocialOrganization) actor;
		}*/
		return null;
	}

	/*private List<LeadDto> getLeadsToTransfer(ActivityExecution execution) {
	 @SuppressWarnings("unchecked")
		List<LeadDto> leads = (List<LeadDto>) execution
				.getVariable(ProcessContextParameterConstants.TRANSFER_LEAD_LEAD_LIST);
		return null;
	}*/

	private SocialActor getFromSocialActor(ActivityExecution execution) {
		Long memberId = (Long) execution
				.getVariable(ProcessContextParameterConstants.MEMBER_ID);

		/* get the member */
		SocialActor from = this.socialActorRepository.findOne(memberId);
		return from;
	}

	private Object getEmailToAddress(SocialActor socialActor) {
		String emailAddress = null;
		if (socialActor instanceof SocialOrganization)
			emailAddress = ((SocialOrganization) socialActor).getPrimaryEmail();
		else if (socialActor instanceof SocialPerson)
			emailAddress = ((SocialPerson) socialActor).getPrimaryEmail();

		return emailAddress;
	}

	/* private TransferLeadRequest createTransferLeadsRequest(List<LeadDto> leads,
			SocialActor from, SocialActor to) {
		TransferLeadRequest transferLeadRequest = this.transferLeadRequestRepository
				.create();

		transferLeadRequest.setFromSocialActorId(from.getId());
		transferLeadRequest.setToSocialActorId(to.getId());
		transferLeadRequest.setTransferLeads(new ArrayList<TransferLead>());

		for (LeadDto lead : leads) {
			Lead parentLead = getLeadfromLeadDto(lead);


			
			TransferLead transferLead = transferLeadRepository.create();
			transferLead.setForSocialActorId(getOrganizationFromSocialActor(to)
					.getId());
			
			// Note: FromSocialActorId is actually the ID of the member or organization
			// that this lead represents (if any) and not the social actor that originated
			// the transfer.
			transferLead.setFromSocialActorId(lead.getFromSocialActorId());
			
			transferLead.setLeadStatus(LeadStatusType.Unassigned);
			transferLead.setViewed(Boolean.FALSE);
			transferLead.setParentLead(parentLead);
			transferLead.setTransferState(TransferState.Pending);
			transferLead.setLeadType(LeadType.TransferLead);
			
			transferLead.setCompany(parentLead.getCompany());
			transferLead.setContactEmail(parentLead.getContactEmail());
			transferLead.setContactPhone(parentLead.getContactPhone());
			transferLead.setFirstName(parentLead.getFirstName());
			transferLead.setIndustry(parentLead.getIndustry());
			transferLead.setKeyword(parentLead.getKeyword());
			transferLead.setLastName(parentLead.getLastName());
			transferLead.setLeadPhotoLocation(parentLead.getLeadPhotoLocation());
			transferLead.setLocation(parentLead.getLocation());
			transferLead.setName(parentLead.getName());
			transferLead.setSicCode(parentLead.getSicCode());
			transferLead.setViewed(false);
			
			
			
			transferLeadRequest.getTransferLeads().add(transferLead);
			transferLead.setTransferLeadRequest(transferLeadRequest);

			transferLeadRepository.save(transferLead);
		}
		transferLeadRequest = transferLeadRequestRepository
				.save(transferLeadRequest);
		return null;
	}*/

	/*private SocialActor getSocialActorFromContact(Contact contact) {
		SocialActor socialActor = null;

		socialActor = socialActorRepository.findOne(contact.getId());

		return socialActor;
	}*/

	/*private Lead getLeadfromLeadDto(LeadDto leadDto) {
		Lead retLead = null;

		if (leadDto.getLeadType().equals(LeadType.ClickThroughLead) == true) {
			retLead = this.clickThroughLeadRepository.findOne(leadDto.getId());
		}

		if (leadDto.getLeadType().equals(LeadType.ContactLead) == true) {
			retLead = this.contactLeadRepository.findOne(leadDto.getId());
		}

		if (leadDto.getLeadType().equals(LeadType.SearchLead) == true) {
			retLead = this.searchLeadRepository.findOne(leadDto.getId());
		}

		if (leadDto.getLeadType().equals(LeadType.AcquiredLead) == true) {
			retLead = this.acquiredLeadRepository.findOne(leadDto.getId());
		}

		return retLead;
	}*/
}
