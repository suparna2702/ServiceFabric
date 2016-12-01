package com.similan.service.internal.impl.security.component;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.SecurityCheckResult;
import com.similan.service.internal.impl.security.SimilanSecurityException;

public class AbstractComponentEnforcer {
  @Autowired
  private ExecutionContext executionContext;
  @Autowired
  SocialEmployeeRepository employeeRepository;

  protected boolean hasRoleIn(SocialPerson employee,
      SocialOrganization business, EmployeeRole role) {
    SocialEmployee employment = employeeRepository
        .findByContactToAndContactFromAndRolesIn(employee, business,
            role.getAncestors());
    return employment != null;
  }

  protected boolean isEmployee(SocialPerson employee,
      SocialOrganization business) {
    SocialEmployee employment = employeeRepository
        .findByContactToAndContactFrom(employee, business);
    return employment != null;
  }

  public abstract class SecurityCheck {
    public SimilanSecurityException checkAllowed()
        throws SimilanSecurityException {
      SecurityCheckResult result = internalEvaluate();
      if (!result.isAllowed()) {
        throw new SimilanSecurityException(result.getMessage());
      }
      return null;
    }

    public boolean isAllowed() {
      return internalEvaluate().isAllowed();
    }
    
    protected SecurityCheckResult internalEvaluate() {
      SecurityCheckResult result = evaluate(executionContext);
      if (result == null) {
        return deny("Denied.");
      }
      return result;
    }

    protected abstract SecurityCheckResult evaluate(ExecutionContext context);

    protected SecurityCheckResult allow() {
      return new SecurityCheckResult(true, "Allowed.");
    }

    protected SecurityCheckResult deny(String message) {
      return new SecurityCheckResult(false, message);
    }
    
    public SecurityCheck combinedWith(final SecurityCheck other) {
      final SecurityCheck one = this;
      return new SecurityCheck() {
        @Override
        protected SecurityCheckResult evaluate(ExecutionContext context) {
          SecurityCheckResult oneResult = one.evaluate(context);
          if (!oneResult.isAllowed()) {
            return oneResult;
          }
          return other.evaluate(context);
        }
      };
    }
  }

  protected SecurityCheck isInvoker(final SocialPerson person,
      final String message) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (person == context.invoker()) {
          return allow();
        }
        return deny(message);
      }
    };
  }

  protected SecurityCheck invokerHasRoleIn(final SocialOrganization business,
      final String message, final EmployeeRole role) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (hasRoleIn(context.invoker(), business, role)) {
          return allow();
        }
        return deny(message);
      }
    };
  }

  protected SecurityCheck invokerIsEmployee(final SocialOrganization business,
      final String message) {
    return new SecurityCheck() {
      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        if (isEmployee(context.invoker(), business)) {
          return allow();
        }
        return deny(message);
      }
    };
  }

  protected SecurityCheck alwaysAllow() {
    return new SecurityCheck() {

      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        return allow();
      }
    };
  }

  protected SecurityCheck alwaysDeny(final String message) {
    return new SecurityCheck() {

      @Override
      protected SecurityCheckResult evaluate(ExecutionContext context) {
        return deny(message);
      }
    };
  }
  
}
