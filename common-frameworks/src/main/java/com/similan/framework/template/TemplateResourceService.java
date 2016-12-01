package com.similan.framework.template;

import java.util.Map;

public interface TemplateResourceService {

  public class EmailContent {

    private final String subject;

    private final String body;

    public EmailContent(String subject, String body) {
      this.subject = subject;
      this.body = body;
    }

    public String getSubject() {
      return subject;
    }

    public String getBody() {
      return body;
    }
    
    @Override
    public String toString() {
      return "Subject: " + subject + ", Body: " +  body;
    }
  }

  String getComposedTemplateContent(String emailTemplateName,
      Map<String, Object> arguments);

}
