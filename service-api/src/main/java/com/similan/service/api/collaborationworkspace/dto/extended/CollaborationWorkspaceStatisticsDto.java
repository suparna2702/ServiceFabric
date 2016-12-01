package com.similan.service.api.collaborationworkspace.dto.extended;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

public class CollaborationWorkspaceStatisticsDto extends
    KeyHolderDto<CollaborationWorkspaceKey> {

  @XmlAttribute
  private Long documentViewCount;

  @XmlAttribute
  private Long documentDownloadCount;

  @XmlAttribute
  private Long documentCommentCount;

  @XmlAttribute
  private Long workspaceAccessCount;
  
  private Long documentSharedCount;

  protected CollaborationWorkspaceStatisticsDto() {
  }

  public CollaborationWorkspaceStatisticsDto(CollaborationWorkspaceKey key,
		  Long documentViewCount, Long documentDownloadCount, Long documentCommentCount,
		  Long workspaceAccessCount, Long documentSharedCount) {
    super(key);
    this.documentViewCount = documentViewCount;
    this.documentDownloadCount = documentDownloadCount;
    this.documentCommentCount = documentCommentCount;
    this.workspaceAccessCount = workspaceAccessCount;
    this.documentSharedCount = documentSharedCount;
  }

	public Long getDocumentViewCount() {
		return documentViewCount;
	}
	
	public Long getDocumentDownloadCount() {
		return documentDownloadCount;
	}
	
	public Long getDocumentCommentCount() {
		return documentCommentCount;
	}
	
	public Long getWorkspaceAccessCount() {
		return workspaceAccessCount;
	}
	
	public Long getDocumentSharedCount() {
		return documentSharedCount;
	}
  
  

}
