package com.similan.service.impl.wall.entrymarshaller.advertisement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.advertisement.DisplayNoticeViewedWallEntry;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.advertisement.DisplayNoticeViewedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.advertisement.DisplayNoticeMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class DisplayNoticeViewedWallEntryMarshaller extends
    WallEntryMarshaller<DisplayNoticeViewedWallEntry, SocialActorKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private DisplayNoticeMarshaller displayNoticeMarshaller;

  @Override
  public WallEntryDto<SocialActorKey> marshall(
      WallEntryKey<SocialActorKey> entryKey,
      ActorDto initiatorDto, Date date,
      DisplayNoticeViewedWallEntry entry) {

    GenericReference<IWallEntrySubject> subject = entry.getSubject();
    IKeyHolderDto<DisplayNoticeKey> noticeKey = genericReferenceMarshaller
        .marshall(subject, IWallEntrySubjectKey.class);

    DisplayNotice displayNotice = displayNoticeMarshaller
        .unmarshallDisplayNoticeKey(noticeKey.getKey(), true);

    DisplayNoticeViewedWallEntryDto wallEntryDto = new DisplayNoticeViewedWallEntryDto(
        entryKey, initiatorDto, date);
    wallEntryDto.setIconAsset(displayNotice.getIconAsset());
    wallEntryDto.setId(displayNotice.getId());
    wallEntryDto.setName(displayNotice.getName());

    return wallEntryDto;
  }
}
