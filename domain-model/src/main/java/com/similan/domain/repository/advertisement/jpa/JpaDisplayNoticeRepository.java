package com.similan.domain.repository.advertisement.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaDisplayNoticeRepository extends
    JpaRepository<DisplayNotice, Long> {
  
  public DisplayNotice findByUuid(String uuid);

  public List<DisplayNotice> findByOwner(SocialOrganization owner);

  public List<DisplayNotice> findByOwnerAndActive(SocialOrganization owner,
      Boolean active);

}
