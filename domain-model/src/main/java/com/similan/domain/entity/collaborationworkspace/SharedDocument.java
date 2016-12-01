package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.bookmark.IBookmarkable;
import com.similan.domain.entity.document.Document;
import com.similan.domain.share.ISharable;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "SharedDocument")
@DiscriminatorValue("SharedDocument")
@ToString
@Getter
@Setter
public class SharedDocument extends CollaborationWorkspaceElement implements
    IBookmarkable, ISharable {

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Document document;

  @Column
  private Boolean unshared = Boolean.FALSE;

  protected SharedDocument() {
  }

  public SharedDocument(Date creationDate) {
    super(creationDate);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.SHARED_DOCUMENT;
  }

}
