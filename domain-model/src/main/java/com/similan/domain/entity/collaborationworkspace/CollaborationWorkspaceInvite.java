package com.similan.domain.entity.collaborationworkspace;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialActor;

@Entity(name = "CollaborationWorkspaceInvite")
public class CollaborationWorkspaceInvite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private CollaborationWorkspace workspace;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private SocialActor invitee;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private SocialActor inviter;

	@Column
	private String processId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CollaborationWorkspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(
			CollaborationWorkspace workspace) {
		this.workspace = workspace;
	}

	public SocialActor getInvitee() {
		return invitee;
	}

	public void setInvitee(SocialActor invitee) {
		this.invitee = invitee;
	}

	public SocialActor getInviter() {
		return inviter;
	}

	public void setInviter(SocialActor inviter) {
		this.inviter = inviter;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}
