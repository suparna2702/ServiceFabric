package com.similan.portal.view.member;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

@Scope("view")
@Component("authenticatedMemberInfoView")
@Slf4j
public class AuthenticatedMemberInfoView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired(required = false)
  private OrganizationDetailInfoDto employer = null;

  public String getMemberPhoto() {
    log.info("Returning member photo ");
    if (this.member != null
        && StringUtils.isNotBlank(this.member.getPhotoLocation())) {
      return this.member.getPhotoLocation();
    }

    return IMAGES_MEMBER_DEFAULT_PHOTO;
  }

  public MemberInfoDto getMember() {
    return member;
  }

  public String getBusinessLogo() {
    if (this.employer != null
        && StringUtils.isNotBlank(this.employer.getImagePath())) {
      return this.employer.getImagePath();
    }

    return IMAGES_PRODUCT_DEFAULT_LOGO;
  }

  public List<CollaborationWorkspaceKey> getPartnerWorkspaces() {
    if (employer != null) {
      return this.getPartnerWorkspaces(employer, member);
    } else {
      return null;
    }
  }

  public OrganizationDetailInfoDto getEmployer() {
    return employer;
  }

}
