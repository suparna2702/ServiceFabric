package com.similan.portal.service.listener;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.poll.PollRunEventDto;

public class PollRunEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	public PollRunEvent(PollRunEventDto source) {
		super(source);
	}
	
	@Override
	public PollRunEventDto getSource(){
		return (PollRunEventDto)super.getSource();
	}


}
