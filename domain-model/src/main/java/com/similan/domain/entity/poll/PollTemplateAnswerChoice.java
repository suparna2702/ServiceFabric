package com.similan.domain.entity.poll;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "PollTemplateAnswerChoice")
public class PollTemplateAnswerChoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String answerChoiceText;

  @Column
  private Integer answerIndex;

  @Column
  private String choiceUUID = UUID.randomUUID().toString();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAnswerChoiceText() {
    return answerChoiceText;
  }

  public void setAnswerChoiceText(String answerChoiceText) {
    this.answerChoiceText = answerChoiceText;
  }

  public Integer getAnswerIndex() {
    return answerIndex;
  }

  public void setAnswerIndex(Integer answerIndex) {
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
    return "PollTemplateAnswerChoice [id=" + id + ", answerChoiceText="
        + answerChoiceText + ", answerIndex=" + answerIndex + ", choiceUUID="
        + choiceUUID + "]";
  }

}
