package com.similan.service.api.managementworkspace.dto.basic;

import java.util.Date;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

public class ManagementWorkspaceDto extends
    KeyHolderDto<ManagementWorkspaceKey> {

  private String description;

  private Date creationDate;

  private ActorDto creator;

  protected ManagementWorkspaceDto() {
  }

  public ManagementWorkspaceDto(ManagementWorkspaceKey key, String description) {
    super(key);
    this.description = description;
  }
  
  public String getDisplayName() {
    return this.getKey().getName();
  }

  public ActorDto getCreator() {
    return creator;
  }

  public void setCreator(ActorDto creator) {
    this.creator = creator;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
