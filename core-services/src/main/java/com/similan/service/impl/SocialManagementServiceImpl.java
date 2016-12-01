package com.similan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.ConnectionOperations;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.ProfileOperations;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.service.api.AssetStorage;
import com.similan.service.api.SocialManagementService;

@Slf4j
public class SocialManagementServiceImpl implements SocialManagementService {
  @Autowired
	private PlatformCommonSettings platformCommonSettings;
	@Autowired
	private WorkflowManager workflowManager;
	@Autowired
	private WorkflowTransientStateRepository transientStateRepository;
	@Autowired
	private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
	@Autowired
	private AssetStorage assetStorage;


	public void inviteLinkedInInitiate(List<LinkedInConnection> invitees,
			MemberInfoDto inviter, String accessToken, String secretAccessToken) {
		log.info("Initiating invite for linkedin connections");
		List<WorkflowTransientState> transStateList = this.transientStateRepository
				.findByType(WorkflowTransientStateType.MemberInvite);

		for (LinkedInConnection invitee : invitees) {
			log.info("Initiating invite for " + invitee.getLastName());
			if (transStateList != null) {
				for (WorkflowTransientState transState : transStateList) {
					WorkflowTransientStateAttribute inviterIdAttr = transState
							.getAttributeByName(ProcessContextParameterConstants.INVITER_ID);
					if (inviterIdAttr != null) {
						if (inviterIdAttr.getAttributeValue().equalsIgnoreCase(
								String.valueOf(inviter.getId()))) {
							WorkflowTransientStateAttribute inviteeLinkedInIdAttr = transState
									.getAttributeByName(ProcessContextParameterConstants.INVITEE_LINKEDIN_ID);
							if (inviteeLinkedInIdAttr != null) {
								if (inviteeLinkedInIdAttr.getAttributeValue()
										.equalsIgnoreCase(
												invitee.getLinkedInId())) {
									break;
								}
							}
						}
					}
				}
			}

			log.info("Creating business process");
			BusinessProcessDefinition inviteProcess = this.businessProcessDefinitionRepository
					.findByName("linkedInMemberInvite");
			log.info("Member sign up with business process "
					+ inviteProcess.getName());

			WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
			initialRequest.setFlowDefinitionName(inviteProcess.getName());

			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ProcessContextParameterConstants.INVITEE_FIRSTNAME,
					invitee.getFirstName());
			parameters.put(ProcessContextParameterConstants.INVITEE_LASTNAME,
					invitee.getLastName());
			parameters.put(
					ProcessContextParameterConstants.INVITEE_LINKEDIN_ID,
					invitee.getLinkedInId());
			parameters.put(ProcessContextParameterConstants.INVITEE_PASSWORD,
					"123@br2011");
			parameters.put(ProcessContextParameterConstants.BUSINESS_NAME,
					invitee.getCompany());

			parameters.put(ProcessContextParameterConstants.MEMBER_INVITE_TYPE,
					"Other");
			parameters.put(ProcessContextParameterConstants.INVITER_FIRSTNAME,
					inviter.getFirstName());
			parameters.put(ProcessContextParameterConstants.INVITER_LASTNAME,
					inviter.getLastName());
			parameters.put(ProcessContextParameterConstants.INVITER_ID,
					new Long(inviter.getId()));

			parameters.put(ProcessContextParameterConstants.LINKED_IN_TOKEN,
					accessToken);
			parameters.put(
					ProcessContextParameterConstants.LINKED_IN_SECRET_TOKEN,
					secretAccessToken);
			/* set the parameters */
			initialRequest.setExecutionParameters(parameters);

			/* execute the workflow */
			this.workflowManager.initiate(initialRequest);
			log.info("Business process initiated");

		}
	}

	public MemberInfoDto mergeMemberWithLinkedInData(MemberInfoDto data,
			String accessToken, String secretAccessToken) {

		LinkedInTemplate linkedInTemplate = getLinkedInTemplate(accessToken,
				secretAccessToken);

		ProfileOperations profileOperations = linkedInTemplate
				.profileOperations();
		LinkedInProfileFull profile = profileOperations.getUserProfileFull();

		if (profile.getFirstName() != null)
			data.setFirstName(profile.getFirstName());
		if (profile.getLastName() != null)
			data.setLastName(profile.getLastName());
		if (profile.getSummary() != null) {
			data.setDescription(profile.getSummary());
			data.setAboutMe(profile.getSummary());
		}
		if (profile.getHeadline() != null)
			data.setTitle(profile.getHeadline());

		if (profile.getPublicProfileUrl() != null)
			importExternalProfilePhoto(data, profile.getProfilePictureUrl());
		// if (profile.getIndustry() != null)
		// data.setIndustry(profile.getIndustry());

		// if (profile.getInterests() != null) {
		// MemberInterestInfo memberInterestInfo = new MemberInterestInfo();
		// memberInterestInfo.setInterest(profile.getInterests());
		// List<MemberInterestInfo> memberInterestInfoList = new
		// ArrayList<MemberInterestInfo>();
		// data.setInterest(memberInterestInfoList);
		// }
		// List<PhoneNumber> phoneNumbers = profile.getPhoneNumbers();
		// for (PhoneNumber phoneNumber : phoneNumbers) {
		// if (phoneNumber.getPhoneType().equalsIgnoreCase("work")) {
		// data.setBusinessPhone(phoneNumber.getPhoneNumber());
		// }
		// if (phoneNumber.getPhoneType().equalsIgnoreCase("mobile")) {
		// data.setMobilePhone(phoneNumber.getPhoneNumber());
		// }
		// }

		return data;

	}

	public LinkedInTemplate getLinkedInTemplate(String accessToken,
			String secretAccessToken) {
		LinkedInTemplate linkedInTemplate = new LinkedInTemplate(
				platformCommonSettings.getLinkedInApiKey().getValue(),
				platformCommonSettings.getLinkedInApiSecretKey().getValue(),
				accessToken, secretAccessToken);
		return linkedInTemplate;
	}

	public List<LinkedInConnection> getLinkedInConnections(String accessToken,
			String secretAccessToken) {
		ConnectionOperations connectionOperations = getLinkedInTemplate(
				accessToken, secretAccessToken).connectionOperations();
		List<LinkedInProfile> connections = connectionOperations
				.getConnections();
		List<LinkedInConnection> results = new ArrayList<LinkedInConnection>();

		for (LinkedInProfile connection : connections) {
			LinkedInConnection result = new LinkedInConnection();
			result.setLinkedInId(connection.getId());
			result.setFirstName(connection.getFirstName());
			result.setLastName(connection.getLastName());
			result.setDescription(connection.getHeadline());
			result.setPhotoLocation(connection.getProfilePictureUrl());
			results.add(result);
		}
		return results;
	}

	private void importExternalProfilePhoto(MemberInfoDto member, String url) {

		String key = this.assetStorage.importResource("member", member.getId()
				.toString(), url);
		key = this.assetStorage.makePersistent(key);
		if (key != null)
			member.setPhotoLocation(key);
		else
			member.setPhotoLocation("/images/memberDefaultPhoto.png");

	}

}
