package com.similan.service.internal.impl.process;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.process.BusinessProcess;
import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.manager.email.EmailManager;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BusinessProcessActions<PROCESS extends BusinessProcess> {
  @Autowired
  EmailManager emailManager;
  @Autowired
  VelocityTemplateRepository templateRepository;
  @Autowired
  PlatformCommonSettings commonSettings;
  @Setter
  @Getter
  private PROCESS process;

  @Delegate
  protected abstract BusinessProcess process();

  protected void sendEmail(String templateName, String email,
      Map<String, Object> parameters) {
    log.info("To email: " + email);

    log.info("Fetching template " + templateName + " to email " + email);
    VelocityDocumentTemplate template = templateRepository
        .findByName(templateName);

    List<VelocityTemplateAttribute> attributeList = template.getAttributes();
    log.info("Template attr list " + attributeList);

    parameters.put("businessLogo", getBusinessLogo());

    String fromAddress = commonSettings.getPlatformEmailSenderAddress()
        .getValue();

    /* send email */
    this.emailManager.send(fromAddress, email, templateName, parameters);
  }
}
