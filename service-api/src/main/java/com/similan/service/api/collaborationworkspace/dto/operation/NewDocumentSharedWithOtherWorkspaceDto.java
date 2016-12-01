package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewDocumentSharedWithOtherWorkspaceDto extends OperationDto {
	
	@XmlElement
	private SocialActorKey sharer;
	
	@XmlElement
	private CollaborationWorkspaceKey workspaceToKey;
	
	public NewDocumentSharedWithOtherWorkspaceDto(){
		
	}
	
	public NewDocumentSharedWithOtherWorkspaceDto(SocialActorKey sharer, 
			          CollaborationWorkspaceKey workspaceKey){
		this.sharer = sharer;
		this.workspaceToKey = workspaceKey;
	}

	public SocialActorKey getSharer() {
		return sharer;
	}

	public CollaborationWorkspaceKey getWorkspaceToKey() {
		return workspaceToKey;
	}

    
}
