package com.similan.portal.service.listener;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.lead.LeadDto;

public class LeadEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	public LeadEvent(LeadDto source) {
		super(source);
	}
	
	@Override
	public LeadDto getSource(){
		return (LeadDto)super.getSource();
	}

}
