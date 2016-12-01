package com.similan.service.api.settings.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartnerProgramNotificationConfigurationDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean partnerJoined = Boolean.TRUE;

  private Boolean partnerProgramUpdate = Boolean.TRUE;

}
