package com.similan.portal.view.survey;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.poll.PollAnswerType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollTemplateAnswerChoiceDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.framework.dto.poll.PollTemplateQuestionDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("pollTemplateCreateEditView")
@Slf4j
public class PollTemplateCreateEditView extends BaseView {
  private static final long serialVersionUID = 1L;

  private PollTemplateDto pollTemplate;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @Autowired
  private MemberInfoDto memberInfo;

  private String questionHeader = StringUtils.EMPTY;

  private String questionChoice = StringUtils.EMPTY;

  @PostConstruct
  public void init() {
    String pid = this.getContextParam("pid");
    log.info("Poll id to be fetched " + pid);

    if (pid != null) {
      Long pollId = Long.valueOf(pid);
      log.info("Found poll with id " + pollId);
      pollTemplate = this.getCampaignManagementService().getPollTemplateByid(
          pollId);
    }

    if (pollTemplate == null) {
      log.info("Creating a new poll");
      pollTemplate = new PollTemplateDto();
    }
  }

  public String getQuestionHeader() {
    return questionHeader;
  }

  public void setQuestionHeader(String questionHeader) {
    this.questionHeader = questionHeader;
  }

  public String getQuestionChoice() {
    return questionChoice;
  }

  public void setQuestionChoice(String questionChoice) {
    this.questionChoice = questionChoice;
  }

  public void addRatingQuestion() {

    PollTemplateQuestionDto question = new PollTemplateQuestionDto();
    question.setAnswerType(PollAnswerType.Rating);
    question.setShowDetails(false);
    this.pollTemplate.getTemplateQuestion().add(question);

    log.info("Adding poll question addRatingQuestion "
        + question.getQuestionUUID());
  }

  public void addMultiChoiceQuestion() {

    PollTemplateQuestionDto question = new PollTemplateQuestionDto();
    question.setAnswerType(PollAnswerType.MultiChoice);
    this.pollTemplate.getTemplateQuestion().add(question);

    log.info("Adding poll question addMultiChoiceQuestion "
        + question.getQuestionUUID());
  }

  public void addSingleChoiceQuestion() {
    PollTemplateQuestionDto question = new PollTemplateQuestionDto();
    question.setAnswerType(PollAnswerType.SingleChoiceRadio);
    this.pollTemplate.getTemplateQuestion().add(question);

    log.info("Adding poll question addSingleChoiceQuestion "
        + question.getQuestionUUID());
  }

  public void addTextInputQuestion() {
    PollTemplateQuestionDto question = new PollTemplateQuestionDto();
    question.setAnswerType(PollAnswerType.TextInput);
    question.setShowDetails(false);
    this.pollTemplate.getTemplateQuestion().add(question);

    log.info("Adding poll question addTextInputQuestion "
        + question.getQuestionUUID());
  }

  public PollTemplateDto getPollTemplate() {
    return pollTemplate;
  }

  public void addQuestionHeader(String uuid) {
    log.info("Adding question header for question " + uuid);
    for (PollTemplateQuestionDto question : this.pollTemplate
        .getTemplateQuestion()) {

      if (uuid.equalsIgnoreCase(question.getQuestionUUID()) == true) {
        question.setShowDetails(true);
        break;
      }
    }
  }

  public void addChoice(String uuid) {
    log.info("Adding choice for question " + uuid + " with choice text "
        + this.questionChoice + " for poll template "
        + this.pollTemplate.getTemplateName());

    if (StringUtils.isBlank(this.questionChoice)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Choice text cannot be empty"));
      return;
    }

    for (PollTemplateQuestionDto question : this.pollTemplate
        .getTemplateQuestion()) {

      if (uuid.equalsIgnoreCase(question.getQuestionUUID()) == true) {
        PollTemplateAnswerChoiceDto choice = new PollTemplateAnswerChoiceDto();
        choice.setAnswerChoiceText(this.questionChoice);
        question.getTemplateAnswerChoice().add(choice);

        log.info("Added choice " + question.getQuestionUUID()
            + " choice text " + choice.getAnswerChoiceText()
            + " question header " + question.getQuestionText());

        break;
      }

    }

    this.questionChoice = StringUtils.EMPTY;
  }

  public String saveSurveyTemplate() {
    log.info("Saving poll template " + this.pollTemplate);
    try {

      this.getCampaignManagementService().savePollTemplate(pollTemplate,
          orgInfo);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Survey has been saved successfully"));
    } catch (Exception exp) {
      log.info("Error saving template ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error in saving survey template"));
    }

    return "result";
  }

  public void deleteQuestion(String uuID) {
    log.info("deleting answer choice with UUID " + uuID);

    /**
     * 1. Remove the answer choice from templateQuestion 2. Delete from DB
     */
    for (PollTemplateQuestionDto questionDeleted : this.pollTemplate
        .getTemplateQuestion()) {
      if (questionDeleted.getQuestionUUID().equalsIgnoreCase(uuID) == true) {

        /**
         * if it is an unsaved question just delete from list otherwise remove
         * from list and DB
         */
        this.pollTemplate.getTemplateQuestion().remove(questionDeleted);
        if (questionDeleted.getId() != Long.MIN_VALUE) {
          this.getCampaignManagementService().deleteQuestionFromPoll(
              this.pollTemplate, questionDeleted);
        }

        break;
      }
    }

  }

//  private List<String> validateQuestion(PollTemplateQuestionDto question) {
//    List<String> results = new ArrayList<String>();
//
//    // Question text cannot be empty
//    if (StringUtils.isBlank(question.getQuestionText().trim()))
//      results.add("The question must be specified");
//
//    // Multi choice questions always should have more than one choice
//    // and choice text cannot be empty
//    if (question.getMultiChoice()
//        && question.getTemplateAnswerChoice().size() < 2)
//      results
//          .add("Multiple choice questions must have more then one possible answer.");
//
//    // Single choice (List and Radio) much have more than one choice and
//    // choice text cannot be empty
//    if ((question.getSingleChoiceList() || question.getSingleChoiceRadio())
//        && question.getTemplateAnswerChoice().size() < 2)
//      results
//          .add("Single choice questions must have more then one possible answer.");
//
//    return results;
//  }

}
