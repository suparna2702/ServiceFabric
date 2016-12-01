package com.similan.adminapp.service;

import java.util.List;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.MemberState;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.util.MemberFeedbackDto;
import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;
import com.similan.service.impl.MemberManagementServiceImpl;

@Service("memberAdminService")
public class MemberAdminServiceImpl implements MemberAdminService {

  @Autowired
  private MemberManagementServiceImpl memberManagementService;

  @Transactional
  public WallEntryStatisticsDto getBasicStatistics(){
    return memberManagementService.getBasicStatistics();
  }

  public MemberManagementServiceImpl getMemberManagementService() {
    return memberManagementService;
  }

  public void setMemberManagementService(
      MemberManagementServiceImpl memberManagementService) {
    this.memberManagementService = memberManagementService;
  }

  @Transactional
  public List<MemberFeedbackDto> getAllMemberFeedbacks() {
    return memberManagementService.getAllMemberFeedbacks();
  }

  @Transactional
  public List<MemberInfoDto> getMembers(int numMembers) throws Exception {
    return this.memberManagementService.getMembers(numMembers);
  }

  @Transactional
  public List<MemberInfoDto> getEmbeddedMembers() {
    return this.memberManagementService.getAllEmbeddedMembers();
  }

  @Transactional
  public List<MemberInfoDto> getOnlineMembers(int numMembers) {
    return this.memberManagementService.getOnlineMembers();
  }

  @Transactional
  public void suspendMember(MemberInfoDto member) {
    // TODO Auto-generated method stub

  }

  @Transactional
  public void deleteMember(Long memberId) {
    this.memberManagementService.deleteMember(memberId);
  }

  @Transactional
  public void reactivateMember(MemberInfoDto member) {
    // TODO Auto-generated method stub

  }

  public List<MemberInfoDto> getMemberByStatus(MemberState status) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @Transactional
  public void sendEmailToAllMembers(String emailSubject, String emailContent)
      throws ResourceNotFoundException, ParseErrorException, Exception {
    this.memberManagementService.sendAdminNotificationMembers(emailSubject,
        emailContent);

  }
}
