package com.similan.service.internal.api.event.io.partner;

import com.similan.service.internal.api.event.io.Event;

public class PartnerProgramEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long partnerProgramId;

  protected PartnerProgramEvent() {

  }

  public Long getPartnerProgramId() {
    return partnerProgramId;
  }

  public void setPartnerProgramId(Long partnerProgramId) {
    this.partnerProgramId = partnerProgramId;
  }

}
