package com.similan.service.internal.api.email.io;

import java.util.List;
import java.util.Map;

import com.similan.domain.entity.community.SocialActor;

public class NewEmail {

  private SocialActor from;

  private List<SocialActor> to;

  private String template;
  
  private Map<String, Object> parameters;

  public NewEmail(SocialActor from, List<SocialActor> to, String template,
		  Map<String, Object> parameters) {
    this.from = from;
    this.to = to;
    this.template = template;
    this.parameters = parameters;
  }
  
  public SocialActor getFrom() {
    return from;
  }

  public List<SocialActor> getTo() {
    return to;
  }

  public String getTemplate() {
    return template;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

}
