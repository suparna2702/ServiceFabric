package com.similan.domain.entity.document;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.document.dto.basic.DocumentViewerItemType;

@Entity(name = "DocumentViewerItem")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "documentinstance_id",
    "name" }))
public abstract class DocumentViewerItem  implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private DocumentInstance documentInstance;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, insertable = false, updatable = false)
  private DocumentViewerItemType type;

  protected DocumentViewerItem() {
  }

  public DocumentViewerItem(String name, DocumentViewerItemType type) {
    this.name = name;
    this.type = type;
  }
  
  @Override
  public EntityType getEntityType() {
    return EntityType.DOCUMENT_VIEWER_ITEM;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DocumentViewerItemType getType() {
    return type;
  }

}
