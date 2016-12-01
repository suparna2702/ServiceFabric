package com.similan.service.internal.api.event.io.partner;

public class PartnerProgramJoinEvent extends PartnerProgramEvent {

  private static final long serialVersionUID = 1L;
  private Long partnershipId;

  public Long getPartnershipId() {
    return partnershipId;
  }

  public void setPartnershipId(Long partnershipId) {
    this.partnershipId = partnershipId;
  }

}
