package com.similan.framework.dto.events;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.CommunityEventDto;

public class CommunitySpringEvent extends ApplicationEvent  {
	
	private static final long serialVersionUID = 1L;
	
	public CommunitySpringEvent(CommunityEventDto source) {
		super(source);
	}
	
	@Override
	public CommunityEventDto getSource(){
		return (CommunityEventDto)super.getSource();
	}


}
