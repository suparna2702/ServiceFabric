package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.service.api.connection.dto.basic.ContactType;

public interface JpaSocialContactRepository extends JpaRepository<SocialContact, Long> {
	
  List<SocialContact> findByContactFrom(SocialActor actor);
	
	List<SocialContact> findByContactTo(SocialActor actor);
	
	List<SocialContact> findByContactFromAndContactType(SocialActor actor, ContactType contType);
}
