package com.similan.domain.repository.community;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.community.SocialPersonExpertise;
import com.similan.domain.entity.community.SocialPersonInterest;
import com.similan.domain.repository.community.jpa.JpaSocialPersonRepository;

@Repository
public class SocialPersonRepository {
  @Autowired
  private JpaSocialPersonRepository repository;

  public List<SocialPerson> findAll() {
    return repository.findAll();
  }

  public SocialPerson findOne(Long id) {
    return repository.findOne(id);
  }

  public SocialPerson save(SocialPerson person) {
    return repository.save(person);
  }

  public SocialPerson findByEmail(String email) {
    return repository.findByEmail(email);
  }

  public SocialPerson findByEmailAndState(String email, MemberState state) {
    return repository.findByEmailAndState(email, state);
  }
	
  public Long getTotalMemberCount() {
    return repository.getTotalMemberCount();
  }

  public Long getTotalMemberCountByStatus(MemberState state) {
    return repository.getTotalMemberCountByStatus(state);
  }
	
  public String findPhotoById(Long id) {
    return repository.findPhotoById(id);
  }
  
  public void delete(Long memberId) {
    repository.delete(memberId);
  }

  public SocialPerson create(String name) {
    SocialPerson person = new SocialPerson();
    person.setName(name);
    return person;
  }

  public SocialPersonExpertise createExpertise() {
    SocialPersonExpertise expertise = new SocialPersonExpertise();
    return expertise;
  }

  public SocialPersonInterest createInterest() {
    SocialPersonInterest interest = new SocialPersonInterest();
    return interest;
  }
  
  public List<String> getAllMemberEmails(){
    return this.repository.getAllMemberEmails();
  }

  public SocialContact createContact(SocialPerson owner, SocialActor contact) {
    SocialContact socialContact = new SocialContact();

    socialContact.setContactFrom((SocialActor) owner);
    socialContact.setContactTo(contact);
    socialContact.setCreated(new Date());

    return socialContact;
  }

}
