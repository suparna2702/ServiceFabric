package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentDownloadedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_DOWNLOADED")
public class DocumentDownloadedWallEntry extends
                         CollaborationWorkspaceDocumentWallEntry {
	
	public DocumentDownloadedWallEntry(){
		
	}
	
	public DocumentDownloadedWallEntry(int number,
		      Date date){
		super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_DOWNLOADED, 
				number, date);
		this.setShowWall(Boolean.TRUE);
	}

}
