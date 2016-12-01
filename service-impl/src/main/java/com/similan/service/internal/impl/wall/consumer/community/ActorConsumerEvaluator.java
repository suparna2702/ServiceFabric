package com.similan.service.internal.impl.wall.consumer.community;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.service.internal.impl.wall.ConsumerEvaluator;

@Slf4j
@Component
public class ActorConsumerEvaluator implements ConsumerEvaluator<SocialActor> {
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;

  @Override
  public Set<SocialActor> getConsumers(SocialActor initiator, SocialActor wallContainer) {
    Set<SocialActor> consumers = new HashSet<SocialActor>();
    
    //create an entry in own feed so that it shows on own network wall
    consumers.add(initiator);

    //direct contect from
    List<SocialContact> contactsFrom = socialContactRepository.findByContactFrom(wallContainer);
    for (SocialContact contact : contactsFrom) {
      log.info("Contact found " + contact.getContactFrom().getName());
      consumers.add(contact.getContactTo());
    }
    
    //contact to
    List<SocialContact> contactsTo = socialContactRepository.findByContactTo(wallContainer);
    for (SocialContact contact : contactsTo) {
        log.info("Contact found " + contact.getContactTo().getName());
        consumers.add(contact.getContactFrom());
      }

    if (wallContainer instanceof SocialOrganization) {
      Set<SocialActor> orgContact = getSpecificConsumers(initiator,
          (SocialOrganization) wallContainer);
      consumers.addAll(orgContact);
    }
    return consumers;
  }

  protected Set<SocialActor> getSpecificConsumers(SocialActor initiator, 
      SocialOrganization wallContainer) {
    Set<SocialActor> consumers = new HashSet<SocialActor>();
    List<SocialEmployee> employees = this.socialEmployeeRepository.findByContactFrom(wallContainer);
    for (SocialEmployee employee : employees) {
      if (employee.getContactTo() != initiator) {
        consumers.add(employee.getContactTo());
      }
    }
    return consumers;
  }

}
