package com.similan.framework.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Slf4j
public class SpringUtils {
	
	
	public static Object getSpringBean(String name){
		
		Object springBean = null;
		
		try {
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance()
					                                   .getExternalContext().getContext();
			WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			
			if(appContext != null){
				springBean = appContext.getBean(name);
			}
		}
		catch(Exception ex){
			log.error("Cannot get face context ", ex);
		}
		
		return springBean;
	}
}
