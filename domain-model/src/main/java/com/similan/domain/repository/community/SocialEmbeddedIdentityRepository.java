package com.similan.domain.repository.community;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmbeddedIdentity;
import com.similan.domain.repository.community.jpa.JpaSocialEmbeddedIdentityRepository;

@Repository
public class SocialEmbeddedIdentityRepository {
  @Autowired
  private JpaSocialEmbeddedIdentityRepository repository;
	
	public SocialEmbeddedIdentity findEmbeddedIdentity(String UUID) {
    return repository.findEmbeddedIdentity(UUID);
  }
	
	public SocialEmbeddedIdentity findEmbeddedIdentity(SocialActor socialActor) {
    return repository.findEmbeddedIdentity(socialActor);
  }
	
	public SocialEmbeddedIdentity save(SocialEmbeddedIdentity embdIdentity) {
    return repository.save(embdIdentity);
  }
	
	public List<SocialEmbeddedIdentity> findAll() {
    return repository.findAll();
  }
	
	public SocialEmbeddedIdentity create(SocialActor socialActor){
		UUID embeddedUUID = UUID.randomUUID();
		
		SocialEmbeddedIdentity identity = new SocialEmbeddedIdentity();
		identity.setEmbeddedActor(socialActor);
		identity.setUuid(embeddedUUID.toString());
		return identity;
	}



}
