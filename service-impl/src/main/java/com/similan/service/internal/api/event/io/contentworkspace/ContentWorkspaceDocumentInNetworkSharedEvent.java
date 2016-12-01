package com.similan.service.internal.api.event.io.contentworkspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentWorkspaceDocumentInNetworkSharedEvent extends
    ContentWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private Long inNetworkSharedId;

}
