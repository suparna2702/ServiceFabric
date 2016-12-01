package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ProfileNotificationConfiguration {

  @Column
  private Boolean profileUpdate = Boolean.TRUE;

}
