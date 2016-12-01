package com.similan.portal.ui.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converter class for String lists. It assumes that the incoming string will follow
 * the following pattern:
 * <pre>[element1, element2, ...., elementN]</pre>
 * @author psaavedra
 */
public class GenericListConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		value = value.replace('[', ' ').replace(']', ' ').trim();
		String[] values = value.split(",");
		List<String> result = new ArrayList<String>(values.length);
		for (String val : values) {
			result.add(val.trim());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

}
