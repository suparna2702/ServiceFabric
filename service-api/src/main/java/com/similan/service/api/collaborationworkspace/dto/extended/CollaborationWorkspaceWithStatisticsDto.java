package com.similan.service.api.collaborationworkspace.dto.extended;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

public class CollaborationWorkspaceWithStatisticsDto extends
                  KeyHolderDto<CollaborationWorkspaceKey>{
	
	@XmlElement
	private CollaborationWorkspaceDto workspace;
	
	@XmlElement
	private CollaborationWorkspaceStatisticsDto workspaceStatistics;
	
	protected CollaborationWorkspaceWithStatisticsDto(){
		
	}
	
	public CollaborationWorkspaceWithStatisticsDto(CollaborationWorkspaceKey key,
			CollaborationWorkspaceDto workspace, 
			CollaborationWorkspaceStatisticsDto newWorkspaceWithStat){
		super(key);
		this.workspace = workspace;
		this.workspaceStatistics = newWorkspaceWithStat;
	}

	public CollaborationWorkspaceDto getWorkspace() {
		return workspace;
	}

	public CollaborationWorkspaceStatisticsDto getWorkspaceStatistics() {
		return workspaceStatistics;
	}
	
	

}
