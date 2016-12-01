package com.similan.portal.model;

import java.io.Serializable;

import com.similan.framework.dto.update.PollEventDto;
import com.similan.service.api.community.dto.basic.ActorDto;

public class PollEventModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PollEventDto pollEvent;
	
	private ActorDto requester;
	
	public PollEventModel(PollEventDto pollEvent,
			ActorDto requester){
		this.pollEvent = pollEvent;
		this.requester = requester;
		
	}

	public PollEventDto getPollEvent() {
		return pollEvent;
	}

	public ActorDto getRequester() {
		return requester;
	}

}
