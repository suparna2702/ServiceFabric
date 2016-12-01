package com.similan.domain.entity.poll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;

@Entity(name = "PollTemplate")
public class PollTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String templateName;

  @Column(length = 5000)
  private String templateDescription;
  
  @Column
  private String logo;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PollTemplateQuestion> templateQuestion;

  @ManyToOne
  @JoinColumn
  private SocialOrganization pollOwner;

  @ManyToOne
  @JoinColumn
  private SocialActor pollFor;

  @Column
  private Date createStart;
  
  private GenericReference<IPollSubject> subject;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getLogo() {
	  return logo;
  }

  public void setLogo(String logo) {
	  this.logo = logo;
  }

  public GenericReference<IPollSubject> getSubject() {
	  return subject;
  }

  public void setSubject(GenericReference<IPollSubject> subject) {
	  this.subject = subject;
  }

  public String getTemplateDescription() {
    return templateDescription;
  }

  public void setTemplateDescription(String templateDescription) {
    this.templateDescription = templateDescription;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public List<PollTemplateQuestion> getTemplateQuestion() {

    if (templateQuestion == null) {
      templateQuestion = new ArrayList<PollTemplateQuestion>();
    }

    return templateQuestion;
  }

  public void setTemplateQuestion(List<PollTemplateQuestion> templateQuestion) {
    this.templateQuestion = templateQuestion;
  }

  public SocialOrganization getPollOwner() {
    return pollOwner;
  }

  public void setPollOwner(SocialOrganization pollOwner) {
    this.pollOwner = pollOwner;
  }

  public SocialActor getPollFor() {
    return pollFor;
  }

  public void setPollFor(SocialActor pollFor) {
    this.pollFor = pollFor;
  }

  public Date getCreateStart() {
    return createStart;
  }

  public void setCreateStart(Date createStart) {
    this.createStart = createStart;
  }

}
