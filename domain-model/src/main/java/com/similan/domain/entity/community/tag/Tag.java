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

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Tag")
@Getter
@Setter
@ToString
public class Tag implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "owner_id")
  private SocialOrganization owner;

  @Column
  private Boolean hasChild = Boolean.FALSE;

  @Override
  public EntityType getEntityType() {
    return EntityType.TAG;
  }

}
