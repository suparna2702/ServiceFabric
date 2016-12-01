package com.similan.domain.entity.document;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "DocumentInstance")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "document_id",
    "version" }) })
@Getter
@Setter
public class DocumentInstance implements ICommentable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Document document;

  @Column(nullable = false)
  private int version;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private Asset originalAsset;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private Asset iconAsset;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private Asset textAsset;

  @Column(nullable = true)
  private String viewerId;

  @OneToMany(mappedBy = "documentInstance")
  private List<DocumentViewerItem> items = new LinkedList<>();

  @OneToMany(mappedBy = "documentInstance")
  @OrderColumn(name = "number")
  private List<DocumentPage> pages = new LinkedList<>();

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;

  @Column(columnDefinition = "TEXT")
  private String text;

  protected DocumentInstance() {
  }

  public DocumentInstance(int version) {
    this.version = version;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.DOCUMENT_INSTANCE;
  }

}
