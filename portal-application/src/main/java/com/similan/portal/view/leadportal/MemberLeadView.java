package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("view")
@Component("memberLeadView")
@Slf4j
public class MemberLeadView extends LeadManagementView {
	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init() {		
		this.initializeView();
	}
	
	private void initializeView() {
		
		/*log.info("Member info for member " + this.getMember().getId());
		this.socialActorId = this.getMember().getId();
		log.info("Leads to be fetched for member " + this.socialActorId );
		
		try {
			
			if(this.getMember().getContacts() == null){
				
				log.info("Contact list will be fetched ");
				Set<Contact> contactList = memberService.getContacts(this.getMember());
				
				if(contactList != null){
					log.info("Number of contacts " + contactList.size());
					this.getMember().setContacts(new ArrayList<Contact>(contactList));
				}
			}
			
			this.socialActorContact = this.getMember();
			super.init();
			
		}
		catch(Exception exp){
			
			log.error("Cannot initialize member leads ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
	                "Lead cannot be fetched"));
		}*/
		
	}
}
