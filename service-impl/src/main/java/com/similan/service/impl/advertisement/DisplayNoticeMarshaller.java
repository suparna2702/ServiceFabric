package com.similan.service.impl.advertisement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.advertisement.DisplayNoticeLandingPage;
import com.similan.domain.repository.advertisement.DisplayNoticeLandingPageRepository;
import com.similan.domain.repository.advertisement.DisplayNoticeRepository;
import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.advertisement.dto.error.DisplayNoticeErrorCode;
import com.similan.service.api.advertisement.dto.error.DisplayNoticeException;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.advertisement.dto.operation.DisplayNoticeLandingPageDto;
import com.similan.service.impl.Marshaller;

@Component
public class DisplayNoticeMarshaller extends Marshaller {

  @Autowired
  private DisplayNoticeRepository displayNoticeRepository;

  @Autowired
  private DisplayNoticeLandingPageRepository displayNoticeLandingPageRepository;

  public DisplayNotice unmarshallDisplayNoticeKey(DisplayNoticeKey key,
      boolean required) {
    DisplayNotice notice = this.displayNoticeRepository.findOne(key.getId());

    if (required) {
      if (notice != null) {
        return notice;
      } else {
        throw new DisplayNoticeException(DisplayNoticeErrorCode.DISPLAY_NOTICE_NOT_FOUND, "Display notice not found");
      }
    }

    return notice;
  }

  public DisplayNoticeKey marshallDisplayNoticeKey(DisplayNotice notice) {
    DisplayNoticeKey noticeKey = new DisplayNoticeKey();
    noticeKey.setId(notice.getId());
    return noticeKey;
  }

  public DisplayNoticeDto marshallDisplayNoticeKey(DisplayNoticeKey key,
      boolean required) {
    DisplayNotice notice = this.displayNoticeRepository.findOne(key.getId());
    return marshallDisplayNotice(notice);
  }

  public List<DisplayNoticeDto> marshallDisplayNotices(
      List<DisplayNotice> noticeList) {
    List<DisplayNoticeDto> retList = new ArrayList<DisplayNoticeDto>();

    for (DisplayNotice displayNotice : noticeList) {
      DisplayNoticeDto noticeDto = this.marshallDisplayNotice(displayNotice);
      retList.add(noticeDto);
    }

    return retList;
  }

  public DisplayNoticeDto marshallDisplayNotice(DisplayNotice notice) {

    DisplayNoticeKey key = new DisplayNoticeKey();
    key.setId(notice.getId());

    DisplayNoticeDto retDto = new DisplayNoticeDto(key);
    retDto.setActive(notice.getActive());
    retDto.setIconAsset(notice.getIconAsset());
    retDto.setName(notice.getName());
    retDto.setCreateDate(notice.getCreateDate());
    retDto.setUuid(notice.getUuid());

    return retDto;
  }

  public DisplayNoticeLandingPageDto marshallDisplayNoticeLandingPage(
      DisplayNoticeLandingPage landingPage) {

    DisplayNoticeLandingPageDto landingPageRet = new DisplayNoticeLandingPageDto();
    landingPageRet.setId(landingPage.getId());
    landingPageRet.setText(landingPage.getText());
    landingPageRet.setUrl(landingPage.getUrl());

    return landingPageRet;
  }

}
