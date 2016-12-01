package com.similan.domain.entity.document;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.share.ISharable;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Document")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "workspace_id",
    "name", "deletiondate" }) })
@FilterDef(name = "notDeleted")
@Filter(name = "notDeleted", condition = "deletiondate is null")
@Getter
@Setter
public class Document implements IWallEntrySubject, IBookmarkable, ISharable,
    ICommentable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = true)
  private Date deletionDate;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(length = 5000)
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private ManagementWorkspace workspace;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private DocumentInstance lastInstance;

  @OneToMany(mappedBy = "document")
  @OrderBy("version DESC")
  private List<DocumentInstance> instances;

  @ManyToMany(mappedBy = "documents")
  @OrderBy("name")
  private List<DocumentLabel> labels;

  @ManyToMany(mappedBy = "documents")
  @OrderBy("name")
  private List<DocumentCategory> categories;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private SocialActor lockOwner;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;

  protected Document() {
  }

  public Document(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.DOCUMENT;
  }

}
