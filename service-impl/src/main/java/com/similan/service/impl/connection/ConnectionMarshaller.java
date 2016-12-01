package com.similan.service.impl.connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.dto.basic.ConnectionDto;
import com.similan.service.api.connection.dto.basic.ContactType;
import com.similan.service.api.connection.dto.key.ConnectionKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Component
public class ConnectionMarshaller extends Marshaller {
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  
  public ConnectionKey marshallConnectionKey(SocialContact contact) {
    SocialActorKey fromKey = actorMarshaller
        .marshallActorKey(contact.getContactFrom());
    SocialActorKey toKey = actorMarshaller
        .marshallActorKey(contact.getContactTo());

    ConnectionKey connectionKey = new ConnectionKey(fromKey, toKey);
    return connectionKey;
  }

  public ConnectionDto marshallConnection(SocialContact connection) {
    ConnectionKey connectionKey = this.marshallConnectionKey(connection);
    SocialActor contact = connection.getContactTo();
    ActorDto contactDto = actorMarshaller.marshallActor(contact);
    Date date = connection.getCreated();
    ContactType type = connection.getContactType();
    ConnectionDto retConnectionDto = new ConnectionDto(connectionKey,
        contactDto, type, date);
    return retConnectionDto;
  }

  public List<ConnectionDto> marshallConnections(Set<SocialContact> connections) {
    List<ConnectionDto> connectionDtos = new ArrayList<ConnectionDto>(
        connections.size());
    for (SocialContact connection : connections) {
      ConnectionDto connectionDto = marshallConnection(connection);
      connectionDtos.add(connectionDto);
    }
    return connectionDtos;
  }
}
