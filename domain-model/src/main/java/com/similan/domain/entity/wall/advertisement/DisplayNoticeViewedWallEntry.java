package com.similan.domain.entity.wall.advertisement;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DisplayNoticeViewedWallEntry")
@DiscriminatorValue("DISPLAY_NOTICE_VIEWED")
@Setter
@Getter
public class DisplayNoticeViewedWallEntry extends WallEntry {

  protected DisplayNoticeViewedWallEntry() {

  }

  public DisplayNoticeViewedWallEntry(int number, Date date) {
    super(WallEntryType.DISPLAY_NOTICE_VIEWED, number, date);
  }

}
