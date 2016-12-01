package com.similan.service.impl;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.client.utils.URIBuilder;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmbeddedIdentity;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.community.SocialPersonExpertise;
import com.similan.domain.entity.community.SocialPersonInterest;
import com.similan.domain.entity.event.MemberLoginEvent;
import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.entity.util.AttributeConstantUtil;
import com.similan.domain.entity.util.MemberFeedback;
import com.similan.domain.entity.util.MemberFeedbackStatus;
import com.similan.domain.entity.util.NonmemberContactMessage;
import com.similan.domain.entity.util.NonmemberContactMessageType;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.common.AddressRepository;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmbeddedIdentityRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonExpertiseRepository;
import com.similan.domain.repository.community.SocialPersonInterestRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.event.MemberLoginEventRepository;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.domain.repository.util.MemberFeedbackRepository;
import com.similan.domain.repository.util.NonmemberContactMessageRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.framework.dto.CommunityEventType;
import com.similan.framework.dto.InviteValidationResult;
import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.MemberInviteListDto;
import com.similan.framework.dto.NonmemberContactMessageInfoDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.MemberInviteInfoDto;
import com.similan.framework.dto.member.MemberExpertiseInfo;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.member.MemberInterestInfo;
import com.similan.framework.dto.util.MemberFeedbackDto;
import com.similan.framework.geo.Geocoder;
import com.similan.framework.importexport.csv.BeanImportingException;
import com.similan.framework.importexport.csv.CsvJavaBeanImporter;
import com.similan.framework.manager.email.EmailManager;
import com.similan.framework.service.GeoCoderService;
import com.similan.framework.util.BeanPropertyUpdator;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResponse;
import com.similan.framework.workflow.api.WorkflowResponseCode;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.api.MemberManagementService;
import com.similan.service.api.MessagingService;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;
import com.similan.service.api.feedback.dto.MemberFeedbackType;
import com.similan.service.api.feedback.dto.NewErrorReportingDto;
import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;
import com.similan.service.exception.ContactAlreadyExistsException;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.exception.MemberManagementServiceException;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallServiceImpl;
import com.similan.service.internal.api.email.EmailParameterConstants;
import com.similan.service.internal.impl.security.component.MemberEnforcer;
import com.similan.service.marshallers.BusinessMarshaller;
import com.similan.service.marshallers.MemberFeedbackMarshaller;
import com.similan.service.marshallers.MemberMarshaller;

