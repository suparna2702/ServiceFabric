package com.similan.domain.entity.document;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "DocumentCategory")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "owner_id",
    "name" }) })
@Getter
@Setter
@ToString
public class DocumentCategory implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "owner_id")
  private SocialActor owner;

  @ManyToMany
  @OrderBy("name")
  private List<Document> documents;

  protected DocumentCategory() {
  }

  public DocumentCategory(String name) {
    this.name = name;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.DOCUMENT_CATEGORY;
  }

}
