package com.similan.domain.entity.managementworkspace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "ManagementWorkspace")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "owner_id",
    "name" }) })
public class ManagementWorkspace implements IWallContainer, IWallEntrySubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(length = 5000)
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "owner_id")
  private SocialActor owner;

  @ManyToOne
  @JoinColumn(name = "creator_id")
  private SocialActor creator;

  @OneToMany(mappedBy = "workspace")
  @OrderBy("name")
  private List<Document> documents = new ArrayList<Document>();

  @Column
  private Date creationDate;

  protected ManagementWorkspace() {
  }

  public SocialActor getCreator() {
    return creator;
  }

  public void setCreator(SocialActor creator) {
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

  public ManagementWorkspace(String name) {
    this.name = name;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.MANAGEMENT_WORKSPACE;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public SocialActor getOwner() {
    return owner;
  }

  public void setOwner(SocialActor owner) {
    this.owner = owner;
  }

  public List<Document> getDocuments() {
    return documents;
  }

}
