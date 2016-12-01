package com.similan.portal.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.similan.portal.model.SelectableContact;

public class SelectableContactModelConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object obj) throws ConverterException {
		
		if(obj != null){
			  if(obj instanceof SelectableContact){
				  SelectableContact contact = (SelectableContact) obj;
				  return contact.toString();
				  
			  }
			}
			
		return null;
	}

}
