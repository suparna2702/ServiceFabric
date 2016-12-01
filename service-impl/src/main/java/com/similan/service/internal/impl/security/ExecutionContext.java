package com.similan.service.internal.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.service.api.base.dto.error.SimilanUnauthorizedException;
import com.similan.service.api.security.Invoker;

@Component
public class ExecutionContext {
  @Autowired
  private SocialPersonRepository socialPersonRepository;
  
  public SocialPerson invoker() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null) {
      throw new SimilanUnauthorizedException("No security context.");
    }
    Authentication authentication = context.getAuthentication();
    if (authentication == null) {
      throw new SimilanUnauthorizedException("No authentication.");
    }
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      throw new SimilanUnauthorizedException("No principal.");
    }
    Invoker invoker = (Invoker)principal;
    return socialPersonRepository.findOne(invoker.getId());
  }
}
