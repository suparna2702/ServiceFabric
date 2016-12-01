package com.similan.service.api;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.MemberFeedbackStatus;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.framework.dto.MemberInviteListDto;
import com.similan.framework.dto.NonmemberContactMessageInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.MemberInviteInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.util.MemberFeedbackDto;
import com.similan.service.api.feedback.dto.NewErrorReportingDto;
import com.similan.service.exception.CoreServiceException;

/**
 * 
 * @author supapal
 * 
 */
public interface MemberManagementService {

	public void sendAdminNotificationMembers(String emailSubject, String emailContent)
			throws ResourceNotFoundException, ParseErrorException, Exception;

	public void contactUs(NonmemberContactMessageInfoDto message);

	public void deleteInviteRequest(Long inviteRequestId);

	public void reInviteMember(Long inviteRequestId);

	public List<MemberInviteInfoDto> findPendingInvitesByInviter(Long inviterParamId);

	public MemberInviteListDto inviteListFromStream(InputStreamReader streamReader);

	public void addMemberFeedback(Long memberId, String memberFeedback);

	public List<MemberFeedbackDto> getAllMemberFeedbacks();

	public List<MemberFeedbackDto> getAllMemberFeedbacks(MemberFeedbackStatus status);

	public MemberInfoDto memberLogin(String email, String password) throws CoreServiceException;

	public MemberInfoDto embeddedLogin(String uuid) throws CoreServiceException;

	public MemberInfoDto memberById(Long id) throws Exception;

	public SocialPerson memberByEmail(String email) throws Exception;

	public boolean inviteInitiate(String firstName, String lastName, String email, String inviteeCompany,
			String memberInviteType, String firstName2, String lastName2, long id);

	public void memberUpdate(MemberInfoDto memberInfo) throws Exception;

	public boolean memberEmailChangeInitiate(MemberInfoDto member) throws CoreServiceException;

	public boolean memberEmailChangeComplete(long memberId, String pid);

	public boolean memberPasswordChange(MemberInfoDto member);

	public List<CommunityEventDto> getLoginInfoList(Long id);

	public List<MemberInfoDto> getMembers(int number) throws Exception;

	public List<MemberInfoDto> getAllEmbeddedMembers();

	public List<MemberInfoDto> getOnlineMembers();

	public List<SocialPerson> findAllMembers();

	public void memberRequestToJoin(String firstName, String lastName, String email, String businessName);

	public void existingMemberAcceptInviteDecision(long id, String processInstanceId);

	public void existingMemberRejectInviteDecision(long id, String processInstanceId);

	public MemberInfoDto isMemberWithEmailExists(String email);

	public boolean forgotPasswordInitiate(String forgotPasswordEmail) throws CoreServiceException;

	public boolean memberChangeForgottenPasswordComplete(MemberInfoDto memberInfo, String pid);

	public void memberInviteSignupInputComplete(MemberInfoDto member, String pid);

	public void shareComment(MemberInfoDto member, String shareComment);

	public void reportProblem(NewErrorReportingDto error);

	public Set<EmployeeRole> getMemberRoles(MemberInfoDto member, OrganizationDetailInfoDto orgInfo);
}
