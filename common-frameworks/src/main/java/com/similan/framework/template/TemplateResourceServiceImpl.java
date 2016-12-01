package com.similan.framework.template;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.entity.template.VelocityTemplateAttributeSourceType;
import com.similan.domain.repository.template.VelocityTemplateRepository;

@Slf4j
@Component
public class TemplateResourceServiceImpl implements TemplateResourceService  {
	@Autowired
	private VelocityTemplateRepository templateRepository;
	@Autowired
	private VelocityEngine velocityEngine;
	
  public String getComposedTemplateContent(String templateName,
      Map<String, Object> attributes) {
    try {
      return doGetComposedTemplateContent(templateName, attributes);
    } catch (Exception e) {
      throw new IllegalArgumentException(String.format(
          "Error composing template %s with attributes: %s", templateName,
          attributes), e);
    }
  }
  
	public String doGetComposedTemplateContent(String templateName, Map<String, Object> attributes) throws Exception {
		
		Template template = this.velocityEngine.getTemplate(templateName);
		
		/* get the Template from presenter */
		StringWriter stringWriter = new StringWriter();
		VelocityContext velocityContext = new VelocityContext();
		
		/* get the template */
		VelocityDocumentTemplate docTemplate = this.templateRepository.findByName(templateName);
		
		if(docTemplate != null){
			List<VelocityTemplateAttribute> docTemplateAttrs = docTemplate.getAttributes();
			
			/* put them into the map */
			for(VelocityTemplateAttribute attr : docTemplateAttrs){
				
				if(attr.getSourceType().equals(VelocityTemplateAttributeSourceType.Static) == true){
					
					if(attributes != null){
						attributes.put(attr.getName(), attr.getValue());
					}
				}
			}
		}
		
		/* add attributes to the context */
		if(attributes != null){
			
			Set<String> keySet = attributes.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			
			/* populate the context */
			while(keyIterator.hasNext()){
				
				String key = keyIterator.next();
				Object value = attributes.get(key);
				log.info("Attribute name added " + key + " value " + value);
				velocityContext.put(key, value);
			}
		}

		stringWriter.close();
		template.merge(velocityContext, stringWriter);
		String content = stringWriter.toString();
		log.info("The composed email message " + content);
		
		return content;
	}
}
