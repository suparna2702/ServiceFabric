package com.similan.service.api.connection;

import java.util.List;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface ConnectionService {

  /**
   * Direct connections
   * 
   * @param ownerKey
   * @return
   */
  List<SocialActorContactDto> getDirectConnections(SocialActorKey ownerKey);

  /**
   * DIrect connections, Employees, Partners
   * 
   * @param ownerKey
   * @return
   */
  List<SocialActorContactDto> getExtendedConnections(SocialActorKey ownerKey);

   List<ActorDto> getEmployee(SocialActorKey ownerKey);
}
