package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentSharedWithOtherWorkspaceWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_SHARED_ANOTHER_WORKSPACE")
public class DocumentSharedWithOtherWorkspaceWallEntry extends
                             CollaborationWorkspaceDocumentWallEntry {
	
	@ManyToOne
	@JoinColumn
	private CollaborationWorkspace sharedFromSpace;

	public DocumentSharedWithOtherWorkspaceWallEntry(int number, Date date){
		super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED_ANOTHER_WORKSPACE, number, date);
		this.setShowWall(Boolean.TRUE);
	}
	
	public DocumentSharedWithOtherWorkspaceWallEntry(){
		
	}
	
	public CollaborationWorkspace getSharedFromSpace() {
		return sharedFromSpace;
	}

	public void setSharedFromSpace(CollaborationWorkspace sharedFromSpace) {
		this.sharedFromSpace = sharedFromSpace;
	}
	
}
