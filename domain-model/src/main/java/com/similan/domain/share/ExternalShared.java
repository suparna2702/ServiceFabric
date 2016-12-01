package com.similan.domain.share;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "ExternalShared")
@ToString
@Getter
@Setter
public class ExternalShared extends Shared {

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private ExternalSocialPerson sharedTo;

  @Override
  public EntityType getEntityType() {
    return EntityType.EXTERNAL_SHARED;
  }

}
