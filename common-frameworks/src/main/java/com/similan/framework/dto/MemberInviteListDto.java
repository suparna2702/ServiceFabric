package com.similan.framework.dto;

import java.io.Serializable;
import java.util.List;

public class MemberInviteListDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<MemberInviteDto> memberInvites;

	public List<MemberInviteDto> getMemberInvites() {
		return memberInvites;
	}

	public void setMemberInvites(List<MemberInviteDto> memberInvites) {
		this.memberInvites = memberInvites;
	}
}
