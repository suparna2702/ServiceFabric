package com.similan.domain.entity.process.signup;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.domain.entity.process.BusinessProcess;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseSignUpProcess extends BusinessProcess {
  String firstName;
  String lastName;
  String username;
  String email;
}
