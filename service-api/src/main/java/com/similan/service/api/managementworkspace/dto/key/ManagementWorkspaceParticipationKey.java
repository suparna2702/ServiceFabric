package com.similan.service.api.managementworkspace.dto.key;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class ManagementWorkspaceParticipationKey extends Key {
	
	@XmlElement
	private SocialActorKey perticipant;
	
	@XmlElement
	private ManagementWorkspaceKey managementWorkspace;
	
	public ManagementWorkspaceParticipationKey(SocialActorKey perticipant, 
			ManagementWorkspaceKey managementWorkspace){
		this.managementWorkspace = managementWorkspace;
		this.perticipant = perticipant;
	}
	
	public ManagementWorkspaceParticipationKey(String perticipantName,
			String managementWorkspaceName, String ownerName){
		SocialActorKey perticipant = new SocialActorKey(perticipantName);
		ManagementWorkspaceKey workspace = new ManagementWorkspaceKey(ownerName, managementWorkspaceName);
		this.managementWorkspace = workspace;
		this.perticipant = perticipant;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.MANAGEMENT_WORKSPACE_PERTICIPATION;
	}

	public SocialActorKey getPerticipant() {
		return perticipant;
	}

	public ManagementWorkspaceKey getManagementWorkspace() {
		return managementWorkspace;
	}
	
	

}
