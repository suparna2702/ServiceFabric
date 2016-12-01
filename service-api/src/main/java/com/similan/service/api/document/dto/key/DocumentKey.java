package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class DocumentKey extends Key implements IBookmarkableKey,
    IWallEntrySubjectKey, ICommentableKey {

  @XmlElement
  private ManagementWorkspaceKey workspace;

  @XmlAttribute
  private String name;

  public DocumentKey() {
  }

  public DocumentKey(String workspaceOwnerName, String workspaceName,
      String name) {
    this(new ManagementWorkspaceKey(workspaceOwnerName, workspaceName), name);
  }

  public DocumentKey(ManagementWorkspaceKey workspace, String name) {
    this.workspace = workspace;
    this.name = name;
  }

  public ManagementWorkspaceKey getWorkspace() {
    return workspace;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT;
  }

  public void setWorkspace(ManagementWorkspaceKey workspace) {
    this.workspace = workspace;
  }

  public void setName(String name) {
    this.name = name;
  }

}
