package com.similan.service.api.feedback.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@ToString
public class NewErrorReportingDto extends OperationDto {

  private String memberFeedback;

  private SocialActorKey reporter;

}
