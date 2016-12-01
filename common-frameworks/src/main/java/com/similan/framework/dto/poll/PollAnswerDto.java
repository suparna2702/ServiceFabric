package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.poll.PollAnswerType;

public class PollAnswerDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long questionId;

  private String questionText;

  private PollAnswerType answerType;

  private List<PollAnswerChoiceDto> answerChoices;

  public PollAnswerDto() {
    id = Long.MIN_VALUE;
    answerType = PollAnswerType.MultiChoice;
    answerChoices = new ArrayList<PollAnswerChoiceDto>();
  }

  public boolean getChoice() {
    if (answerType.equals(PollAnswerType.MultiChoice)
        || answerType.equals(PollAnswerType.SingleChoiceList)
        || answerType.equals(PollAnswerType.SingleChoiceRadio)) {
      return true;
    } else {
      return false;
    }
  }

  public void setChoice(boolean choice) {

  }

  public boolean getTextInput() {
    if (answerType.equals(PollAnswerType.TextInput)) {
      return true;
    } else {
      return false;
    }
  }

  public void setTextInput() {

  }

  public boolean getRating() {
    if (answerType.equals(PollAnswerType.Rating)) {
      return true;
    } else {
      return false;
    }
  }

  public void setRating(boolean rating) {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<PollAnswerChoiceDto> getAnswerChoices() {
    return answerChoices;
  }

  public void setAnswerChoices(List<PollAnswerChoiceDto> answerChoices) {
    this.answerChoices = answerChoices;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
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

}
