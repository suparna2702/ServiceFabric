package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class CollaborationWorkspaceAccessedWallEntryDto extends
    CollaborationWorkspaceWallEntryDto {
  @XmlElement
  private CollaborationWorkspaceDto accessedWorkspace;

  @XmlElement
  private ActorDto accessor;

  @XmlElement
  private CollaborationWorkspaceAccessedType accessType;

  protected CollaborationWorkspaceAccessedWallEntryDto() {

  }

  public CollaborationWorkspaceAccessedWallEntryDto(
      CollaborationWorkspaceDto accessedWorkspace,
      ActorDto accessor,
      CollaborationWorkspaceAccessedType accessType,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date) {
    super(key, accessor, date);
    this.accessedWorkspace = accessedWorkspace;
    this.accessor = accessor;
    this.accessType = accessType;
  }

  public CollaborationWorkspaceDto getAccessedWorkspace() {
    return accessedWorkspace;
  }

  public ActorDto getAccessor() {
    return accessor;
  }

  public CollaborationWorkspaceAccessedType getAccessType() {
    return accessType;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_ACCESSED;
  }

  @Override
  public String toString() {
    return "CollaborationWorkspaceAccessedWallEntryDto [accessedWorkspace="
        + accessedWorkspace + ", accessor=" + accessor + ", accessType="
        + accessType + ", getAccessedWorkspace()=" + getAccessedWorkspace()
        + ", getAccessor()=" + getAccessor() + ", getAccessType()="
        + getAccessType() + ", getType()=" + getType() + ", getInitiator()="
        + getInitiator() + ", getDate()=" + getDate() + ", getKey()="
        + getKey() + "]";
  }

}
