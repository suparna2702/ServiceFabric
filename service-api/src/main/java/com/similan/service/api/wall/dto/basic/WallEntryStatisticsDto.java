package com.similan.service.api.wall.dto.basic;

import java.io.Serializable;
import java.util.List;

public class WallEntryStatisticsDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long totalEntry;

  private List<WallEntryTypeCountDto> wallEntryCount;

  public WallEntryStatisticsDto(Long totalEntry,
      List<WallEntryTypeCountDto> wallEntryCount) {
    this.totalEntry = totalEntry;
    this.wallEntryCount = wallEntryCount;
  }

  public Long getTotalEntry() {
    return totalEntry;
  }

  public List<WallEntryTypeCountDto> getWallEntryCount() {
    return wallEntryCount;
  }

  @Override
  public String toString() {
    return "WallEntryStatisticsDto [totalEntry=" + totalEntry
        + ", wallEntryCount=" + wallEntryCount + "]";
  }

}
