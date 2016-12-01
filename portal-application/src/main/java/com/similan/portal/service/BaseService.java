package com.similan.portal.service;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.similan.domain.repository.global.GlobalRepository;
import com.similan.service.api.MemberManagementService;
import com.similan.service.impl.OrganizationManagementServiceImpl;

@Slf4j
public abstract class BaseService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Object getSpringBean(String name){
		
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
	
	protected OrganizationManagementServiceImpl getOrganizationService(){
		OrganizationManagementServiceImpl orgService = (OrganizationManagementServiceImpl)this.getSpringBean("organizationManagementService");
		return orgService;
	}
	
	protected GlobalRepository getGlobalRepository(){
	  GlobalRepository repo = (GlobalRepository)this.getSpringBean("globalRepository");
		return repo;
	}
	
	protected MemberManagementService getAccountManagementService(){
		
		MemberManagementService accountService = (MemberManagementService)this.getSpringBean("accountManagementService");
		return accountService;
	}


}
