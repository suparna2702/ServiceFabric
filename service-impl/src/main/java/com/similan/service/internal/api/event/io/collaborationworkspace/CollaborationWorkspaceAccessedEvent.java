package com.similan.service.internal.api.event.io.collaborationworkspace;

import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;

public class CollaborationWorkspaceAccessedEvent  
                     extends CollaborationWorkspaceEvent {

	private static final long serialVersionUID = 1L;
	
	private long accessedWorkspace;
	
	private long accessor;
	
	private CollaborationWorkspaceAccessedType accessType;
	
	public CollaborationWorkspaceAccessedEvent(long accessedWorkspace, long accessor, 
			CollaborationWorkspaceAccessedType accessType){
		this.accessedWorkspace = accessedWorkspace;
		this.accessor = accessor;
		this.accessType = accessType;
	}

	public long getAccessedWorkspace() {
		return accessedWorkspace;
	}

	public long getAccessor() {
		return accessor;
	}

	public CollaborationWorkspaceAccessedType getAccessType() {
		return accessType;
	}
	

}
