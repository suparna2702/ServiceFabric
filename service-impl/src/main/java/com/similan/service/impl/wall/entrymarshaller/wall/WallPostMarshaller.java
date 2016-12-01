package com.similan.service.impl.wall.entrymarshaller.wall;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.wall.wall.LinkReference;
import com.similan.domain.entity.wall.wall.WallPost;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.wall.WallEntryMarshaller;
import com.similan.service.impl.wall.entrymarshaller.link.LinkReferenceMarshaller;

@Component
public class WallPostMarshaller extends
    WallEntryMarshaller<WallPost, IWallContainerKey> {

  @Autowired
  private LinkReferenceMarshaller linkMarshaller;

  @Override
  public WallEntryDto<IWallContainerKey> marshall(
      WallEntryKey<IWallContainerKey> entryKey, ActorDto initiatorDto,
      Date date, WallPost entry) {
    String content = entry.getContent();
    LinkReferenceDto link = marshall(entry.getLink());
    WallPostDto<IWallContainerKey> entryDto = new WallPostDto<>(entryKey,
        initiatorDto, date, content, link);
    return entryDto;
  }

  private LinkReferenceDto marshall(LinkReference link) {
    if (link == null) {
      return null;
    }
    return linkMarshaller.marshall(link);
  }

}
