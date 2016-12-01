package com.similan.service.api.collaborationworkspace.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class CollaborationWorkspaceKey extends Key implements IBookmarkableKey,
    IWallContainerKey, IWallEntrySubjectKey {

  @XmlElement
  private SocialActorKey owner;

  @XmlAttribute
  private String name;

  public CollaborationWorkspaceKey() {
  }

  public CollaborationWorkspaceKey(String ownerName, String name) {
    this(new SocialActorKey(ownerName), name);
  }

  public CollaborationWorkspaceKey(SocialActorKey owner, String name) {
    this.owner = owner;
    this.name = name;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE;
  }

}
