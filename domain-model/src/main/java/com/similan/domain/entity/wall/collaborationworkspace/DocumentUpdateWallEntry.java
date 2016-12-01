package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentUpdateWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_UPDATED")
public class DocumentUpdateWallEntry extends
                    CollaborationWorkspaceDocumentWallEntry{
	
	private Integer previousVersion;
	
	public DocumentUpdateWallEntry(){
		
	}
	
	public DocumentUpdateWallEntry(int number,
		      Date date, Integer previousVersion){
		super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UPDATED, 
				number, date);
		this.previousVersion = previousVersion;
		this.setShowWall(Boolean.TRUE);
	}

	public Integer getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(Integer previousVersion) {
		this.previousVersion = previousVersion;
	}
	
}
