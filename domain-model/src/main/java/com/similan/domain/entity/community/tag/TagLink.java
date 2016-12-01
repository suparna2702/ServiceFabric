package com.similan.domain.entity.community.tag;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;

@Getter
@Setter
@ToString
public class TagLink implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "parent_id")
  private Tag parent;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "child_id")
  private Tag child;

  @Override
  public EntityType getEntityType() {
    return EntityType.TAG_LINK;
  }

}
