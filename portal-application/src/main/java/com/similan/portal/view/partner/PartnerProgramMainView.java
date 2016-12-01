package com.similan.portal.view.partner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;

@Scope("view")
@Component("partnerProgramMainView")
@Slf4j
public class PartnerProgramMainView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private List<PartnerProgramDefinitionDto> partnerPrograms = new ArrayList<PartnerProgramDefinitionDto>();

  @PostConstruct
  public void init() {

    try {
      partnerPrograms = this.partnerManagementService
          .getPartnerProgramsForBusiness(orgInfo);
      for (PartnerProgramDefinitionDto program : partnerPrograms) {
        CollaborationWorkspaceKey workspaceKey = this.partnerManagementService
            .getPartnerWorkspace(program.getId());

        if (workspaceKey != null) {
          CollaborationWorkspaceDto workspace = this
              .getCollabWorkspaceService().get(workspaceKey);
          program.setWorkspace(workspace);
        }
      }
    } catch (Exception exp) {
      log.error("Security exception ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "You do not have privilage to view the Partner Programs "));
    }

  }

  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    return partnerPrograms;
  }

}
