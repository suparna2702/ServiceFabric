package com.similan.service.internal.impl.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.SecurityCheckResult;

@Component
public class PartnerProgramEnforcer extends AbstractComponentEnforcer {
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramDefinitionRepository;
  @Autowired
  private PartnershipRepository partnershipRepository;
  @Autowired
  private PartnershipRepository employeeRepository;

  public SecurityCheck create(final SocialOrganization owner) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (!hasRoleIn(context.invoker(), owner,
            EmployeeRole.PARTNER_PROGRAM_ADMIN)) {
          return deny("You are not a partner program admin for "
              + owner.getDisplayName() + ".");
        }
        int allowances = owner.getAccountType().getPartnerProgramAllowances();
        int count = partnerProgramDefinitionRepository.countByOwner(owner);
        if (count >= allowances) {
          return deny("Allowances exceeded.");
        }
        return allow();
      }
    };
  }

  public SecurityCheck getWorkspace(PartnerProgramDefinition program) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck getPartnershipsForPartner(
      final SocialOrganization partner) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck getForOwner(final SocialOrganization owner) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (!isEmployee(context.invoker(), owner)) {
          return deny("Only for employees.");
        }
        return allow();
      }
    };
  }

  public SecurityCheck getAll() {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck approve(PartnerProgramDefinition program) {
    return isInvoker((SocialPerson) program.getCreator(),
        "You can only approve parnerships for programs you created.");
  }

  public SecurityCheck getPartners(PartnerProgramDefinition program) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck join(PartnerProgramDefinition program) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck getPartnership(Partnership partnership) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck get(PartnerProgramDefinition program) {
    // TODO: what's the rule?
    return alwaysAllow();
  }

  public SecurityCheck update(PartnerProgramDefinition program) {
    return isInvoker((SocialPerson) program.getCreator(),
        "You can only update programs you created.");
  }

  public SecurityCheck submitInput(Partnership partnership) {
    return invokerIsEmployee(partnership.getPartner(),
        "You can only submit programs for your company.");
  }

  public SecurityCheck isPartnerForProgram(SocialOrganization organization,
      PartnerProgramDefinition program) {
    // TODO: what's the rule?
    return alwaysAllow();
  }
}
