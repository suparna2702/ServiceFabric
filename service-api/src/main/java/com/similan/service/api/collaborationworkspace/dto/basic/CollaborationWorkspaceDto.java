package com.similan.service.api.collaborationworkspace.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class CollaborationWorkspaceDto extends
    KeyHolderDto<CollaborationWorkspaceKey> {

  @XmlAttribute
  private String logo;

  @XmlAttribute
  private String description;

  @XmlAttribute
  private Date creationDate;

  @XmlAttribute
  private Boolean partnerWorkspace = Boolean.FALSE;

  @XmlAttribute
  private Boolean showParticipants = Boolean.TRUE;

  @XmlAttribute
  private Boolean showActivity = Boolean.TRUE;

  @XmlAttribute
  private SocialActorKey creator;

  @XmlAttribute
  private int numberOfParticipants;

  protected CollaborationWorkspaceDto() {
  }

  public CollaborationWorkspaceDto(CollaborationWorkspaceKey key,
      String description, String logo, Date creationDate,
      Boolean partnerWorkspace, SocialActorKey creator, int numberOfParticipants) {
    super(key);
    this.description = description;
    this.creationDate = creationDate;
    this.logo = logo;
    this.partnerWorkspace = partnerWorkspace;
    this.creator = creator;
    this.numberOfParticipants = numberOfParticipants;
  }
  
  @Deprecated
  public String getDisplayName(){
    return this.getKey().getName();
  }

  public Boolean getShowParticipants() {
    return showParticipants;
  }

  public void setShowParticipants(Boolean showParticipants) {
    this.showParticipants = showParticipants;
  }

  public Boolean getShowActivity() {
    return showActivity;
  }

  public void setShowActivity(Boolean showActivity) {
    this.showActivity = showActivity;
  }

  public String getLogo() {
    return logo;
  }

  public String getDescription() {
    return description;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public Boolean getPartnerWorkspace() {
    return partnerWorkspace;
  }

  public SocialActorKey getCreator() {
    return creator;
  }

  public int getNumberOfParticipants() {
    return numberOfParticipants;
  }

}
