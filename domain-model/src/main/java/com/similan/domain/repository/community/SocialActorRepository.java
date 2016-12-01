package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.community.jpa.JpaSocialActorRepository;

@Repository
public class SocialActorRepository {
  @Autowired
  JpaSocialActorRepository repository;

  public SocialActor findOne(Long id) {
    return repository.findOne(id);
  }

  public SocialActor findOne(String name) {
    return repository.findOne(name);
  }

}
