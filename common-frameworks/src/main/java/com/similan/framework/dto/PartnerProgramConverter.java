package com.similan.framework.dto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;

public class PartnerProgramConverter implements Converter {

	public String getAsString(FacesContext facesContext,
	        UIComponent uiComponent, Object obj) throws ConverterException {
		
		if(obj != null){
			  if(obj instanceof PartnerProgramDefinitionDto){
				  PartnerProgramDefinitionDto partnerProgram = (PartnerProgramDefinitionDto) obj;
				  String ret = partnerProgram.getName();
				  return ret;
				  
			  }
			}
			
			return null;
	}

	public String getAsObject(FacesContext facesContext,
	        UIComponent uIComponent, String str) throws ConverterException {
		return null;
	}

}
