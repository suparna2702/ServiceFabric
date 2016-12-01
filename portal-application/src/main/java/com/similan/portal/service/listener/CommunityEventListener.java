package com.similan.portal.service.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.event.SearchEvent;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.CommunityEventType;
import com.similan.framework.dto.SearchResultDto;
import com.similan.framework.dto.SearchResultItemDto;
import com.similan.framework.dto.SearchResultSummery;
import com.similan.framework.dto.events.CommunitySpringEvent;
import com.similan.service.impl.SearchAnalyticServiceImpl;

@Component
@Slf4j
public class CommunityEventListener implements ApplicationListener<CommunitySpringEvent>, BeanFactoryAware{

	private BeanFactory beanFactory;
	
	
	private JAXBContext jaxbContext;
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	public void setJaxbContext(JAXBContext jaxbContext) {
		this.jaxbContext = jaxbContext;
	}
	
	/**
	 * 
	 * @param searchEvent
	 * @param event
	 * @throws JAXBException
	 */
	private void storeSearchDataFile(SearchEvent searchEvent, 
			                         CommunitySpringEvent event) throws JAXBException{
		
		PlatformCommonSettings platformSettings = (PlatformCommonSettings)beanFactory.getBean("platformCommonSettings");
		String dataFileDir = platformSettings.getRootDataFileDirectory().getValue();
		
		/* now save the file */
		String searchDataFileDir = dataFileDir + "/searchData/"; 
		
		if(this.jaxbContext == null){
			this.jaxbContext = JAXBContext.newInstance(SearchResultDto.class, SearchResultSummery.class, 
					                                   SearchResultItemDto.class);
		}
		
		Writer strWriter = new StringWriter();
		Marshaller jaxbMarshaller = this.jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.marshal(event.getSource().getSearchResult(), 
				strWriter);
		
		File searchDir = new File(searchDataFileDir);
		if(searchDir.exists() != true){
			searchDir.mkdir();
		}
		
		File outputFile = new File(searchDir, searchEvent.getFileUUID());
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(outputFile);
			IOUtils.write(strWriter.toString(), out);
		}
		catch (IOException e) {
			log.error("Error storing search file ", e);
			throw new RuntimeException(e);
		} 
		finally {
			IOUtils.closeQuietly(out);
		}
		
	}
	
	private void handleSearchEvent(CommunitySpringEvent event){
		
		try {
			
			SearchAnalyticServiceImpl searchAnalyticService = (SearchAnalyticServiceImpl)beanFactory.getBean("searchAnalyticService");
			searchAnalyticService.storeSearchEvents(event.getSource().getSearchResult());
			
		} 
		catch (Exception e) {
			log.error("Error creating JAXB context ", e);
		}
	}
	
	/*private void handleMemberLoginEvent(CommunitySpringEvent event){
		
		MemberLoginEventRepository loginRepository = (MemberLoginEventRepository)beanFactory.getBean("memberLoginEventRepository");
		MemberLoginEvent loginEvent = loginRepository.create();
		loginEvent.setLoginInfo(event.getSource().getLoginInfo());
		loginEvent.setEventGenerator(event.getSource().getEventGeneratorId());
		loginEvent.setEventGenerationTime(new Date());
		loginRepository.save(loginEvent);
	}*/
	
    /*private void handleMemberLogoutEvent(CommunitySpringEvent event){
    	
    	MemberLoginEventRepository loginRepository = (MemberLoginEventRepository)beanFactory.getBean("memberLoginEventRepository");
    	MemberLoginEvent loginEvent = loginRepository.findEventBySessionId(event.getSource()
    			                                     .getLoginInfo().getWebSessionId());
    	loginEvent.getLoginInfo().setLogoutTime(new Date());
    	loginRepository.save(loginEvent);
	}*/

	/**
	 * main event processing method
	 */
	@Transactional
	public void onApplicationEvent(CommunitySpringEvent event) {
		
		log.info("event type " + event.getSource().getEventType());
		
		/* now only handle search events */
		if(event.getSource().getEventType()
				            .equals(CommunityEventType.SearchEvent) == true){
			this.handleSearchEvent(event);
		}
		else if(event.getSource().getEventType()
				            .equals(CommunityEventType.MemberLoginEvent) == true){
			//this.handleMemberLoginEvent(event);
		}
		else if(event.getSource().getEventType()
	            .equals(CommunityEventType.MemberLogoutEvent) == true){
            //this.handleMemberLogoutEvent(event);
        }
	}

}
