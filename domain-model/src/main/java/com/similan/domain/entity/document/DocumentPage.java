package com.similan.domain.entity.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "DocumentPage")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {
    "documentinstance_id", "number" }) })
public class DocumentPage implements
    ICommentable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private DocumentInstance documentInstance;

  @Column(nullable = false)
  private int number;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Asset pageAsset;

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private Asset thumbnailAsset;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;

  protected DocumentPage() {
  }

  public DocumentPage(int number) {
    this.number = number;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.DOCUMENT_PAGE;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DocumentInstance getDocumentInstance() {
    return documentInstance;
  }

  public void setDocumentInstance(DocumentInstance documentInstance) {
    this.documentInstance = documentInstance;
  }

  public int getNumber() {
    return number;
  }

  public void setPageAsset(Asset pageAsset) {
    this.pageAsset = pageAsset;
  }

  public Asset getPageAsset() {
    return pageAsset;
  }

  public void setThumbnailAsset(Asset thumbnailAsset) {
    this.thumbnailAsset = thumbnailAsset;
  }

  public Asset getThumbnailAsset() {
    return thumbnailAsset;
  }

  public void setLastComment(Comment lastComment) {
    this.lastComment = lastComment;
  }

  public Comment getLastComment() {
    return lastComment;
  }
}
