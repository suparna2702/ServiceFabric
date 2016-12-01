package com.similan.domain.entity.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.similan.service.api.base.dto.key.EntityType;

@Embeddable
public class GenericReference<T extends IDomainEntity> {

  @Enumerated(EnumType.STRING)
  @Column
  private EntityType type;

  @Column
  private Long id;
  
  protected GenericReference(){
  }

  public GenericReference(EntityType type, Long id) {
    this.type = type;
    this.id = id;
  }

  public EntityType getType() {
    return type;
  }

  public Long getId() {
    return id;
  }

  public void setType(EntityType type) {
	 this.type = type;
  }

  public void setId(Long id) {
	 this.id = id;
  }
  
}
