package com.similan.framework.workflow;

import java.io.Serializable;

public class WorkflowTransientStateAttributeDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String attributeName;

  private String attributeType;

  private String attributeValue;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getAttributeType() {
    return attributeType;
  }

  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }

  public String getAttributeValue() {
    return attributeValue;
  }

  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
  }

  @Override
  public String toString() {
    return "WorkflowTransientStateAttributeDto [id=" + id + ", attributeName="
        + attributeName + ", attributeType=" + attributeType
        + ", attributeValue=" + attributeValue + "]";
  }

}
