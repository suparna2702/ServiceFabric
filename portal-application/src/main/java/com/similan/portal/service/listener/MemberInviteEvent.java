package com.similan.portal.service.listener;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.MemberInviteDto;

public class MemberInviteEvent extends ApplicationEvent {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -1014571564366322207L;

	public MemberInviteEvent(MemberInviteDto source) {
		super(source);
	}

	@Override
	public MemberInviteDto getSource() {
		return (MemberInviteDto) super.getSource();
	}

}
