package com.similan.service.internal.api.email;

import com.similan.service.internal.api.email.io.NewEmail;

public interface EmailInternalService {

  public void send(NewEmail emailMessage);

}
