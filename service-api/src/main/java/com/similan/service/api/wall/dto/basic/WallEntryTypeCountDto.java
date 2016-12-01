package com.similan.service.api.wall.dto.basic;

import java.io.Serializable;

public class WallEntryTypeCountDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private WallEntryType entryType;

  private Long count = 0L;

  public WallEntryTypeCountDto(Long count, WallEntryType entryType) {
    this.count = count;
    this.entryType = entryType;
  }

  public WallEntryType getEntryType() {
    return entryType;
  }

  public Long getCount() {
    return count;
  }

  @Override
  public String toString() {
    return "WallEntryTypeCountDto [entryType=" + entryType + ", count=" + count
        + "]";
  }

}
