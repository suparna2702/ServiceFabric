package com.similan.process;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.process.signup.BasicMemberSignUpProcess;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.service.api.community.SocialActorMemberService;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.operation.MemberSignupDto;

public class BasicMemberSignUpTest extends BaseProcessTest {
  @Autowired
  private SocialActorMemberService memberService;
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private SocialPersonRepository memberRepository;

  @Test
  public void signUp() {
    String unique = UUID.randomUUID().toString();
    MemberSignupDto signupDto = new MemberSignupDto();
    signupDto.setFirstName("test-fname");
    signupDto.setLastName("test-lname");
    signupDto.setEmail("test+" + unique + "@businessreach.com");
    signupDto.setUsername("test-user-" + unique);
    memberService.signUp(signupDto);
    SocialPerson unconfirmedMember = memberRepository.findByEmail(signupDto
        .getEmail());
    Assert.assertEquals(MemberState.Pending, unconfirmedMember.getState());
    String token = findActivationToken(signupDto.getEmail());
    MemberDto memberDto = memberService.verify(token);
    Assert.assertEquals(signupDto.getFirstName(), memberDto.getFirstName());
    Assert.assertEquals(signupDto.getLastName(), memberDto.getLastName());
    Assert.assertEquals(signupDto.getEmail(), memberDto.getContactEmail());
    SocialPerson activatedMember = memberRepository.findByEmail(signupDto
        .getEmail());
    Assert.assertEquals(MemberState.Active, activatedMember.getState());
  }

  private String findActivationToken(String email) {
    return entityManager
        .createQuery(
            "select process from "
                + BasicMemberSignUpProcess.class.getSimpleName()
                + " process where process.email = :email",
            BasicMemberSignUpProcess.class).setParameter("email", email)
        .getSingleResult().getUuid();
  }
}
