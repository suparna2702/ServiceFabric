package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.poll.IPollSubject;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "CollaborationWorkspace")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "owner_id",
    "name" }) })
@Getter
@Setter
public class CollaborationWorkspace implements IWallContainer,
    IWallEntrySubject, IPollSubject, IBookmarkable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(nullable = true, length = 500)
  private String logo;

  @Column(nullable = true, length = 2000)
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "owner_id")
  private SocialActor owner;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "creator_id")
  private SocialActor creator;

  @OneToMany(mappedBy = "workspace")
  private List<CollaborationWorkspaceParticipation> participations;

  @Column
  @Enumerated(EnumType.STRING)
  private CollaborationWorkspaceStatus status;

  @OneToMany(mappedBy = "workspace")
  private List<SharedDocument> sharedDocuments;

  @OneToMany(mappedBy = "workspace")
  private List<Task> tasks;

  @Column
  private Date timeStamp;

  @Column
  private Boolean partnerWorkspace = Boolean.FALSE;

  @Embedded
  private CollaborationWorkspaceSettings config = new CollaborationWorkspaceSettings();

  protected CollaborationWorkspace() {
  }

  public CollaborationWorkspace(String name, String description,
      CollaborationWorkspaceStatus status) {
    this.name = name;
    this.description = description;
    this.status = status;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.COLLABORATION_WORKSPACE;
  }

}
