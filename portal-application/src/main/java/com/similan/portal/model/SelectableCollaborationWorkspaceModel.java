package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;

@Slf4j
public class SelectableCollaborationWorkspaceModel extends ListDataModel<CollaborationWorkspaceDto> 
                    implements SelectableDataModel<CollaborationWorkspaceDto>, Serializable {

	private static final long serialVersionUID = 861599672330991729L;
			
	public SelectableCollaborationWorkspaceModel(List<CollaborationWorkspaceDto> workspaceList){
		super(workspaceList);
	}

	@Override
	@SuppressWarnings("unchecked")
	public CollaborationWorkspaceDto getRowData(String rowKey) {
		log.info("Row key " + rowKey);
		List<CollaborationWorkspaceDto> workspaces = (List<CollaborationWorkspaceDto>)this.getWrappedData();
		for(CollaborationWorkspaceDto workspace : workspaces){
			if(workspace.getKey().getName().equalsIgnoreCase(rowKey)){
				return workspace;
			}
		}
		
		return null;
	}

	@Override
	public Object getRowKey(CollaborationWorkspaceDto workspace) {
		log.info("Workspace " + workspace);
		return workspace.getKey()
				         .getName();
	}

}
