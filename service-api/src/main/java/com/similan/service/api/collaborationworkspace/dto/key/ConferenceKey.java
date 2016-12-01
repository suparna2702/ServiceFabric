package com.similan.service.api.collaborationworkspace.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class ConferenceKey extends Key {

  @XmlElement
  private CollaborationWorkspaceKey workspace;

  @XmlAttribute
  private String name;

  public ConferenceKey() {
  }

  public ConferenceKey(String workspaceOwnerName, String workspaceName,
      String name) {
    this(new CollaborationWorkspaceKey(workspaceOwnerName, workspaceName), name);
  }

  public ConferenceKey(CollaborationWorkspaceKey workspace, String name) {
    this.workspace = workspace;
    this.name = name;
  }

  public CollaborationWorkspaceKey getWorkspace() {
    return workspace;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.CONFERENCE;
  }

}
