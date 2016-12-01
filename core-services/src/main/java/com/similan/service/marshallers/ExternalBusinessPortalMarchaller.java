package com.similan.service.marshallers;

import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.community.ExternalBusinessPortal;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;

public class ExternalBusinessPortalMarchaller {

  public ExternalBusinessPortalDto marshall(
      ExternalBusinessPortal externalPortal) {

    ExternalBusinessPortalDto retPortal = new ExternalBusinessPortalDto();
    retPortal.setPortalName(externalPortal.getPortalName());
    retPortal.setPortalUrl(externalPortal.getPortalUrl());
    retPortal.setUuid(externalPortal.getUuid());
    retPortal.setId(externalPortal.getId());

    return retPortal;
  }

  public List<ExternalBusinessPortalDto> marshall(
      List<ExternalBusinessPortal> portals) {

    List<ExternalBusinessPortalDto> retList = new ArrayList<ExternalBusinessPortalDto>();
    for (ExternalBusinessPortal portal : portals) {
      ExternalBusinessPortalDto portalDto = marshall(portal);
      retList.add(portalDto);
    }

    return retList;
  }

}
