package com.similan.service.internal.impl.security.component;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialPerson;

@Component
public class MemberEnforcer extends AbstractComponentEnforcer {
  public SecurityCheck update(SocialPerson member) {
    return isInvoker(member, "You can only edit your own profile.");
  }

  public SecurityCheck changePassword(SocialPerson member) {
    return update(member);
  }

  public SecurityCheck changeEmail(SocialPerson member) {
    return update(member);
  }
}
