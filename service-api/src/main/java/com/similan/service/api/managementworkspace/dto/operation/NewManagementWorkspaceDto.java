package com.similan.service.api.managementworkspace.dto.operation;

import com.similan.service.api.base.dto.operation.OperationDto;

public class NewManagementWorkspaceDto extends OperationDto {
  private String description; 

  public NewManagementWorkspaceDto() {
  }
  
  public String getDescription() {
	return description;
  }

  public void setDescription(String description) {
	this.description = description;
  }

}
