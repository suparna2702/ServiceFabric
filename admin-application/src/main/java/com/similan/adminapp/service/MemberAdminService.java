package com.similan.adminapp.service;

import java.util.List;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.similan.domain.entity.community.MemberState;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.util.MemberFeedbackDto;
import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;

public interface MemberAdminService {
   public WallEntryStatisticsDto getBasicStatistics();

  /**
   * 
   * @param emailSubject
   * @param emailContent
   * @throws ResourceNotFoundException
   * @throws ParseErrorException
   * @throws Exception
   */
  public void sendEmailToAllMembers(String emailSubject, String emailContent)
      throws ResourceNotFoundException, ParseErrorException, Exception;

   public List<MemberFeedbackDto> getAllMemberFeedbacks();

   public void deleteMember(Long memberId);

   public List<MemberInfoDto> getMembers(int numMembers) throws Exception;

   public List<MemberInfoDto> getOnlineMembers(int numMembers);

   public void suspendMember(MemberInfoDto member);

   public void reactivateMember(MemberInfoDto member);

   public List<MemberInfoDto> getMemberByStatus(MemberState status);

   public List<MemberInfoDto> getEmbeddedMembers();

}
