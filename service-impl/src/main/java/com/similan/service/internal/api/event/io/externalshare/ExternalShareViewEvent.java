package com.similan.service.internal.api.event.io.externalshare;

import lombok.Getter;

import com.similan.service.internal.api.event.io.Event;

@Getter
public class ExternalShareViewEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long externalShare;

  public ExternalShareViewEvent(Long externalShare) {
    this.externalShare = externalShare;
  }

}
