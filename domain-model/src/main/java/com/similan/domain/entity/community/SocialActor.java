package com.similan.domain.entity.community;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import lombok.ToString;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "SocialActor")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SocialActorType", discriminatorType = DiscriminatorType.STRING)
@ToString
public abstract class SocialActor  implements IWallContainer, IWallEntrySubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "memberId")
  private Long id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column
  @Enumerated(EnumType.STRING)
  private SocialMemberVisibilityType visibilityType;

  @Column
  private Boolean publicSearchVisibility;

  @Column
  private Boolean communitySearchVisibility;

  @Column
  private String tweeterHashTagsMsg;

  @Column
  private String tweeterHashTagsMonitor;
  
  @OneToMany(cascade = {CascadeType.ALL})
  private List<Address> addresses;
  
  @OneToOne
  @JoinColumn
  private ManagementWorkspace defaultManagementWorkspace;

  @OneToMany(mappedBy="owner")
  @OrderBy("name")
  private List<ManagementWorkspace> managementWorkspaces;
  
  public void setManagementWorkspaces(
		List<ManagementWorkspace> managementWorkspaces) {
	this.managementWorkspaces = managementWorkspaces;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.SOCIAL_ACTOR;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public List<Address> getAddresses() {
	if(addresses == null){
		addresses = new ArrayList<Address>();
	}
	return addresses;
  }

  public void setAddresses(List<Address> addresses) {
	this.addresses = addresses;
  }

  public String getTweeterHashTagsMsg() {
    return tweeterHashTagsMsg;
  }

  public void setTweeterHashTagsMsg(String tweeterHashTagsMsg) {
    this.tweeterHashTagsMsg = tweeterHashTagsMsg;
  }

  public String getTweeterHashTagsMonitor() {
    return tweeterHashTagsMonitor;
  }

  public void setTweeterHashTagsMonitor(String tweeterHashTagsMonitor) {
    this.tweeterHashTagsMonitor = tweeterHashTagsMonitor;
  }

  public SocialMemberVisibilityType getVisibilityType() {
    return visibilityType;
  }

  public void setVisibilityType(SocialMemberVisibilityType visibilityType) {
    this.visibilityType = visibilityType;
  }

  public Boolean getPublicSearchVisibility() {
    return publicSearchVisibility;
  }

  public Boolean getCommunitySearchVisibility() {
    return communitySearchVisibility;
  }

  public void setCommunitySearchVisibility(Boolean communitySearchVisibility) {
    this.communitySearchVisibility = communitySearchVisibility;
  }

  public void setPublicSearchVisibility(Boolean publicSearchVisibility) {
    this.publicSearchVisibility = publicSearchVisibility;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public void setDefaultManagementWorkspace(
      ManagementWorkspace defaultManagementWorkspace) {
    this.defaultManagementWorkspace = defaultManagementWorkspace;
  }
  
  public ManagementWorkspace getDefaultManagementWorkspace() {
    return defaultManagementWorkspace;
  }
  
  public List<ManagementWorkspace> getManagementWorkspaces() {
    return managementWorkspaces;
  }
  
  public abstract String getImage();
  
  public abstract String getDisplayName();
}
