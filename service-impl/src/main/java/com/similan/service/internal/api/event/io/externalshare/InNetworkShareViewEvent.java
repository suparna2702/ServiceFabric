package com.similan.service.internal.api.event.io.externalshare;

import lombok.Getter;

import com.similan.service.internal.api.event.io.Event;

@Getter
public class InNetworkShareViewEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long inNetworkShareShare;

  public InNetworkShareViewEvent(Long inNetworkShareShare) {
    this.inNetworkShareShare = inNetworkShareShare;
  }

}
