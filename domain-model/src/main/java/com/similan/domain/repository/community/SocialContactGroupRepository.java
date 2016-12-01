package com.similan.domain.repository.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContactGroup;
import com.similan.domain.repository.community.jpa.JpaSocialContactGroupRepository;

@Repository
public class SocialContactGroupRepository {
  @Autowired
  private JpaSocialContactGroupRepository repository;

  public SocialContactGroup save(SocialContactGroup person) {
    return repository.save(person);
  }
  
  public SocialContactGroup create() {
	  SocialContactGroup group = new SocialContactGroup();
    return group;
  }

  public SocialContactGroup findOne(Long contactId) {
    return repository.findOne(contactId);
  }
  
  public List<SocialContactGroup> findByOwner(SocialActor owner) {
    return repository.findByOwner(owner);
  }

}
