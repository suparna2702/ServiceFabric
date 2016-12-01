package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.entity.poll.PollAnswerType;

public class PollTemplateQuestionDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Integer questionIndex;

  private String questionText;

  private PollAnswerType answerType;

  private Boolean required;

  private Boolean showDetails;

  private List<PollTemplateAnswerChoiceDto> templateAnswerChoice;

  private List<PollTemplateAnswerChoiceDto> selectedAnswerChoice;

  private PollTemplateAnswerChoiceDto selectedAnswer;

  private String answerText;

  private Integer answerRating;

  private String questionUUID;

  public PollTemplateQuestionDto() {

    id = Long.MIN_VALUE;
    questionIndex = 0;
    questionText = StringUtils.EMPTY;
    answerType = PollAnswerType.MultiChoice;
    questionUUID = UUID.randomUUID().toString();
    templateAnswerChoice = new ArrayList<PollTemplateAnswerChoiceDto>();
    selectedAnswerChoice = new ArrayList<PollTemplateAnswerChoiceDto>();
    questionIndex = 5;
    required = Boolean.TRUE;
    showDetails = Boolean.TRUE;
 
  }

  public String questionType() {
    switch (this.answerType) {
    case MultiChoice:
      return "Multi Choice";
    case SingleChoiceList:
    case SingleChoiceRadio:
      return "Single Choice";
    case TextInput:
      return "Text Input";
    case Rating:
      return "Rating";
    default:
      break;
    }
    return this.answerType.toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getShowDetails() {
    return showDetails;
  }

  public void setShowDetails(Boolean showDetails) {
    this.showDetails = showDetails;
  }

  public String getQuestionUUID() {
    return questionUUID;
  }

  public void setQuestionUUID(String questionUUID) {
    this.questionUUID = questionUUID;
  }

  public boolean getMultiChoice() {

    if (answerType.equals(PollAnswerType.MultiChoice) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void setMultiChoice(boolean choice) {

  }

  public boolean getSingleChoiceRadio() {

    if (answerType.equals(PollAnswerType.SingleChoiceRadio) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void setSingleChoiceRadio(boolean choice) {

  }

  public boolean getSingleChoiceList() {

    if (answerType.equals(PollAnswerType.SingleChoiceList) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void setSingleChoiceList(boolean choice) {

  }

  public boolean getTextInput() {

    if (answerType.equals(PollAnswerType.TextInput) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void setTextInput(boolean choice) {

  }

  public boolean getRating() {

    if (answerType.equals(PollAnswerType.Rating) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void setRating(boolean choice) {

  }

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public Integer getQuestionIndex() {
    return questionIndex;
  }

  public void setQuestionIndex(Integer questionIndex) {
    this.questionIndex = questionIndex;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public PollAnswerType getAnswerType() {
    return answerType;
  }

  public void setAnswerType(PollAnswerType answerType) {
    this.answerType = answerType;
  }

  public List<PollTemplateAnswerChoiceDto> getTemplateAnswerChoice() {
    return templateAnswerChoice;
  }

  public void setTemplateAnswerChoice(
      List<PollTemplateAnswerChoiceDto> templateAnswerChoice) {
    this.templateAnswerChoice = templateAnswerChoice;
  }

  public List<PollTemplateAnswerChoiceDto> getSelectedAnswerChoice() {
    return selectedAnswerChoice;
  }

  public void setSelectedAnswerChoice(
      List<PollTemplateAnswerChoiceDto> selectedAnswerChoice) {
    this.selectedAnswerChoice = selectedAnswerChoice;
  }

  public PollTemplateAnswerChoiceDto getSelectedAnswer() {
    return selectedAnswer;
  }

  public void setSelectedAnswer(PollTemplateAnswerChoiceDto selectedAnswer) {
    this.selectedAnswer = selectedAnswer;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public Integer getAnswerRating() {
    return answerRating;
  }

  public void setAnswerRating(Integer answerRating) {
    this.answerRating = answerRating;
  }

  public void setSelectedAnswerChoiceIds(Long ids[]) {

    selectedAnswerChoice = new ArrayList<PollTemplateAnswerChoiceDto>();
    if (ids != null) {
      for (PollTemplateAnswerChoiceDto answer : templateAnswerChoice) {

        for (int i = 0; i < ids.length; i++) {
          Long id = ids[i];

          if (id == answer.getId()) {
            selectedAnswerChoice.add(answer);
          }

        }

      }
    }
  }

  public Long[] getSelectedAnswerChoiceIds() {
    List<Long> result = new ArrayList<Long>();
    if (selectedAnswerChoice != null) {
      for (PollTemplateAnswerChoiceDto answer : selectedAnswerChoice) {
        result.add(answer.getId());
      }
    }
    
    Long[] out = new Long[result.size()];
    return result.toArray(out);
  }

  public void setSelectedAnswerId(Long id) {
    
    if (id != null) {
      for (PollTemplateAnswerChoiceDto answer : templateAnswerChoice) {
        if (id == answer.getId()) {
          selectedAnswer = answer;
          break;
        }
      }
    } else {
      selectedAnswer = null;
    }
  }

  public Long getSelectedAnswerId() {
    if (selectedAnswer != null) {
      return selectedAnswer.getId();

    } else {
      return Long.valueOf("0");
    }
  }
}
