package com.similan.portal.service.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.lead.LeadType;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.service.api.LeadManagementService;
import com.similan.service.impl.SearchAnalyticServiceImpl;


@Component
@Slf4j
public class LeadEventListener implements ApplicationListener<LeadEvent>, BeanFactoryAware {

	/**
	 * The logger.
	 */
	
	/**
	 * Bean factory used to obtain the member management service.
	 * Note, the lookup is done because auto wiring threw a circular dependency error.
	 */
	private BeanFactory beanFactory;
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Transactional
	public void onApplicationEvent(LeadEvent event) {
		
		/**
		 * 1. Get the object
		 * 2. Determine type of event
		 * 3. Store into repo
		 */
		LeadDto lead = event.getSource();
		log.info("Storing lead of type " + lead.getLeadType());
		
		LeadManagementService leadService = (LeadManagementService)this.beanFactory.getBean(LeadManagementService.class);
		try {
			leadService.storeLead(lead);
			if(lead.getLeadType().equals(LeadType.ClickThroughLead) == true){
				//generate click through search events
				SearchAnalyticServiceImpl searchAnalyticService = (SearchAnalyticServiceImpl)beanFactory.getBean("searchAnalyticService");
				searchAnalyticService.storeClickThroughEvent(lead);
			}
		}
		catch(Exception exp){
			log.error("cannot save lead event", exp);
		}
		
	}

}
