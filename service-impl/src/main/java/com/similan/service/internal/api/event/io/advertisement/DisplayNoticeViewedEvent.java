package com.similan.service.internal.api.event.io.advertisement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.internal.api.event.io.Event;

@Getter
@Setter
@ToString
public class DisplayNoticeViewedEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long noticeId;

  private Long viewerId;

}
