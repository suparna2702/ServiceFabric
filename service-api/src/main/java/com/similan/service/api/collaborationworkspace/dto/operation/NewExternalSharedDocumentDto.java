package com.similan.service.api.collaborationworkspace.dto.operation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;

@Getter
@Setter
@ToString
public class NewExternalSharedDocumentDto extends OperationDto {

  @XmlElement
  private String header;

  @XmlElement
  private String message;

  @XmlElement
  private SocialActorKey sharer;

  @XmlElement
  private CollaborationWorkspaceKey workspaceToKey;

  @XmlElement
  private List<ExternalSocialPersonDto> shareList;

}
