package com.similan.service.api.collaborationworkspace.dto.operation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

@Getter
@Setter
@ToString
public class NewInNetworkSharedDocumentDto extends OperationDto {
  
  @XmlElement
  private String header;

  @XmlElement
  private String message;

  @XmlElement
  private SocialActorKey sharer;
  
  @XmlElement
  private ManagementWorkspaceKey workspaceKey;
  
  @XmlElement
  private List<SocialActorKey> sharedTo;

}
