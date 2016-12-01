package com.similan.service.api.wall.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.ToString;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@Getter
@ToString
public class WallPostDto<WallContainerKey extends IWallContainerKey> extends
    WallEntryDto<WallContainerKey> {

  @XmlAttribute
  private String content;
  @XmlElement
  private LinkReferenceDto link;

  protected WallPostDto() {
  }

  public WallPostDto(WallEntryKey<WallContainerKey> key, ActorDto initiator,
      Date date, String content, LinkReferenceDto link) {
    super(key, initiator, date);
    this.content = content;
    this.link = link;
  }

  public WallEntryType getType() {
    return WallEntryType.WALL_POST;
  }

}
