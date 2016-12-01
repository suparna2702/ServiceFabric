package com.similan.framework.dto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.similan.framework.dto.partner.PartnerRequiredAttributeDto;

public class PartnerRequiredAttributeConverter implements Converter {
	
	public String getAsString( FacesContext facesContext,
	        UIComponent uiComponent, Object obj) {
		
		if(obj != null){
		  if(obj instanceof PartnerRequiredAttributeDto){
			  PartnerRequiredAttributeDto attributes = (PartnerRequiredAttributeDto) obj;
			  return attributes.toString();
			  
		  }
		}
		
		return null;
	}
	
	public Object getAsObject( FacesContext facesContext,
	        UIComponent uIComponent, String str) throws ConverterException {
		return null;
	}

}
