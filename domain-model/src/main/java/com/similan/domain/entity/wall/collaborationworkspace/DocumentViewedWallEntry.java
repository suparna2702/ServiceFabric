package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentViewedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_VIEWED")
public class DocumentViewedWallEntry extends
                     CollaborationWorkspaceDocumentWallEntry{
	
	public DocumentViewedWallEntry(){
		
	}
	
	public DocumentViewedWallEntry(int number, Date date){
		super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_VIEWED, 
				number, date);
		this.setShowWall(Boolean.TRUE);
	}
	
}
