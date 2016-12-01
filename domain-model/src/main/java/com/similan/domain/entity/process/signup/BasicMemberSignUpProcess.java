package com.similan.domain.entity.process.signup;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.domain.entity.community.SocialPerson;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicMemberSignUpProcess extends BaseSignUpProcess {
  String password;
  String validationUrl;
  @ManyToOne
  @JoinColumn
  SocialPerson member;
}
