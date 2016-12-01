package com.similan.service.internal.api.event.io.collaborationworkspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DocumentUnsharedEvent extends CollaborationWorkspaceEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private long sharedDocumentId;

  private long unsharer;
}