@Slf4j
public class MemberManagementServiceImpl implements MemberManagementService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private WorkflowTransientStateRepository transientStateRepository;

	@Autowired
	private MemberLoginEventRepository loginEventRepository;

	@Autowired
	private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;

	@Autowired
	private SocialPersonRepository memberRepository;

	@Autowired
	private SocialOrganizationRepository organizationRepository;

	@Autowired
	private SocialEmbeddedIdentityRepository embeddedIdentityRepository;

	@Autowired
	private WorkflowManager workflowManager;

	@Autowired
	@Qualifier("memberInfoBasicAttributeUpdater")
	private BeanPropertyUpdator memberInfoAttributeUpdater;

	@Autowired
	@Qualifier("memberInfoAccountAttributeUpdater")
	private BeanPropertyUpdator memberInfoAccountAttributeUpdater;

	@Autowired
	@Qualifier("memberBasicAttributeUpdater")
	private BeanPropertyUpdator memberAttributeUpdater;

	@Autowired
	@Qualifier("memberAccountAttributeUpdater")
	private BeanPropertyUpdator memberAccountAttributeUpdater;

	@Autowired
	private Geocoder geocoder;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private GeoCoderService geoCoderService;

	@Autowired
	private SocialPersonInterestRepository socialPersonInterestRepository;

	@Autowired
	private SocialPersonExpertiseRepository socialPersonExpertiseRepository;

	@Autowired
	private SocialEmployeeRepository socialEmployeeRepository;

	@Autowired
	private SocialContactRepository socialContactRepository;

	@Autowired
	private MemberFeedbackRepository feedbackRepository;

	@Autowired
	private EmailManager emailMessagingService;

	@Autowired
	private MemberFeedbackMarshaller memberFeedbackMarshaller;

	@Autowired
	private PlatformCommonSettings commonSettings;

	@Autowired
	private NonmemberContactMessageRepository nonmemberContactMessageRepository;

	@Autowired
	@Qualifier("basicCsvMemberInviteImporter")
	private CsvJavaBeanImporter memberImporter;

	@Autowired
	private MemberMarshaller memberMarshaller;

	@Autowired
	private BusinessMarshaller businessMarshaller;

	@Autowired
	private MemberEnforcer memberEnforcer;

	@Autowired
	private WallServiceImpl wallService;

	@Autowired
	private SocialActorMarshaller actorMarshaller;

	public void deleteMember(Long id) {
		// FIXME: we need to check in future
		// other associations of this member
		// such as connections etc.
	}

	@Transactional
	public WallEntryStatisticsDto getBasicStatistics() {
		return wallService.getBasicStatistics();
	}

	@Override
	public MemberInviteListDto inviteListFromStream(InputStreamReader streamReader) {
		MemberInviteListDto memberInviteList = new MemberInviteListDto();

		if (streamReader != null) {
			memberInviteList.setMemberInvites(new ArrayList<MemberInviteDto>());

			try {
				List<Object> memberList = memberImporter.importFromCsv(streamReader);

				/* iterate through the list and put it in member invite list */
				for (Object obj : memberList) {
					MemberInviteDto memInvite = (MemberInviteDto) obj;

					EmailValidator emailValidator = EmailValidator.getInstance();
					Boolean valid = emailValidator.isValid(memInvite.getEmail());
					if (valid) {
						memInvite.setValidationResult(InviteValidationResult.OK);
					} else {
						memInvite.setValidationResult(InviteValidationResult.INVALID_EMAIL);
					}
					memberInviteList.getMemberInvites().add(memInvite);
				}
				log.info("Number of members uploaded " + memberInviteList.getMemberInvites().size());
			} catch (BeanImportingException bie) {
				throw bie;
			} catch (Exception exp) {
				log.error("Import error in bulk invite ", exp);
			}
		}

		return memberInviteList;
	}

	@Override
	public List<MemberFeedbackDto> getAllMemberFeedbacks(MemberFeedbackStatus status) {
		return null;
	}

	@Override
	@Transactional
	public void sendAdminNotificationMembers(String emailSubject, String emailContent)
			throws ResourceNotFoundException, ParseErrorException, Exception {
		log.info("Sending email to all members " + emailContent);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("emailContent", emailContent);
		parameters.put("emailSubject", emailSubject);

		List<String> memberEmailList = this.memberRepository.getAllMemberEmails();
		for (String toEmailAddr : memberEmailList) {

			this.emailMessagingService.send(commonSettings.getAdminEmailSenderAddress().getValue(), toEmailAddr,
					"memberAdminNotification.vm", parameters);
		}
	}

	@Override
	public List<MemberFeedbackDto> getAllMemberFeedbacks() {
		List<MemberFeedback> allFeedbacks = this.feedbackRepository.findAll();
		return this.memberFeedbackMarshaller.marshallMemberFeedbacks(allFeedbacks);
	}

	private void sendFeedbackEmail(MemberFeedback feedback) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(EmailParameterConstants.FEEDBACK_PROVIDER, feedback.getReporter().getDisplayName());
		parameters.put(EmailParameterConstants.FEEDBACK_MESSAGE, feedback.getMemberFeedback());

		try {
			this.emailMessagingService.send(commonSettings.getAdminEmailSenderAddress().getValue(),
					commonSettings.getPlatformSupportEmailAddress().getValue(), "memberFeedback.vm", parameters);

		} catch (Exception exp) {
			log.error("Failed to send feedback email " + feedback.getMemberFeedback(), exp);
		}
	}

	private void sendFeedbackAcknowledgementEmail(MemberFeedback feedback) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(EmailParameterConstants.FEEDBACK_PROVIDER, feedback.getReporter().getPhotoLocation());
		parameters.put(EmailParameterConstants.FEEDBACK_UUID, feedback.getUuid());
		parameters.put(EmailParameterConstants.FEEDBACK_MESSAGE, feedback.getMemberFeedback());

		try {
			this.emailMessagingService.send(commonSettings.getAdminEmailSenderAddress().getValue(),
					feedback.getReporter().getPrimaryEmail(), "memberFeedbackAcknowledgement.vm", parameters);

		} catch (Exception exp) {
			log.error("Failed to send feedback ack email " + feedback.getMemberFeedback() + " to "
					+ feedback.getReporter().getDisplayName(), exp);
		}

	}

	@Override
	@Transactional
	public void reportProblem(NewErrorReportingDto error) {

		SocialPerson errorReporter = (SocialPerson) actorMarshaller.unmarshallActorKey(error.getReporter(), true);

		MemberFeedback feedback = feedbackRepository.create(error.getMemberFeedback(), errorReporter,
				MemberFeedbackType.ERROR);

		feedbackRepository.save(feedback);

		// send necessary email
		this.sendFeedbackEmail(feedback);
		this.sendFeedbackAcknowledgementEmail(feedback);
	}

	@Override
	@Transactional
	public void addMemberFeedback(Long memberId, String memberFeedback) {

		/* get member feedback repo and save it */
		SocialPerson feedbackProvider = this.memberRepository.findOne(memberId);
		MemberFeedback feedback = feedbackRepository.create(memberFeedback, feedbackProvider,
				MemberFeedbackType.FEEDBACK);

		feedbackRepository.save(feedback);

		// send necessary email
		this.sendFeedbackEmail(feedback);

	}

	@Override
	public List<MemberInviteInfoDto> findPendingInvitesByInviter(Long inviterParamId) {
		List<MemberInviteInfoDto> pendingInviteList = new ArrayList<MemberInviteInfoDto>();

		List<WorkflowTransientState> transStateList = this.transientStateRepository
				.findByType(WorkflowTransientStateType.MemberInvite);
		for (WorkflowTransientState transState : transStateList) {

			WorkflowTransientStateAttribute inviterIdAttr = transState
					.getAttributeByName(ProcessContextParameterConstants.INVITER_ID);

			if (inviterIdAttr != null) {
				Long inviterId = Long.valueOf(inviterIdAttr.getAttributeValue());
				if (inviterId.equals(inviterParamId)) {

					MemberInviteInfoDto inviteInfo = new MemberInviteInfoDto();

					WorkflowTransientStateAttribute inviteeIdAttr = transState
							.getAttributeByName(ProcessContextParameterConstants.MEMBER_ID);

					if (inviteeIdAttr != null) {

						long inviteeId = Long.valueOf(inviteeIdAttr.getAttributeValue());
						SocialPerson invitee = this.memberRepository.findOne(inviteeId);
						inviteInfo.setInviteeFirstName(invitee.getFirstName());
						inviteInfo.setInviteeLastName(invitee.getLastName());
						inviteInfo.setInviteeEmail(invitee.getPrimaryEmail());
					}

					inviteInfo.setTimeStamp(transState.getTimeStamp());
					inviteInfo.setWorkflowInstanceId(transState.getId());

					pendingInviteList.add(inviteInfo);
				}
			}
		}

		return pendingInviteList;
	}

	public boolean inviteInitiate(String inviteeFirstName, String inviteeLastName, String inviteeEmail,
			String inviteeCompany, String memberInviteType, String inviterFirstName, String inviterLastName,
			long memberId) {

		SocialPerson member = memberRepository.findOne(memberId);
		validateInviteeNotAlreadyContact(inviteeEmail, member);

		/*
		 * check whether there is already an invite flow for this member
		 */
		List<WorkflowTransientState> transStateList = this.transientStateRepository
				.findByType(WorkflowTransientStateType.MemberInvite);

		if (transStateList != null) {
			for (WorkflowTransientState transState : transStateList) {
				WorkflowTransientStateAttribute inviterIdAttr = transState
						.getAttributeByName(ProcessContextParameterConstants.INVITER_ID);
				if (inviterIdAttr != null) {
					if (inviterIdAttr.getAttributeValue().equalsIgnoreCase(String.valueOf(memberId))) {
						WorkflowTransientStateAttribute inviteeMailAttr = transState
								.getAttributeByName(ProcessContextParameterConstants.INVITEE_EMAIL);
						if (inviteeMailAttr != null) {
							if (inviteeMailAttr.getAttributeValue().equalsIgnoreCase(inviteeEmail)) {
								throw new MemberManagementServiceException(
										"For this member already an INVITE process ");
							}
						}
					}
				}
			}
		}

		SocialPerson inviteePerson = this.memberRepository.findByEmailAndState(inviteeEmail, MemberState.Active);

		if (inviteePerson != null) {

			/*
			 * this is an existing member so just set the values correctly
			 */
			inviteeFirstName = inviteePerson.getFirstName();
			inviteeLastName = inviteePerson.getLastName();
		}

		/*
		 * 1. Get the invite flow from the business process repository 2. Then
		 * trigger the flow with the input parameters 3. Return the status
		 */
		BusinessProcessDefinition inviteProcess = this.businessProcessDefinitionRepository.findByName("memberInvite");
		log.info("Member sign up with business process " + inviteProcess.getName());

		WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
		initialRequest.setFlowDefinitionName(inviteProcess.getName());

		/* set the parameters */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ProcessContextParameterConstants.INVITEE_FIRSTNAME, inviteeFirstName);
		parameters.put(ProcessContextParameterConstants.INVITEE_LASTNAME, inviteeLastName);
		parameters.put(ProcessContextParameterConstants.INVITEE_EMAIL, inviteeEmail);
		parameters.put(ProcessContextParameterConstants.INVITEE_PASSWORD, "123@br2011");
		parameters.put(ProcessContextParameterConstants.BUSINESS_NAME, inviteeCompany);

		parameters.put(ProcessContextParameterConstants.TO_EMAIL, inviteeEmail);
		parameters.put(ProcessContextParameterConstants.MEMBER_INVITE_TYPE, memberInviteType);
		parameters.put(ProcessContextParameterConstants.INVITER_FIRSTNAME, inviterFirstName);
		parameters.put(ProcessContextParameterConstants.INVITER_LASTNAME, inviterLastName);
		parameters.put(ProcessContextParameterConstants.INVITER_ID, new Long(memberId));

		/* set the parameters */
		initialRequest.setExecutionParameters(parameters);

		/* execute the workflow */
		WorkflowResponse wfResponse = this.workflowManager.initiate(initialRequest);

		if (wfResponse != null) {

			if (wfResponse.getResponseCode().equals(WorkflowResponseCode.Success) == true) {
				return true;
			} else {
				return false;
			}

		}

		return false;
	}

	private void validateInviteeNotAlreadyContact(String inviteeEmail, SocialActor member) {
		// FIXME it is a duplicate method in OrganizationManagementService
		// should be consolidated
		String primaryEmail = StringUtils.EMPTY;
		if (member instanceof SocialPerson) {
			primaryEmail = ((SocialPerson) member).getPrimaryEmail();
		} else if (member instanceof SocialOrganization) {
			primaryEmail = ((SocialOrganization) member).getPrimaryEmail();
		}
		if (primaryEmail.equalsIgnoreCase(inviteeEmail) == true) {
			log.info("Invitee and inviter are the same member thus cannot be connected " + inviteeEmail);
			throw new ContactAlreadyExistsException(primaryEmail);
		}

		List<SocialContact> contactsFrom = this.socialContactRepository.findByContactFrom(member);
		for (SocialContact contact : contactsFrom) {
			SocialActor actor = contact.getContactTo();
			String email = null;
			if (actor != null) {
				if (actor instanceof SocialPerson)
					email = ((SocialPerson) actor).getPrimaryEmail();
				else if (actor instanceof SocialOrganization)
					email = ((SocialOrganization) actor).getPrimaryEmail();
			}
			if (email != null && email.equals(inviteeEmail))
				throw new ContactAlreadyExistsException(email);
		}

		List<SocialContact> contactsTo = this.socialContactRepository.findByContactTo(member);
		for (SocialContact contact : contactsTo) {
			SocialActor actor = contact.getContactFrom();
			String email = null;
			if (actor != null) {
				if (actor instanceof SocialPerson)
					email = ((SocialPerson) actor).getPrimaryEmail();
				else if (actor instanceof SocialOrganization)
					email = ((SocialOrganization) actor).getPrimaryEmail();
			}
			if (email != null && email.equals(inviteeEmail))
				throw new ContactAlreadyExistsException(email);
		}
	}

	private void updateInterest(MemberInfoDto memberInfo, SocialPerson member) {
		List<MemberInterestInfo> interestList = memberInfo.getInterest();

		if (interestList != null) {
			List<SocialPersonInterest> newList = new ArrayList<SocialPersonInterest>();

			for (MemberInterestInfo memberInterestInfo : interestList) {
				/* adding the ones which are newly created */
				if (memberInterestInfo.getId() == Long.MIN_VALUE) {
					log.info("Adding new interest " + memberInterestInfo.getInterest());
					SocialPersonInterest interest = this.memberRepository.createInterest();
					log.info("New interest added " + memberInterestInfo.getInterest());

					interest.setInterest(memberInterestInfo.getInterest());
					this.socialPersonInterestRepository.save(interest);

					/* setting the id to avoid duplication */
					memberInterestInfo.setId(interest.getId());
					newList.add(interest);
				} else {

					/* updating a social interest */
					for (SocialPersonInterest socialInterest : member.getInterest()) {
						if (memberInterestInfo.getId() == socialInterest.getId()) {
							if (memberInterestInfo.getInterest()
									.equalsIgnoreCase(socialInterest.getInterest()) == false) {
								socialInterest.setInterest(memberInterestInfo.getInterest());
							}
							newList.add(socialInterest);
						}
					}
				}
			}

			/* deleting a interest */
			for (MemberInterestInfo memberInterestInfo : memberInfo.getDeletedInterestInfo()) {

				if (memberInterestInfo.getId() != 0) {
					for (SocialPersonInterest socialInterest : member.getInterest()) {
						if (socialInterest.getId() == memberInterestInfo.getId()) {

							log.info("deleting interest " + socialInterest.getId());
							this.socialPersonInterestRepository.delete(socialInterest.getId());
						}
					}
				}
			}
			member.setInterest(newList);
			memberInfo.getDeletedInterestInfo().clear();
		}
	}

	private void updateExpertise(MemberInfoDto memberInfo, SocialPerson member) {

		/* expertise */
		List<MemberExpertiseInfo> expertiseList = memberInfo.getExpertise();

		if (expertiseList != null) {
			List<SocialPersonExpertise> newList = new ArrayList<SocialPersonExpertise>();
			for (MemberExpertiseInfo memberExpertiseInfo : expertiseList) {

				/* adding the ones which are newly created */
				if (memberExpertiseInfo.getId() == Long.MIN_VALUE) {

					log.info("Adding new expertise " + memberExpertiseInfo.getExpertise());
					SocialPersonExpertise expertise = this.memberRepository.createExpertise();
					expertise.setExpertise(memberExpertiseInfo.getExpertise());
					this.socialPersonExpertiseRepository.save(expertise);
					memberExpertiseInfo.setId(expertise.getId());

					newList.add(expertise);
				} else {

					/* updating the existing ones */
					for (SocialPersonExpertise socialExpertise : member.getExpertise()) {

						if (memberExpertiseInfo.getId() == socialExpertise.getId()) {
							log.info("Updating expertise " + socialExpertise.getId());

							if (memberExpertiseInfo.getExpertise()
									.contentEquals(socialExpertise.getExpertise()) == false) {
								socialExpertise.setExpertise(memberExpertiseInfo.getExpertise());
							}
							newList.add(socialExpertise);
						}
					}
				}
			}

			/* Now removing the ones which are not there in member info list */
			log.info("deleted expertise info list " + memberInfo.getDeletedExpertiseInfo().size());
			for (MemberExpertiseInfo memberExpertiseInfo : memberInfo.getDeletedExpertiseInfo()) {

				if (memberExpertiseInfo.getId() != 0) {
					for (SocialPersonExpertise socialExpertise : member.getExpertise()) {
						if (socialExpertise.getId() == memberExpertiseInfo.getId()) {

							log.info("deleing expertise " + socialExpertise.getId());
							this.socialPersonExpertiseRepository.delete(socialExpertise.getId());
						}
					}
				}
			}
			member.setExpertise(newList);
			memberInfo.getDeletedExpertiseInfo().clear();

		}
	}

	private void updateEmployer(MemberInfoDto memberInfo, SocialPerson member) {

		/*
		 * Case-1: A new business entity as employer Case-2: An existing
		 * business entity as employer Case-3: Employer has changed and a new
		 * business entity Case-4: Employer has changed and an existing business
		 * entity
		 */

		/* an organization has been selected as employer */
		if (memberInfo.getSelectedOrgTag() != null) {

			SocialOrganization socialOrg = null;

			/* This is a new organization name that has been created */
			if (memberInfo.getSelectedOrgTag().getId() == Long.MIN_VALUE) {

				/* it is an new org so create a new one and associate them */
				log.info("New organization would be created as employer ");
				String uniqueName = UUID.randomUUID().toString();
				socialOrg = this.organizationRepository.create(OrganizationType.RESELLER, uniqueName);
				socialOrg.setBusinessName(memberInfo.getSelectedOrgTag().getName());
				socialOrg.setVisibilityType(SocialMemberVisibilityType.NotVisible);
				this.organizationRepository.save(socialOrg);

				if (memberInfo.getEmployer() != null) {
					// SocialOrganization socialOrgOld =
					// this.organizationRepository
					// .findOne(memberInfo.getEmployer().getId());
					// this.getOrganizationRepository().deleteRelationshipBetween(socialOrgOld,
					// member, "EMPLOYEE");
				}

				this.organizationRepository.createEmployee(socialOrg, member, SocialEmployeeType.Admin);

			} else {

				/*
				 * we have chosen an existing organization as employer question
				 * is whether it is a replacement of an existing employer or new
				 * employer
				 */

				socialOrg = this.organizationRepository.findOne(memberInfo.getSelectedOrgTag().getId());

				if (memberInfo.getEmployer() != null) {

					if (memberInfo.getSelectedOrgTag().getId() != memberInfo.getEmployer().getId()) {
						log.info("The employer has been changed to " + memberInfo.getSelectedOrgTag().getId()
								+ " from employer " + memberInfo.getEmployer().getId());

						SocialOrganization socialOrgOld = this.organizationRepository
								.findOne(memberInfo.getEmployer().getId());
						this.socialEmployeeRepository.delete(socialOrgOld, member);

						log.info("Found replacing org " + socialOrg.getId());
						this.organizationRepository.createEmployee(socialOrg, member, SocialEmployeeType.Admin);

					}
				} else {
					log.info("creating new employer with an existing business entity ");
					this.organizationRepository.createEmployee(socialOrg, member, SocialEmployeeType.Admin);
				}

			}

			if (socialOrg != null) {

				this.organizationRepository.save(socialOrg);
				if (memberInfo.getSelectedOrgTag().getId() == Long.MIN_VALUE) {
					memberInfo.getSelectedOrgTag().setId(socialOrg.getId());
					memberInfo.setEmployer(memberInfo.getSelectedOrgTag());
				}
			}
		}
	}

	private void updateMemberAddress(MemberInfoDto memberInfo, SocialPerson member) throws Exception {

		this.memberAccountAttributeUpdater.update(memberInfo, member.getMemberAccount());

		Address address = this.geoCoderService.getVerifiedAddressFromDto(memberInfo.getAddressDto());

		/* only for existing address address */
		if (memberInfo.getAddressDto().getId() != Long.MIN_VALUE) {
			Iterator<Address> iterator = member.getAddresses().iterator();
			while (iterator.hasNext()) {
				Address addr = iterator.next();
				if (addr.getId() == memberInfo.getAddressDto().getId()) {
					iterator.remove();
					log.error("Removing address with ID " + addr.getId());
					this.addressRepository.delete(addr.getId());
				}
			}
		}

		/* add the address to the addr list */
		member.getAddresses().add(address);
		memberInfo.getAddressDto().setId(address.getId());
	}

	/**
	 * 
	 * @param memberInfo
	 * @throws Exception
	 */
	public void memberUpdate(MemberInfoDto memberInfo) throws Exception {
		if (memberInfo == null) {
			throw new CoreServiceException("Cannot save NULL member");
		}

		if (StringUtils.isEmpty(memberInfo.getEmail()) == true) {
			throw new CoreServiceException("Cannot save member with invalid email address");
		} else {
			StringUtils.strip(memberInfo.getEmail());
			if (EmailValidator.getInstance().isValid(memberInfo.getEmail()) != true) {
				throw new CoreServiceException(
						"Cannot save member with invalid email address " + memberInfo.getEmail());
			}
		}

		log.info(" Member first name " + memberInfo.getFirstName());

		/*
		 * 1. Get the member 2. Compare the values 3. If changed then update 4.
		 * create activity
		 */

		SocialPerson member = this.memberRepository.findOne(memberInfo.getId());

		memberEnforcer.update(member).checkAllowed();
		if (member != null) {

			/* later we should write a bean value comparator */
			if (member.getMemberAccount() == null) {
				member.setMemberAccount(new Account());
			}

			this.memberAttributeUpdater.update(memberInfo, member);
			this.memberAccountAttributeUpdater.update(memberInfo, member.getMemberAccount());

			/* update address */
			this.updateMemberAddress(memberInfo, member);

			/* expertise update */
			this.updateExpertise(memberInfo, member);

			/* Interest update */
			this.updateInterest(memberInfo, member);

			/* update employer */
			this.updateEmployer(memberInfo, member);

			this.memberRepository.save(member);

		}
	}

	/**
	 * 
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<MemberInfoDto> getMembers(int number) throws Exception {

		List<MemberInfoDto> memberList = new ArrayList<MemberInfoDto>();
		List<SocialPerson> socialPersonList = this.memberRepository.findAll();

		for (SocialPerson person : socialPersonList) {

			try {
				MemberInfoDto memberInfo = this.getMemberInfoFromMemberBasic(person);
				memberList.add(memberInfo);
			} catch (Exception exp) {
				log.error("Cannot get member", exp);
			}
		}

		return memberList;
	}

	/**
	 * 
	 * @param member
	 * @return
	 * @throws Exception
	 */
	private MemberInfoDto getMemberInfoFromMemberBasic(SocialPerson member) throws Exception {

		MemberInfoDto memberInfo = new MemberInfoDto();

		this.memberInfoAttributeUpdater.update(member, memberInfo);

		List<Address> addresses = member.getAddresses();
		if (!addresses.isEmpty()) {
			Address address = addresses.iterator().next();
			memberInfo.setCity(address.getCity());
			memberInfo.setStreet(address.getNumber() == null ? null : address.getNumber() + " " + address.getStreet());
			memberInfo.setState(address.getState());
			log.info("State retrieved " + memberInfo.getState());

			memberInfo.setCountry(address.getCountry());
			memberInfo.setZipCode(address.getZipCode());
		}

		if (member.getMemberAccount() != null) {
			this.memberInfoAccountAttributeUpdater.update(member.getMemberAccount(), memberInfo);
		}

		return memberInfo;
	}

	/**
	 * 
	 * @param member
	 * @return
	 * @throws Exception
	 */
	private MemberInfoDto getMemberInfoFromMember(SocialPerson member) throws Exception {

		MemberInfoDto memberInfo = new MemberInfoDto();
		memberInfo.setName(member.getName());

		/* check whether it has employee and admin role */
		SocialEmployee employee = null;

		try {
			employee = member.getEmployer();
			log.info("Employer " + employee);
		} catch (Exception exp) {
			log.error("cannot get employer ", exp);
		}

		if (employee != null) {

			SocialOrganization employer = (SocialOrganization) employee.getContactFrom();
			log.info("Employer found id " + employee.getId() + " business name " + employer.getBusinessName() + " id "
					+ employer.getId());

			SocialEmployeeType employeeType = employee.getEmployeeType();
			memberInfo.setMemberOrgAssociationRole(employeeType);

			OrganizationBasicInfoDto employerOrg = new OrganizationBasicInfoDto();
			employerOrg.setId(employer.getId());
			employerOrg.setName(employer.getBusinessName());
			employerOrg.setLogoLocation(employer.getLogoUrl());
			employerOrg.setMemberVisibilityType(employer.getVisibilityType());
			log.info("Employer visibility : " + employer.getVisibilityType());

			memberInfo.setEmployer(employerOrg);
			memberInfo.setSelectedOrgTag(employerOrg);
		}

		this.memberInfoAttributeUpdater.update(member, memberInfo);

		List<Address> addresses = member.getAddresses();
		if (!addresses.isEmpty()) {

			Address address = addresses.iterator().next();
			memberInfo.getAddressDto().setCity(address.getCity());
			memberInfo.getAddressDto()
					.setStreet(address.getNumber() == null ? null : address.getNumber() + " " + address.getStreet());
			memberInfo.getAddressDto().setState(address.getState());
			log.info("State retrieved " + memberInfo.getState());

			memberInfo.getAddressDto().setCountry(address.getCountry());
			memberInfo.getAddressDto().setZipCode(address.getZipCode());
			memberInfo.getAddressDto().setLatitude(address.getLatitude());
			memberInfo.getAddressDto().setLongitude(address.getLongitude());
			memberInfo.getAddressDto().setId(address.getId());
		}

		if (member.getMemberAccount() != null) {
			this.memberInfoAccountAttributeUpdater.update(member.getMemberAccount(), memberInfo);
		}

		/* copy expertise and interest */
		List<SocialPersonInterest> personalInterests = member.getInterest();

		if (personalInterests != null) {

			for (SocialPersonInterest personalInterest : personalInterests) {
				MemberInterestInfo interestInfo = new MemberInterestInfo();
				interestInfo.setId(personalInterest.getId());
				interestInfo.setInterest(personalInterest.getInterest());

				memberInfo.getInterest().add(interestInfo);
			}
		}

		List<SocialPersonExpertise> personalExpertise = member.getExpertise();

		if (personalExpertise != null) {

			for (SocialPersonExpertise expertise : personalExpertise) {
				MemberExpertiseInfo expertiseInfo = new MemberExpertiseInfo();
				expertiseInfo.setId(expertise.getId());
				expertiseInfo.setExpertise(expertise.getExpertise());
				memberInfo.getExpertise().add(expertiseInfo);
			}
		}

		return memberInfo;
	}

	/**
	 * 
	 * @param memberInfo
	 * @return
	 * @throws CoreServiceException
	 */
	public MemberInfoDto memberLogin(String email, String password) throws CoreServiceException {

		MemberInfoDto memberInfo = null;

		log.info("Member login for user name :" + email + " member password " + password);
		/**
		 * 1. Get the member by user name where user name should be unique. To
		 * expedite this processs we need an inmemory map of cached member Id
		 * and User Name 2. Then check credentials : if matches user is logged
		 * in
		 */
		SocialPerson member = null;

		try {
			member = this.memberRepository.findByEmail(email);
		} catch (Exception exp) {
			log.error("Member not found with email " + email, exp);
			throw new CoreServiceException(CoreServiceErrorCode.MEMBER_EMAIL_NOT_FOUND, exp);
		}

		if (member != null) {

			if (member.getMemberAccount().getPassword().contentEquals(password) == true
					&& member.getState() == MemberState.Active) {

				try {
					memberInfo = this.getMemberInfoFromMember(member);
					memberInfo.setLogged(true);
					return memberInfo;
				} catch (Exception exp) {
					log.error("Cannot create member info from social member ", exp);
					throw new CoreServiceException(CoreServiceErrorCode.MEMBER_INFO_CREATION_FAILED, exp);
				}
			} else {
				throw new CoreServiceException(CoreServiceErrorCode.MEMBER_INCORRECT_CREDENTIAL);
			}
		} else {
			throw new CoreServiceException(CoreServiceErrorCode.MEMBER_EMAIL_NOT_FOUND);
		}
	}

	/**
	 * 
	 * @param uuid
	 * @return memberInfo
	 * @throws CoreServiceException
	 */
	public MemberInfoDto embeddedLogin(String uuid) throws CoreServiceException {

		MemberInfoDto memberInfo = null;

		log.info("Embedded login for user uuid :" + uuid);

		SocialActor member = null;

		try {

			SocialEmbeddedIdentity embeddedMemberIdentity = this.embeddedIdentityRepository.findEmbeddedIdentity(uuid);
			member = embeddedMemberIdentity.getEmbeddedActor();

			if (member == null) {
				throw new CoreServiceException(CoreServiceErrorCode.MEMBER_UUID_NOT_FOUND);
			}
		} catch (Exception exp) {
			log.error("Member not found with uuid " + uuid, exp);
			throw new CoreServiceException(CoreServiceErrorCode.MEMBER_UUID_NOT_FOUND, exp);
		}

		if (member instanceof SocialPerson) {
			try {
				memberInfo = this.getMemberInfoFromMember((SocialPerson) member);
				memberInfo.setLogged(false);
				memberInfo.setEmbeddedMode(true);
				return memberInfo;
			} catch (Exception exp) {
				throw new CoreServiceException(CoreServiceErrorCode.MEMBER_UUID_NOT_FOUND, exp);
			}
		} else {
			throw new CoreServiceException(CoreServiceErrorCode.MEMBER_UUID_NOT_FOUND);
		}
	}

	public List<MemberInfoDto> getAllEmbeddedMembers() {

		List<MemberInfoDto> memberInfoList = new ArrayList<MemberInfoDto>();
		List<SocialEmbeddedIdentity> embeddedIdentityList = this.embeddedIdentityRepository.findAll();

		for (SocialEmbeddedIdentity socialEmbeddedIdentity : embeddedIdentityList) {

			/*
			 * for now catching the exception since many of the members are
			 * dangling
			 */
			try {
				SocialActor socialActor = socialEmbeddedIdentity.getEmbeddedActor();
				if (socialActor instanceof SocialPerson) {
					MemberInfoDto memberInfo = this.getMemberInfoFromMemberBasic((SocialPerson) socialActor);
					memberInfoList.add(memberInfo);
				}
			} catch (Exception exp) {
				log.error("Cannot create member info from embedded identity", exp);
			}
		}

		return memberInfoList;
	}

	public boolean memberEmailChangeInitiate(MemberInfoDto member) throws CoreServiceException {

		SocialPerson newmember = null;

		try {
			newmember = this.memberRepository.findByEmail(member.getNewEmail());
		} catch (Exception exp) {
			// Ignore
		}

		if (newmember == null) {
			SocialPerson memberEntity = this.memberRepository.findOne(member.getId());
			memberEnforcer.changeEmail(memberEntity).checkAllowed();

			BusinessProcessDefinition emailChangeProcess = this.businessProcessDefinitionRepository
					.findByName("primaryEmailChange");

			WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
			initialRequest.setFlowDefinitionName(emailChangeProcess.getName());

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME, member.getFirstName());
			parameters.put(ProcessContextParameterConstants.MEMBER_LASTNAME, member.getLastName());
			parameters.put(ProcessContextParameterConstants.MEMBER_ID, Long.valueOf(member.getId()));
			parameters.put(ProcessContextParameterConstants.MEMBER_CHANGE_NEW_EMAIL, member.getNewEmail());

			/* set the parameters */
			initialRequest.setExecutionParameters(parameters);

			/* execute the workflow */
			WorkflowResponse wfResponse = this.workflowManager.initiate(initialRequest);

			if (wfResponse != null) {

				if (wfResponse.getResponseCode().equals(WorkflowResponseCode.Success) == true) {
					return true;
				} else {
					return false;
				}

			}
		} else {
			throw new CoreServiceException(CoreServiceErrorCode.DUPLICATE_PRIMARY_EMAIL_FOUND);
		}
		return false;
	}

	public boolean memberEmailChangeComplete(long memberId, String pid) {

		/* get the member object */
		boolean retValue = false;

		SocialPerson member = this.memberRepository.findOne(memberId);

		if (member != null) {

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.MEMBER_ID, Long.valueOf(memberId));

			/*
			 * this is a hack to ward off the execption from deleting the JBPM
			 * work flow
			 */
			try {

				WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
				resumeRequest.setExecutionParameters(parameters);
				resumeRequest.setWorkflowInstanceId(pid);
				this.workflowManager.resume(resumeRequest);

			} catch (Exception exp) {
				log.error("Error in work flow signalling ", exp);
			}

			retValue = true;

		}

		return retValue;
	}

	public boolean memberPasswordChange(MemberInfoDto member) {
		SocialPerson memberEntity = memberRepository.findOne(member.getId());
		memberEnforcer.changePassword(memberEntity).checkAllowed();

		BusinessProcessDefinition passwordChangeProcess = this.businessProcessDefinitionRepository
				.findByName("passwordChange");

		log.info("The password change process is found " + passwordChangeProcess.getName() + " and id "
				+ passwordChangeProcess.getId());

		WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
		initialRequest.setFlowDefinitionName(passwordChangeProcess.getName());

		/* set the parameters */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME, member.getFirstName());
		parameters.put(ProcessContextParameterConstants.MEMBER_LASTNAME, member.getLastName());
		parameters.put(ProcessContextParameterConstants.MEMBER_ID, Long.valueOf(member.getId()));
		parameters.put(ProcessContextParameterConstants.MEMBER_CHANGE_PASSWORD, member.getNewPassword());
		parameters.put(ProcessContextParameterConstants.PASSWORD, member.getPassword());

		/* set the parameters */
		initialRequest.setExecutionParameters(parameters);

		/* execute the workflow */
		this.workflowManager.initiateWithoutResponse(initialRequest);

		return true;
	}

	public MemberInfoDto memberById(Long id) throws Exception {

		SocialPerson member = this.memberRepository.findOne(id);
		MemberInfoDto info = this.getMemberInfoFromMember(member);
		return info;
	}

	public SocialPerson memberByEmail(String email) {
		SocialPerson member = this.memberRepository.findByEmail(email);
		return member;
	}

	public List<MemberInfoDto> getOnlineMembers() {
		List<MemberInfoDto> memberList = new ArrayList<MemberInfoDto>();
		List<MemberLoginEvent> loginEvents = this.loginEventRepository.findLoggerInMemberEvents();

		for (MemberLoginEvent loginEvent : loginEvents) {

			try {
				SocialPerson socialPerson = this.memberRepository.findOne(loginEvent.getEventGenerator());
				MemberInfoDto memberInfo = this.getMemberInfoFromMemberBasic(socialPerson);
				memberList.add(memberInfo);
			} catch (Exception exp) {
				log.error("Failure to get member from login event", exp);
			}
		}

		return memberList;
	}

	public List<CommunityEventDto> getLoginInfoList(Long id) {

		List<CommunityEventDto> loginEventList = new ArrayList<CommunityEventDto>();
		List<MemberLoginEvent> eventList = this.loginEventRepository.findEventsByMember(id);

		for (MemberLoginEvent memberLoginEvent : eventList) {

			CommunityEventDto commEvent = new CommunityEventDto();
			commEvent.setLoginInfo(memberLoginEvent.getLoginInfo());
			commEvent.setEventGenerationTime(memberLoginEvent.getEventGenerationTime());
			commEvent.setEventType(CommunityEventType.MemberLoginEvent);
			loginEventList.add(commEvent);
		}

		return loginEventList;
	}

	public boolean forgotPasswordInitiate(String forgotPasswordEmail) throws CoreServiceException {

		SocialPerson member = this.memberRepository.findByEmailAndState(forgotPasswordEmail, MemberState.Active);

		if (member == null) {
			throw new CoreServiceException(CoreServiceErrorCode.MEMBER_EMAIL_NOT_FOUND);
		} else {
			BusinessProcessDefinition forgotPasswordProcess = this.businessProcessDefinitionRepository
					.findByName("forgotPassword");

			WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
			initialRequest.setFlowDefinitionName(forgotPasswordProcess.getName());

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME, member.getFirstName());
			parameters.put(ProcessContextParameterConstants.MEMBER_LASTNAME, member.getLastName());
			parameters.put(ProcessContextParameterConstants.MEMBER_ID, member.getId());

			/* set the parameters */
			initialRequest.setExecutionParameters(parameters);

			/* execute the workflow */
			WorkflowResponse wfResponse = this.workflowManager.initiate(initialRequest);

			if (wfResponse != null) {

				if (wfResponse.getResponseCode().equals(WorkflowResponseCode.Success) == true) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

	}

	public boolean memberChangeForgottenPasswordComplete(MemberInfoDto memberInfo, String pid) {
		/* get the member object */
		boolean retValue = false;

		if (memberInfo != null) {

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.MEMBER_ID, Long.valueOf(memberInfo.getId()));
			parameters.put(ProcessContextParameterConstants.TO_EMAIL, memberInfo.getEmail());
			parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME, memberInfo.getFirstName());
			parameters.put(ProcessContextParameterConstants.MEMBER_CHANGE_PASSWORD, memberInfo.getNewPassword());
			parameters.put(ProcessContextParameterConstants.PASSWORD, memberInfo.getPassword());
			/*
			 * this is a hack to ward off the execption from deleting the JBPM
			 * work flow
			 */
			WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
			resumeRequest.setExecutionParameters(parameters);
			resumeRequest.setWorkflowInstanceId(pid);
			this.workflowManager.resume(resumeRequest);

			retValue = true;

		}

		return retValue;

	}

	public List<SocialPerson> findAllMembers() {
		return this.memberRepository.findAll();
	}

	public void memberInviteSignupInputComplete(MemberInfoDto member, String pid) {

		log.info("In memberInviteSignupInput");

		WorkflowTransientState tranState = this.transientStateRepository.findByMemberIdAndType(member.getId(),
				WorkflowTransientStateType.MemberInvite);

		if (tranState != null) {
			List<WorkflowTransientStateAttribute> attributes = tranState.getAttributes();
			if (attributes == null) {
				attributes = new ArrayList<WorkflowTransientStateAttribute>();
			}

			WorkflowTransientStateAttribute firstNameAttr = transientStateRepository.createAttribute();
			firstNameAttr.setAttributeName(ProcessContextParameterConstants.MEMBER_FIRSTNAME);
			firstNameAttr.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			firstNameAttr.setAttributeValue(member.getFirstName());
			attributes.add(firstNameAttr);

			WorkflowTransientStateAttribute lastNameAttr = transientStateRepository.createAttribute();
			lastNameAttr.setAttributeName(ProcessContextParameterConstants.MEMBER_LASTNAME);
			lastNameAttr.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			lastNameAttr.setAttributeValue(member.getLastName());
			attributes.add(lastNameAttr);

			WorkflowTransientStateAttribute passwordNameAttr = transientStateRepository.createAttribute();
			passwordNameAttr.setAttributeName(ProcessContextParameterConstants.PASSWORD);
			passwordNameAttr.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			passwordNameAttr.setAttributeValue(member.getPassword());
			attributes.add(passwordNameAttr);

			WorkflowTransientStateAttribute companyAttr = transientStateRepository.createAttribute();
			companyAttr.setAttributeName(ProcessContextParameterConstants.BUSINESS_NAME);
			companyAttr.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			companyAttr.setAttributeValue(member.getCompany());
			attributes.add(companyAttr);

			WorkflowTransientStateAttribute emailAttr = transientStateRepository.createAttribute();
			emailAttr.setAttributeName(ProcessContextParameterConstants.EMAIL);
			emailAttr.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
			emailAttr.setAttributeValue(member.getEmail());
			attributes.add(emailAttr);

			tranState.setAttributes(attributes);
			transientStateRepository.save(tranState);

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.MEMBER_ID, member.getId());
			parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME, member.getFirstName());
			parameters.put(ProcessContextParameterConstants.MEMBER_LASTNAME, member.getLastName());
			parameters.put(ProcessContextParameterConstants.EMAIL, member.getEmail());
			parameters.put(ProcessContextParameterConstants.TO_EMAIL, member.getEmail());

			WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
			resumeRequest.setExecutionParameters(parameters);
			resumeRequest.setWorkflowInstanceId(pid);
			log.info("Restarting workflow");
			this.workflowManager.resume(resumeRequest);
			log.info("Starting resumed");
		}
	}

	public void existingMemberAcceptInviteDecision(long memberId, String processId) {
		existingMemberInviteDecision(memberId, processId, true);
	}

	public void existingMemberRejectInviteDecision(long memberId, String processId) {

		existingMemberInviteDecision(memberId, processId, false);
	}

	public void existingMemberInviteDecision(long memberId, String processId, boolean accept) {
		/* set the parameters */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
		if (accept)
			parameters.put(ProcessContextParameterConstants.MEMBER_INVITE_DECISION, "Accepted");
		else
			parameters.put(ProcessContextParameterConstants.MEMBER_INVITE_DECISION, "Rejected");
		WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
		resumeRequest.setExecutionParameters(parameters);
		resumeRequest.setWorkflowInstanceId(processId);
		this.workflowManager.resume(resumeRequest);
	}

	public MemberInfoDto isMemberWithEmailExists(String email) {
		SocialPerson person = memberRepository.findByEmail(email);

		if (person != null) {
			MemberInfoDto member = this.memberMarshaller.marshallMember(person);

			// get the employers
			List<SocialEmployee> employees = this.socialEmployeeRepository.findByContactTo(person);

			if (employees != null && employees.size() > 0) {

				SocialOrganization employer = (SocialOrganization) employees.get(0).getContactFrom();
				OrganizationBasicInfoDto employerDto = this.businessMarshaller.marshallBusiness(employer);
				member.setEmployer(employerDto);
			}

			return member;
		} else
			return null;
	}

	public void shareComment(MemberInfoDto memberInfo, String shareComment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reInviteMember(Long inviteRequestId) {
		log.info("Invite request id " + inviteRequestId);
		SocialPerson inviter = this.memberRepository.findOne(inviteRequestId);

		WorkflowTransientState inviteRequest = this.transientStateRepository.findOne(inviteRequestId);

		if (inviteRequest != null) {
			// check it is of type member invite
			WorkflowTransientStateType stateType = inviteRequest.getStateType();
			log.info("Invite request type " + stateType);

			if (stateType.equals(WorkflowTransientStateType.MemberInvite)) {
				/*
				 * lets get all the necessary params to initiate reinvite delete
				 * the currentt one issue reinvite inviteInitiate(String
				 * inviteeFirstName, String inviteeLastName, String
				 * inviteeEmail, String inviteeCompany, String memberInviteType,
				 * String inviterFirstName, String inviterLastName, long
				 * memberId)
				 */
				WorkflowTransientStateAttribute inviteeIdAttr = inviteRequest
						.getAttributeByName(ProcessContextParameterConstants.MEMBER_ID);
				String inviteeId = inviteeIdAttr.getAttributeValue();

				SocialPerson invitee = this.memberRepository.findOne(Long.valueOf(inviteeId));

				WorkflowTransientStateAttribute memberInviteTypeAttr = inviteRequest
						.getAttributeByName(ProcessContextParameterConstants.MEMBER_INVITE_TYPE);
				String memberInviteType = memberInviteTypeAttr.getAttributeValue();

				this.transientStateRepository.delete(inviteRequest);
				this.memberRepository.delete(invitee.getId());

				this.inviteInitiate(invitee.getFirstName(), invitee.getLastName(), invitee.getPrimaryEmail(),
						invitee.getCompany(), memberInviteType, inviter.getFirstName(), inviter.getLastName(),
						inviter.getId());

			}
		}

	}

	@Override
	public void deleteInviteRequest(Long inviteRequestId) {
		log.info("Invite request id " + inviteRequestId);
		WorkflowTransientState inviteRequest = this.transientStateRepository.findOne(inviteRequestId);

		if (inviteRequest != null) {
			// check it is of type member invite
			WorkflowTransientStateType stateType = inviteRequest.getStateType();
			log.info("Invite request type " + stateType);

			if (stateType.equals(WorkflowTransientStateType.MemberInvite)) {

				WorkflowTransientStateAttribute inviteeIdAttr = inviteRequest
						.getAttributeByName(ProcessContextParameterConstants.MEMBER_ID);
				String inviteeId = inviteeIdAttr.getAttributeValue();

				SocialPerson invitee = this.memberRepository.findOne(Long.valueOf(inviteeId));
				this.memberRepository.delete(invitee.getId());
				this.transientStateRepository.delete(inviteRequest);
			}
		}

	}

	@Transactional
	@Override
	public void memberRequestToJoin(String firstName, String lastName, String email, String businessName) {
		// just send the member sign up page via email and initiate the work
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("firstName", firstName);
		parameters.put("lastName", lastName);
		parameters.put("email", email);
		parameters.put("business", businessName);

		// define the join URL
		String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

		try {
			URIBuilder uri = new URIBuilder(hostUrl + "member/memberSignup.xhtml");
			parameters.put("joinUrl", uri.toString());

			this.emailMessagingService.send(email,
					commonSettings.getPlatformSupportEmailAddress().getValue(), "memberJoinNotification.vm", parameters);
		} catch (Exception exp) {
			log.error("Failed to send email " + email, exp);
		}
	}

	@Transactional
	@Override
	public void contactUs(NonmemberContactMessageInfoDto message) {

		NonmemberContactMessage contactMessage = nonmemberContactMessageRepository.create(message.getSubject(),
				message.getEmail(), message.getComment());

		if (message.equals(NonmemberContactMessageType.MoreInfoRequest)) {
			contactMessage.setFirstName(message.getFirstName());
			contactMessage.setLastName(message.getLastName());
			contactMessage.setMessageType(NonmemberContactMessageType.MoreInfoRequest);
			contactMessage.setEmail(message.getEmail());
			contactMessage.setBusiness(message.getBusiness());
		}

		nonmemberContactMessageRepository.save(contactMessage);

		// send email
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("firstName", message.getFirstName());
		parameters.put("lastName", message.getLastName());
		parameters.put("email", message.getEmail());
		parameters.put("business", message.getBusiness());

		try {
			this.emailMessagingService.send(commonSettings.getAdminEmailSenderAddress().getValue(),
					commonSettings.getPlatformSupportEmailAddress().getValue(), "moreInfoRequest.vm", parameters);
		} catch (Exception exp) {
			log.error("Failed to send feedback email " + message, exp);
		}
	}

	@Override
	@Transactional
	public Set<EmployeeRole> getMemberRoles(MemberInfoDto memberInfo, OrganizationDetailInfoDto orgInfo) {
		SocialPerson member = this.memberRepository.findOne(memberInfo.getId());
		SocialEmployee employer = member.getEmployer();

		if (employer != null) {
			SocialActor employerOrg = employer.getContactFrom();
			if (employerOrg.getId().longValue() == orgInfo.getId().longValue()) {
				Set<EmployeeRole> retRoles = new HashSet<EmployeeRole>();
				Set<EmployeeRole> roles = employer.getRoles();
				if (roles != null) {
					for (EmployeeRole role : roles) {
						retRoles.add(role);
					}
				}
				return retRoles;
			}
		}

		return null;
	}

}
