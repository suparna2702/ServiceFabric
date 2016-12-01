package com.similan.service.impl.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.BusinessDto;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.error.ActorErrorCode;
import com.similan.service.api.community.dto.error.ActorException;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.impl.Marshaller;

@Component
public class SocialActorMarshaller extends Marshaller {
  @Autowired
  private SocialActorRepository socialActorRepository;

  public SocialActor unmarshallActorKey(SocialActorKey actorKey,
      boolean required) {
    String name = actorKey.getName();
    SocialActor actor = socialActorRepository.findOne(name);
    if (actor == null && required) {
      throw new ActorException(ActorErrorCode.ACTOR_NOT_FOUND, "Actor " + actorKey
          + "  not found.");
    }
    return actor;
  }

  public SocialActorKey marshallActorKey(SocialActor actor) {
    String name = actor.getName();
    SocialActorKey key = new SocialActorKey(name);
    key.setId(actor.getId());
    return key;
  }

  public List<SocialActorKey> marshallActorKeys(List<SocialActor> actors) {
    List<SocialActorKey> actorKeys = new ArrayList<SocialActorKey>(
        actors.size());
    for (SocialActor actor : actors) {
      SocialActorKey actorKey = marshallActorKey(actor);
      actorKeys.add(actorKey);
    }
    return actorKeys;
  }

  public List<SocialActor> unmarshallActorKeys(List<SocialActorKey> actorsKeys,
      boolean required) {
    List<SocialActor> actors = new ArrayList<SocialActor>(actorsKeys.size());
    for (SocialActorKey actorsKey : actorsKeys) {
      SocialActor actor = unmarshallActorKey(actorsKey, required);
      actors.add(actor);
    }
    return actors;
  }

  public List<SocialActorContactDto> marshallActorContactsFrom(
      List<SocialContact> connections) {
    List<SocialActorContactDto> contactList = new ArrayList<SocialActorContactDto>();

    for (SocialContact contact : connections) {
      SocialActorContactDto contactDto = this
          .marshallActorContactFrom(contact);
      contactList.add(contactDto);
    }
    return contactList;
  }

  public ExternalSocialPersonDto marshallExternalSocialPersonDto(
      ExternalSocialPerson person) {
    ExternalSocialPersonDto retPerson = new ExternalSocialPersonDto(
        person.getPrimaryEmail(), person.getMobilePhone(),
        person.getMobilePhone(), person.getFirstName(), person.getLastName());
    return retPerson;

  }

  public SocialActorContactDto marshallActorContactFrom(
      SocialContact contact) {
    SocialActor contactOwner = contact.getContactFrom();
    SocialActor contactActor = contact.getContactTo();

    ActorDto actorDto = this
        .marshallActor(contactActor);
    SocialActorKey ownerKey = this.marshallActorKey(contactOwner);

    SocialActorContactDto contactDto = new SocialActorContactDto(ownerKey,
        actorDto, contact.getContactType(), contact.getCreated());
    return contactDto;
  }

  public List<SocialActorContactDto> marshallActorContactsTo(
      List<SocialContact> connections) {
    List<SocialActorContactDto> contactList = new ArrayList<SocialActorContactDto>();

    for (SocialContact contact : connections) {
      SocialActorContactDto contactDto = this
          .marshallActorContactTo(contact);
      contactList.add(contactDto);
    }
    return contactList;
  }

  public SocialActorContactDto marshallActorContactTo(
      SocialContact contact) {
    SocialActor contactOwner = contact.getContactTo();
    SocialActor contactActor = contact.getContactFrom();

    ActorDto displayableContact = this
        .marshallActor(contactActor);
    SocialActorKey ownerKey = this.marshallActorKey(contactOwner);

    SocialActorContactDto contactDto = new SocialActorContactDto(ownerKey,
        displayableContact, contact.getContactType(), contact.getCreated());
    return contactDto;
  }

  public ActorDto marshallActor(SocialActor actor) {
    if (actor instanceof SocialPerson) {
      return marshallMember((SocialPerson) actor);
    } else if (actor instanceof SocialOrganization) {
      return marshallBusiness((SocialOrganization) actor);
    }
    throw new UnsupportedOperationException("Invalid actor " + actor.getClass().getName());
  }
  
  public BusinessDto marshallBusiness(SocialOrganization business) {
    SocialActorKey key = marshallActorKey(business);

    BusinessDto businessDto = new BusinessDto(
        key, business.getBusinessName());
    businessDto.setContactEmail(business.getPrimaryEmail());
    businessDto.setId(business.getId());
    businessDto.setDisplayImage(business.getImage());
    businessDto.setId(business.getId());
    return businessDto;
  }
  

  public MemberDto marshallMember(SocialPerson member) {
    SocialActorKey key = marshallActorKey(member);
    String businessName = StringUtils.EMPTY;

    if (member.getEmployer() != null) {
      SocialEmployee employee = member.getEmployer();
      SocialOrganization employer = (SocialOrganization) employee
          .getContactFrom();
      businessName = employer.getBusinessName();
    }

    MemberDto membedDto = new MemberDto(key, member.getFirstName(),
        member.getLastName());
    membedDto.setEmployer(businessName);
    membedDto.setContactEmail(member.getPrimaryEmail());
    membedDto.setId(member.getId());
    membedDto.setDisplayImage(member.getImage());
    return membedDto;
  }
}
