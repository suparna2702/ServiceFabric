package com.similan.portal.integration;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.service.api.PartnerManagementService;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

/**
 * An authentication handler that publishes a login event and then behaves as a
 * {@link SavedRequestAwareAuthenticationSuccessHandler}
 * 
 * @author psaavedra
 */
@Slf4j
public class EventPublishingAuthenticationSuccessHandler extends
    SavedRequestAwareAuthenticationSuccessHandler {


  @Autowired
  private ApplicationEventPublisher eventPublisher;
  @Autowired
  private PartnerManagementService partnerService;
  @Autowired
  private PartnershipRepository partnershipRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response, Authentication authentication)
      throws ServletException, IOException {

    String biz = request.getParameter("loginForm:business");
    if (biz != null) {

      UserDetails userDetails = (UserDetails) SecurityContextHolder
          .getContext().getAuthentication().getPrincipal();
      PersistentUser member = (PersistentUser) userDetails;

      Long employerId = member.getOrgId();
      log.info("Employer id " + employerId + " member id " + member.getId()
          + "Login for partner " + biz);

      Long partnerId = Long.valueOf(biz);
      List<Partnership> partnership = this.partnershipRepository
          .findPartnershipByProgramOwnerAndPartner(partnerId, employerId);

      if (partnership.size() <= 0) {
        // this means no partnership so login cannot be valid
        throw new UsernameNotFoundException(
            "Login Failed. You are not a partner!!!");
      }

      CollaborationWorkspaceKey workspaceKey = this.partnerService
          .getDefaultWorkspaceForParticipatingPartnerPrograms(employerId);
      log.info("Workspace Key " + workspaceKey);

      if (workspaceKey != null) {

        StringBuilder url = new StringBuilder()
            .append("/collabspace/workspaceDetail.xhtml?").append("wsname=")
            .append(workspaceKey.getName()).append("&owsname=")
            .append(workspaceKey.getOwner().getName());
        log.info("Url " + url.toString());

        response.sendRedirect(request.getContextPath() + url.toString());
      } else {

        log.info("Url redirecting "
            + "/business/businessPublicProfile.xhtml?oid=" + biz);

        response.sendRedirect(request.getContextPath()
            + "/business/businessPublicProfile.xhtml?oid=" + biz);
      }
    } else {
      super.onAuthenticationSuccess(request, response, authentication);
    }
  }

}
