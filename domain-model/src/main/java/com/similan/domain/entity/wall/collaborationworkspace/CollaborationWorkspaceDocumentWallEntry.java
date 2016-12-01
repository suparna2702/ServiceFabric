package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CollaborationWorkspaceDocumentWallEntry")
public class CollaborationWorkspaceDocumentWallEntry extends
		CollaborationWorkspaceWallEntry {
	
	@Column
	private Integer documentVersion;
	
	public CollaborationWorkspaceDocumentWallEntry(){
		
	}
	
	protected CollaborationWorkspaceDocumentWallEntry(WallEntryType type, int number,
		      Date date){
		super(type, number, date);		
	}

	public Integer getDocumentVersion() {
		return documentVersion;
	}

	public void setDocumentVersion(Integer documentVersion) {
		this.documentVersion = documentVersion;
	}

}
