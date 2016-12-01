package com.similan.service.internal.impl.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceInvite;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.SecurityCheckResult;

@Component
public class CollaborationWorkspaceEnforcer extends AbstractComponentEnforcer {
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private CollaborationWorkspaceParticipationRepository collaborationWorkspaceParticipationRepository;

  public SecurityCheck create(final SocialOrganization owner) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (!hasRoleIn(context.invoker(), owner,
            EmployeeRole.COLLABORATION_WORKSPACE_ADMIN)) {
          return deny("You are not a collaboration workspace admin for "
              + owner.getDisplayName() + ".");
        }
        int allowances = owner.getAccountType()
            .getCollaborationWorkspaceAllowances();
        int count = collaborationWorkspaceRepository.countByOwner(owner);
        if (count >= allowances) {
          return deny("Allowances exceeded.");
        }
        return allow();
      }
    };
  }

  public SecurityCheck unshare(final CollaborationWorkspace workspace) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {

        CollaborationWorkspaceParticipation participation = collaborationWorkspaceParticipationRepository
            .findByWorkspaceAndParticipant(workspace, context.invoker());

        if (participation == null) {
          return deny("You cannot unshare a document in this workspace "
              + "since you are not a participant of this workspace.");
        }

        SocialOrganization owner = (SocialOrganization) workspace.getOwner();
        if (!hasRoleIn(context.invoker(), owner,
            EmployeeRole.COLLABORATION_WORKSPACE_ADMIN)) {
          return deny("You are not a collaboration workspace admin for "
              + owner.getDisplayName() + ".");
        }

        return allow();
      }
    };
  }

  public SecurityCheck getParticipationsByParticipant(
      final SocialPerson participant) {
    return isInvoker(participant, "You can only get your participations.");
  }

  public SecurityCheck getByOwner(SocialOrganization owner) {
    return invokerHasRoleIn(owner,
        "You can only read workspaces for your business.", EmployeeRole.REGULAR);
  }

  public SecurityCheck getForPerticipant(SocialPerson participant) {
    return isInvoker(participant,
        "You can only get workspaces you participate in.");
  }

  public SecurityCheck get(final CollaborationWorkspace workspace) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        CollaborationWorkspaceParticipation participation = collaborationWorkspaceParticipationRepository
            .findByWorkspaceAndParticipant(workspace, context.invoker());
        if (participation != null) {
          return allow();
        }
        return deny("You don't have access to the workspace.");
      }
    };
  }

  public SecurityCheck getDetail(CollaborationWorkspace workspace) {
    return get(workspace);
  }

  public SecurityCheck invite(CollaborationWorkspace workspace) {
    return update(workspace);
  }

  public SecurityCheck getParticipations(CollaborationWorkspace workspace) {
    return get(workspace);
  }

  public SecurityCheck respondToInvite(CollaborationWorkspaceInvite invite) {
    return isInvoker((SocialPerson) invite.getInvitee(),
        "You can only respond to your invites.");
  }

  public SecurityCheck getParticipants(CollaborationWorkspace workspace) {
    return get(workspace);
  }

  public SecurityCheck update(CollaborationWorkspace workspace) {
    return isInvoker((SocialPerson) workspace.getCreator(),
        "You can only manage workspaces you created.");
  }

  public SecurityCheck getWithStatistics(CollaborationWorkspace workspace) {
    return get(workspace);
  }
}
