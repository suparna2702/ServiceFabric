package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @author suparna1108
 *
 */

public class PollTemplateAnswerChoiceDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String answerChoiceText;

  private Integer answerRating;

  private int answerIndex;

  private String choiceUUID;

  public PollTemplateAnswerChoiceDto() {
    id = Long.MIN_VALUE;
    choiceUUID = UUID.randomUUID().toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAnswerRating() {
    return answerRating;
  }

  public void setAnswerRating(Integer answerRating) {
    this.answerRating = answerRating;
  }

  public String getAnswerChoiceText() {
    return answerChoiceText;
  }

  public void setAnswerChoiceText(String answerChoiceText) {
    this.answerChoiceText = answerChoiceText;
  }

  public int getAnswerIndex() {
    return answerIndex;
  }

  public void setAnswerIndex(int answerIndex) {
    this.answerIndex = answerIndex;
  }

  public String getChoiceUUID() {
    return choiceUUID;
  }

  public void setChoiceUUID(String choiceUUID) {
    this.choiceUUID = choiceUUID;
  }

  @Override
  public String toString() {
    return "PollTemplateAnswerChoiceDto [id=" + id + ", answerChoiceText="
        + answerChoiceText + ", answerRating=" + answerRating
        + ", answerIndex=" + answerIndex + ", choiceUUID=" + choiceUUID + "]";
  }

}
