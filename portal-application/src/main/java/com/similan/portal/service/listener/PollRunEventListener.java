package com.similan.portal.service.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.service.impl.CampaignManagementServiceImpl;

@Component
@Slf4j
public class PollRunEventListener implements ApplicationListener<PollRunEvent> , BeanFactoryAware{
	
    private BeanFactory beanFactory;
	

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		
	}

	@Transactional
	public void onApplicationEvent(PollRunEvent event) {
		log.info("Received poll run event ");
		
		/* get the campaign service and save the events */
		CampaignManagementServiceImpl campaignService = (CampaignManagementServiceImpl)this.beanFactory.getBean("campaignManagementService");
		campaignService.runPollForPartners(event.getSource());
		
	}

}
