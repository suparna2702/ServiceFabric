package com.similan.service.internal.impl.process.signup;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.experimental.FieldDefaults;

import com.similan.domain.entity.process.signup.BaseSignUpProcess;
import com.similan.service.internal.impl.process.BusinessProcessActions;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseSignUpActions<PROCESS extends BaseSignUpProcess>
    extends BusinessProcessActions<PROCESS> {
  @Override
  @Delegate
  protected BaseSignUpProcess process() {
    return getProcess();
  }

  public void sendWelcomeEmail() {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("firstName", getFirstName());
    parameters.put("lastName", getLastName());
    sendEmail("memberWelcomeEmail.vm", getEmail(), parameters);
  }
}
