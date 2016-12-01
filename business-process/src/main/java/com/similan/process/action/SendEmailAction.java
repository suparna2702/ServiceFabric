package com.similan.process.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.entity.template.VelocityTemplateAttributeSourceType;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.manager.email.EmailManager;
import com.similan.framework.manager.email.EmailManagerImpl;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
public class SendEmailAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;
  
  @Autowired
  private EmailManager emailManager;
  @Autowired
  private VelocityTemplateRepository templateRepository;
  @Autowired
  private PlatformCommonSettings commonSettings;

  private String templateName;

  private String toEmailContextVar = null;

  public String getToEmailContextVar() {
    return toEmailContextVar;
  }

  public void setToEmailContextVar(String toEmailContextVar) {
    this.toEmailContextVar = toEmailContextVar;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * 
   * @param execution
   * @throws Exception
   */
  public void execute(ActivityExecution execution) throws Exception {

    /**
     * 1. Get all the necessary parameters from the context 2. Get the template
     * and its required attributes 3. Extract them from context and Send email
     */
    Map<String, Object> emailInputParams = new HashMap<String, Object>();
    String toEmail = null;

    if (this.toEmailContextVar != null) {
      toEmail = (String) execution.getVariable(this.toEmailContextVar);
      log.info("toEmailContextVar " + this.toEmailContextVar);
    } else {
      toEmail = (String) execution
          .getVariable(ProcessContextParameterConstants.TO_EMAIL);
      log.info("TO_EMAIL " + toEmail);
    }

    log.info("Fetching template " + this.templateName + " to email "
        + toEmail);
    VelocityDocumentTemplate template = templateRepository
        .findByName(this.templateName);

    List<VelocityTemplateAttribute> attributeList = template.getAttributes();
    log.info("Template attr list " + attributeList);

    for (VelocityTemplateAttribute attr : attributeList) {

      if (attr.getSourceType().equals(
          VelocityTemplateAttributeSourceType.Dynamic) == true) {

        /* it can be a non-string value */
        Object emailParamObject = execution.getVariable(attr.getName());

        if (emailParamObject != null) {

          emailInputParams.put(attr.getName(), emailParamObject);
          log.info("Email param name " + attr.getName()
              + "  param value " + emailParamObject.toString());
        }
      }
    }

    String brandedLogoUrl = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_LOGO);
    log.info("BUSINESS_LOGO " + brandedLogoUrl);

    if (StringUtils.isNotBlank(brandedLogoUrl)) {
      emailInputParams.put(ProcessContextParameterConstants.BUSINESS_LOGO,
          brandedLogoUrl);
    }

    String fromAddress = commonSettings.getPlatformEmailSenderAddress()
        .getValue();

    /* send email */
    this.emailManager.send(fromAddress, toEmail, this.templateName,
        emailInputParams);

  }
}
