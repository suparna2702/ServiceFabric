package com.similan.framework.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JsfUtils {
	
	public static Object findBackingBean(String beanName) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication()
	    		      .evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
	}
	
	public static String getContextParam(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext()
				                                              .getRequest();
		String paramValue =  request.getParameter(paramName);
		return paramValue;
	}



}
