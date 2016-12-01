package com.similan.domain.repository.community;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.repository.community.jpa.JpaExternalSocialPersonRepository;

@Repository
public class ExternalSocialPersonRepository {

  @Autowired
  private JpaExternalSocialPersonRepository repository;
  
  public ExternalSocialPerson save(ExternalSocialPerson person) {
    return this.repository.save(person);
  }

  public ExternalSocialPerson findOne(Long id) {
    return this.repository.findOne(id);
  }

  public ExternalSocialPerson findByPrimaryEmail(String email) {
    return this.repository.findByPrimaryEmail(email);
  }

  public ExternalSocialPerson findByName(String name) {
    return this.repository.findByName(name);
  }

  public ExternalSocialPerson create(String firstName, String lastName,
      String mobilePhone, String primaryEmail) {

    ExternalSocialPerson retPerson = new ExternalSocialPerson();
    retPerson.setFirstName(firstName);
    retPerson.setLastName(lastName);
    retPerson.setPrimaryEmail(primaryEmail);
    retPerson.setMobilePhone(mobilePhone);
    retPerson.setName(UUID.randomUUID().toString());

    return retPerson;
  }

}
