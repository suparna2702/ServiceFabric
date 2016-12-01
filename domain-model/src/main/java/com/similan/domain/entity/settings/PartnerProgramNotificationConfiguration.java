package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class PartnerProgramNotificationConfiguration {

  @Column
  private Boolean partnerJoined = Boolean.TRUE;

  @Column
  private Boolean partnerProgramUpdate = Boolean.TRUE;

}
