package com.similan.service.api;

import java.util.List;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.ExistingPartnerInitiateDto;
import com.similan.framework.dto.partner.ExistingPartnerInviteCompleteDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerProgramInviteDto;
import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface PartnerManagementService {

  public CollaborationWorkspaceKey getDefaultWorkspaceForParticipatingPartnerPrograms(
      Long orgId);

  public void approvePartnerProgramPerticipation(Long partnerProgramId,
      Long partnershipId, Long partnerOrgId);

  public void initiateExistingPartnerInvite(
      ExistingPartnerInitiateDto initiateDto);

  public void existingPartnerInviteComplete(
      ExistingPartnerInviteCompleteDto compleDto);

  public List<CollaborationWorkspaceKey> getWorkspacesForParticipatingPartnerPrograms(
      OrganizationDetailInfoDto orgInfo, MemberInfoDto memberInfo);

  public List<PartnerProgramInviteDto> getPartnerInvitesByInviter(
      Long inviterParamId);

  public List<PartnerProgramInviteDto> getPartnerInvitesByPartnerProgramBusiness(
      Long organizationId);

  public void sendPartnerInviteReminder(List<PartnerProgramInviteDto> invites,
      SocialActorKey remindSender);

  public Boolean isPartnerForProgram(Long orgId, Long progId);

  public CollaborationWorkspaceKey getPartnerWorkspace(Long partnerProgramId);

  public PartnershipDto getPartnershipDtoById(Long id);

  public List<PartnerProgramDefinitionDto> getPartnerPrograms();

  public List<PartnerProgramDefinitionDto> getPartnerProgramsForBusiness(
      OrganizationDetailInfoDto orgInfo);

  public List<PartnershipDto> getPartnersForProgram(Long programId);

  public void participateInPartnerProgram(OrganizationDetailInfoDto partOrg,
      PartnerProgramDefinitionDto selectedPartnerProgram,
      MemberInfoDto memberInfo);

  public void partnerProgramApprovalNotification(long msId,
      String processInstanceId, boolean approved, String comment);

  public PartnerProgramDefinitionDto getPartnerProgramById(Long id);

  public Long savePartnerProgram(PartnerProgramDefinitionDto partnerProgram);

  public void submitPartnerProgramApprovalFormInput(
      PartnerProgramDefinitionDto partnerProg, Long partnerOrgId,
      Long memStateId, Long inviterId, String processInstanceId);

  public void rejectPartnerProgramPerticipation(Long valueOf);

}
