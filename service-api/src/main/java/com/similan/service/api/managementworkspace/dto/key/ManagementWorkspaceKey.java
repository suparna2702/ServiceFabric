package com.similan.service.api.managementworkspace.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;

public class ManagementWorkspaceKey extends Key implements IWallContainerKey,
    IWallEntrySubjectKey {

  @XmlElement
  private SocialActorKey owner;

  @XmlAttribute
  private String name;

  public ManagementWorkspaceKey() {
  }

  public ManagementWorkspaceKey(String ownerName, String name) {
    this(new SocialActorKey(ownerName), name);
  }

  public ManagementWorkspaceKey(SocialActorKey owner, String name) {
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
    return EntityType.MANAGEMENT_WORKSPACE;
  }

  public void setOwner(SocialActorKey owner) {
    this.owner = owner;
  }

  public void setName(String name) {
    this.name = name;
  }

}
