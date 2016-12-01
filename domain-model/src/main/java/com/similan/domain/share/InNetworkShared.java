package com.similan.domain.share;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "InNetworkShared")
@ToString
@Getter
@Setter
public class InNetworkShared extends Shared {
  

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor sharedToActor;
  
  @Override
  public EntityType getEntityType() {
    return EntityType.INNETWORK_SHARED;
  }

}
