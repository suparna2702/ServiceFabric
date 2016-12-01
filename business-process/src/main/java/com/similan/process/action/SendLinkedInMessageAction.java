package com.similan.process.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.CommunicationOperations;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.entity.template.VelocityTemplateAttributeSourceType;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.manager.email.EmailManagerImpl;
import com.similan.framework.template.TemplateResourceService;
import com.similan.framework.template.TemplateResourceService.EmailContent;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.SocialManagementService;

@Slf4j
@Component
public class SendLinkedInMessageAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;
  
  private static final String TEMPLATE_NAME = "linkedInMemberInviteTemplate.vm";

  @Autowired
  private VelocityTemplateRepository velocityTemplateRepository;
  @Autowired
  private TemplateResourceService templateResourceService;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private SocialManagementService socialManagementService;

  public void execute(ActivityExecution execution) throws Exception {
    	
    	/**
    	 * 1. Get all the necessary parameters from the context
    	 * 2. Get the template and its required attributes
    	 * 3. Extract them from context and Send email
    	 */
    	Map<String, Object> messageInputParams = new HashMap<String, Object>();
		String toLinkedInId = (String)execution.getVariable(ProcessContextParameterConstants.INVITEE_LINKEDIN_ID);
		String token = (String)execution.getVariable(ProcessContextParameterConstants.LINKED_IN_TOKEN);
		String secretToken = (String)execution.getVariable(ProcessContextParameterConstants.LINKED_IN_SECRET_TOKEN);
    
    	log.info("Fetching template " + this.TEMPLATE_NAME + " to linkedInID " + toLinkedInId);
    	VelocityDocumentTemplate template = velocityTemplateRepository.findByName(this.TEMPLATE_NAME);
    	
    	List<VelocityTemplateAttribute> attributeList = template.getAttributes();
    	log.info("Template attr list " + attributeList);
    	
    	for(VelocityTemplateAttribute attr : attributeList){
    		
    		if(attr.getSourceType().equals(VelocityTemplateAttributeSourceType.Dynamic) == true){
    			
    			/* it can be a non-string value */
    			Object templateParamObject = execution.getVariable(attr.getName());
    			
    			if(templateParamObject != null){
    		
        			messageInputParams.put(attr.getName(), templateParamObject);
        			log.info("Email param name " + attr.getName() + "  param value " + templateParamObject.toString());
    			}
    		}
    	}
    		
		/* get the composed content */
		String allContent = this.templateResourceService.getComposedTemplateContent(TEMPLATE_NAME, messageInputParams);
    EmailContent content = EmailManagerImpl.getEmailContent(allContent);
		log.info("LinkedIn message to be sent to  " + toLinkedInId + " content " + content);
		
    	LinkedInTemplate linkedInTempalte = socialManagementService.getLinkedInTemplate(token, secretToken);
    	CommunicationOperations communicationOperations = linkedInTempalte.communicationOperations();
    	
    	// Comment out during testing to prevent messages being sent
    	communicationOperations.sendMessage(content.getSubject(), content.getBody(), toLinkedInId);
		
	}
}
