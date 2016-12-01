package com.similan.framework.dto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class OrganizationLocationConverter implements Converter {
	
	public String getAsString( FacesContext facesContext,
	        UIComponent uiComponent, Object obj) {
		
		if(obj != null){
		  if(obj instanceof OrganizationAddressDto){
			  OrganizationAddressDto location = (OrganizationAddressDto) obj;
			  return location.toString();
			  
		  }
		}
		
		return null;
	}
	
	public Object getAsObject( FacesContext facesContext,
	        UIComponent uIComponent, String str) throws ConverterException {
		return null;
	}

}
