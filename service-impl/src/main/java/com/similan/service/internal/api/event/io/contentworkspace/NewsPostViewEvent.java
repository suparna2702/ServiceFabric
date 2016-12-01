package com.similan.service.internal.api.event.io.contentworkspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceEvent;

@Getter
@Setter
@ToString
public class NewsPostViewEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private Long newsPostId;

  private Long viewerId;

}
