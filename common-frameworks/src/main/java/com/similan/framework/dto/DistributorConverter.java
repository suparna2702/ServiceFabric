package com.similan.framework.dto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter( value="distributorConverter" )
public class DistributorConverter implements Converter {

	public String getAsString( FacesContext facesContext,
	        UIComponent uiComponent, Object obj) {
		
		if(obj != null){
		  if(obj instanceof OrganizationDetailInfoDto){
			  OrganizationDetailInfoDto distributor = (OrganizationDetailInfoDto) obj;
			  String distributorStr = distributor.getBusinessName();
			  return distributorStr;
			  
		  }
		}
		
		return null;
	}
	
	public Object getAsObject( FacesContext facesContext,
	        UIComponent uIComponent, String str) throws ConverterException {
		return null;
	}
}
