package com.similan.service.impl.connection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.ConnectionService;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;

@Service
public class ConnectionServiceImpl extends ServiceImpl implements
    ConnectionService {
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  @Transactional
  public List<SocialActorContactDto> getDirectConnections(
      SocialActorKey ownerKey) {
    SocialActor socialActor = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    //connections from are also included
    List<SocialContact> connectionsFrom = socialContactRepository
        .findByContactFrom(socialActor);

    List<SocialContact> allContacts = new ArrayList<SocialContact>();
    if (connectionsFrom != null) {
      allContacts.addAll(connectionsFrom);
    }

    List<SocialActorContactDto> connectionsDto = actorMarshaller.marshallActorContactsFrom(allContacts);

    //connections to are also included
    List<SocialContact> connectionsTo = socialContactRepository
        .findByContactTo(socialActor);
    if (connectionsTo != null) {
      connectionsDto.addAll(actorMarshaller
          .marshallActorContactsTo(connectionsTo));
    }

    return connectionsDto;

  }

  @Override
  @Transactional
  public List<SocialActorContactDto> getExtendedConnections(
      SocialActorKey ownerKey) {

    SocialActor ownerActor = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    if (ownerActor instanceof SocialPerson) {
      SocialPerson person = (SocialPerson) ownerActor;
      return this.getSocialPersonExtendedConnections(person);
    } else if (ownerActor instanceof SocialOrganization) {
      SocialOrganization organization = (SocialOrganization) ownerActor;
      return this.getSocialOrganizationExtendedConnections(organization);
    }

    return null;
  }

  private List<SocialActorContactDto> getSocialOrganizationExtendedConnections(
      SocialOrganization org) {
    SocialActorKey orgKey = new SocialActorKey(org.getName());
    List<SocialActorContactDto> orgContactList = this
        .getDirectConnections(orgKey);
    return orgContactList;
  }

  private List<SocialActorContactDto> getSocialPersonExtendedConnections(
      SocialPerson personActor) {
    SocialActorKey ownerKey = new SocialActorKey(personActor.getName());
    List<SocialActorContactDto> contactList = this
        .getDirectConnections(ownerKey);

    if (personActor.getEmployer() != null) {
      SocialOrganization org = (SocialOrganization) personActor.getEmployer()
          .getContactFrom();
      SocialActorKey orgKey = new SocialActorKey(org.getName());
      List<SocialActorContactDto> orgContactList = this
          .getDirectConnections(orgKey);
      contactList.addAll(orgContactList);
    }

    return contactList;
  }

  @Override
  @Transactional
  public List<ActorDto> getEmployee(SocialActorKey ownerKey) {
    SocialActor ownerActor = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    List<SocialEmployee> employeeList = this.socialEmployeeRepository
        .findByContactFrom((SocialOrganization) ownerActor);

    List<ActorDto> retList = new ArrayList<ActorDto>();
    for (SocialEmployee empl : employeeList) {
      SocialActor actor = empl.getContactTo();
      ActorDto actorDto = actorMarshaller.marshallActor(actor);
      retList.add(actorDto);
    }

    return retList;
  }

}
