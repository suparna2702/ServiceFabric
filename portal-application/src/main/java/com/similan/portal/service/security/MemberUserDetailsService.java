package com.similan.portal.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.MemberManagementService;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.api.security.Invoker;

/**
 * A {@link UserDetailsService} implementation that is backed by the
 * {@link MemberManagementService}.
 * 
 * @author pablo
 */
@Slf4j
public class MemberUserDetailsService implements UserDetailsService {


  @Autowired
  private MemberManagementService memberManagementService;

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetailsService#
   * loadUserByUsername(java.lang.String)
   */
  @Transactional
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    try {
      log.info("Attemping to load user for username {}", username);
      SocialPerson member = this.memberManagementService
          .memberByEmail(username);
      if (member == null) {
        log.warn("Member with email [" + username + "] not found");
        throw new UsernameNotFoundException("Could not find user with email "
            + username);
      }

      log.info("Found social member {}", member);
      List<? extends GrantedAuthority> authorities = obtainAuthorities(member);

      User user = new PersistentUser(member.getId(), username, member
          .getMemberAccount().getPassword(),
          member.getState() == MemberState.Active, true, true, true,
          authorities);

      SocialEmployee employer = member.getEmployer();
      if (employer != null) {
        SocialActor employerBusiness = employer.getContactFrom();

        log.info("Found social org " + employerBusiness.getId()
            + " employee relationship id " + employer.getId());

        ((PersistentUser) user).setOrgId(employerBusiness.getId());
      }

      return user;
    } catch (UsernameNotFoundException unfe) {
      // Skip loading a known condition.
      throw unfe;
    } catch (Exception e) {
      // Security exception is not logged
      log.warn("Error loading user " + username, e);
      throw new UsernameNotFoundException("Error finding user", e);
    }
  }

  protected List<? extends GrantedAuthority> obtainAuthorities(
      SocialPerson member) {

    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

    /* everybody will have role member */
    authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
    SocialEmployee employer = member.getEmployer();

    /*
     * get all the employee roles and add and along with the business account
     * type
     */
    if (employer != null) {
      Set<EmployeeRole> employeeRoles = employer.getRoles();
      for (EmployeeRole role : employeeRoles) {
        log.info("role added " + role.toString());
        authorities.add(new SimpleGrantedAuthority(role.toString()));
      }

      /**
       * get the business account type and add
       */
      SocialOrganization org = (SocialOrganization) employer.getContactFrom();
      BusinessAccountType accountType = org.getAccountType();
      if (accountType != null) {
        authorities.add(new SimpleGrantedAuthority(accountType.getName()));
      } else {
        authorities.add(new SimpleGrantedAuthority(
            BusinessAccountSubscriptionType.BUSINESS_STANDARD));
      }

      // this is for all businesses
      authorities.add(new SimpleGrantedAuthority("ROLE_BUSINESS"));

    }

    return authorities;
  }

  public static class PersistentUser extends User implements Invoker {
    /**
     * Serial version.
     */
    private static final long serialVersionUID = -3922922315589929243L;
    private long id;
    private boolean embedded = false;
    private Long orgId;
    private MemberInfoDto memberInfo = null;
    private OrganizationDetailInfoDto orgInfo = null;

    public PersistentUser(Long id, String username, String password,
        boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities) {

      super(username, password, enabled, accountNonExpired,
          credentialsNonExpired, accountNonLocked, authorities);
      this.id = id;
    }

    public PersistentUser(long id, String username, Long orgId,
        Collection<? extends GrantedAuthority> authorities) {
      super(username, null, true, true, true, true, authorities);
      this.id = id;
      this.embedded = true;
      this.orgId = orgId;
    }

    /**
     * @return the id
     */
    public long getId() {
      return id;
    }

    public boolean isEmbedded() {
      return embedded;
    }

    /**
     * @return the orgId
     */
    public Long getOrgId() {
      return orgId;
    }

    public MemberInfoDto getMemberInfo() {
      return memberInfo;
    }

    public void setMemberInfo(MemberInfoDto memberInfo) {
      this.memberInfo = memberInfo;
    }

    public OrganizationDetailInfoDto getOrgInfo() {
      return orgInfo;
    }

    public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
      this.orgInfo = orgInfo;
    }

    public void setOrgId(Long orgId) {
      this.orgId = orgId;
    }

  }
}
