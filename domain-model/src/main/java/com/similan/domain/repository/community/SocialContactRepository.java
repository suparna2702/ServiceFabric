package com.similan.domain.repository.community;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.repository.community.jpa.JpaSocialContactRepository;
import com.similan.service.api.connection.dto.basic.ContactType;

@Repository
public class SocialContactRepository {
  @Autowired
  private JpaSocialContactRepository repository;
	
	public List<SocialContact> findAll() {
    return repository.findAll();
  }

	public SocialContact findOne(Long id) {
    return repository.findOne(id);
  }

	public SocialContact save(SocialContact person) {
    return repository.save(person);
  }
	
	public List<SocialContact> findByContactFrom(SocialActor actor) {
    return repository.findByContactFrom(actor);
  }
	
	public List<SocialContact> findByContactTo(SocialActor actor) {
    return repository.findByContactTo(actor);
  }
	
	public List<SocialContact> findByContactFromAndContactType(SocialActor actor, ContactType contType) {
    return repository.findByContactFromAndContactType(actor, contType);
  }
	
	public SocialContact create(ContactType contectType, 
			SocialActor owner, SocialActor contact) {
		
		SocialContact socialContact = this.create(owner, contact);
		socialContact.setContactType(contectType);
		return socialContact;
		
	}
	
	public SocialContact create(SocialActor owner, SocialActor contact) {
	    SocialContact socialContact = new SocialContact();

	    socialContact.setContactFrom((SocialActor) owner);
	    socialContact.setContactTo(contact);
	    socialContact.setCreated(new Date());
	    socialContact.setContactType(ContactType.Unspecified);

	    return socialContact;
	}

}
