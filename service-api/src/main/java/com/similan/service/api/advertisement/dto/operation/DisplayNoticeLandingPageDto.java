package com.similan.service.api.advertisement.dto.operation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;

@Getter
@Setter
@ToString
public class DisplayNoticeLandingPageDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private DisplayNoticeKey noticeKey;

  private Long id;

  private String name;

  private String text;

  private String url;

}
