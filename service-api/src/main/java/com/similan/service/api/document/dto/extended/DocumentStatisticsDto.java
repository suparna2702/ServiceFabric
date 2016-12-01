package com.similan.service.api.document.dto.extended;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.document.dto.key.DocumentKey;

public class DocumentStatisticsDto extends KeyHolderDto<DocumentKey> {

  @XmlAttribute
  private Long vieweCount;

  @XmlAttribute
  private Long commentCount;

  @XmlAttribute
  private Long downloadCount;

  protected DocumentStatisticsDto() {
  }

  public DocumentStatisticsDto(DocumentKey key, Long vieweCount,
      Long commentCount, Long downloadCount) {
    super(key);
    this.vieweCount = vieweCount;
    this.commentCount = commentCount;
    this.downloadCount = downloadCount;
  }

  public Long getVieweCount() {
    return vieweCount;
  }

  public Long getCommentCount() {
    return commentCount;
  }

  public Long getDownloadCount() {
    return downloadCount;
  }

}
