package com.similan.domain.entity.community.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "TaggedEntity")
@Getter
@Setter
@ToString
public class TaggedEntity implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn
  private Tag tag;

  private GenericReference<ITaggableEntity> taggedEntity;

  @Override
  public EntityType getEntityType() {
    return EntityType.TAGGED_ENTITY;
  }

}
