package com.similan.service.api.managementworkspace.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

public class ManagementWorkspaceStatisticsDto extends
    KeyHolderDto<ManagementWorkspaceKey> {

  private Long documentCount;

  private Long documentViewedCount;

  private Long documentDownloadCount;

  private Long participantCount;

  protected ManagementWorkspaceStatisticsDto() {

  }

  public ManagementWorkspaceStatisticsDto(Long documentCount,
      Long participantCount, Long documentViewedCount,
      Long documentDownloadCount) {
    this.documentCount = documentCount;
    this.documentViewedCount = documentViewedCount;
    this.participantCount = participantCount;
    this.documentDownloadCount = documentDownloadCount;
  }

  public Long getDocumentCount() {
    return documentCount;
  }

  public Long getDocumentViewedCount() {
    return documentViewedCount;
  }

  public Long getDocumentDownloadCount() {
    return documentDownloadCount;
  }

  public Long getParticipantCount() {
    return participantCount;
  }

  @Override
  public String toString() {
    return "ManagementWorkspaceStatisticsDto [documentCount=" + documentCount
        + ", documentViewedCount=" + documentViewedCount
        + ", documentDownloadCount=" + documentDownloadCount
        + ", participantCount=" + participantCount + "]";
  }

}
