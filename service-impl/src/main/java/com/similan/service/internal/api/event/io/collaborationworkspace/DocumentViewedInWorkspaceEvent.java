package com.similan.service.internal.api.event.io.collaborationworkspace;

public class DocumentViewedInWorkspaceEvent extends CollaborationWorkspaceEvent {

	private static final long serialVersionUID = 1L;
	
	private long viewedDocumentId;
	
	private long viewerId;
	
	public DocumentViewedInWorkspaceEvent(long viewedDocumentId, long viewerId){
		this.viewedDocumentId = viewedDocumentId;
		this.viewerId = viewerId;
	}

	public long getViewedDocumentId() {
		return viewedDocumentId;
	}

	public long getViewerId() {
		return viewerId;
	}

}
