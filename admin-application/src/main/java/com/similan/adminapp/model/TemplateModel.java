package com.similan.adminapp.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

import com.similan.adminapp.dto.TemplateInfo;

@Slf4j
public class TemplateModel extends ListDataModel<TemplateInfo> 
                           implements SelectableDataModel<TemplateInfo>, Serializable{

	private static final long serialVersionUID = 1L;
			
	public TemplateModel(){
		
	}
	
	public TemplateModel(List<TemplateInfo> templateList){
		super(templateList);
	}

	public Object getRowKey(TemplateInfo templateInfo) {
		return templateInfo.getName();
	}

	public TemplateInfo getRowData(String templateName) {
		log.info("Fetching template with name " + templateName);
		@SuppressWarnings("unchecked")
    List<TemplateInfo> templateList = (List<TemplateInfo>)getWrappedData();
		
		for(TemplateInfo templateInfo : templateList){
			if(templateInfo.equals(templateName) == true){
				return templateInfo;
			}
		}
		
		return null;
	}

}
