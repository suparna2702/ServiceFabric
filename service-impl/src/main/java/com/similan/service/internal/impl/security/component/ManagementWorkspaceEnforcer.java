package com.similan.service.internal.impl.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceParticipationRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceRepository;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.SecurityCheckResult;

@Component
public class ManagementWorkspaceEnforcer extends AbstractComponentEnforcer {
  @Autowired
  private ManagementWorkspaceRepository managementWorkspaceRepository;
  @Autowired
  private ManagementWorkspaceParticipationRepository managementWorkspaceParticipationRepository;
  @Autowired
  private DocumentRepository documentRepository;

  public SecurityCheck create(final SocialOrganization owner) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (!hasRoleIn(context.invoker(), owner,
            EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN)) {
          return deny("You are not a management workspace admin for "
              + owner.getDisplayName() + ".");
        }
        int allowances = owner.getAccountType()
            .getManagementWorkspaceAllowances();
        int count = managementWorkspaceRepository.countByOwner(owner);
        if (count >= allowances) {
          return deny("Allowances exceeded.");
        }
        return allow();
      }
    };
  }

  public SecurityCheck getForOwner(SocialOrganization owner) {
    return invokerHasRoleIn(owner,
        "You can only read workspaces for your business.", EmployeeRole.REGULAR);
  }

  public SecurityCheck getForParticipant(SocialPerson participant) {
    return isInvoker(participant,
        "You can only get workspaces you participate in.");
  }

  public SecurityCheck get(final ManagementWorkspace workspace) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        ManagementWorkspaceParticipation participation = managementWorkspaceParticipationRepository
            .findByWorkspaceAndParticipant(workspace, context.invoker());
        if (participation != null) {
          return allow();
        }
        return deny("You don't have access to the workspace.");
      }
    };
  }

  public SecurityCheck addParticipant(ManagementWorkspace workspace) {
    return update(workspace);
  }

  public SecurityCheck getParticipants(ManagementWorkspace workspace) {
    return get(workspace);
  }

  public SecurityCheck update(ManagementWorkspace workspace) {
    return isInvoker((SocialPerson) workspace.getCreator(),
        "You can only manage workspaces you created.");
  }

  public SecurityCheck getDocuments(ManagementWorkspace workspace) {
    return get(workspace);
  }
}
