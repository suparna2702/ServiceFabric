package com.similan.service.api.wall.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class WallEntryDto<WallContainerKey extends IWallContainerKey>
    extends KeyHolderDto<WallEntryKey<WallContainerKey>> implements
    Comparable<WallEntryDto<WallContainerKey>> {

  @XmlElement
  private ActorDto initiator;

  @XmlAttribute
  private Date date;

  protected WallEntryDto() {
  }

  public WallEntryDto(WallEntryKey<WallContainerKey> key, ActorDto initiator,
      Date date) {
    super(key);
    this.initiator = initiator;
    this.date = date;
  }

  public abstract WallEntryType getType();

  public ActorDto getInitiator() {
    return initiator;
  }

  public Date getDate() {
    return date;
  }

  /**
   * This is for sorting purpose
   * 
   * @param compareTo
   * @return
   */
  public int compareTo(WallEntryDto<WallContainerKey> compareTo) {
    Date fromDate = this.getDate();
    Date toDate = compareTo.getDate();

    if (fromDate.before(toDate)) {
      return 1;
    } else if (fromDate.after(toDate)) {
      return -1;
    }

    return 0;
  }

}
