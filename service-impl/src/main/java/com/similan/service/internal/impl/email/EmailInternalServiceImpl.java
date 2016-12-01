package com.similan.service.internal.impl.email;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.manager.email.EmailManagerImpl;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.io.NewEmail;

@Slf4j
@Service
public class EmailInternalServiceImpl extends ServiceImpl implements
    EmailInternalService {
  @Autowired
  private EmailManagerImpl emailManager;
  @Autowired
  private PlatformCommonSettings commonSettings;

  @Override
  @Transactional
  public void send(NewEmail emailMessage) {
    SocialActor from = emailMessage.getFrom();
    log.info("Sending email from " + from.getDisplayName());

    String fromEmail = this.commonSettings.getPlatformEmailSenderAddress()
        .getValue();

    for (SocialActor to : emailMessage.getTo()) {
      Set<String> toEmails = getToEmails(to);
      for (String toEmail : toEmails) {
        doSend(fromEmail, toEmail, emailMessage.getTemplate(),
            emailMessage.getParameters());
      }
    }
  }

  private void doSend(String fromEmail, String toEmail, String template,
      Map<String, Object> parameters) {
    try {
      this.emailManager.send(fromEmail, toEmail, template, parameters);
    } catch (Exception e) {
      log.error("Error sending email: " + e, e);
      // TODO: one JMS is implemented and used we will uncomment this exception
      // but for now we swallow the exception
      // throw new EmailInternalServiceException("Error sending email: " + e,
      // e);
    }
  }

  private String getFromEmail(SocialActor actor) {
    if (actor instanceof SocialPerson) {
      return ((SocialPerson) actor).getPrimaryEmail();
    } else if (actor instanceof SocialOrganization) {
      return ((SocialOrganization) actor).getPrimaryEmail();
    } else if (actor instanceof ExternalSocialPerson) {
      return ((ExternalSocialPerson) actor).getPrimaryEmail();
    } else {
      throw new IllegalArgumentException("Unsupported actor type: "
          + actor.getClass().getName());
    }
  }

  private Set<String> getToEmails(SocialActor actor) {
    if (actor instanceof SocialPerson) {
      return Collections.singleton(((SocialPerson) actor).getPrimaryEmail());
    } else if (actor instanceof SocialOrganization) {
      return Collections.singleton(((SocialOrganization) actor)
          .getPrimaryEmail());
    } else if (actor instanceof ExternalSocialPerson) {
      return Collections.singleton(((ExternalSocialPerson) actor)
          .getPrimaryEmail());
    } else {
      throw new IllegalArgumentException("Unsupported actor type: "
          + actor.getClass().getName());
    }
  }
}
