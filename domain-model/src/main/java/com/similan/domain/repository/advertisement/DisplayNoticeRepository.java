package com.similan.domain.repository.advertisement;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.advertisement.jpa.JpaDisplayNoticeRepository;

@Repository
public class DisplayNoticeRepository {
  @Autowired
  private JpaDisplayNoticeRepository repository;

  public DisplayNotice findOne(Long id) {
    return repository.findOne(id);
  }

  public DisplayNotice findByUuid(String uuid) {
    return repository.findByUuid(uuid);
  }

  public DisplayNotice save(DisplayNotice advert) {
    return repository.save(advert);
  }

  public List<DisplayNotice> findByOwner(SocialOrganization owner) {
    return repository.findByOwner(owner);
  }

  public List<DisplayNotice> findByOwnerAndActive(SocialOrganization owner,
      Boolean active) {
    return repository.findByOwnerAndActive(owner, active);
  }

  public DisplayNotice create(SocialOrganization owner, SocialPerson creator,
      String iconAsset, String name) {

    DisplayNotice retAdvert = new DisplayNotice();
    retAdvert.setCreator(creator);
    retAdvert.setOwner(owner);
    retAdvert.setCreateDate(new Date());
    retAdvert.setIconAsset(iconAsset);
    retAdvert.setName(name);

    return retAdvert;
  }

}
