package com.similan.framework.manager.email;

import java.util.Map;

public interface EmailManager {

  public void send(String from, String to, String template,
      Map<String, Object> arguments);

}
