package com.similan.domain.entity.community;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "MemberJoinValidation")
public class MemberJoinValidation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private long memberId;
	
	@Column
	private String processInstanceId;
	
	@Column
	private String memberJoidUUID;
	
	@Column
	private Date timeStamp;
	
	@Column
	private long inviterId;
	
	@Column
	@Enumerated(EnumType.STRING)
	private MemberJoinIntent joinIntent;
	
	@Column
	@Enumerated(EnumType.STRING)
	private MemberInviteType memberInviteType;
	
	public MemberInviteType getMemberInviteType() {
		return memberInviteType;
	}

	public void setMemberInviteType(MemberInviteType memberInviteType) {
		this.memberInviteType = memberInviteType;
	}

	public MemberJoinIntent getJoinIntent() {
		return joinIntent;
	}

	public void setJoinIntent(MemberJoinIntent joinIntent) {
		this.joinIntent = joinIntent;
	}

	public long getInviterId() {
		return inviterId;
	}

	public void setInviterId(long inviterId) {
		this.inviterId = inviterId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getMemberJoidUUID() {
		return memberJoidUUID;
	}

	public void setMemberJoidUUID(String memberJoidUUID) {
		this.memberJoidUUID = memberJoidUUID;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
