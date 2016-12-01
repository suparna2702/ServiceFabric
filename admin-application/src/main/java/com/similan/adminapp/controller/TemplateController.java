package com.similan.adminapp.controller;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.similan.adminapp.dto.TemplateInfo;
import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.entity.template.VelocityTemplateAttribute;
import com.similan.domain.entity.template.VelocityTemplateAttributeSourceType;
import com.similan.domain.entity.template.VelocityTemplateType;
import com.similan.domain.repository.template.VelocityTemplateRepository;

@Controller
@Slf4j
public class TemplateController {
	@Autowired
	private VelocityTemplateRepository templateRepository;

	@RequestMapping("/createTemplate")
	public String createTemplate(HttpServletRequest request, ModelMap model)	{
		return "createTemplate";
	}
	
	@Transactional
	@RequestMapping("/viewAllTemplates")
	public String viewAllTemplates(HttpServletRequest request, ModelMap model)	{
		
		List<VelocityDocumentTemplate> documentTemplate = templateRepository.findAll();
		
		if(documentTemplate != null){
			
			List<TemplateInfo> templateInfoList = new ArrayList<TemplateInfo>();
			log.info("Number of templates " + documentTemplate.size());
			
			for(VelocityDocumentTemplate template : documentTemplate){
				
				TemplateInfo templateInfo = new TemplateInfo();
				templateInfo.setName(template.getName());
				
				/* fill up the attributes */
				List<VelocityTemplateAttribute> attrList = template.getAttributes();
				
				if(attrList != null){
					
					StringBuilder prop = new StringBuilder();
					for(VelocityTemplateAttribute templAttr : attrList){
						
						if(templAttr.getName() != null){
							prop.append(templAttr.getName()).append("\n");
						}
						
						if(templAttr.getSourceType() != null){
							prop.append(templAttr.getSourceType().toString()).append("\n");
						}
						
						if(templAttr.getValue() != null){
							prop.append(templAttr.getValue()).append("\n").append(" / ");
						}
						
					}
					
					String propertyString = prop.toString();
					log.info("Template property " + propertyString);
					templateInfo.setProperty(propertyString);
				}
				
				templateInfoList.add(templateInfo);
			}
			
			/* add it to model */
			model.put("templateList", templateInfoList);
			
		}
		
		return "viewAllTemplates";
	}
	
	@Transactional
	@RequestMapping("/saveTemplate")
	public String saveTemplate(HttpServletRequest request, ModelMap model)	{
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile fileTemplate = multipartRequest.getFile("templateFile");
		String templateName = request.getParameter("templateName");
		
		/* we want to put the VM at the end */
		if(templateName.contains(".vm") == false){
			templateName += ".vm";
		}
		
		log.info("Template name " + templateName);
		
		String attribute_1 = request.getParameter("attribute_1");
		String attribute_2 = request.getParameter("attribute_2");
		String attribute_3 = request.getParameter("attribute_3");
		String attribute_4 = request.getParameter("attribute_4");
		
		if (fileTemplate != null ) {
			
			try {
				
				InputStreamReader streamReaderTemplate = new InputStreamReader(fileTemplate.getInputStream());
				
				VelocityDocumentTemplate docTemplate = templateRepository.create();
				docTemplate.setName(templateName);
				
				StringWriter writer = new StringWriter();
				IOUtils.copy(streamReaderTemplate,writer);
				String theTemplateString = writer.toString();
				
				log.info("The template file " + theTemplateString);
				
				docTemplate.setTemplateContent(theTemplateString);
				docTemplate.setTemplateType(VelocityTemplateType.Email);
				
				/* create attributes */
				List<VelocityTemplateAttribute> templateAttributes = new ArrayList<VelocityTemplateAttribute>();
				
				if(attribute_1 != null && attribute_1.equalsIgnoreCase("") != true){
					
					log.info("attribute_1 name " + attribute_1);
					VelocityTemplateAttribute attr = templateRepository.createAttribute();
					attr.setName(attribute_1);
					attr.setSourceType(VelocityTemplateAttributeSourceType.Dynamic);
					templateAttributes.add(attr);
				}
				
				if(attribute_2 != null && attribute_2.equalsIgnoreCase("") != true){
					
					log.info("attribute_2 name " + attribute_2);
					VelocityTemplateAttribute attr = templateRepository.createAttribute();
					attr.setName(attribute_2);
					attr.setSourceType(VelocityTemplateAttributeSourceType.Dynamic);
					templateAttributes.add(attr);
				}
				
				if(attribute_3 != null && attribute_3.equalsIgnoreCase("") != true){
					
					log.info("attribute_3 name " + attribute_3);
					VelocityTemplateAttribute attr = templateRepository.createAttribute();
					attr.setName(attribute_3);
					attr.setSourceType(VelocityTemplateAttributeSourceType.Dynamic);
					templateAttributes.add(attr);
				}
				
				if(attribute_4 != null && attribute_4.equalsIgnoreCase("") != true){
					
					log.info("attribute_4 name " + attribute_4);
					VelocityTemplateAttribute attr = templateRepository.createAttribute();
					attr.setName(attribute_4);
					attr.setSourceType(VelocityTemplateAttributeSourceType.Dynamic);
					templateAttributes.add(attr);
				}
				
				docTemplate.setAttributes(templateAttributes);
				log.info("saving email template");
				this.templateRepository.save(docTemplate);
				
			}
			catch(Exception exp){
				log.error("Error while creating template ", exp);
			}
		}

		return "viewAllTemplates";
	}


}
