package com.similan.process.action.invite;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class PartnerApprovalStatusPrepAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;

  @Override
  public void execute(ActivityExecution execution) throws Exception {

    String inviteeEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_EMAIL);

    log.info("Invitee email " + inviteeEmail);

    Long businessId = (Long) execution
        .getVariable(ProcessContextParameterConstants.ORGANIZATION_ID);

    SocialOrganization business = organizationRepository.findOne(businessId);

    execution.setVariable(
        ProcessContextParameterConstants.PARTNER_WELCOME_MESSAGE,
        business.getPartnerWelcomeMessage());

    if (!StringUtils.isBlank(business.getImage())) {

      String logoUrl = platformCommonSettings.getPortalApplcationUrl()
          .getValue() + "spring/assets" + business.getImage();
      log.info("Branded logo URL : " + logoUrl);

      execution.setVariable(ProcessContextParameterConstants.BUSINESS_LOGO,
          logoUrl);
    }

    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        inviteeEmail);

  }

}
