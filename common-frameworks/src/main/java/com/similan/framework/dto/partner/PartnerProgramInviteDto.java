package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartnerProgramInviteDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String programName;

  private String inviteeBusiness;
  
  private String inviteeEmail;
  
  private String inviteeFirstName;
  
  private String inviteeLastName;

  private Long workflowInstanceId;

  private Date timeStamp;

}
