package com.similan.framework.dto.community;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalBusinessPortalDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id = Long.MIN_VALUE;

  private String uuid = UUID.randomUUID().toString();

  private String portalName;

  private String portalUrl;

}
