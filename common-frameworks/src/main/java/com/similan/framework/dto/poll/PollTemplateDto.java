package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PollTemplateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String templateName;

  private String templateDescription;

  private Date createStart;

  private List<PollTemplateQuestionDto> templateQuestion;

  private PollRunStatisticsDto eventStatistics;

  public PollTemplateDto() {
    id = Long.MIN_VALUE;
    templateName = StringUtils.EMPTY;
    templateQuestion = new ArrayList<PollTemplateQuestionDto>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public PollRunStatisticsDto getEventStatistics() {
    return eventStatistics;
  }

  public void setEventStatistics(PollRunStatisticsDto eventStatistics) {
    this.eventStatistics = eventStatistics;
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

  public Date getCreateStart() {
    return createStart;
  }

  public void setCreateStart(Date createStart) {
    this.createStart = createStart;
  }

  public List<PollTemplateQuestionDto> getTemplateQuestion() {
    return templateQuestion;
  }

  public void setTemplateQuestion(List<PollTemplateQuestionDto> templateQuestion) {
    this.templateQuestion = templateQuestion;
  }

  @Override
  public String toString() {
    return "PollTemplateDto [id=" + id + ", templateName=" + templateName
        + ", templateDescription=" + templateDescription + ", createStart="
        + createStart + ", templateQuestion=" + templateQuestion + "]";
  }

}
